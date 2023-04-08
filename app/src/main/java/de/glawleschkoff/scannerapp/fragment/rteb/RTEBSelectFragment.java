package de.glawleschkoff.scannerapp.fragment.rteb;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.glawleschkoff.scannerapp.R;
import de.glawleschkoff.scannerapp.databinding.FragmentRtebselectBinding;
import de.glawleschkoff.scannerapp.model.RTEBFeedbackModel;
import de.glawleschkoff.scannerapp.model.RestteilModel;
import de.glawleschkoff.scannerapp.util.RTEBCardRVAdapter;
import de.glawleschkoff.scannerapp.util.RVItem;
import de.glawleschkoff.scannerapp.viewmodel.MetaViewModel;
import de.glawleschkoff.scannerapp.viewmodel.RTEBViewModel;

public class RTEBSelectFragment extends Fragment {

    private FragmentRtebselectBinding binding;
    private RTEBViewModel rtebViewModel;
    private MetaViewModel metaViewModel;
    private RTEBCardRVAdapter rtebCardRVAdapter;

    public static RTEBSelectFragment newInstance(){
        return new RTEBSelectFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rtebViewModel = new ViewModelProvider(requireActivity()).get(RTEBViewModel.class);
        metaViewModel = new ViewModelProvider(requireActivity()).get(MetaViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRtebselectBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<RVItem> list1 = new ArrayList<>();
        list1.add(new RVItem("Lädt...",""));

        rtebCardRVAdapter = new RTEBCardRVAdapter(list1,this.getContext(), x -> {
            if(x!=null){
                switch(x){
                    case "Länge":
                        pickLänge(false); break;
                    case "Breite":
                        pickBreite(); break;
                    default:
                }
            }
        });
        binding.rvoutside.setAdapter(rtebCardRVAdapter);
        binding.rvoutside.setLayoutManager(new LinearLayoutManager(this.getContext()));

        rtebViewModel.getFeedbackRestteil().observe(getViewLifecycleOwner(), x -> {
            if(x!=null){
                rtebCardRVAdapter.setRvItemList(Arrays.asList(
                        new RVItem("ID",x.getId()),
                        new RVItem("Kante",x.getKante()),
                        new RVItem("Länge",String.valueOf(x.getLänge())),
                        new RVItem("Breite",String.valueOf(x.getBreite()))
                ));
            }
        });

        binding.button2.setOnClickListener(x -> {
            if(rtebViewModel.getFeedbackRestteil().getValue()!=null && metaViewModel.getMitarbeiter().getValue()!=null){
                String id = rtebViewModel.getFeedbackRestteil().getValue().getId();
                String scannerNr = metaViewModel.getScannerNr().getValue();
                String kurzbefehl = "RTEB";
                String mitarbeiter = metaViewModel.getMitarbeiter().getValue();
                String länge = String.valueOf(rtebViewModel.getFeedbackRestteil().getValue().getLänge());
                String breite = String.valueOf(rtebViewModel.getFeedbackRestteil().getValue().getBreite());
                String kante = rtebViewModel.getFeedbackRestteil().getValue().getKante();

                rtebViewModel.createFeedback(new RTEBFeedbackModel(id,scannerNr,kurzbefehl,mitarbeiter,länge,breite,kante));
                Navigation.findNavController(requireView()).navigate(R.id.action_RTEBSelectFragment_to_RTEBScanFragment);
            }
        });

        binding.button.setOnClickListener(x -> {
            System.out.println("nein");
            Navigation.findNavController(requireView()).navigate(R.id.action_RTEBSelectFragment_to_RTEBScanFragment);
        });
        metaViewModel.getMitarbeiter().observe(getViewLifecycleOwner(),x -> {
            if(x==null){
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_RTEBSelectFragment_to_loginFragment);
            }
        });
        pickLänge(true);
    }

    private void pickLänge(boolean b){
        final AlertDialog.Builder d = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog, null);
        final NumberPicker np = (NumberPicker) dialogView.findViewById(R.id.numberPicker1);
        np.setMaxValue(100000);
        np.setMinValue(0);
        np.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.valueOf(value*5);
            }
        });
        np.setValue((rtebViewModel.getFeedbackRestteil().getValue().getLänge() -
                rtebViewModel.getFeedbackRestteil().getValue().getLänge() % 5)/5);
        np.setWrapSelectorWheel(false);
        d.setMessage("Länge wählen (mm)");
        d.setView(dialogView);
        d.setPositiveButton("Weiter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                rtebViewModel.setFeedbackRestteil(new RestteilModel(
                        rtebViewModel.getFeedbackRestteil().getValue().getId(),
                        rtebViewModel.getFeedbackRestteil().getValue().getKante(),
                        np.getValue()*5,
                        rtebViewModel.getFeedbackRestteil().getValue().getBreite()
                ));
                if(b){
                    pickBreite();
                }
            }
        });
        d.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = d.create();
        alertDialog.show();
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void pickBreite(){
        final AlertDialog.Builder d = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog, null);
        final NumberPicker np = (NumberPicker) dialogView.findViewById(R.id.numberPicker1);
        np.setMaxValue(100000);
        np.setMinValue(0);
        np.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.valueOf(value*5);
            }
        });
        np.setValue((rtebViewModel.getFeedbackRestteil().getValue().getBreite() -
                rtebViewModel.getFeedbackRestteil().getValue().getBreite() % 5)/5);
        np.setWrapSelectorWheel(false);
        d.setMessage("Breite wählen (mm)");
        d.setView(dialogView);
        d.setPositiveButton("Weiter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                rtebViewModel.setFeedbackRestteil(new RestteilModel(
                        rtebViewModel.getFeedbackRestteil().getValue().getId(),
                        rtebViewModel.getFeedbackRestteil().getValue().getKante(),
                        rtebViewModel.getFeedbackRestteil().getValue().getLänge(),
                        np.getValue()*5
                ));
            }
        });
        d.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = d.create();
        alertDialog.show();
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}
