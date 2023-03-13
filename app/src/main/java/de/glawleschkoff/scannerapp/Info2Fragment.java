package de.glawleschkoff.scannerapp;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;

import de.glawleschkoff.scannerapp.databinding.FragmentInfo2Binding;

public class Info2Fragment extends Fragment {

    private FragmentInfo2Binding binding;
    private InfoViewModel mViewModel;
    private RecyclerViewAdapter recyclerViewAdapter;

    public static Info2Fragment newInstance() {
        return new Info2Fragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(InfoViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentInfo2Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        recyclerViewAdapter = new RecyclerViewAdapter(this.getContext(),
                Arrays.asList(new RecyclerViewItem("Typ...", "Wert...")));
        binding.rv.setAdapter(recyclerViewAdapter);
        binding.rv.setLayoutManager(new LinearLayoutManager(this.getContext()));

        mViewModel.data().observe(getViewLifecycleOwner(), response -> recyclerViewAdapter.setRecyclerViewItems(response));

        binding.button.setOnClickListener(x -> {
            mViewModel.resetResponseState();
            mViewModel.resetData();
            Navigation.findNavController(requireView()).navigate(R.id.action_info2Fragment_to_info1Fragment);
        });

        return view;
    }
}