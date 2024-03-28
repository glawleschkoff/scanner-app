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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import de.glawleschkoff.scannerapp.databinding.FragmentBtinshow1Binding;
import de.glawleschkoff.scannerapp.util.RVAdapter;
import de.glawleschkoff.scannerapp.util.RVItem;
import de.glawleschkoff.scannerapp.viewmodel.BTINViewModel;

public class BTINShow1Fragment extends Fragment {

    private FragmentBtinshow1Binding binding;
    private BTINViewModel btinViewModel;
    private RVAdapter rvAdapter;

    public static BTINShow1Fragment newInstance(){
        return new BTINShow1Fragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btinViewModel = new ViewModelProvider(requireActivity()).get(BTINViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBtinshow1Binding.inflate(getLayoutInflater());
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

        btinViewModel.getResponseBitmap().observe(getViewLifecycleOwner(), x -> {
            if(x.getResponse()!=null){
                binding.image.setImageBitmap(x.getResponse());
            }
        });

        btinViewModel.getResponseUSERALBDetails().observe(getViewLifecycleOwner(), x -> {
            if(btinViewModel.getResponseUSERALBDetails().getValue().getResponse()!=null){
                btinViewModel.requestBitmap(btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKundenAuftrag(),
                        btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKundenPosition());
            }

            if(x.getResponse()!=null){
                rvAdapter.setRecyclerViewItems(Arrays.asList(
                        new RVItem("Exemplar Nr", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getExemplarNr()),
                        new RVItem("KundenAuftrag", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKundenAuftrag()),
                        new RVItem("Position", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKundenPosition()),
                        new RVItem("Platte", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getPlatte()),
                        new RVItem("Länge", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getPl_FLng()),
                        new RVItem("Breite", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getPl_FBrt()),
                        new RVItem("BAZ", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getBaz_Fortschritt()
                                .replace("0","_ ")
                                .replace("1","O ")
                                .replace("2","X ")),
                        new RVItem("Kante", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKante_Fortschritt()
                                .replace("0","_ ")
                                .replace("1","O ")
                                .replace("2","X ")),
                        new RVItem("Prod Stopp", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getProdStopp()),
                        new RVItem("ProdDatum", LocalDate.parse(btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getProdDatum()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))),
                        new RVItem("FertigDatum",LocalDate.parse(btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getFertigDatum()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))),
                        new RVItem("Freigabe", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getProdFreigabe()),
                        new RVItem("Status", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getStatus()),
                        new RVItem("Ardis Job", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getArdisJob()),
                        new RVItem("Sp Nr", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getArdisSPln()),
                        new RVItem("Maschine", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getMaschine()
                                .replace("1","Bimacut")
                                .replace("2","Säge")),
                        new RVItem("Platten ID", btinViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getPlattenID())
                ));
            }
        });
    }
}
