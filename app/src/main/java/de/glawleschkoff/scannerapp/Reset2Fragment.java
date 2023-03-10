package de.glawleschkoff.scannerapp;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;

import de.glawleschkoff.scannerapp.databinding.FragmentZuruecksetzen2Binding;

public class Reset2Fragment extends Fragment {

    private FragmentZuruecksetzen2Binding binding;
    private ResetViewModel mViewModel;
    private RecyclerViewAdapter recyclerViewAdapter;

    public static Reset2Fragment newInstance() {
        return new Reset2Fragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(ResetViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentZuruecksetzen2Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        recyclerViewAdapter = new RecyclerViewAdapter(this.getContext(),
                Arrays.asList(new RecyclerViewItem("Typ...", "Wert...")));
        binding.rv.setAdapter(recyclerViewAdapter);
        binding.rv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mViewModel.data().observe(getViewLifecycleOwner(), response -> recyclerViewAdapter.setRecyclerViewItems(response));

        return view;
    }

}