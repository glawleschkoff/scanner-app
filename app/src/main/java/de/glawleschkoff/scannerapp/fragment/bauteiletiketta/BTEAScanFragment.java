package de.glawleschkoff.scannerapp.fragment.bauteiletiketta;

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
import de.glawleschkoff.scannerapp.databinding.FragmentBteascanBinding;
import de.glawleschkoff.scannerapp.viewmodel.BTEAViewModel;
import de.glawleschkoff.scannerapp.viewmodel.SuperViewModel;
import io.reactivex.annotations.NonNull;

public class BTEAScanFragment extends Fragment implements ScanManager.DataListener {

    private FragmentBteascanBinding binding;
    private BTEAViewModel bteaViewModel;
    private SuperViewModel superViewModel;
    private ScanManager scanManager;

    public static BTEAScanFragment newInstance() {
        return new BTEAScanFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bteaViewModel = new ViewModelProvider(requireActivity()).get(BTEAViewModel.class);
        superViewModel = new ViewModelProvider(requireActivity()).get(SuperViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBteascanBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        //scanManager = ScanManager.createScanManager(this.getContext());
        //scanManager.addDataListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Bauteil Etikett A");

        binding.text.setOnClickListener(x -> {
            bteaViewModel.requestUSERALBDetails("4132359-001");
            //btzsViewModel.requestBauteil("3914986-001");
            //btzsViewModel.requestFeedback("3914986-001_BTZS.csv");
        });

        bteaViewModel.getResponseUSERALBDetails().observe(getViewLifecycleOwner(), x -> {
            if(getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED){
                if(bteaViewModel.getResponseUSERALBDetails().getValue().getErrorMessage() != null){
                    scanManager.lockScanner();
                    new AlertDialog.Builder(getContext())
                            .setMessage("Bauteil nicht gefunden")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    scanManager.unlockScanner();
                                    Navigation.findNavController(requireView()).navigate(R.id.action_bteaScanFragment_self);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } else {
                    Navigation.findNavController(requireView())
                            .navigate(R.id.action_bteaScanFragment_to_bteaSelectFragment);
                }
            }
        });

        binding.bt1.setOnClickListener(x -> {
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_bteaScanFragment_to_menuFragment);
        });

        superViewModel.getMitarbeiter().observe(getViewLifecycleOwner(), x -> {
            if(x==null){
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_bteaScanFragment_to_loginFragment);
            }
        });
    }

    @Override
    public void onDataReceived(DecodeResult decodeResult) {
        DecodeResult.Result result = decodeResult.getResult();
        String data = decodeResult.getData();
        if(result == DecodeResult.Result.SUCCESS){
            String id = data.startsWith(" ")?data.substring(1):data;
            bteaViewModel.requestUSERALBDetails(id);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //scanManager.removeDataListener(this);
        //scanManager.releaseScanManager();
    }
}
