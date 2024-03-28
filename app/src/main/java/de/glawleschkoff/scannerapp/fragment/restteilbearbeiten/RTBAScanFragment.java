package de.glawleschkoff.scannerapp.fragment.restteilbearbeiten;

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
import de.glawleschkoff.scannerapp.databinding.FragmentRtbascanBinding;
import de.glawleschkoff.scannerapp.viewmodel.SuperViewModel;
import de.glawleschkoff.scannerapp.viewmodel.RTBAViewModel;
import io.reactivex.annotations.NonNull;

public class RTBAScanFragment extends Fragment implements ScanManager.DataListener {

    private FragmentRtbascanBinding binding;
    private RTBAViewModel rtbaViewModel;
    private SuperViewModel superViewModel;
    private ScanManager scanManager;

    public static RTBAScanFragment newInstance() {
        return new RTBAScanFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rtbaViewModel = new ViewModelProvider(requireActivity()).get(RTBAViewModel.class);
        superViewModel = new ViewModelProvider(requireActivity()).get(SuperViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRtbascanBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        //scanManager = ScanManager.createScanManager(this.getContext());
        //scanManager.addDataListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Restteil Bearbeiten");

        binding.text.setOnClickListener(x -> {
            rtbaViewModel.requestUSERPlattenlager("5068405801");
        });

        rtbaViewModel.getUSERPlattenlager().observe(getViewLifecycleOwner(), x -> {
            if(getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED){
                if(rtbaViewModel.getUSERPlattenlager().getValue().getErrorMessage() != null){
                    scanManager.lockScanner();
                    new AlertDialog.Builder(getContext())
                            .setMessage("Restteil nicht gefunden")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    scanManager.unlockScanner();
                                    Navigation.findNavController(requireView()).navigate(R.id.action_rtbaScanFragment_self);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } else if(rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getMenge()==0) {
                    scanManager.lockScanner();
                    new AlertDialog.Builder(getContext())
                            .setMessage("Ausgelagert von "+ rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getAuslagerInfo())
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    scanManager.unlockScanner();
                                    Navigation.findNavController(requireView()).navigate(R.id.action_rtbaScanFragment_self);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } else if(rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getOptimiert()==0 &&
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getProduktion()==0 &&
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getMenge()!=0){
                    Navigation.findNavController(requireView())
                            .navigate(R.id.action_rtbaScanFragment_to_rtbaSelectFragment);
                } else {
                    scanManager.lockScanner();
                    new AlertDialog.Builder(getContext())
                            .setMessage("Restteil ist reserviert")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    scanManager.unlockScanner();
                                    Navigation.findNavController(requireView()).navigate(R.id.action_rtbaScanFragment_self);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });

        binding.bt1.setOnClickListener(x -> {
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_rtbaScanFragment_to_menuFragment);
        });

        superViewModel.getMitarbeiter().observe(getViewLifecycleOwner(), x -> {
            if(x==null){
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_rtbaScanFragment_to_loginFragment);
            }
        });
    }

    @Override
    public void onDataReceived(DecodeResult decodeResult) {
        DecodeResult.Result result = decodeResult.getResult();
        String data = decodeResult.getData();
        if(result == DecodeResult.Result.SUCCESS){
            String id = data.startsWith(" ")?data.substring(1):data;
            rtbaViewModel.requestUSERPlattenlager(id);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //scanManager.removeDataListener(this);
        //scanManager.releaseScanManager();
    }
}
