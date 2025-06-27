package com.example.cookbook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cookbook.data.AiResponseRepository;
import com.example.cookbook.data.AppDatabase;
import com.example.cookbook.model.ChatMessage;
import com.example.cookbook.model.InstructionStep;
import com.example.cookbook.model.Recipe;
//import com.example.cookbook.ui.theme.AiChatBottomSheet;
import com.example.cookbook.ui.theme.ChatAdapter;
import com.example.cookbook.ui.theme.InstructionStepAdapter;
import com.example.cookbook.utils.ChatMemoryCache;
import com.example.cookbook.utils.LocaleHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView nameTextView, caloriesTextView, ratingTextView;
    private RecyclerView instructionStepsRecyclerView;
    private InstructionStepAdapter instructionStepAdapter;
    private ImageView img1, img2, img3;
    private Recipe recipe;
    private int recipeId = -1;
    private boolean isChatVisible = false;
    private FrameLayout aiChatView;
    private RecyclerView recyclerViewChat;
    private EditText inputChat;
    private ImageButton sendChat;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        nameTextView = findViewById(R.id.textViewRecipeName);
        caloriesTextView = findViewById(R.id.textViewRecipeCalories);
        ratingTextView = findViewById(R.id.textViewRecipeRating);
        ImageView favoriteIcon = findViewById(R.id.imageViewFavoriteDetail);
        favoriteIcon.setImageResource(R.drawable.ic_favorite);
        instructionStepsRecyclerView = findViewById(R.id.instructionStepsRecyclerView);

        img1 = findViewById(R.id.imageViewRecipe1);
        img2 = findViewById(R.id.imageViewRecipe2);
        img3 = findViewById(R.id.imageViewRecipe3);

        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        nameTextView.startAnimation(fadeIn);
        caloriesTextView.startAnimation(fadeIn);
        ratingTextView.startAnimation(fadeIn);

        instructionStepsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        instructionStepAdapter = new InstructionStepAdapter(this);
        instructionStepsRecyclerView.setAdapter(instructionStepAdapter);

        FloatingActionButton aiFab = findViewById(R.id.ai_fab);
        aiFab.setOnClickListener(v -> toggleChatVisibility());

        FrameLayout aiChatContainer = findViewById(R.id.aiChatContainer);
        LinearLayout aiChatView = findViewById(R.id.aiChatView);

        recyclerViewChat = aiChatView.findViewById(R.id.recyclerViewChat);
        inputChat = aiChatView.findViewById(R.id.editTextChat);
        sendChat = aiChatView.findViewById(R.id.buttonSendChat);

        chatAdapter = new ChatAdapter(chatMessages);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewChat.setAdapter(chatAdapter);

        sendChat.setOnClickListener(v -> {
            String question = inputChat.getText().toString().trim();
            if (question.isEmpty()) return;

            chatMessages.add(new ChatMessage("user", question));
            chatAdapter.notifyItemInserted(chatMessages.size() - 1);
            recyclerViewChat.scrollToPosition(chatMessages.size() - 1);
            inputChat.setText("");

            askAiAboutRecipe(question, new AiResponseRepository.AiCallback() {
                @Override
                public void onSuccess(String answer) {
                    chatMessages.add(new ChatMessage("ai", answer));
                    chatAdapter.notifyItemInserted(chatMessages.size() - 1);
                    recyclerViewChat.scrollToPosition(chatMessages.size() - 1);
                    ChatMemoryCache.put(recipe.id, chatMessages);
                }

                @Override
                public void onError(String error) {
                    chatMessages.add(new ChatMessage("ai", "Greška: " + error));
                    chatAdapter.notifyItemInserted(chatMessages.size() - 1);
                    recyclerViewChat.scrollToPosition(chatMessages.size() - 1);
                    ChatMemoryCache.put(recipe.id, chatMessages);
                }
            });
        });








        Intent intent = getIntent();
        if (intent != null) {
            String recipeName = intent.getStringExtra("RECIPE_NAME");
            int recipeCalories = intent.getIntExtra("RECIPE_CALORIES", -1);
            float recipeRating = intent.getFloatExtra("RECIPE_RATING", -1f);
            String image1 = intent.getStringExtra("IMAGE_1");
            String image2 = intent.getStringExtra("IMAGE_2");
            String image3 = intent.getStringExtra("IMAGE_3");
            recipeId = intent.getIntExtra("RECIPE_ID", -1);
            String recipeInternalName = intent.getStringExtra("RECIPE_INTERNAL_NAME");

            nameTextView.setText(recipeName != null ? recipeName : getString(R.string.unknown_recipe));
            caloriesTextView.setText(recipeCalories != -1 ? getString(R.string.text_calories) + recipeCalories : getString(R.string.text_calories) + "N/A");
            ratingTextView.setText(recipeRating != -1f ? getString(R.string.text_rating) + recipeRating : getString(R.string.text_rating) + "N/A");

            if (recipeId != -1) {

                final int finalRecipeId = recipeId;

                loadInstructionSteps(recipeId);
                updateLastViewed(recipeId);

                AppDatabase.databaseWriteExecutor.execute(() -> {
                    List<Recipe> all = AppDatabase.getInstance(getApplicationContext()).recipeDao().getAllRecipesSync();
                    for (Recipe r : all) {
                        if (r.id == finalRecipeId) {
                            runOnUiThread(() -> {
                                recipe = r; // ⚠️ obavezno ovdje!

                                chatMessages = new ArrayList<>(ChatMemoryCache.get(recipe.id));
                                chatAdapter = new ChatAdapter(chatMessages);
                                recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
                                recyclerViewChat.setAdapter(chatAdapter);

                                int icon = r.isFavorite ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite;
                                favoriteIcon.setImageResource(icon);

                                favoriteIcon.setOnClickListener(v -> {
                                    boolean newFavoriteStatus = !r.isFavorite;
                                    int iconRes = newFavoriteStatus ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite;
                                    favoriteIcon.setImageResource(iconRes);

                                    AppDatabase.databaseWriteExecutor.execute(() -> {
                                        AppDatabase.getInstance(getApplicationContext())
                                                .recipeDao()
                                                .updateFavoriteWithTimestamp(
                                                        r.id,
                                                        newFavoriteStatus,
                                                        r.lastViewedTimestamp > 0 ? r.lastViewedTimestamp : System.currentTimeMillis()
                                                );
                                    });


                                    r.isFavorite = newFavoriteStatus;
                                });

                            });
                            break;
                        }
                    }
                });

            } else {
                Toast.makeText(this, getString(R.string.error_recipeid), Toast.LENGTH_SHORT).show();
            }

            StorageReference storage = FirebaseStorage.getInstance().getReference().child("RecipeImages");
            if (image1 != null && !image1.isEmpty()) loadImageIfExists(storage.child(image1), img1);
            else img1.setVisibility(View.GONE);
            if (image2 != null && !image2.isEmpty()) loadImageIfExists(storage.child(image2), img2);
            else img2.setVisibility(View.GONE);
            if (image3 != null && !image3.isEmpty()) loadImageIfExists(storage.child(image3), img3);
            else img3.setVisibility(View.GONE);
        }

        ImageView imageViewEdit = findViewById(R.id.imageViewEditRecipe);

        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Recipe> all = AppDatabase.getInstance(getApplicationContext()).recipeDao().getAllRecipesSync();
            for (Recipe r : all) {
                if (r.id == recipeId) {
                    recipe = r;
                    runOnUiThread(() -> {
                        int icon = r.isFavorite ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite;
                        favoriteIcon.setImageResource(icon);

                        favoriteIcon.setOnClickListener(v -> {
                            boolean newFavoriteStatus = !r.isFavorite;
                            int iconRes = newFavoriteStatus ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite;
                            favoriteIcon.setImageResource(iconRes);

                            AppDatabase.databaseWriteExecutor.execute(() -> {
                                AppDatabase.getInstance(getApplicationContext())
                                        .recipeDao()
                                        .updateFavoriteWithTimestamp(
                                                r.id,
                                                newFavoriteStatus,
                                                r.lastViewedTimestamp > 0 ? r.lastViewedTimestamp : System.currentTimeMillis()
                                        );
                            });

                            r.isFavorite = newFavoriteStatus;
                        });

                        if (r.createdByUser) {
                            imageViewEdit.setVisibility(View.VISIBLE);
                            imageViewEdit.setOnClickListener(v -> {
                                Intent editIntent = new Intent(RecipeDetailActivity.this, AddEditRecipeActivity.class);
                                editIntent.putExtra("edit_mode", true);
                                editIntent.putExtra("recipe_id", r.id);
                                startActivityForResult(editIntent, 2001);

                            });
                        } else {
                            imageViewEdit.setVisibility(View.GONE);
                        }

                    });
                    break;
                }
            }
        });



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (recipe != null) {
            ChatMemoryCache.clear(recipe.id);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2001 && resultCode == RESULT_OK && data != null) {
            int updatedId = data.getIntExtra("updated_recipe_id", -1);
            if (updatedId != -1) {
                reloadRecipeDetails(updatedId);
            }
        }
    }
    private void reloadRecipeDetails(int recipeId) {
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());

        AppDatabase.databaseWriteExecutor.execute(() -> {
            Recipe recipe = db.recipeDao().getRecipeByIdSync(recipeId);
            List<InstructionStep> steps = db.instructionStepDao().getStepsForRecipeSync(recipeId);

            runOnUiThread(() -> {
                if (recipe != null) {
                    String language = getResources().getConfiguration().locale.getLanguage();

                    String displayName;
                    if ("bs".equals(language)) {
                        displayName = (recipe.nameBs != null && !recipe.nameBs.isEmpty()) ? recipe.nameBs : recipe.nameEn;
                    } else {
                        displayName = (recipe.nameEn != null && !recipe.nameEn.isEmpty()) ? recipe.nameEn : recipe.nameBs;
                    }

                    nameTextView.setText(displayName);
                    caloriesTextView.setText(getString(R.string.calories_format, recipe.calories));
                    ratingTextView.setText(getString(R.string.rating_format, recipe.rating));
                    instructionStepAdapter.setSteps(steps);
                } else {
                    nameTextView.setText(getString(R.string.unknown_recipe));
                    caloriesTextView.setText(getString(R.string.calories_format, 0));
                    ratingTextView.setText(getString(R.string.rating_format, 0));
                    instructionStepAdapter.setSteps(Collections.emptyList());
                }
            });
        });
    }




    private void toggleChatVisibility() {
        FrameLayout aiChatContainer = findViewById(R.id.aiChatContainer);
        if (aiChatContainer == null) return;

        if (isChatVisible) {
            Animation slideDown = AnimationUtils.loadAnimation(this, R.anim.chat_hide_to_fab);
            slideDown.setAnimationListener(new Animation.AnimationListener() {
                @Override public void onAnimationStart(Animation animation) {}

                @Override public void onAnimationEnd(Animation animation) {
                    aiChatContainer.setVisibility(View.GONE);
                    isChatVisible = false;

                    aiChatContainer.clearAnimation();
                    aiChatContainer.setScaleX(1f);
                    aiChatContainer.setScaleY(1f);
                    aiChatContainer.setAlpha(1f);
                }

                @Override public void onAnimationRepeat(Animation animation) {}
            });
            aiChatContainer.startAnimation(slideDown);
        } else {
            aiChatContainer.setVisibility(View.VISIBLE);
            aiChatContainer.bringToFront();
            Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.chat_popup_from_fab);
            aiChatContainer.startAnimation(slideUp);
            isChatVisible = true;
        }
    }









    private void updateLastViewed(int recipeId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            long timestamp = System.currentTimeMillis();
            AppDatabase.getInstance(getApplicationContext())
                    .recipeDao()
                    .updateLastViewed(recipeId, timestamp);
        });
    }



    private void loadInstructionSteps(int recipeId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<InstructionStep> steps = AppDatabase.getInstance(getApplicationContext())
                    .instructionStepDao()
                    .getStepsForRecipeSync(recipeId);

            runOnUiThread(() -> instructionStepAdapter.setSteps(steps));
        });
    }

    private void loadImageIfExists(StorageReference ref, ImageView imageView) {
        ref.getDownloadUrl().addOnSuccessListener(uri -> {
            imageView.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(uri)
                    .placeholder(R.drawable.placeholder)
                    .into(imageView);
        }).addOnFailureListener(e -> {
            imageView.setVisibility(View.GONE);
        });
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase));
    }

    private void showAiDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("AI Pomoć");

        final EditText input = new EditText(this);
        input.setHint("Postavi pitanje o receptu...");
        builder.setView(input);

        builder.setPositiveButton("Pošalji", (dialog, which) -> {
            String question = input.getText().toString().trim();
            if (!question.isEmpty()) {
                askAiAboutRecipe(question);
            }
        });

        builder.setNegativeButton("Otkaži", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void askAiAboutRecipe(String question) {
        askAiAboutRecipe(question, new AiResponseRepository.AiCallback() {
            @Override
            public void onSuccess(String answer) {
                runOnUiThread(() -> showAiAnswer(answer));
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> Toast.makeText(RecipeDetailActivity.this, error, Toast.LENGTH_LONG).show());
            }
        });
    }

    private void askAiAboutRecipe(String question, AiResponseRepository.AiCallback callback) {
        if (recipe == null) {
            callback.onError("Recept nije dostupan.");
            return;
        }

        String lang = getResources().getConfiguration().locale.getLanguage();
        String title = recipe.getNameForLang(lang);
        String ingredients = recipe.getIngredientsForLang(lang);

        String[] ingredientLines = ingredients.split("\n");
        String limitedIngredients = TextUtils.join("\n", Arrays.copyOfRange(ingredientLines, 0, Math.min(5, ingredientLines.length)));

        List<InstructionStep> steps = instructionStepAdapter.getSteps();
        StringBuilder instructionsBuilder = new StringBuilder();
        for (int i = 0; i < Math.min(3, steps.size()); i++) {
            String stepDescription = lang.equals("bs") ? steps.get(i).descriptionBs : steps.get(i).descriptionEn;
            instructionsBuilder.append((i + 1)).append(". ").append(stepDescription).append("\n");
        }

        String recipeText = "Naziv: " + title + "\nSastojci:\n" + limitedIngredients + "\nUpute:\n" + instructionsBuilder;

        AiResponseRepository aiRepo = new AiResponseRepository();
        aiRepo.askAi(recipeText, question, callback);
    }



    private void showAiAnswer(String answer) {
        new AlertDialog.Builder(this)
                .setTitle("AI Odgovor")
                .setMessage(answer)
                .setPositiveButton("Zatvori", null)
                .show();
    }

}
