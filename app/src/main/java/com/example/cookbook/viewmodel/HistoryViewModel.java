package com.example.cookbook.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.cookbook.data.HistoryRepository;
import com.example.cookbook.model.Recipe;

import java.util.List;

public class HistoryViewModel extends ViewModel {
    private final HistoryRepository repository;
    private final LiveData<List<Recipe>> recentlyViewedRecipes;

    public HistoryViewModel(Application application) {
        repository = new HistoryRepository(application);
        recentlyViewedRecipes = repository.getRecentlyViewedRecipes(10);
    }

    public LiveData<List<Recipe>> getRecentlyViewedRecipes() {
        return recentlyViewedRecipes;
    }

    public void updateRecipe(Recipe recipe) {
        repository.updateRecipe(recipe);
    }
}