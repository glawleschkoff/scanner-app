package de.glawleschkoff.scannerapp;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.stream.Collectors;

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

        binding.button2.setOnClickListener(x -> {
            String exemplarNr = mViewModel.data().getValue().stream().filter(y -> y.getLeftText() == "exemplarNr").collect(Collectors.toList()).get(0).getRightText();
            //String zeitstempelScNr = new Timestamp(System.currentTimeMillis()).toString().concat("#001");
            String scannerNr = "001";
            String mitarbeiter = "MO";
            String kurzbefehl = "BTZS";
            String optionen = "";

            mViewModel.createFeedback(new FeedbackModel(exemplarNr, scannerNr, kurzbefehl, mitarbeiter, optionen));
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == 501){
                    mViewModel.resetResponseState();
                    mViewModel.resetData();
                    Navigation.findNavController(requireView()).navigate(R.id.action_info2Fragment_to_info1Fragment);
                    return true;
                } else return false;
            }
        });
    }
}