package de.glawleschkoff.scannerapp.fragment.info;

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

import de.glawleschkoff.scannerapp.R;
import de.glawleschkoff.scannerapp.databinding.FragmentInfoshowBinding;
import de.glawleschkoff.scannerapp.util.AndLiveData;
import de.glawleschkoff.scannerapp.viewmodel.InfoViewModel;
import de.glawleschkoff.scannerapp.viewmodel.MetaViewModel;

public class InfoShowFragment extends Fragment implements ScanManager.DataListener{

    private FragmentInfoshowBinding binding;
    private InfoViewModel infoViewModel;
    private MetaViewModel metaViewModel;
    private TabLayout tabLayout;
    private ScanManager scanManager;

    public static InfoShowFragment newInstance(){
        return new InfoShowFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        infoViewModel = new ViewModelProvider(requireActivity()).get(InfoViewModel.class);
        metaViewModel = new ViewModelProvider(requireActivity()).get(MetaViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInfoshowBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        scanManager = ScanManager.createScanManager(this.getContext());
        scanManager.addDataListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AndLiveData.use(getViewLifecycleOwner())
                .add(infoViewModel.getResponseBauteil())
                .add(infoViewModel.getResponseFeedback())
                .observe(getViewLifecycleOwner(),x->{
                    if(infoViewModel.getResponseBauteil().getValue().getErrorMessage() != null){
                        new AlertDialog.Builder(getContext())
                                //.setTitle("Delete entry")
                                .setMessage("Bauteil nicht gefunden")

                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Navigation.findNavController(requireView()).navigate(R.id.action_infoShowFragment_to_infoScanFragment2);
                                    }
                                })
                                // A null listener allows the button to dismiss the dialog and take no further action.
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                    } else {
                        Navigation.findNavController(requireView())
                                .navigate(R.id.action_infoShowFragment_self);
                    }
                });

        metaViewModel.getMitarbeiter().observe(getViewLifecycleOwner(),x->{
            if(x==null){
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_infoShowFragment_to_loginFragment);
            }
        });

        binding.button3.setOnClickListener(x -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_infoShowFragment_to_infoScanFragment2);
        });

        tabLayout = binding.tabLayout;
        TabLayout.Tab firstTab = tabLayout.newTab();
        firstTab.setText("Allgemein");
        tabLayout.addTab(firstTab);
        TabLayout.Tab secondTab = tabLayout.newTab();
        secondTab.setText("Platte");
        tabLayout.addTab(secondTab);
        TabLayout.Tab thirdTab = tabLayout.newTab();
        thirdTab.setText("Platten Feedback");
        tabLayout.addTab(thirdTab);
        TabLayout.Tab fourthTab = tabLayout.newTab();
        fourthTab.setText("Kante");
        tabLayout.addTab(fourthTab);
        TabLayout.Tab fifthTab = tabLayout.newTab();
        fifthTab.setText("Kanten Feedback");
        tabLayout.addTab(fifthTab);
        TabLayout.Tab sixthTab = tabLayout.newTab();
        sixthTab.setText("Log");
        tabLayout.addTab(sixthTab);
        FragmentManager fm = getChildFragmentManager();
        //FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(binding.frame.getId(), new InfoShow1Fragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch (tab.getPosition()){
                    case 0: fragment = new InfoShow1Fragment(); break;
                    case 1: fragment = new InfoShow2Fragment(); break;
                    case 2: fragment = new InfoShow3Fragment(); break;
                    case 3: fragment = new InfoShow4Fragment(); break;
                    case 4: fragment = new InfoShow5Fragment(); break;
                    case 5: fragment = new InfoShow6Fragment(); break;
                }
                //FragmentManager fm = getActivity().getSupportFragmentManager();
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
            String id = data.substring(1);
            infoViewModel.requestBauteil(id);
            infoViewModel.requestFeedback(id+"_BTZS.csv");
            infoViewModel.requestCNCFeedback(id);
            infoViewModel.requestKntFeedback(id);
            infoViewModel.requestBauteilLog(id);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        scanManager.removeDataListener(this);
        scanManager.releaseScanManager();
    }

}
