package com.example.cookbook.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Recipe.class,
        parentColumns = "id",
        childColumns = "recipeId",
        onDelete = ForeignKey.CASCADE))
public class InstructionStep {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "recipeId")
    public int recipeId;

    @ColumnInfo(name = "stepNumber")
    public int stepNumber;

    @ColumnInfo(name = "description_en")
    public String descriptionEn;

    @ColumnInfo(name = "description_bs")
    public String descriptionBs;

    @ColumnInfo(name = "imageUrl")
    public String imageUrl;

    public InstructionStep(int recipeId, int stepNumber, String descriptionEn, String descriptionBs, String imageUrl) {
        this.recipeId = recipeId;
        this.stepNumber = stepNumber;
        this.descriptionEn = descriptionEn;
        this.descriptionBs = descriptionBs;
        this.imageUrl = imageUrl;
    }
}
