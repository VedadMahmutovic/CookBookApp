package com.example.cookbook.ui.theme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cookbook.R;
import com.example.cookbook.viewmodel.HistoryViewModel;
import com.example.cookbook.viewmodel.HistoryViewModelFactory;

public class HistoryFragment extends Fragment {
    private RecyclerView historyRecyclerView;
    private RecipeAdapter adapter;
    private HistoryViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        historyRecyclerView = view.findViewById(R.id.historyRecyclerView);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecipeAdapter(requireContext());
        historyRecyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this, new HistoryViewModelFactory(requireActivity().getApplication()))
                .get(HistoryViewModel.class);

        viewModel.getRecentlyViewedRecipes().observe(getViewLifecycleOwner(), recipes -> {
            adapter.setRecipes(recipes);
        });
    }

}

