package de.glawleschkoff.scannerapp.fragment;

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

import java.util.Arrays;

import de.glawleschkoff.scannerapp.viewmodel.InfoViewModel;
import de.glawleschkoff.scannerapp.viewmodel.MetaViewModel;
import de.glawleschkoff.scannerapp.R;
import de.glawleschkoff.scannerapp.util.RecyclerViewAdapter;
import de.glawleschkoff.scannerapp.util.RecyclerViewItem;
import de.glawleschkoff.scannerapp.databinding.FragmentInfo2Binding;
import de.glawleschkoff.scannerapp.model.FeedbackModel;

public class Info2Fragment extends Fragment {

    private FragmentInfo2Binding binding;
    private InfoViewModel mViewModel;
    private MetaViewModel metaViewModel;
    private RecyclerViewAdapter recyclerViewAdapter;

    public static Info2Fragment newInstance() {
        return new Info2Fragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(InfoViewModel.class);
        metaViewModel = new ViewModelProvider(requireActivity()).get(MetaViewModel.class);

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

        mViewModel.getResponseBauteil().observe(getViewLifecycleOwner(), response ->
                recyclerViewAdapter.setRecyclerViewItems(response.body().toListOfRecyclerViewItems()));
        mViewModel.getInfo2fragmentText().observe(getViewLifecycleOwner(), response -> {
            binding.text2.setText(response);
            if (!mViewModel.getInfo2fragmentText().getValue().equals("Bauteil wirklich zurücksetzen?")) {
                binding.button2.setEnabled(false);
                binding.button.setText("Zurück");
            }
        });

        binding.button.setOnClickListener(x -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_info2Fragment_to_info1Fragment);
        });

        binding.button2.setOnClickListener(x -> {
            String exemplarNr = mViewModel.getResponseBauteil().getValue().body().getExemplarNr();
            //String zeitstempelScNr = new Timestamp(System.currentTimeMillis()).toString().concat("#001");
            String scannerNr = "001";
            String mitarbeiter = metaViewModel.getMitarbeiter().getValue();
            String kurzbefehl = "BTZS";
            String optionen = "";

            mViewModel.createFeedback(new FeedbackModel(exemplarNr, scannerNr, kurzbefehl, mitarbeiter,optionen));
            Navigation.findNavController(requireView()).navigate(R.id.action_info2Fragment_to_info1Fragment);
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
                    Navigation.findNavController(requireView()).navigate(R.id.action_info2Fragment_to_info1Fragment);
                    return true;
                } else return false;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewModel.getResponseBauteil().removeObservers(this);
        mViewModel.getInfo2fragmentText().removeObservers(this);
        mViewModel.resetResponseBauteil();
        mViewModel.resetResponseFeedback();
    }
}