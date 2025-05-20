package com.example.cookbook.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.cookbook.model.InstructionStep;
import com.example.cookbook.model.Recipe;

import java.util.Locale;

public class LangUtils {
    public static String getCurrentLang(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return prefs.getString("app_lang", Locale.getDefault().getLanguage());
    }

    public static String getRecipeName(Recipe recipe, String lang) {
        if (lang.equals("bs") && recipe.nameBs != null && !recipe.nameBs.isEmpty()) return recipe.nameBs;
        if (recipe.nameEn != null && !recipe.nameEn.isEmpty()) return recipe.nameEn;
        return recipe.nameBs != null ? recipe.nameBs : "";
    }

    public static String getStepDescription(InstructionStep step, String lang) {
        if (lang.equals("bs") && step.descriptionBs != null && !step.descriptionBs.isEmpty()) return step.descriptionBs;
        if (step.descriptionEn != null && !step.descriptionEn.isEmpty()) return step.descriptionEn;
        return step.descriptionBs != null ? step.descriptionBs : "";
    }
}

