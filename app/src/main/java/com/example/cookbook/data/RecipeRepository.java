package com.example.cookbook.data;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.cookbook.model.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecipeRepository {

    private final RecipeDao recipeDao;
    private final LiveData<List<Recipe>> allRecipes;
    private final LiveData<List<Recipe>> favoriteRecipes;
    private final LiveData<Recipe> randomRecipe;

    public RecipeRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        recipeDao = db.recipeDao();
        allRecipes = recipeDao.getAllRecipes();
        favoriteRecipes = recipeDao.getFavoriteRecipes();
        randomRecipe = recipeDao.getRandomRecipe();
    }

    public LiveData<List<Recipe>> getAllRecipes() {
        return allRecipes;
    }

    public LiveData<List<Recipe>> searchRecipes(String search) {
        return recipeDao.searchRecipes(search);
    }

    public LiveData<List<Recipe>> getFavoriteRecipes() {
        return favoriteRecipes;
    }

    public LiveData<Recipe> getRandomRecipe() {
        return randomRecipe;
    }

    public LiveData<List<Recipe>> searchByIngredients(String[] availableIngredients, String[] missingIngredients) {
        MutableLiveData<List<Recipe>> results = new MutableLiveData<>();
        String lang = Locale.getDefault().getLanguage();
        boolean useBs = lang.equals("bs");

        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                List<Recipe> all = recipeDao.getAllRecipesSync();
                List<Recipe> filtered = new ArrayList<>();

                if ((availableIngredients == null || availableIngredients.length == 0) &&
                        (missingIngredients == null || missingIngredients.length == 0)) {
                    results.postValue(all);
                    return;
                }

                for (Recipe recipe : all) {
                    String ingredients = useBs ? recipe.ingredientsBs : recipe.ingredientsEn;
                    if (ingredients == null) ingredients = "";

                    boolean matches = true;

                    if (availableIngredients != null && availableIngredients.length > 0) {
                        for (String ingredient : availableIngredients) {
                            if (ingredient != null && !ingredient.trim().isEmpty()) {
                                if (!ingredients.toLowerCase().contains(ingredient.toLowerCase().trim())) {
                                    matches = false;
                                    break;
                                }
                            }
                        }
                    }

                    if (matches && missingIngredients != null && missingIngredients.length > 0) {
                        for (String ingredient : missingIngredients) {
                            if (ingredient != null && !ingredient.trim().isEmpty()) {
                                if (ingredients.toLowerCase().contains(ingredient.toLowerCase().trim())) {
                                    matches = false;
                                    break;
                                }
                            }
                        }
                    }

                    if (matches) {
                        filtered.add(recipe);
                    }
                }

                results.postValue(filtered);
            } catch (Exception e) {
                Log.e("RecipeRepository", "Error searching recipes", e);
                results.postValue(new ArrayList<>());
            }
        });

        return results;
    }


    public void update(Recipe recipe) {
        AppDatabase.databaseWriteExecutor.execute(() -> recipeDao.update(recipe));
    }

    public void updateFavoriteStatus(int recipeId, boolean isFavorite) {
        AppDatabase.databaseWriteExecutor.execute(() -> recipeDao.updateFavoriteStatus(recipeId, isFavorite));
    }

    public int getRecipeCountSync() {
        return recipeDao.getRecipeCount();
    }

    public LiveData<List<Recipe>> getRecipesByNameAsc() {
        return recipeDao.getRecipesByNameAsc();
    }

    public LiveData<List<Recipe>> getRecipesByNameDesc() {
        return recipeDao.getRecipesByNameDesc();
    }

    public LiveData<List<Recipe>> getRecipesByCalories() {
        return recipeDao.getRecipesByCalories();
    }

    public LiveData<List<Recipe>> getRecipesByRating() {
        return recipeDao.getRecipesByRating();
    }

    public LiveData<List<Recipe>> getRecipesByCategory() {
        return recipeDao.getRecipesByCategory();
    }
    public LiveData<List<Recipe>> getRecipesByNameAscBs() {
        return recipeDao.getRecipesByNameAscBs();
    }

    public LiveData<List<Recipe>> getRecipesByNameDescBs() {
        return recipeDao.getRecipesByNameDescBs();
    }

    public LiveData<List<Recipe>> getRecipesByCategoryBs() {
        return recipeDao.getRecipesByCategoryBs();
    }

}
