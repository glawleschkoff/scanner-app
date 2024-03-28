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

import de.glawleschkoff.scannerapp.databinding.FragmentBtinshow2Binding;
import de.glawleschkoff.scannerapp.util.RVAdapter;
import de.glawleschkoff.scannerapp.util.RVItem;
import de.glawleschkoff.scannerapp.viewmodel.BTINViewModel;

public class BTINShow2Fragment extends Fragment {

    private FragmentBtinshow2Binding binding;
    private BTINViewModel btinViewModel;
    private RVAdapter rvAdapter;

    public static BTINShow2Fragment newInstance(){
        return new BTINShow2Fragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btinViewModel = new ViewModelProvider(requireActivity()).get(BTINViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBtinshow2Binding.inflate(getLayoutInflater());
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

            if(x.getResponse()!=null){
                rvAdapter.setRecyclerViewItems(Arrays.asList(
                        new RVItem("Platte", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getPlatte()),
                        new RVItem("PL FLng", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getPl_FLng()),
                        new RVItem("PL FBrt", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getPl_FBrt()),
                        new RVItem("ZLng", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getZlng()),
                        new RVItem("ZBrt", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getZbrt()),
                        new RVItem("Prio1Datum", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getPrio1Datum()),
                        new RVItem("Prio2Datum", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getPrio2Datum()),
                        new RVItem("Prio", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getPrio()),
                        new RVItem("ArdisJob", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getArdisJob()),
                        new RVItem("ArdisSPln", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getArdisSPln()),
                        new RVItem("PlattenID", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getPlattenID()),
                        new RVItem("Maschine", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getMaschine()),
                        new RVItem("Säge", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getSäge()),
                        new RVItem("OptiQuote", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getOptiQuote()),
                        new RVItem("OptiQtEff", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getOptiQtEff()),
                        new RVItem("StripNo", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getStripNo()),
                        new RVItem("PlatteKlein", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getPlatteKlein()),
                        new RVItem("Ausw", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getAusw()),
                        new RVItem("AuslageID", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getAuslagerID()),
                        new RVItem("ausgelagert", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getAusgelagert()),
                        new RVItem("Kommentar", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKommentar()),
                        new RVItem("SPlan gedruckt", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getSplan_gedruckt()),
                        new RVItem("BTEti gedruckt", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getBteti_gedruckt()),
                        new RVItem("STEti gedruckt", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getSteti_gedruckt()),
                        new RVItem("FertigZuschnitt", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getFertigZuschnitt()),
                        new RVItem("ZuschnittDatum", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getZuschnittDatum())
                ));
            }
        });
    }
}
