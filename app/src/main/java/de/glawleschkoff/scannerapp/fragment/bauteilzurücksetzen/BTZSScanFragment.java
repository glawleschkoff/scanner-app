package de.glawleschkoff.scannerapp.fragment.bauteilzurücksetzen;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.keyence.autoid.sdk.scan.DecodeResult;
import com.keyence.autoid.sdk.scan.ScanManager;

import de.glawleschkoff.scannerapp.databinding.FragmentBtzsscanBinding;
import de.glawleschkoff.scannerapp.util.AndLiveData;
import de.glawleschkoff.scannerapp.viewmodel.BTZSViewModel;
import de.glawleschkoff.scannerapp.R;
import de.glawleschkoff.scannerapp.viewmodel.SuperViewModel;

public class BTZSScanFragment extends Fragment implements ScanManager.DataListener {

    private FragmentBtzsscanBinding binding;
    private BTZSViewModel btzsViewModel;
    private SuperViewModel superViewModel;
    private ScanManager scanManager;

    public static BTZSScanFragment newInstance() {
        return new BTZSScanFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btzsViewModel = new ViewModelProvider(requireActivity()).get(BTZSViewModel.class);
        superViewModel = new ViewModelProvider(requireActivity()).get(SuperViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentBtzsscanBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        scanManager = ScanManager.createScanManager(this.getContext());
        scanManager.addDataListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Bauteil Zurücksetzen");
        binding.text.setOnClickListener(x -> {
            //btzsViewModel.requestUSERALBDetails("4132359-001");
        });


        binding.bt1.setOnClickListener(x -> {
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_btzsScanFragment_to_menuFragment);
        });
        superViewModel.getMitarbeiter().observe(getViewLifecycleOwner(), x->{
            if(x==null){
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_btzsScanFragment_to_loginFragment);
            }
        });


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
                                        Navigation.findNavController(requireView()).navigate(R.id.action_btzsScanFragment_self);
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    } else {
                        Navigation.findNavController(requireView())
                                .navigate(R.id.action_btzsScanFragment_to_btzsSelectFragment);
                    }
                });
    }

    @Override
    public void onDataReceived(DecodeResult decodeResult) {
        DecodeResult.Result result = decodeResult.getResult();
        String data = decodeResult.getData();
        System.out.println(data);
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