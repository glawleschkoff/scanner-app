package de.glawleschkoff.scannerapp.fragment.btzs;

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

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import de.glawleschkoff.scannerapp.R;
import de.glawleschkoff.scannerapp.databinding.FragmentBtzsselectBinding;
import de.glawleschkoff.scannerapp.model.BTZSFeedbackModel;
import de.glawleschkoff.scannerapp.util.AndLiveData;
import de.glawleschkoff.scannerapp.viewmodel.BTZSViewModel;
import de.glawleschkoff.scannerapp.viewmodel.MetaViewModel;

public class BTZSSelectFragment extends Fragment implements ScanManager.DataListener{

    private FragmentBtzsselectBinding binding;
    private BTZSViewModel btzsViewModel;
    private MetaViewModel metaViewModel;
    private TabLayout tabLayout;
    private ScanManager scanManager;

    public static BTZSSelectFragment newInstance() {
        return new BTZSSelectFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btzsViewModel = new ViewModelProvider(requireActivity()).get(BTZSViewModel.class);
        metaViewModel = new ViewModelProvider(requireActivity()).get(MetaViewModel.class);
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
                .add(btzsViewModel.getResponseBauteil())
                //.add(btzsViewModel.getResponseFeedback())
                .observe(getViewLifecycleOwner(),x->{
                    if(btzsViewModel.getResponseBauteil().getValue().getErrorMessage() != null){
                        System.out.println("fail1");
                        new AlertDialog.Builder(getContext())
                                //.setTitle("Delete entry")
                                .setMessage("Bauteil nicht gefunden")

                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Navigation.findNavController(requireView()).navigate(R.id.action_BTZSSelectFragment3_to_BTZSScanFragment);
                                    }
                                })
                                // A null listener allows the button to dismiss the dialog and take no further action.
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    } else {
                        Navigation.findNavController(requireView())
                                .navigate(R.id.action_BTZSSelectFragment3_self);
                    }
                });

        if(!btzsViewModel.getResponseBauteil().getValue().getResponse().getScannerAntwort().equals("")){
            binding.text2.setText("Bauteil bereits zurück gesetzt");
            binding.button2.setVisibility(View.GONE);
            binding.button2.setEnabled(false);
            binding.button.setText("Zurück");
        } else if(!btzsViewModel.getResponseBauteil().getValue().getResponse().getScannerAnweisung().startsWith("BTZS=J")){
            binding.text2.setText("Zurücksetzen nicht möglich");
            binding.button2.setVisibility(View.GONE);
            binding.button2.setEnabled(false);
            binding.button.setText("Zurück");
        } else{
            System.out.println("wirlich zurücksetzen?");
            binding.text2.setText("Bauteil wirklich zurücksetzen?");
        }



        metaViewModel.getMitarbeiter().observe(getViewLifecycleOwner(),x->{
            if(x==null){
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_BTZSSelectFragment3_to_loginFragment);
            }
        });

        binding.button.setOnClickListener(x -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_BTZSSelectFragment3_to_BTZSScanFragment);
        });

        binding.button2.setOnClickListener(x -> {
            if(btzsViewModel.getResponseBauteil().getValue().getResponse()!=null && metaViewModel.getMitarbeiter().getValue()!=null){
                String exemplarNr = btzsViewModel.getResponseBauteil().getValue().getResponse().getExemplarNr();
                String scannerNr = metaViewModel.getScannerNr().getValue();
                String mitarbeiter = metaViewModel.getMitarbeiter().getValue();
                String kurzbefehl = "BTZS";
                String optionen = "";

                btzsViewModel.updateBauteil(exemplarNr,"BTZS="+
                        new SimpleDateFormat("yyyyMMddHHmmss").format(new Timestamp(System.currentTimeMillis()))+
                                ";"+mitarbeiter);

                //btzsViewModel.createFeedback(new BTZSFeedbackModel(exemplarNr, scannerNr, kurzbefehl, mitarbeiter,optionen));
                new AlertDialog.Builder(getContext())
                        .setMessage("Etikett entfernen")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Navigation.findNavController(requireView()).navigate(R.id.action_BTZSSelectFragment3_to_BTZSScanFragment);
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
        //FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(binding.frame.getId(), new BTZSSelect1Fragment());
        //ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch (tab.getPosition()){
                    case 0: fragment = new BTZSSelect1Fragment(); break;
                }
                //FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentManager fm = getChildFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(binding.frame.getId(), fragment);
                //ft.addToBackStack(null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                System.out.println("unselected");
                //ft.addToBackStack(null);
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
                    Navigation.findNavController(requireView()).navigate(R.id.action_BTZSSelectFragment3_to_BTZSScanFragment);
                    return true;
                } else return false;
            }
        });
    }

    @Override
    public void onDataReceived(DecodeResult decodeResult) {
        DecodeResult.Result result = decodeResult.getResult();
        String codeType = decodeResult.getCodeType();
        String data = decodeResult.getData();
        System.out.println(data);
        if(decodeResult.getResult() == DecodeResult.Result.SUCCESS){
            String id = data.startsWith(" ")?data.substring(1):data;
            btzsViewModel.requestBauteil(id);
            //btzsViewModel.requestFeedback(id+"_BTZS.csv");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        scanManager.removeDataListener(this);
        scanManager.releaseScanManager();
    }

}
