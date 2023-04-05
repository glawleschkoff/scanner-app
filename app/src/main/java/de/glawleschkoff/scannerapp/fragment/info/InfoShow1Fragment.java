package de.glawleschkoff.scannerapp.fragment.info;

import android.graphics.Bitmap;
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

import de.glawleschkoff.scannerapp.databinding.FragmentInfoshow1Binding;
import de.glawleschkoff.scannerapp.util.BitmapCutter;
import de.glawleschkoff.scannerapp.util.RVAdapter;
import de.glawleschkoff.scannerapp.util.RVItem;
import de.glawleschkoff.scannerapp.viewmodel.InfoViewModel;

public class InfoShow1Fragment extends Fragment {

    private FragmentInfoshow1Binding binding;
    private InfoViewModel infoViewModel;
    private RVAdapter rvAdapter;

    public static InfoShow1Fragment newInstance(){
        return new InfoShow1Fragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        infoViewModel = new ViewModelProvider(requireActivity()).get(InfoViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInfoshow1Binding.inflate(getLayoutInflater());
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

        infoViewModel.getResponseBitmap().observe(getViewLifecycleOwner(),x -> {
            binding.image.setImageBitmap(x.getResponse());
        });

        infoViewModel.getResponseBauteil().observe(getViewLifecycleOwner(),x -> {
            infoViewModel.requestBitmap(infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getKundenAuftrag(),
                    infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getKundenPosition());

            rvAdapter.setRecyclerViewItems(Arrays.asList(
                    new RVItem("ExemplarNr",infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getExemplarNr()),
                    new RVItem("KundenAuftrag",infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getKundenAuftrag()),
                    new RVItem("KundenPosition",infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getKundenPosition()),
                    new RVItem("ProdFreigabe",infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getProdFreigabe()),
                    new RVItem("Status",infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getStatus()),
                    new RVItem("ProdDatum",infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getProdDatum()),
                    new RVItem("FertigDatum",infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getFertigDatum()),
                    new RVItem("BeginnPlatte",infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getBeginnPlatte()),
                    new RVItem("BeginnKante",infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getBeginnKante()),
                    new RVItem("BeginnBankQM",infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getBeginnBankQM()),
                    new RVItem("BauteilAnzGes",infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getBauteilAnzGes()),
                    new RVItem("ProdStopp",infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getProdStopp()),
                    new RVItem("SollZtPlatte",infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getSollZtPlatte()),
                    new RVItem("SollZtKante",infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getSollZtKante()),
                    new RVItem("SollZtBankQM",infoViewModel.getResponseBauteil()
                            .getValue().getResponse().getSollZtBankQM())
            ));
        });
    }
}
