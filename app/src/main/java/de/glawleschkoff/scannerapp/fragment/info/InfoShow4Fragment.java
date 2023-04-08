package de.glawleschkoff.scannerapp.fragment.info;

import android.icu.text.IDNA;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Arrays;

import de.glawleschkoff.scannerapp.databinding.FragmentInfoshow4Binding;
import de.glawleschkoff.scannerapp.util.RVAdapter;
import de.glawleschkoff.scannerapp.util.RVItem;
import de.glawleschkoff.scannerapp.viewmodel.InfoViewModel;

public class InfoShow4Fragment extends Fragment {

    private FragmentInfoshow4Binding binding;
    private InfoViewModel infoViewModel;
    private RVAdapter rvAdapter;

    public static InfoShow4Fragment newInstance(){
        return new InfoShow4Fragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        infoViewModel = new ViewModelProvider(requireActivity()).get(InfoViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInfoshow4Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvAdapter = new RVAdapter(this.getContext(),
                Arrays.asList(new RVItem("Lädt...","")));
        binding.rv.setAdapter(rvAdapter);
        binding.rv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        infoViewModel.getResponseBauteil().observe(getViewLifecycleOwner(),x -> {

            if(x.getResponse()!= null){
                rvAdapter.setRecyclerViewItems(Arrays.asList(
                        new RVItem("FertigKante",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getFertigKante()),
                        new RVItem("KA",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getKa()),
                        new RVItem("KA Ist L",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getKa_Ist_L()),
                        new RVItem("KA Läufe",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getKa_Läufe()),
                        new RVItem("KA Verbrauch",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getKa_Verbrauch()),
                        new RVItem("KB",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getKb()),
                        new RVItem("KB Ist L",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getKb_Ist_L()),
                        new RVItem("KB Läufe",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getKb_Läufe()),
                        new RVItem("KB Verbrauch",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getKb_Verbrauch()),
                        new RVItem("KC",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getKc()),
                        new RVItem("KC Ist L",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getKc_Ist_L()),
                        new RVItem("KC Läufe",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getKc_Läufe()),
                        new RVItem("KC Verbrauch",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getKc_Verbrauch()),
                        new RVItem("KD",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getKd()),
                        new RVItem("KD Ist L",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getKd_Ist_L()),
                        new RVItem("KD Läufe",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getKd_Läufe()),
                        new RVItem("KD Verbrauch",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getKd_Verbrauch()),
                        new RVItem("KE",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getKe()),
                        new RVItem("KE Ist L",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getKe_Ist_L()),
                        new RVItem("KE Läufe",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getKe_Läufe()),
                        new RVItem("KE Verbrauch",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getKe_Verbrauch()),
                        new RVItem("KF",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getKf()),
                        new RVItem("KF Ist L",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getKf_Ist_L()),
                        new RVItem("KF Läufe",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getKf_Läufe()),
                        new RVItem("KF Verbrauch",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getKf_Verbrauch()),
                        new RVItem("KG",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getKg()),
                        new RVItem("KG Ist L",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getKg_Ist_L()),
                        new RVItem("KG Läufe",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getKg_Läufe()),
                        new RVItem("KG Verbrauch",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getKg_Verbrauch()),
                        new RVItem("KH",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getKh()),
                        new RVItem("KH Ist L",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getKh_Ist_L()),
                        new RVItem("KH Läufe",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getKh_Läufe()),
                        new RVItem("KH Verbrauch",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getKh_Verbrauch())
                ));
            }
        });
    }
}
