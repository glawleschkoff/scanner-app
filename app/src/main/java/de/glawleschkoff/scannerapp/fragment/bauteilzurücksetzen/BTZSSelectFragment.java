package de.glawleschkoff.scannerapp.fragment.bauteilzurücksetzen;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.tabs.TabLayout;
import com.keyence.autoid.sdk.scan.DecodeResult;
import com.keyence.autoid.sdk.scan.ScanManager;

import java.util.Arrays;
import java.util.stream.Collectors;

import de.glawleschkoff.scannerapp.R;
import de.glawleschkoff.scannerapp.databinding.FragmentBtzsselectBinding;
import de.glawleschkoff.scannerapp.util.AndLiveData;
import de.glawleschkoff.scannerapp.viewmodel.BTZSViewModel;
import de.glawleschkoff.scannerapp.viewmodel.SuperViewModel;

public class BTZSSelectFragment extends Fragment implements ScanManager.DataListener{

    private FragmentBtzsselectBinding binding;
    private BTZSViewModel btzsViewModel;
    private SuperViewModel superViewModel;
    private TabLayout tabLayout;
    private ScanManager scanManager;

    public static BTZSSelectFragment newInstance() {
        return new BTZSSelectFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btzsViewModel = new ViewModelProvider(requireActivity()).get(BTZSViewModel.class);
        superViewModel = new ViewModelProvider(requireActivity()).get(SuperViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBtzsselectBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        scanManager = ScanManager.createScanManager(this.getContext());
        scanManager.addDataListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AndLiveData.use(getViewLifecycleOwner())
                .add(btzsViewModel.getResponseUSERALBDetails())
                .observe(getViewLifecycleOwner(),x->{
                    if(btzsViewModel.getResponseUSERALBDetails().getValue().getErrorMessage() != null){
                        scanManager.lockScanner();
                        new AlertDialog.Builder(getContext())
                                .setMessage("Bauteil nicht gefunden")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        scanManager.unlockScanner();
                                        Navigation.findNavController(requireView()).navigate(R.id.action_btzsSelectFragment_to_btzsScanFragment);
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    } else {
                        Navigation.findNavController(requireView())
                                .navigate(R.id.action_btzsSelectFragment_self);
                    }
                });
        // falls scannerAntwort nicht leer ist
        // falls Teil von scannerAntwort mit BTZS startet
        if(!Arrays.stream(btzsViewModel.getResponseUSERALBDetails().getValue().getResponse()
                .getScannerAntwort().split("#")).filter(x -> x.startsWith("BTZS")).collect(Collectors.toList()).isEmpty()){
            binding.text2.setText("Bauteil bereits zurück gesetzt");
            binding.button2.setVisibility(View.GONE);
            binding.button2.setEnabled(false);
            binding.button.setText("Zurück");
            // sonst, falls scannerAnweisung nicht mit BTZS=J startet
            // sonst, falls BTZS von scannerAnweisung =N ist
        } else if(!Arrays.stream(btzsViewModel.getResponseUSERALBDetails().getValue().getResponse()
                .getScannerAnweisung().split("#")).filter(x -> x.startsWith("BTZS=N")).collect(Collectors.toList()).isEmpty()){
            binding.text2.setText("Zurücksetzen nicht möglich");
            binding.button2.setVisibility(View.GONE);
            binding.button2.setEnabled(false);
            binding.button.setText("Zurück");
        } else{
            binding.text2.setText("Bauteil wirklich zurücksetzen?");
        }

        superViewModel.getMitarbeiter().observe(getViewLifecycleOwner(), x->{
            if(x==null){
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_btzsSelectFragment_to_loginFragment);
            }
        });

        binding.button.setOnClickListener(x -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_btzsSelectFragment_to_btzsScanFragment);
        });

        binding.button2.setOnClickListener(x -> {
            if(btzsViewModel.getResponseUSERALBDetails().getValue().getResponse()!=null && superViewModel.getMitarbeiter().getValue()!=null){
                String exemplarNr = btzsViewModel.getResponseUSERALBDetails().getValue().getResponse().getExemplarNr();

                String antwort = btzsViewModel.getResponseUSERALBDetails().getValue().getResponse().getScannerAntwort().equals("")?
                        "BTZS="+";;"+";"+ superViewModel.getMitarbeiter().getValue():
                        btzsViewModel.getResponseUSERALBDetails().getValue().getResponse().getScannerAntwort()+"#"+
                                "BTZS="+";;"+";"+ superViewModel.getMitarbeiter().getValue();
                btzsViewModel.updateUSERALBDetails(exemplarNr,antwort);

                new AlertDialog.Builder(getContext())
                        .setMessage("Etikett entfernen")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Navigation.findNavController(requireView()).navigate(R.id.action_btzsSelectFragment_to_btzsScanFragment);
                            }
                        })
                        .create()
                        .show();
            }
        });

        tabLayout = binding.tabLayout;
        TabLayout.Tab firstTab = tabLayout.newTab();
        firstTab.setText("Bauteil");
        tabLayout.addTab(firstTab);
        System.out.println("fail2");
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(binding.frame.getId(), new BTZSSelect1Fragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch (tab.getPosition()){
                    case 0: fragment = new BTZSSelect1Fragment(); break;
                }
                FragmentManager fm = getChildFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(binding.frame.getId(), fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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
                    Navigation.findNavController(requireView()).navigate(R.id.action_btzsSelectFragment_to_btzsScanFragment);
                    return true;
                } else return false;
            }
        });
    }

    @Override
    public void onDataReceived(DecodeResult decodeResult) {
        DecodeResult.Result result = decodeResult.getResult();
        String data = decodeResult.getData();
        if(result == DecodeResult.Result.SUCCESS){
            String id = data.startsWith(" ")?data.substring(1):data;
            btzsViewModel.requestUSERALBDetails(id);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        scanManager.removeDataListener(this);
        scanManager.releaseScanManager();
    }
}
