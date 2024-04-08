package de.glawleschkoff.scannerapp.fragment.bauteilnachbearbeiten;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.keyence.autoid.sdk.scan.DecodeResult;
import com.keyence.autoid.sdk.scan.ScanManager;

import org.jetbrains.annotations.Nullable;

import de.glawleschkoff.scannerapp.R;
import de.glawleschkoff.scannerapp.databinding.FragmentBtnbscanBinding;
import de.glawleschkoff.scannerapp.viewmodel.BTNBViewModel;
import de.glawleschkoff.scannerapp.viewmodel.SuperViewModel;
import io.reactivex.annotations.NonNull;

public class BTNBScanFragment extends Fragment implements ScanManager.DataListener {

    private FragmentBtnbscanBinding binding;
    private BTNBViewModel btnbViewModel;
    private SuperViewModel superViewModel;
    private ScanManager scanManager;

    public static BTNBScanFragment newInstance() {
        return new BTNBScanFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btnbViewModel = new ViewModelProvider(requireActivity()).get(BTNBViewModel.class);
        superViewModel = new ViewModelProvider(requireActivity()).get(SuperViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBtnbscanBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        scanManager = ScanManager.createScanManager(this.getContext());
        scanManager.addDataListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Bauteil Nachbearbeiten");

        binding.text.setOnClickListener(x -> {
            //btnbViewModel.requestUSERALBDetails("4186643-001");
            //btnbViewModel.requestUSERALBDetails("4132359-001");
        });

        btnbViewModel.getResponseUSERALBDetails().observe(getViewLifecycleOwner(), x -> {
            if(getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED){
                if(btnbViewModel.getResponseUSERALBDetails().getValue().getErrorMessage() != null) {
                    scanManager.lockScanner();
                    new AlertDialog.Builder(getContext())
                            .setMessage("Bauteil nicht gefunden")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    scanManager.unlockScanner();
                                    Navigation.findNavController(requireView()).navigate(R.id.action_BTNBScanFragment_self);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } else {
                    Navigation.findNavController(requireView()).navigate(R.id.action_BTNBScanFragment_to_BTNBSelectFragment);
                }
            }
        });

        binding.bt1.setOnClickListener(x -> {
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_BTNBScanFragment_to_menuFragment);
        });

        superViewModel.getMitarbeiter().observe(getViewLifecycleOwner(), x -> {
            if(x==null){
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_BTNBScanFragment_to_loginFragment);
            }
        });
    }

    @Override
    public void onDataReceived(DecodeResult decodeResult) {
        DecodeResult.Result result = decodeResult.getResult();
        String data = decodeResult.getData();
        if(result == DecodeResult.Result.SUCCESS){
            String id = data.startsWith(" ")?data.substring(1):data;
            btnbViewModel.requestUSERALBDetails(id);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        scanManager.removeDataListener(this);
        scanManager.releaseScanManager();
    }
}
