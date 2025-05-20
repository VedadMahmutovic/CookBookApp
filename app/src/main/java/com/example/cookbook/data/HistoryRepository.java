package com.example.cookbook.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.cookbook.model.Recipe;

import java.util.List;

public class HistoryRepository {
    private final HistoryDao historyDao;

    public HistoryRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        historyDao = db.historyDao();
    }

    public LiveData<List<Recipe>> getRecentlyViewedRecipes(int limit) {
        return historyDao.getRecentlyViewedRecipes(limit);
    }

    public void updateRecipe(Recipe recipe) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            historyDao.updateRecipe(recipe);
        });
    }
}