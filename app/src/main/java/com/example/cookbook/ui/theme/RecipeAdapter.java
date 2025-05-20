package com.example.cookbook.ui.theme;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.cookbook.R;
import com.example.cookbook.RecipeDetailActivity;
import com.example.cookbook.data.AppDatabase;
import com.example.cookbook.model.Recipe;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> recipeList = new ArrayList<>();
    private final Context context;
    private final HashMap<String, String> downloadUrlCache = new HashMap<>();
    private boolean isGrid = false;



    public RecipeAdapter(Context context) {
        this.context = context;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipeList = recipes != null ? recipes : new ArrayList<>();
        Log.d("RecipeAdapter", "Updating recipes: " + recipeList.size());
        notifyDataSetChanged();

        // Debug log
        if (recipeList.isEmpty()) {
            Log.d("RecipeAdapter", "Received empty recipe list");
        } else {
            boolean isBosnian = context.getResources().getConfiguration().getLocales().get(0).getLanguage().equals("bs");

            for (Recipe recipe : recipeList) {
                String name = isBosnian ? recipe.nameBs : recipe.nameEn;
                String ingredients = isBosnian ? recipe.ingredientsBs : recipe.ingredientsEn;
                Log.d("RecipeAdapter", name + " - " + ingredients);
            }
        }

    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(isGrid ? R.layout.item_recipe_grid : R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);

        String localizedName = getLocalizedName(recipe);
        String localizedIngredients = getLocalizedIngredients(recipe);

        holder.textViewName.setText(localizedName);
        holder.textViewCalories.setText(String.format(context.getString(R.string.calories_format), recipe.calories));
        holder.textViewRating.setText(String.format(context.getString(R.string.rating_format), recipe.rating));

        holder.bind(recipe, localizedName, localizedIngredients);
    }


    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView textViewName;
        private final TextView textViewCalories;
        private final TextView textViewRating;
        private final ProgressBar progressBar;
        private final ImageView imageViewFavorite;


        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewRecipe);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewCalories = itemView.findViewById(R.id.textViewCalories);
            textViewRating = itemView.findViewById(R.id.textViewRating);
            progressBar = itemView.findViewById(R.id.progressBar);
            imageViewFavorite = itemView.findViewById(R.id.imageViewFavorite);
        }

        public void bind(Recipe recipe, String localizedName, String localizedIngredients) {
            textViewName.setText(localizedName);
            textViewCalories.setText(context.getString(R.string.calories_format, recipe.calories));
            textViewRating.setText(context.getString(R.string.rating_format, recipe.rating));
            Glide.with(context).clear(imageView);
            imageView.setImageDrawable(null);

            int iconRes = recipe.isFavorite ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite;
            imageViewFavorite.setImageResource(iconRes);

            imageViewFavorite.setOnClickListener(v -> {
                imageViewFavorite.animate()
                        .scaleX(0.7f)
                        .scaleY(0.7f)
                        .setDuration(100)
                        .withEndAction(() -> {
                            long lastViewed = recipe.lastViewedTimestamp;
                            recipe.isFavorite = !recipe.isFavorite;
                            recipe.lastViewedTimestamp = lastViewed;

                            int newIcon = recipe.isFavorite ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite;
                            imageViewFavorite.setImageResource(newIcon);

                            imageViewFavorite.animate()
                                    .scaleX(1f)
                                    .scaleY(1f)
                                    .setDuration(150)
                                    .start();

                            AppDatabase.databaseWriteExecutor.execute(() -> {
                                AppDatabase.getInstance(context.getApplicationContext())
                                        .recipeDao()
                                        .updateFavoriteWithTimestamp(recipe.id, recipe.isFavorite,
                                                recipe.lastViewedTimestamp > 0 ? recipe.lastViewedTimestamp : System.currentTimeMillis());
                            });

                            if (recipe.isFavorite) {
                                animateFavoriteToTab(imageViewFavorite);
                            }
                        })
                        .start();
            });





            progressBar.setVisibility(View.VISIBLE);
            RequestOptions options = new RequestOptions()
                    .transform(new RoundedCorners(dpToPx(20)))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder);


            if (recipe.imageUrl != null && !recipe.imageUrl.isEmpty()) {
                imageView.setTag(recipe.imageUrl);
                progressBar.setVisibility(View.VISIBLE);

                if (recipe.imageUrl.startsWith("/")) {
                    Glide.with(context)
                            .load(new File(recipe.imageUrl))
                            .apply(options)
                            .into(new GlideImageTarget(imageView, progressBar, recipe.imageUrl));
                } else {
                    String cachedUrl = downloadUrlCache.get(recipe.imageUrl);
                    if (cachedUrl != null) {
                        Glide.with(context)
                                .load(cachedUrl)
                                .apply(options)
                                .into(new GlideImageTarget(imageView, progressBar, recipe.imageUrl));
                    } else {
                        StorageReference storageRef = FirebaseStorage.getInstance()
                                .getReference()
                                .child("Thumbnails/" + recipe.imageUrl);

                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String url = uri.toString();
                            downloadUrlCache.put(recipe.imageUrl, url);
                            Glide.with(context)
                                    .load(url)
                                    .apply(options)
                                    .into(new GlideImageTarget(imageView, progressBar, recipe.imageUrl));
                        }).addOnFailureListener(e -> {
                            Log.e("RecipeAdapter", "Firebase load fail", e);
                            imageView.setImageResource(R.drawable.placeholder);
                            progressBar.setVisibility(View.GONE);
                        });
                    }
                }
            } else {
                imageView.setImageResource(R.drawable.placeholder);
                progressBar.setVisibility(View.GONE);
            }




            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, RecipeDetailActivity.class);
                intent.putExtra("RECIPE_ID", recipe.id);
                intent.putExtra("RECIPE_NAME", localizedName);
                intent.putExtra("RECIPE_INGREDIENTS", localizedIngredients);
                intent.putExtra("RECIPE_CALORIES", recipe.calories);
                intent.putExtra("RECIPE_RATING", recipe.rating);
                intent.putExtra("RECIPE_INTERNAL_NAME", recipe.internalName);
                context.startActivity(intent);
            });
        }
    }

    private void animateFavoriteToTab(ImageView sourceIcon) {
        Activity activity = (Activity) context;
        ViewGroup rootView = activity.findViewById(android.R.id.content);

        ImageView flyingFavorite = new ImageView(context);
        flyingFavorite.setImageResource(R.drawable.ic_favorite_filled);
        flyingFavorite.setColorFilter(Color.parseColor("#e9d2b0"), PorterDuff.Mode.SRC_IN);
        int size = dpToPx(24);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(size, size);
        rootView.addView(flyingFavorite, params);

        int[] startLoc = new int[2];
        sourceIcon.getLocationOnScreen(startLoc);
        flyingFavorite.setX(startLoc[0] + sourceIcon.getWidth() / 2f - size / 2f);
        flyingFavorite.setY(startLoc[1] + sourceIcon.getHeight() / 2f - size / 2f);


        View bottomTab = activity.findViewById(R.id.favoritesFragment);
        if (bottomTab == null) return;

        int[] endLoc = new int[2];
        bottomTab.getLocationOnScreen(endLoc);
        float endX = endLoc[0] + bottomTab.getWidth() / 2f + dpToPx(1);
        float endY = endLoc[1];

        // Animacije
        ObjectAnimator animX = ObjectAnimator.ofFloat(flyingFavorite, View.X, flyingFavorite.getX(), endX);
        ObjectAnimator animY = ObjectAnimator.ofFloat(flyingFavorite, View.Y, flyingFavorite.getY(), endY);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(flyingFavorite, View.SCALE_X, 1f, 0.2f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(flyingFavorite, View.SCALE_Y, 1f, 0.2f);
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(flyingFavorite, View.ALPHA, 1f, 0f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animX, animY, scaleX, scaleY, fadeOut);
        animatorSet.setDuration(650);
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                rootView.removeView(flyingFavorite);
            }
        });
        animatorSet.start();
    }

    private int dpToPx(int dp) {
        return Math.round(dp * context.getResources().getDisplayMetrics().density);
    }

    public void setGridMode(boolean grid) {
        isGrid = grid;
        notifyDataSetChanged();
    }

    private class GlideImageTarget extends com.bumptech.glide.request.target.ImageViewTarget<android.graphics.drawable.Drawable> {
        private final ProgressBar progressBar;
        private final String expectedUrl;

        public GlideImageTarget(ImageView view, ProgressBar progressBar, String expectedUrl) {
            super(view);
            this.progressBar = progressBar;
            this.expectedUrl = expectedUrl;
        }

        @Override
        protected void setResource(@Nullable android.graphics.drawable.Drawable resource) {
            ImageView imageView = getView();
            Object tag = imageView.getTag();

            if (tag instanceof String && tag.equals(expectedUrl)) {
                imageView.setImageDrawable(resource);
                progressBar.setVisibility(View.GONE);
            }
        }

        @Override
        public void onLoadFailed(@Nullable android.graphics.drawable.Drawable errorDrawable) {
            super.onLoadFailed(errorDrawable);
            getView().setImageDrawable(errorDrawable);
            progressBar.setVisibility(View.GONE);
        }
    }

    private String getLocalizedName(Recipe recipe) {
        return isBosnian() ? recipe.nameBs : recipe.nameEn;
    }

    private String getLocalizedIngredients(Recipe recipe) {
        return isBosnian() ? recipe.ingredientsBs : recipe.ingredientsEn;
    }

    private boolean isBosnian() {
        return context.getResources().getConfiguration().getLocales().get(0).getLanguage().equals("bs");
    }




}