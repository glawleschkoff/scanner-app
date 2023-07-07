package de.glawleschkoff.scannerapp.fragment.info;

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
import de.glawleschkoff.scannerapp.databinding.FragmentInfoscanBinding;
import de.glawleschkoff.scannerapp.util.AndLiveData;
import de.glawleschkoff.scannerapp.viewmodel.InfoViewModel;
import de.glawleschkoff.scannerapp.viewmodel.MetaViewModel;

public class InfoScanFragment extends Fragment implements ScanManager.DataListener {

    private FragmentInfoscanBinding binding;
    private InfoViewModel infoViewModel;
    private MetaViewModel metaViewModel;
    private ScanManager scanManager;

    public static InfoScanFragment newInstance(){
        return new InfoScanFragment();
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
        binding = FragmentInfoscanBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        scanManager = ScanManager.createScanManager(this.getContext());
        scanManager.addDataListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        metaViewModel.getMitarbeiter().observe(getViewLifecycleOwner(),x->{
            if(x==null){
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_infoScanFragment2_to_loginFragment);
            }
        });

        getActivity().setTitle("Bauteil Info");
        /*binding.text.setOnClickListener(x -> {
            infoViewModel.requestBauteil("3995112-001");
            infoViewModel.requestFeedback("3995112-001_BTZS.csv");
            infoViewModel.requestCNCFeedback("3995112-001");
            infoViewModel.requestKntFeedback("3995112-001");
            infoViewModel.requestBauteilLog("3995112-001");
        });*/
        binding.bt1.setOnClickListener(x -> {
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_infoScanFragment2_to_menuFragment);
        });

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
                                        Navigation.findNavController(requireView()).navigate(R.id.action_infoScanFragment2_self);
                                    }
                                })
                                // A null listener allows the button to dismiss the dialog and take no further action.
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                    } else {
                        Navigation.findNavController(requireView())
                                .navigate(R.id.action_infoScanFragment2_to_infoShowFragment);
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
