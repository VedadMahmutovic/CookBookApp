package com.example.cookbook;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import com.example.cookbook.utils.LocaleHelper;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    private LinearLayout aboutButton;
    private TextView settingsTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        LinearLayout languageButton = findViewById(R.id.languageButton);
        languageButton.setOnClickListener(v -> showLanguageDialog());

        aboutButton = findViewById(R.id.aboutButton);
        settingsTitle = findViewById(R.id.settingsTitle);

        aboutButton.setOnClickListener(v -> showAboutDialog());

        SwitchCompat switchDarkMode = findViewById(R.id.switchDarkMode);

        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        boolean darkMode = prefs.getBoolean("dark_mode_enabled", false);
        switchDarkMode.setChecked(darkMode);

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("dark_mode_enabled", isChecked).apply();
            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );

            recreate();
        });

    }

    private void showLanguageDialog() {
        Dialog dialog = new Dialog(this);

        FrameLayout root = new FrameLayout(this);
        root.setLayoutParams(new FrameLayout.LayoutParams(dpToPx(300), ViewGroup.LayoutParams.WRAP_CONTENT));
        root.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_clip_background));
        root.setClipToOutline(true);

        ImageView bg = new ImageView(this);
        bg.setImageResource(R.drawable.ic_dialog_background);
        bg.setScaleType(ImageView.ScaleType.FIT_XY);
        bg.setAdjustViewBounds(true);
        bg.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dpToPx(24), dpToPx(24), dpToPx(24), dpToPx(12));
        content.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView title = new TextView(this);
        title.setText(getString(R.string.title_choose_language));
        title.setTextSize(18);
        title.setTextColor(Color.parseColor("#4d412e"));
        title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        title.setTypeface(title.getTypeface(), Typeface.BOLD);
        title.setPadding(0, 0, 0, dpToPx(16));
        content.addView(title);

        String[] languages = {getString(R.string.text_bosnian), getString(R.string.text_english)};
        int[] flagResIds = {R.drawable.flag_bosnia, R.drawable.flag_uk};

        for (int i = 0; i < languages.length; i++) {
            String langCode = (i == 0) ? "bs" : "en";

            LinearLayout langLayout = new LinearLayout(this);
            langLayout.setOrientation(LinearLayout.HORIZONTAL);
            langLayout.setGravity(Gravity.CENTER_VERTICAL);
            langLayout.setPadding(dpToPx(12), dpToPx(8), dpToPx(12), dpToPx(8));
            langLayout.setClickable(true);
            langLayout.setBackgroundResource(R.drawable.bg_welcome_layout);
            langLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));

            ImageView flag = new ImageView(this);
            flag.setImageResource(flagResIds[i]);
            LinearLayout.LayoutParams flagParams = new LinearLayout.LayoutParams(dpToPx(40), dpToPx(28));
            flagParams.setMargins(0, 0, dpToPx(12), 0);
            flag.setLayoutParams(flagParams);

            TextView langText = new TextView(this);
            langText.setText(languages[i]);
            langText.setTextSize(14);
            langText.setTextColor(Color.BLACK);

            langLayout.addView(flag);
            langLayout.addView(langText);

            langLayout.setOnClickListener(v -> {
                setLocale(langCode);
                dialog.dismiss();
            });

            content.addView(langLayout);
        }

        ScrollView scroll = new ScrollView(this);
        scroll.addView(content);

        Button closeButton = new Button(this);
        closeButton.setText(getString(R.string.title_cancel));
        closeButton.setTextSize(12);
        closeButton.setPadding(dpToPx(16), dpToPx(8), dpToPx(16), dpToPx(8));
        closeButton.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_input_rounded));

        FrameLayout.LayoutParams btnParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        btnParams.gravity = Gravity.END | Gravity.BOTTOM;
        btnParams.setMargins(0, 0, dpToPx(8), dpToPx(8));
        closeButton.setLayoutParams(btnParams);
        closeButton.setOnClickListener(v -> dialog.dismiss());

        root.addView(bg);
        root.addView(scroll);
        root.addView(closeButton);

        dialog.setContentView(root);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(dpToPx(250), ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        title.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            int totalHeight = content.getHeight();
            int maxHeight = dpToPx(200);
            FrameLayout.LayoutParams scrollParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    Math.min(totalHeight + dpToPx(48), maxHeight)
            );
            scroll.setLayoutParams(scrollParams);
        });

        dialog.show();
    }
    private void setLocale(String langCode) {
        LocaleHelper.setLocale(this, langCode);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finishAffinity();
    }


    private void showAboutDialog() {
        Dialog dialog = new Dialog(this);

        FrameLayout root = new FrameLayout(this);
        root.setLayoutParams(new FrameLayout.LayoutParams(dpToPx(300), ViewGroup.LayoutParams.WRAP_CONTENT));
        root.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_clip_background));
        root.setClipToOutline(true);

        ImageView bg = new ImageView(this);
        bg.setImageResource(R.drawable.ic_dialog_background);
        bg.setScaleType(ImageView.ScaleType.FIT_XY);
        bg.setAdjustViewBounds(true);
        bg.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView aboutText = new TextView(this);
        aboutText.setText(getString(R.string.about_message));
        aboutText.setTextSize(16);
        aboutText.setTextColor(Color.parseColor("#4d412e"));
        aboutText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        aboutText.setLineSpacing(0, 1.2f);
        aboutText.setPadding(dpToPx(24), dpToPx(24), dpToPx(24), dpToPx(12));

        ScrollView scroll = new ScrollView(this);
        scroll.setPadding(0, 0, 0, 0);
        scroll.addView(aboutText);

        Button closeButton = new Button(this);
        closeButton.setText(getString(R.string.text_okay));
        closeButton.setTextSize(12);
        closeButton.setPadding(dpToPx(16), dpToPx(8), dpToPx(16), dpToPx(8));
        closeButton.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_input_rounded));

        FrameLayout.LayoutParams btnParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        btnParams.gravity = Gravity.END | Gravity.BOTTOM;
        btnParams.setMargins(0, 0, dpToPx(16), dpToPx(16));
        closeButton.setLayoutParams(btnParams);
        closeButton.setOnClickListener(v -> dialog.dismiss());

        root.addView(bg);
        root.addView(scroll);
        root.addView(closeButton);

        dialog.setContentView(root);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(dpToPx(300), ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        aboutText.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            int textHeight = aboutText.getHeight();
            int maxHeight = dpToPx(250);

            FrameLayout.LayoutParams scrollParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    Math.min(textHeight + dpToPx(48), maxHeight)
            );
            scroll.setLayoutParams(scrollParams);
        });

        dialog.show();
    }
    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (LocaleHelper.isLangChanged(this, Locale.getDefault().getLanguage())) {
            recreate();
        }
    }

    @Override
    public void recreate() {
        super.recreate();
        overridePendingTransition(R.anim.fade_scale_in, R.anim.fade_slide_out);
    }

}
