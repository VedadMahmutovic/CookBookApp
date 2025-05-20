package com.example.cookbook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.data.AppDatabase;
import com.example.cookbook.data.InstructionStepDao;
import com.example.cookbook.data.RecipeDao;
import com.example.cookbook.model.InstructionStep;
import com.example.cookbook.model.Recipe;
import com.example.cookbook.ui.theme.InstructionStepInputAdapter;
import com.example.cookbook.utils.LocaleHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;

public class AddEditRecipeActivity extends AppCompatActivity {

    private static final int REQUEST_SELECT_IMAGE = 1001;

    private ImageView imageView;
    private Button buttonImage, buttonAddStep, buttonSave;
    private EditText nameInput, ingredientsInput, caloriesInput, categoryInput;
    private RecyclerView stepsRecycler;
    private InstructionStepInputAdapter stepAdapter;
    private Uri selectedImageUri = null;
    private File savedImageFile = null;
    private List<InstructionStep> steps = new ArrayList<>();
    private boolean isEdit = false;
    private int editRecipeId = -1;
    private Button buttonDelete;
    private RatingBar ratingBar;
    private TextView textViewRatingValue;




    private RecipeDao recipeDao;
    private InstructionStepDao stepDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_recipe);
        isEdit = getIntent().getBooleanExtra("edit_mode", false);
        editRecipeId = getIntent().getIntExtra("recipe_id", -1);
        imageView = findViewById(R.id.imageViewRecipe);
        buttonImage = findViewById(R.id.buttonSelectImage);
        buttonAddStep = findViewById(R.id.buttonAddStep);
        buttonSave = findViewById(R.id.buttonSaveRecipe);
        nameInput = findViewById(R.id.editTextName);
        ingredientsInput = findViewById(R.id.editTextIngredients);
        caloriesInput = findViewById(R.id.editTextCalories);
        categoryInput = findViewById(R.id.editTextCategory);
        stepsRecycler = findViewById(R.id.stepsRecyclerView);
        buttonDelete = findViewById(R.id.buttonDeleteRecipe);
        ratingBar = findViewById(R.id.ratingBar);
        textViewRatingValue = findViewById(R.id.textViewRatingValue);


        ratingBar.setOnRatingBarChangeListener((bar, rating, fromUser) -> {
            textViewRatingValue.setText(String.valueOf(rating));
        });






        stepAdapter = new InstructionStepInputAdapter(steps, this, stepsRecycler);

        stepsRecycler.setLayoutManager(new LinearLayoutManager(this));
        stepsRecycler.setAdapter(stepAdapter);

        recipeDao = AppDatabase.getInstance(this).recipeDao();
        stepDao = AppDatabase.getInstance(this).instructionStepDao();

        if (isEdit && editRecipeId != -1) {
            AppDatabase.databaseWriteExecutor.execute(() -> {
                Recipe recipe = AppDatabase.getInstance(this).recipeDao().getRecipeByIdSync(editRecipeId);
                List<InstructionStep> stepovi = AppDatabase.getInstance(this).instructionStepDao().getStepsForRecipeSync(editRecipeId);

                runOnUiThread(() -> {
                    nameInput.setText(recipe.nameEn);
                    ingredientsInput.setText(recipe.ingredientsEn);
                    caloriesInput.setText(String.valueOf(recipe.calories));
                    categoryInput.setText(recipe.categoryEn);
                    ratingBar.setRating(recipe.rating);
                    textViewRatingValue.setText(String.valueOf(recipe.rating));
                    if (recipe.imageUrl != null && !recipe.imageUrl.isEmpty()) {
                        selectedImageUri = Uri.fromFile(new File(recipe.imageUrl));
                        imageView.setImageBitmap(BitmapFactory.decodeFile(recipe.imageUrl));
                        savedImageFile = new File(recipe.imageUrl);
                    }

                    steps.clear();
                    steps.addAll(stepovi);
                    stepAdapter.notifyDataSetChanged();
                });
            });
        }



        buttonImage.setOnClickListener(v -> selectImage());

        buttonAddStep.setOnClickListener(v -> {
            int nextStepNum = steps.size() + 1;
            steps.add(new InstructionStep(0, nextStepNum, "", "", null));
            stepAdapter.notifyItemInserted(steps.size() - 1);
        });


        buttonSave.setOnClickListener(v -> saveRecipe());

        if (isEdit) {
            buttonDelete.setVisibility(View.VISIBLE);
            buttonDelete.setOnClickListener(v -> {
                new android.app.AlertDialog.Builder(this)
                        .setTitle(R.string.delete_confirm)
                        .setMessage(R.string.text_delete_recipe)
                        .setPositiveButton(getString(R.string.text_yes), (dialog, which) -> {
                            if (editRecipeId != -1) {
                                AppDatabase.databaseWriteExecutor.execute(() -> {
                                    Recipe recipeToDelete = new Recipe();
                                    recipeToDelete.id = editRecipeId;
                                    recipeDao.delete(recipeToDelete);
                                    stepDao.deleteStepsByRecipeId(editRecipeId);

                                    runOnUiThread(() -> {
                                        Toast.makeText(this, getString(R.string.deleted_recipe), Toast.LENGTH_SHORT).show();
                                        finish();
                                    });
                                });
                            }
                        })
                        .setNegativeButton(getString(R.string.title_cancel), null)
                        .show();
            });

        } else {
            buttonDelete.setVisibility(View.GONE);
        }
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SELECT_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            try {
                InputStream stream = getContentResolver().openInputStream(selectedImageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(stream);
                imageView.setImageBitmap(bitmap);

                // Save image locally
                File dir = new File(getFilesDir(), "images");
                if (!dir.exists()) dir.mkdirs();
                String filename = UUID.randomUUID().toString() + ".jpg";
                savedImageFile = new File(dir, filename);

                FileOutputStream out = new FileOutputStream(savedImageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out);
                out.close();



            } catch (Exception e) {
                Toast.makeText(this, getString(R.string.text_upload_fail), Toast.LENGTH_SHORT).show();
            }

        }

        else if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            if (stepAdapter instanceof InstructionStepInputAdapter) {
                ((InstructionStepInputAdapter) stepAdapter).handleImageResult(requestCode, data.getData());
            }
        }
    }

    private void saveRecipe() {
        String name = nameInput.getText().toString().trim();
        String ingredients = ingredientsInput.getText().toString().trim();
        String caloriesStr = caloriesInput.getText().toString().trim();
        String category = categoryInput.getText().toString().trim();

        stepAdapter.updateStepDescriptions();

        if (name.isEmpty() || ingredients.isEmpty() || caloriesStr.isEmpty()) {
            Toast.makeText(this, getString(R.string.text_field_fill), Toast.LENGTH_SHORT).show();
            return;
        }

        int calories = Integer.parseInt(caloriesStr);
        float rating = ratingBar.getRating();
        String imagePath = savedImageFile != null ? savedImageFile.getAbsolutePath() : "";

        Recipe recipe = new Recipe();
        recipe.nameEn = name;
        recipe.nameBs = name;
        recipe.ingredientsEn = ingredients;
        recipe.ingredientsBs = ingredients;
        recipe.categoryEn = category;
        recipe.categoryBs = category;
        recipe.calories = calories;
        recipe.imageUrl = imagePath;
        recipe.rating = rating;
        recipe.isFavorite = false;
        recipe.lastViewedTimestamp = System.currentTimeMillis();
        recipe.createdByUser = true;

        if (isEdit && editRecipeId != -1) {
            recipe.id = editRecipeId;

            AppDatabase.databaseWriteExecutor.execute(() -> {
                recipeDao.update(recipe);
                stepDao.deleteStepsByRecipeId(editRecipeId);
                for (InstructionStep step : steps) {
                    step.recipeId = editRecipeId;
                    stepDao.insert(step);
                }

                runOnUiThread(() -> {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("updated_recipe_id", recipe.id);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                });
            });

        } else {
            AppDatabase.databaseWriteExecutor.execute(() -> {
                long recipeId = recipeDao.insertRecipeReturnId(recipe);
                for (InstructionStep step : steps) {
                    step.recipeId = (int) recipeId;
                    stepDao.insert(step);
                }

                runOnUiThread(() -> {
                    Toast.makeText(this, getString(R.string.text_recipe_saved), Toast.LENGTH_SHORT).show();
                    finish();
                });
            });
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (LocaleHelper.isLangChanged(this, Locale.getDefault().getLanguage())) {
            recreate();
        }
    }


}