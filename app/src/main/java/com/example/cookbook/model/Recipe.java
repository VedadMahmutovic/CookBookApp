package com.example.cookbook.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;

import java.util.List;
import java.util.Locale;

@Entity
public class Recipe {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name_en")
    public String nameEn;

    @ColumnInfo(name = "name_bs")
    public String nameBs;

    @ColumnInfo(name = "ingredients_en")
    public String ingredientsEn;

    @ColumnInfo(name = "ingredients_bs")
    public String ingredientsBs;

    @ColumnInfo(name = "category_en")
    public String categoryEn;

    @ColumnInfo(name = "category_bs")
    public String categoryBs;

    @ColumnInfo(name = "calories")
    public int calories;

    @ColumnInfo(name = "image_url")
    public String imageUrl;

    @ColumnInfo(name = "rating")
    public float rating;

    @ColumnInfo(name = "is_favorite")
    public boolean isFavorite;

    @ColumnInfo(name = "last_viewed")
    public long lastViewedTimestamp;

    @ColumnInfo(name = "image1")
    public String image1;

    @ColumnInfo(name = "image2")
    public String image2;

    @ColumnInfo(name = "image3")
    public String image3;

    @ColumnInfo(name = "created_by_user")
    public boolean createdByUser = false;
    @ColumnInfo(name = "internal_name")
    public String internalName;

    @Ignore
    public Recipe() {}

    public Recipe(String nameEn, String nameBs,
                  String ingredientsEn, String ingredientsBs,
                  int calories, String imageUrl, float rating,
                  boolean isFavorite, long lastViewedTimestamp,
                  String categoryEn, String categoryBs, String internalName) {

        this.nameEn = nameEn;
        this.nameBs = nameBs;
        this.ingredientsEn = ingredientsEn;
        this.ingredientsBs = ingredientsBs;
        this.categoryEn = categoryEn;
        this.categoryBs = categoryBs;
        this.calories = calories;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.isFavorite = isFavorite;
        this.lastViewedTimestamp = lastViewedTimestamp;
        this.internalName = internalName;
    }

    @Ignore
    public String getNameForLang(String lang) {
        if (lang.equals("bs") && nameBs != null && !nameBs.isEmpty()) return nameBs;
        return (nameEn != null && !nameEn.isEmpty()) ? nameEn : nameBs;
    }

    @Ignore
    public String getIngredientsForLang(String lang) {
        if (lang.equals("bs") && ingredientsBs != null && !ingredientsBs.isEmpty()) return ingredientsBs;
        return (ingredientsEn != null && !ingredientsEn.isEmpty()) ? ingredientsEn : ingredientsBs;
    }

    @Ignore
    public String getCategoryForLang(String lang) {
        if (lang.equals("bs") && categoryBs != null && !categoryBs.isEmpty()) return categoryBs;
        return (categoryEn != null && !categoryEn.isEmpty()) ? categoryEn : categoryBs;
    }

    @Ignore
    public boolean containsIngredient(String ingredient, String lang) {
        String ing = getIngredientsForLang(lang).toLowerCase(Locale.ROOT);
        return ing.contains(ingredient.toLowerCase(Locale.ROOT).trim());
    }

    @Ignore
    public boolean containsAllIngredients(String[] ingredientsToCheck, String lang) {
        if (ingredientsToCheck == null || ingredientsToCheck.length == 0) return true;
        for (String ingredient : ingredientsToCheck) {
            if (!containsIngredient(ingredient, lang)) return false;
        }
        return true;
    }

    @Ignore
    public boolean containsNoneOfIngredients(String[] ingredientsToCheck, String lang) {
        if (ingredientsToCheck == null || ingredientsToCheck.length == 0) return true;
        for (String ingredient : ingredientsToCheck) {
            if (containsIngredient(ingredient, lang)) return false;
        }
        return true;
    }

    @Ignore
    public boolean containsAllIngredients(List<String> ingredients, String lang) {
        for (String ingredient : ingredients) {
            if (!containsIngredient(ingredient, lang)) return false;
        }
        return true;
    }

    @Ignore
    public boolean containsAnyIngredient(List<String> ingredients, String lang) {
        for (String ingredient : ingredients) {
            if (containsIngredient(ingredient, lang)) return true;
        }
        return false;
    }
}
