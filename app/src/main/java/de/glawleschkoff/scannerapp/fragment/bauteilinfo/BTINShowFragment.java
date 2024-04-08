package de.glawleschkoff.scannerapp.fragment.bauteilinfo;

import android.content.DialogInterface;
import android.os.Bundle;
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

import de.glawleschkoff.scannerapp.R;
import de.glawleschkoff.scannerapp.databinding.FragmentBtinshowBinding;
import de.glawleschkoff.scannerapp.util.AndLiveData;
import de.glawleschkoff.scannerapp.viewmodel.BTINViewModel;
import de.glawleschkoff.scannerapp.viewmodel.SuperViewModel;

public class BTINShowFragment extends Fragment implements ScanManager.DataListener{

    private FragmentBtinshowBinding binding;
    private BTINViewModel btinViewModel;
    private SuperViewModel superViewModel;
    private TabLayout tabLayout;
    private ScanManager scanManager;

    public static BTINShowFragment newInstance(){
        return new BTINShowFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btinViewModel = new ViewModelProvider(requireActivity()).get(BTINViewModel.class);
        superViewModel = new ViewModelProvider(requireActivity()).get(SuperViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBtinshowBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        scanManager = ScanManager.createScanManager(this.getContext());
        scanManager.addDataListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AndLiveData.use(getViewLifecycleOwner())
                .add(btinViewModel.getResponseUSERALBDetails())
                .observe(getViewLifecycleOwner(),x->{
                    if(btinViewModel.getResponseUSERALBDetails().getValue().getErrorMessage() != null){
                        scanManager.lockScanner();
                        new AlertDialog.Builder(getContext())
                                .setMessage("Bauteil nicht gefunden")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        scanManager.unlockScanner();
                                        Navigation.findNavController(requireView()).navigate(R.id.action_btinShowFragment_to_btinScanFragment);
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    } else {
                        Navigation.findNavController(requireView())
                                .navigate(R.id.action_btinShowFragment_self);
                    }
                });

        superViewModel.getMitarbeiter().observe(getViewLifecycleOwner(), x->{
            if(x==null){
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_btinShowFragment_to_loginFragment);
            }
        });

        binding.button3.setOnClickListener(x -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_btinShowFragment_to_btinScanFragment);
        });

        tabLayout = binding.tabLayout;
        TabLayout.Tab firstTab = tabLayout.newTab();
        firstTab.setText("Allgemein");

        TabLayout.Tab secondTab = tabLayout.newTab();
        secondTab.setText("Platte");

        TabLayout.Tab thirdTab = tabLayout.newTab();
        thirdTab.setText("BAZ Feedback");

        TabLayout.Tab fourthTab = tabLayout.newTab();
        fourthTab.setText("Kante");

        TabLayout.Tab fifthTab = tabLayout.newTab();
        fifthTab.setText("Kante Feedback");

        TabLayout.Tab sixthTab = tabLayout.newTab();
        sixthTab.setText("Bauteil Log");

        tabLayout.addTab(firstTab);
        tabLayout.addTab(sixthTab);
        tabLayout.addTab(thirdTab);
        tabLayout.addTab(fifthTab);
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(binding.frame.getId(), new BTINShow1Fragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch (tab.getPosition()){
                    case 0: fragment = new BTINShow1Fragment(); break;
                    case 1: fragment = new BTINShow6Fragment(); break;
                    case 2: fragment = new BTINShow3Fragment(); break;
                    case 3: fragment = new BTINShow5Fragment(); break;
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
    public void onDataReceived(DecodeResult decodeResult) {
        DecodeResult.Result result = decodeResult.getResult();
        String data = decodeResult.getData();
        if(result == DecodeResult.Result.SUCCESS){
            String id = data.startsWith(" ")?data.substring(1):data;
            btinViewModel.requestUSERALBDetails(id);
            btinViewModel.requestUSERCNCFeedback(id);
            btinViewModel.requestUSERKntFeedback(id);
            btinViewModel.requestUSERBauteilLog(id);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        scanManager.removeDataListener(this);
        scanManager.releaseScanManager();
    }
}
