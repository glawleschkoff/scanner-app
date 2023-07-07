package de.glawleschkoff.scannerapp.fragment.btzs;

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
import de.glawleschkoff.scannerapp.viewmodel.MetaViewModel;

public class BTZSScanFragment extends Fragment implements ScanManager.DataListener {

    private FragmentBtzsscanBinding binding;
    private BTZSViewModel btzsViewModel;
    private MetaViewModel metaViewModel;
    private ScanManager scanManager;

    public static BTZSScanFragment newInstance() {
        return new BTZSScanFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btzsViewModel = new ViewModelProvider(requireActivity()).get(BTZSViewModel.class);
        metaViewModel = new ViewModelProvider(requireActivity()).get(MetaViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentBtzsscanBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        //scanManager = ScanManager.createScanManager(this.getContext());
        //scanManager.addDataListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Bauteil Zurücksetzen");
        binding.text.setOnClickListener(x -> {
            btzsViewModel.requestBauteil("3914986-001");
            btzsViewModel.requestFeedback("3914986-001_BTZS.csv");
        });
        binding.bt1.setOnClickListener(x -> {
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_BTZSScanFragment_to_menuFragment);
        });
        metaViewModel.getMitarbeiter().observe(getViewLifecycleOwner(),x->{
            if(x==null){
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_BTZSScanFragment_to_loginFragment);
            }
        });

        AndLiveData.use(getViewLifecycleOwner())
                .add(btzsViewModel.getResponseBauteil())
                .add(btzsViewModel.getResponseFeedback())
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
                                        Navigation.findNavController(requireView()).navigate(R.id.action_BTZSScanFragment_self);
                                    }
                                })
                                // A null listener allows the button to dismiss the dialog and take no further action.
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    } else {

                        Navigation.findNavController(requireView())
                                .navigate(R.id.action_BTZSScanFragment_to_BTZSSelectFragment3);
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
            btzsViewModel.requestFeedback(id+"_BTZS.csv");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //scanManager.removeDataListener(this);
        //scanManager.releaseScanManager();
    }

}