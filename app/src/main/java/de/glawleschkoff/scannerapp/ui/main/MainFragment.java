package de.glawleschkoff.scannerapp.ui.main;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.keyence.autoid.sdk.scan.ScanManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.glawleschkoff.scannerapp.R;
import de.glawleschkoff.scannerapp.RecyclerViewBottomAdapter;
import de.glawleschkoff.scannerapp.RecyclerViewBottomItem;
import de.glawleschkoff.scannerapp.RecyclerViewTopAdapter;
import de.glawleschkoff.scannerapp.RecyclerViewTopItem;
import de.glawleschkoff.scannerapp.databinding.FragmentMainBinding;

public class MainFragment extends Fragment {

    private FragmentMainBinding binding;
    private MainViewModel mViewModel;
    private RecyclerViewTopAdapter recyclerViewTopAdapter;
    private RecyclerViewBottomAdapter recyclerViewBottomAdapter;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        recyclerViewTopAdapter = new RecyclerViewTopAdapter(this.getContext(),
                Arrays.asList(new RecyclerViewTopItem("Informationen ...")));
        recyclerViewBottomAdapter = new RecyclerViewBottomAdapter(this.getContext(),
                Arrays.asList(new RecyclerViewBottomItem("Das ist ein Knopf")));
        binding.rvTop.setAdapter(recyclerViewTopAdapter);
        binding.rvBottom.setAdapter(recyclerViewBottomAdapter);
        binding.rvTop.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.rvBottom.setLayoutManager(new LinearLayoutManager(this.getContext()));

        mViewModel.getTopItems().observe(getViewLifecycleOwner(),
                response -> recyclerViewTopAdapter.setRecyclerViewTopItems(response));
        mViewModel.getBottomItems().observe(getViewLifecycleOwner(),
                response -> recyclerViewBottomAdapter.setRecyclerViewBottomItems(response));
        return view;
    }

}