package de.glawleschkoff.scannerapp.fragment.info;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            if(x.getResponse()!=null){
                binding.image.setImageBitmap(x.getResponse());
            }
        });

        infoViewModel.getResponseBauteil().observe(getViewLifecycleOwner(),x -> {
            if(infoViewModel.getResponseBauteil().getValue().getResponse()!=null){
                infoViewModel.requestBitmap(infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getKundenAuftrag(),
                        infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getKundenPosition());
            }

            if(x.getResponse()!=null){
                rvAdapter.setRecyclerViewItems(Arrays.asList(
                        new RVItem("Exemplar Nr",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getExemplarNr()),
                        new RVItem("KundenAuftrag",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getKundenAuftrag()),
                        new RVItem("Position",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getKundenPosition()),
                        new RVItem("Platte",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getPlatte()),
                        new RVItem("Länge",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getPl_FLng()),
                        new RVItem("Breite",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getPl_FBrt()),
                        new RVItem("BAZ",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getBaz_Fortschritt()
                                .replace("0","_ ")
                                .replace("1","O ")
                                .replace("2","X ")),
                        new RVItem("Kante",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getKante_Fortschritt()
                                .replace("0","_ ")
                                .replace("1","O ")
                                .replace("2","X ")),
                        new RVItem("Prod Stopp",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getProdStopp()),
                        new RVItem("ProdDatum", LocalDate.parse(infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getProdDatum()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))),
                        new RVItem("FertigDatum",LocalDate.parse(infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getFertigDatum()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))),
                        new RVItem("Freigabe",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getProdFreigabe()),
                        new RVItem("Status",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getStatus()),
                        new RVItem("Ardis Job",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getArdisJob()),
                        new RVItem("Sp Nr",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getArdisSPln()),
                        new RVItem("Maschine",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getMaschine()
                                .replace("1","Bimacut")
                                .replace("2","Säge")),
                        new RVItem("Platten ID",infoViewModel.getResponseBauteil()
                                .getValue().getResponse().getPlattenID())


                        /*
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
                                .getValue().getResponse().getSollZtBankQM())*/
                ));
            }
        });
    }
}
