package de.glawleschkoff.scannerapp.fragment.bauteilinfo;

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

import de.glawleschkoff.scannerapp.databinding.FragmentBtinshow4Binding;
import de.glawleschkoff.scannerapp.util.RVAdapter;
import de.glawleschkoff.scannerapp.util.RVItem;
import de.glawleschkoff.scannerapp.viewmodel.BTINViewModel;

public class BTINShow4Fragment extends Fragment {

    private FragmentBtinshow4Binding binding;
    private BTINViewModel btinViewModel;
    private RVAdapter rvAdapter;

    public static BTINShow4Fragment newInstance(){
        return new BTINShow4Fragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btinViewModel = new ViewModelProvider(requireActivity()).get(BTINViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBtinshow4Binding.inflate(getLayoutInflater());
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
        btinViewModel.getResponseUSERALBDetails().observe(getViewLifecycleOwner(), x -> {

            if(x.getResponse()!= null){
                rvAdapter.setRecyclerViewItems(Arrays.asList(
                        new RVItem("FertigKante", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getFertigKante()),
                        new RVItem("KA", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKa()),
                        new RVItem("KA Ist L", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKa_Ist_L()),
                        new RVItem("KA Läufe", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKa_Läufe()),
                        new RVItem("KA Verbrauch", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKa_Verbrauch()),
                        new RVItem("KB", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKb()),
                        new RVItem("KB Ist L", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKb_Ist_L()),
                        new RVItem("KB Läufe", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKb_Läufe()),
                        new RVItem("KB Verbrauch", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKb_Verbrauch()),
                        new RVItem("KC", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKc()),
                        new RVItem("KC Ist L", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKc_Ist_L()),
                        new RVItem("KC Läufe", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKc_Läufe()),
                        new RVItem("KC Verbrauch", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKc_Verbrauch()),
                        new RVItem("KD", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKd()),
                        new RVItem("KD Ist L", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKd_Ist_L()),
                        new RVItem("KD Läufe", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKd_Läufe()),
                        new RVItem("KD Verbrauch", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKd_Verbrauch()),
                        new RVItem("KE", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKe()),
                        new RVItem("KE Ist L", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKe_Ist_L()),
                        new RVItem("KE Läufe", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKe_Läufe()),
                        new RVItem("KE Verbrauch", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKe_Verbrauch()),
                        new RVItem("KF", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKf()),
                        new RVItem("KF Ist L", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKf_Ist_L()),
                        new RVItem("KF Läufe", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKf_Läufe()),
                        new RVItem("KF Verbrauch", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKf_Verbrauch()),
                        new RVItem("KG", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKg()),
                        new RVItem("KG Ist L", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKg_Ist_L()),
                        new RVItem("KG Läufe", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKg_Läufe()),
                        new RVItem("KG Verbrauch", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKg_Verbrauch()),
                        new RVItem("KH", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKh()),
                        new RVItem("KH Ist L", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKh_Ist_L()),
                        new RVItem("KH Läufe", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKh_Läufe()),
                        new RVItem("KH Verbrauch", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKh_Verbrauch())
                ));
            }
        });
    }
}
