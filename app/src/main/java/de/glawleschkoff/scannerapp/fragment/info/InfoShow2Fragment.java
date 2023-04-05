package de.glawleschkoff.scannerapp.fragment.info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.tabs.TabLayout;

import java.util.Arrays;

import de.glawleschkoff.scannerapp.databinding.FragmentInfoshow2Binding;
import de.glawleschkoff.scannerapp.util.RVAdapter;
import de.glawleschkoff.scannerapp.util.RVItem;
import de.glawleschkoff.scannerapp.viewmodel.InfoViewModel;

public class InfoShow2Fragment extends Fragment {

    private FragmentInfoshow2Binding binding;
    private InfoViewModel infoViewModel;
    private RVAdapter rvAdapter;

    public static InfoShow2Fragment newInstance(){
        return new InfoShow2Fragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        infoViewModel = new ViewModelProvider(requireActivity()).get(InfoViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInfoshow2Binding.inflate(getLayoutInflater());
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
            rvAdapter.setRecyclerViewItems(Arrays.asList(
                    new RVItem("Platte",infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getPlatte()),
                    new RVItem("PL FLng",infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getPl_FLng()),
                    new RVItem("PL FBrt",infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getPl_FBrt()),
                    new RVItem("ZLng",infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getZlng()),
                    new RVItem("ZBrt",infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getZbrt()),
                    new RVItem("Prio1Datum",infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getPrio1Datum()),
                    new RVItem("Prio2Datum",infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getPrio2Datum()),
                    new RVItem("Prio",infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getPrio()),
                    new RVItem("ArdisJob",infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getArdisJob()),
                    new RVItem("ArdisSPln",infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getArdisSPln()),
                    new RVItem("PlattenID",infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getPlattenID()),
                    new RVItem("Maschine",infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getMaschine()),
                    new RVItem("Säge",infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getSäge()),
                    new RVItem("OptiQuote",infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getOptiQuote()),
                    new RVItem("OptiQtEff",infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getOptiQtEff()),
                    new RVItem("StripNo",infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getStripNo()),
                    new RVItem("PlatteKlein",infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getPlatteKlein()),
                    new RVItem("Ausw",infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getAusw()),
                    new RVItem("AuslageID",infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getAuslagerID()),
                    new RVItem("ausgelagert",infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getAusgelagert()),
                    new RVItem("Kommentar",infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getKommentar()),
                    new RVItem("SPlan gedruckt",infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getSplan_gedruckt()),
                    new RVItem("BTEti gedruckt",infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getBteti_gedruckt()),
                    new RVItem("STEti gedruckt",infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getSteti_gedruckt()),
                    new RVItem("FertigZuschnitt",infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getFertigZuschnitt()),
                    new RVItem("ZuschnittDatum",infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getZuschnittDatum())
            ));
        });
    }
}
