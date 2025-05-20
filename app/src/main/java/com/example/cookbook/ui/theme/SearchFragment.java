package com.example.cookbook.ui.theme;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.R;
import com.example.cookbook.data.RecipeRepository;
import com.example.cookbook.viewmodel.RecipeViewModel;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

public class SearchFragment extends Fragment {
    private MaterialAutoCompleteTextView availableIngredientsInput;
    private MaterialAutoCompleteTextView missingIngredientsInput;
    private RecyclerView searchResultsRecyclerView;
    private ProgressBar progressBar;
    private TextView noResultsText;
    private RecipeAdapter recipeAdapter;
    private RecipeViewModel recipeViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViews(view);
        setupRecyclerView();
        setupViewModel();
        setupTextWatchers();
    }

    private void initializeViews(View view) {
        availableIngredientsInput = view.findViewById(R.id.availableIngredientsInput);
        missingIngredientsInput = view.findViewById(R.id.missingIngredientsInput);
        searchResultsRecyclerView = view.findViewById(R.id.searchResultsRecyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        noResultsText = view.findViewById(R.id.noResultsText);
    }

    private void setupRecyclerView() {
        recipeAdapter = new RecipeAdapter(requireContext());
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        searchResultsRecyclerView.setAdapter(recipeAdapter);
    }

    private void setupViewModel() {
        RecipeRepository repository = new RecipeRepository(requireActivity().getApplication());
        RecipeViewModel.Factory factory = new RecipeViewModel.Factory(repository);
        recipeViewModel = new ViewModelProvider(this, factory).get(RecipeViewModel.class);

        recipeViewModel.getIngredientSearchResults().observe(getViewLifecycleOwner(), recipes -> {
            progressBar.setVisibility(View.GONE);

            if (recipes == null || recipes.isEmpty()) {
                noResultsText.setVisibility(View.VISIBLE);
                noResultsText.setText(getString(R.string.no_recipes_found));
                searchResultsRecyclerView.setVisibility(View.GONE);
            } else {
                noResultsText.setVisibility(View.GONE);
                searchResultsRecyclerView.setVisibility(View.VISIBLE);
                recipeAdapter.setRecipes(recipes);
            }
        });
    }

    private void setupTextWatchers() {
        availableIngredientsInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                performSearch();
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        missingIngredientsInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                performSearch();
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void performSearch() {
        try {
            progressBar.setVisibility(View.VISIBLE);
            noResultsText.setVisibility(View.GONE);
            searchResultsRecyclerView.setVisibility(View.GONE);

            String availableIngredients = availableIngredientsInput.getText().toString().trim();
            String missingIngredients = missingIngredientsInput.getText().toString().trim();

            if (availableIngredients.isEmpty() && missingIngredients.isEmpty()) {
                showErrorMessage(getString(R.string.error_ingredient));
                return;
            }

            recipeViewModel.searchRecipesByIngredients(availableIngredients, missingIngredients);
        } catch (Exception e) {
            showErrorMessage(getString(R.string.error_searching));
            Log.e("SearchFragment", "Search error", e);
        }
    }

    private void showErrorMessage(String message) {
        progressBar.setVisibility(View.GONE);
        noResultsText.setVisibility(View.VISIBLE);
        noResultsText.setText(message);
        searchResultsRecyclerView.setVisibility(View.GONE);
    }

}