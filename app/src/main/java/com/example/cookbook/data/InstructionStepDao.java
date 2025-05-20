package com.example.cookbook.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cookbook.model.InstructionStep;

import java.util.List;

@Dao
public interface InstructionStepDao {

    @Insert
    void insert(InstructionStep instructionStep);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<InstructionStep> steps);
    @Update
    void update(InstructionStep instructionStep);
    @Delete
    void delete(InstructionStep instructionStep);
    @Query("SELECT * FROM InstructionStep WHERE recipeId = :recipeId ORDER BY stepNumber ASC")
    LiveData<List<InstructionStep>> getStepsForRecipe(int recipeId);
    @Query("SELECT * FROM InstructionStep WHERE recipeId = :recipeId ORDER BY stepNumber ASC")
    List<InstructionStep> getStepsForRecipeSync(int recipeId);
    @Query("DELETE FROM InstructionStep WHERE recipeId = :recipeId")
    void deleteStepsForRecipe(int recipeId);
    @Query("DELETE FROM InstructionStep WHERE recipeId = :recipeId")
    void deleteStepsByRecipeId(int recipeId);

}