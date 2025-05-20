package com.example.cookbook.ui.theme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.AddEditRecipeActivity;
import com.example.cookbook.R;
import com.example.cookbook.SettingsActivity;
import com.example.cookbook.data.AppDatabase;
import com.example.cookbook.data.DatabaseInitializer;
import com.example.cookbook.data.RecipeRepository;
import com.example.cookbook.viewmodel.RecipeViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private static final String PREFS_NAME = "CookbookPrefs";
    private RecipeRepository repository;
    private RecipeAdapter adapter;
    private Spinner sortSpinner;
    private RecipeViewModel recipeViewModel;
    private TextView usernameText;
    private ImageView profileIcon;
    private EditText searchEditText;
    private SharedPreferences sharedPreferences;
    private boolean isCheckingData = false;
    private boolean isGrid = false;
    private ImageView viewToggle;
    private ImageView collapseArrow;
    private boolean isCollapsed = false;
    private ViewGroup rootLayout;
    private FloatingActionButton fabAddRecipe;






    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new RecipeRepository(requireActivity().getApplication());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViews(view);
        setupUserProfile();
        setupRecyclerView();
        setupSearch();
        setupMenuOptions();
        observeRecipes();

        observeRecipes();
        checkDatabaseInitialization();

        setupSortSpinner();
        setupViewModel();

        setupToggleView();

        setupCollapseToggle();

        fabAddRecipe.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AddEditRecipeActivity.class);
            startActivity(intent);
        });

    }

    private void checkDatabaseInitialization() {
        if (isCheckingData) return;
        isCheckingData = true;

        AppDatabase.databaseWriteExecutor.execute(() -> {
            int count = repository.getRecipeCountSync();
            Log.d(TAG, "Current recipe count: " + count);

            if (count == 0) {
                Log.d(TAG, "No recipes found, initializing database");
                new DatabaseInitializer(requireContext()).insertInitialData();
            }

            new Handler(Looper.getMainLooper()).post(() -> {
                isCheckingData = false;
                if (count == 0) {
                    Toast.makeText(getContext(), getString(R.string.loading_recipes), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void initializeViews(View view) {
        usernameText = view.findViewById(R.id.usernameText);
        profileIcon = view.findViewById(R.id.profileIcon);
        searchEditText = view.findViewById(R.id.searchEditText);
        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        sortSpinner = view.findViewById(R.id.sortSpinner);
        viewToggle = view.findViewById(R.id.viewToggle);
        collapseArrow = view.findViewById(R.id.collapseArrow);

        rootLayout = view.findViewById(R.id.rootContainer);

        fabAddRecipe = view.findViewById(R.id.fabAddRecipe);
    }

    private void setupUserProfile() {
        updateUsername();

        usernameText.setOnClickListener(v -> navigateToProfileFragment());
        profileIcon.setOnClickListener(v -> navigateToProfileFragment());
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = requireView().findViewById(R.id.recipeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecipeAdapter(getContext());
        recyclerView.setAdapter(adapter);
    }

    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchRecipes(s.toString());
            }
        });
    }

    private void setupMenuOptions() {
        ImageView moreOptions = requireView().findViewById(R.id.moreOptions);
        moreOptions.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(requireContext(), moreOptions);
            popup.getMenuInflater().inflate(R.menu.main_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.menu_settings) {
                    startActivity(new Intent(getContext(), SettingsActivity.class));
                    return true;
                }
                return false;
            });
            popup.show();
        });
    }

    private void observeRecipes() {
        repository.getAllRecipes().observe(getViewLifecycleOwner(), recipes -> {
            if (recipes == null) {
                Log.e(TAG, "Recipes LiveData is null");
                return;
            }

            Log.d(TAG, "Displaying recipes: " + recipes.size());
            adapter.setRecipes(recipes);

            if (recipes.isEmpty()) {
                Toast.makeText(getContext(), getString(R.string.no_recipes_home), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeDatabase() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                new DatabaseInitializer(requireContext()).insertInitialData();

                // Verify data was inserted
                int count = repository.getRecipeCountSync();
                Log.d(TAG, "Database initialized with " + count + " recipes");

                // Update UI on main thread
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (count == 0) {
                        Toast.makeText(getContext(), getString(R.string.loading_failed), Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Database initialization failed", e);
                new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(getContext(), getString(R.string.loading_error), Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void searchRecipes(String query) {
        if (query.isEmpty()) {
            repository.getAllRecipes().observe(getViewLifecycleOwner(), recipes ->
                    adapter.setRecipes(recipes != null ? recipes : new ArrayList<>()));
        } else {
            repository.searchRecipes(query).observe(getViewLifecycleOwner(), recipes -> {
                if (recipes != null) {
                    adapter.setRecipes(recipes);
                }
            });
        }
    }

    private void navigateToProfileFragment() {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, new ProfileFragment())
                .addToBackStack(null)
                .commit();

        View bottomNav = requireActivity().findViewById(R.id.bottomNavigationView);
        if (bottomNav instanceof com.google.android.material.bottomnavigation.BottomNavigationView) {
            ((com.google.android.material.bottomnavigation.BottomNavigationView) bottomNav)
                    .setSelectedItemId(R.id.profileFragment);
        }
    }


    private void updateUsername() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.reload().addOnCompleteListener(task -> {
                String displayName = user.getDisplayName();
                usernameText.setText(displayName != null ? displayName : getString(R.string.display_name));
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUsername();
    }

    private void setupViewModel() {
        RecipeViewModel.Factory factory = new RecipeViewModel.Factory(new RecipeRepository(requireActivity().getApplication()));
        recipeViewModel = new ViewModelProvider(this, factory).get(RecipeViewModel.class);
    }

    private void setupSortSpinner() {
        String[] sortOptions = {getString(R.string.name_asc), getString(R.string.name_desc), getString(R.string.calories), getString(R.string.rating), getString(R.string.category)};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, sortOptions);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(spinnerAdapter);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String key = "";
                switch (position) {
                    case 0: key = "name_asc"; break;
                    case 1: key = "name_desc"; break;
                    case 2: key = "calories"; break;
                    case 3: key = "rating"; break;
                    case 4: key = "category"; break;
                }


                if (!key.isEmpty()) {
                    recipeViewModel.getSortedRecipes(key).observe(getViewLifecycleOwner(), recipes -> {
                        if (recipes != null) {
                            adapter.setRecipes(recipes);
                        }
                    });
                }
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupToggleView() {
        viewToggle.setOnClickListener(v -> {
            isGrid = !isGrid;
            int columns = isGrid ? 2 : 1;

            RecyclerView recyclerView = requireView().findViewById(R.id.recipeRecyclerView);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), columns));
            adapter.setGridMode(isGrid);
            recyclerView.setAdapter(adapter);
            viewToggle.setImageResource(isGrid ? R.drawable.ic_list : R.drawable.ic_grid);
        });
    }

    private void setupCollapseToggle() {
        isCollapsed = sharedPreferences.getBoolean("collapseState", false);

        collapseArrow.setImageResource(isCollapsed ? R.drawable.ic_arrow_down : R.drawable.ic_arrow_up);

        toggleCollapsedState(isCollapsed, false);

        collapseArrow.setOnClickListener(v -> {
            isCollapsed = !isCollapsed;
            toggleCollapsedState(isCollapsed, true);

            sharedPreferences.edit().putBoolean("collapseState", isCollapsed).apply();
        });
    }

    private void toggleCollapsedState(boolean collapse, boolean animate) {
        int visibility = collapse ? View.GONE : View.VISIBLE;

        if (animate) {
            AutoTransition transition = new AutoTransition();
            transition.setDuration(100);

            transition.addListener(new Transition.TransitionListener() {
                @Override public void onTransitionStart(@NonNull Transition transition) {}
                @Override public void onTransitionCancel(@NonNull Transition transition) {}
                @Override public void onTransitionPause(@NonNull Transition transition) {}
                @Override public void onTransitionResume(@NonNull Transition transition) {}

                @Override
                public void onTransitionEnd(@NonNull Transition transition) {
                    collapseArrow.setImageResource(collapse ? R.drawable.ic_arrow_down : R.drawable.ic_arrow_up);
                    transition.removeListener(this);
                }
            });

            TransitionManager.beginDelayedTransition(rootLayout, transition);
        } else {
            collapseArrow.setImageResource(collapse ? R.drawable.ic_arrow_down : R.drawable.ic_arrow_up);
        }

        searchEditText.setVisibility(visibility);
        sortSpinner.setVisibility(visibility);
        profileIcon.setVisibility(visibility);
        usernameText.setVisibility(visibility);
    }



}