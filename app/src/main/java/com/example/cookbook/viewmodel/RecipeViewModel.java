package com.example.cookbook.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.MutableLiveData;

import com.example.cookbook.data.RecipeRepository;
import com.example.cookbook.model.Recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeViewModel extends ViewModel {
    private final RecipeRepository recipeRepository;
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>("");
    private final MutableLiveData<List<Recipe>> searchResults = new MutableLiveData<>();
    private final MediatorLiveData<List<Recipe>> ingredientSearchResults = new MediatorLiveData<>();
    private LiveData<List<Recipe>> currentSearchSource;

    public RecipeViewModel(RecipeRepository repository) {
        this.recipeRepository = repository;
    }

    public LiveData<List<Recipe>> getSearchResults() {
        return recipeRepository.searchRecipes(searchQuery.getValue() == null ? "" : searchQuery.getValue());
    }

    public LiveData<List<Recipe>> getIngredientSearchResults() {
        return ingredientSearchResults;
    }

    public void setSearchQuery(String query) {
        searchQuery.setValue(query);
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

    public void toggleFavorite(Recipe recipe) {
        boolean newStatus = !recipe.isFavorite;
        recipeRepository.updateFavoriteStatus(recipe.id, newStatus);

    }

    public LiveData<List<Recipe>> getSortedRecipes(String kriterij) {
        switch (kriterij) {
            case "name_asc": return recipeRepository.getRecipesByNameAsc();
            case "name_desc": return recipeRepository.getRecipesByNameDesc();
            case "calories": return recipeRepository.getRecipesByCalories();
            case "rating": return recipeRepository.getRecipesByRating();
            case "category": return recipeRepository.getRecipesByCategory();
            default: return recipeRepository.getAllRecipes();
        }
    }




}