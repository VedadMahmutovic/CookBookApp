package com.example.cookbook.ui.theme;

import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.R;
import com.example.cookbook.data.RecipeRepository;
import com.example.cookbook.model.Recipe;

import java.util.List;

public class FavoritesFragment extends Fragment {

    private RecipeAdapter adapter;
    private TextView emptyPlaceholder;

    public FavoritesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.favoritesRecyclerView);
        emptyPlaceholder = view.findViewById(R.id.emptyPlaceholder);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new RecipeAdapter(requireContext());
        recyclerView.setAdapter(adapter);

        RecipeRepository repository = new RecipeRepository(requireActivity().getApplication());
        repository.getFavoriteRecipes().observe(getViewLifecycleOwner(), new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                Log.d("FavoritesFragment", "Received favorites: " + recipes.size());
                adapter.setRecipes(recipes);

                if (recipes.isEmpty()) {
                    emptyPlaceholder.setVisibility(View.VISIBLE);
                } else {
                    emptyPlaceholder.setVisibility(View.GONE);
                }
            }
        });
    }
}
