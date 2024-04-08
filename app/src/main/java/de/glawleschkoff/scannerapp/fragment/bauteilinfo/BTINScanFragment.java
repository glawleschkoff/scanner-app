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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.keyence.autoid.sdk.scan.DecodeResult;
import com.keyence.autoid.sdk.scan.ScanManager;

import de.glawleschkoff.scannerapp.R;
import de.glawleschkoff.scannerapp.databinding.FragmentBtinscanBinding;
import de.glawleschkoff.scannerapp.util.AndLiveData;
import de.glawleschkoff.scannerapp.viewmodel.BTINViewModel;
import de.glawleschkoff.scannerapp.viewmodel.SuperViewModel;

public class BTINScanFragment extends Fragment implements ScanManager.DataListener {

    private FragmentBtinscanBinding binding;
    private BTINViewModel btinViewModel;
    private SuperViewModel superViewModel;
    private ScanManager scanManager;

    public static BTINScanFragment newInstance(){
        return new BTINScanFragment();
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
        binding = FragmentBtinscanBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        scanManager = ScanManager.createScanManager(this.getContext());
        scanManager.addDataListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        superViewModel.getMitarbeiter().observe(getViewLifecycleOwner(), x->{
            if(x==null){
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_btinScanFragment_to_loginFragment);
            }
        });

        getActivity().setTitle("Bauteil Info");

        binding.text.setOnClickListener(x -> {
            /*btinViewModel.requestUSERALBDetails("4132359-001");
            btinViewModel.requestUSERCNCFeedback("4132359-001");
            btinViewModel.requestUSERKntFeedback("4132359-001");
            btinViewModel.requestUSERBauteilLog("4132359-001");

             */
        });

        binding.bt1.setOnClickListener(x -> {
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_btinScanFragment_to_menuFragment);
        });

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
                                        Navigation.findNavController(requireView()).navigate(R.id.action_btinScanFragment_self);
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                    } else {
                        Navigation.findNavController(requireView())
                                .navigate(R.id.action_btinScanFragment_to_btinShowFragment);
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
