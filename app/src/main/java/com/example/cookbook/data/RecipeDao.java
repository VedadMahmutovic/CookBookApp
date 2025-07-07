package com.example.cookbook.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cookbook.model.Recipe;

import java.util.List;

@Dao
public interface RecipeDao {

    @Insert
    void insert(Recipe recipe);

    @Insert
    long insertRecipeReturnId(Recipe recipe);

    @Update
    void update(Recipe recipe);

    @Delete
    void delete(Recipe recipe);

    @Query("SELECT * FROM Recipe ORDER BY name_en ASC")
    LiveData<List<Recipe>> getAllRecipes();

    @Query("SELECT * FROM Recipe WHERE " +
            "name_en LIKE '%' || :search || '%' OR " +
            "name_bs LIKE '%' || :search || '%' OR " +
            "ingredients_en LIKE '%' || :search || '%' OR " +
            "ingredients_bs LIKE '%' || :search || '%'")
    LiveData<List<Recipe>> searchRecipes(String search);

    @Query("SELECT * FROM Recipe WHERE is_favorite = 1")
    LiveData<List<Recipe>> getFavoriteRecipes();

    @Query("SELECT * FROM Recipe ORDER BY RANDOM() LIMIT 1")
    LiveData<Recipe> getRandomRecipe();

    @Query("SELECT * FROM Recipe")
    List<Recipe> getAllRecipesSync();

    @Query("SELECT COUNT(*) FROM Recipe")
    int getRecipeCount();

    @Query("UPDATE Recipe SET is_favorite = :isFavorite WHERE id = :id")
    void updateFavoriteStatus(int id, boolean isFavorite);

    @Query("UPDATE Recipe SET last_viewed = :timestamp WHERE id = :id")
    void updateLastViewed(int id, long timestamp);

    @Query("UPDATE Recipe SET is_favorite = :isFavorite, last_viewed = :lastViewed WHERE id = :id")
    void updateFavoriteWithTimestamp(int id, boolean isFavorite, long lastViewed);

    @Query("SELECT * FROM Recipe ORDER BY name_en ASC")
    LiveData<List<Recipe>> getRecipesByNameAsc();

    @Query("SELECT * FROM Recipe ORDER BY name_en DESC")
    LiveData<List<Recipe>> getRecipesByNameDesc();

    @Query("SELECT * FROM Recipe ORDER BY calories ASC")
    LiveData<List<Recipe>> getRecipesByCalories();

    @Query("SELECT * FROM Recipe ORDER BY rating DESC")
    LiveData<List<Recipe>> getRecipesByRating();

    @Query("SELECT * FROM Recipe ORDER BY category_en ASC")
    LiveData<List<Recipe>> getRecipesByCategory();

    @Query("SELECT * FROM Recipe WHERE id = :id LIMIT 1")
    Recipe getRecipeByIdSync(int id);
    @Query("SELECT * FROM Recipe ORDER BY name_bs ASC")
    LiveData<List<Recipe>> getRecipesByNameAscBs();

    @Query("SELECT * FROM Recipe ORDER BY name_bs DESC")
    LiveData<List<Recipe>> getRecipesByNameDescBs();

    @Query("SELECT * FROM Recipe ORDER BY category_bs ASC")
    LiveData<List<Recipe>> getRecipesByCategoryBs();

}
