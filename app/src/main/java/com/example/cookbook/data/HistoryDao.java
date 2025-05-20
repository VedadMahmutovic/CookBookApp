package com.example.cookbook.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cookbook.model.Recipe;

import java.util.List;

    @Dao
    public interface HistoryDao {
        @Query("SELECT * FROM Recipe WHERE last_viewed > 0 ORDER BY last_viewed DESC LIMIT :limit")
        LiveData<List<Recipe>> getRecentlyViewedRecipes(int limit);

        @Update
        void updateRecipe(Recipe recipe);
    }
