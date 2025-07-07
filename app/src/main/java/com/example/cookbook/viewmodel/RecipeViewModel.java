package com.example.cookbook.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.cookbook.data.RecipeRepository;
import com.example.cookbook.model.Recipe;

import java.util.List;
public class RecipeViewModel extends ViewModel {
    private final RecipeRepository recipeRepository;
    private final MediatorLiveData<List<Recipe>> ingredientSearchResults = new MediatorLiveData<>();
    private LiveData<List<Recipe>> currentSearchSource;

    public RecipeViewModel(RecipeRepository repository) {
        this.recipeRepository = repository;
    }

    public LiveData<List<Recipe>> getIngredientSearchResults() {
        return ingredientSearchResults;
    }

    public void searchRecipesByIngredients(String availableIngredients, String missingIngredients) {
        String[] available = availableIngredients.isEmpty() ? new String[0] : availableIngredients.split("\\s*,\\s*");
        String[] missing = missingIngredients.isEmpty() ? new String[0] : missingIngredients.split("\\s*,\\s*");

        LiveData<List<Recipe>> newSource = recipeRepository.searchByIngredients(available, missing);

        if (currentSearchSource != null) {
            ingredientSearchResults.removeSource(currentSearchSource);
        }

        currentSearchSource = newSource;
        ingredientSearchResults.addSource(currentSearchSource, ingredientSearchResults::setValue);
    }



    public static class Factory implements ViewModelProvider.Factory {
        private final RecipeRepository repository;

        public Factory(RecipeRepository repository) {
            this.repository = repository;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(RecipeViewModel.class)) {
                return (T) new RecipeViewModel(repository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    public LiveData<List<Recipe>> getSortedRecipes(String key) {
        switch (key) {
            case "name_en_asc": return recipeRepository.getRecipesByNameAsc();
            case "name_en_desc": return recipeRepository.getRecipesByNameDesc();
            case "name_bs_asc": return recipeRepository.getRecipesByNameAscBs();
            case "name_bs_desc": return recipeRepository.getRecipesByNameDescBs();
            case "category_en": return recipeRepository.getRecipesByCategory();
            case "category_bs": return recipeRepository.getRecipesByCategoryBs();
            case "calories": return recipeRepository.getRecipesByCalories();
            case "rating": return recipeRepository.getRecipesByRating();
            default: return recipeRepository.getAllRecipes();
        }
    }






}