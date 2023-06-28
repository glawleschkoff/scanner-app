package de.glawleschkoff.scannerapp.fragment.pleb;

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
import de.glawleschkoff.scannerapp.databinding.FragmentPlebscanBinding;
import de.glawleschkoff.scannerapp.viewmodel.MetaViewModel;
import de.glawleschkoff.scannerapp.viewmodel.PLEBViewModel;
import io.reactivex.annotations.NonNull;

public class PLEBScanFragment extends Fragment implements ScanManager.DataListener {

    private FragmentPlebscanBinding binding;
    private PLEBViewModel plebViewModel;
    private MetaViewModel metaViewModel;
    private ScanManager scanManager;

    public static PLEBScanFragment newInstance() {
        return new PLEBScanFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        plebViewModel = new ViewModelProvider(requireActivity()).get(PLEBViewModel.class);
        metaViewModel = new ViewModelProvider(requireActivity()).get(MetaViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPlebscanBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        scanManager = ScanManager.createScanManager(this.getContext());
        scanManager.addDataListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Plattenlager Einbuchen");

        binding.text.setOnClickListener(x -> {
            plebViewModel.requestPlattenlager("5038200101");
        });

        plebViewModel.getPlattenlagerModel().observe(getViewLifecycleOwner(), x -> {
            if(getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED){
                if(plebViewModel.getPlattenlagerModel().getValue().getErrorMessage() != null){
                    new AlertDialog.Builder(getContext())
                            //.setTitle("Delete entry")
                            .setMessage("Bauteil nicht gefunden")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Navigation.findNavController(requireView()).navigate(R.id.action_PLEBScanFragment_self);
                                }
                            })
                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } else {
                    Navigation.findNavController(requireView())
                            .navigate(R.id.action_PLEBScanFragment_to_PLEBSelectFragment);
                }
            }

        });

        binding.bt1.setOnClickListener(x -> {
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_PLEBScanFragment_to_menuFragment);
        });

        metaViewModel.getMitarbeiter().observe(getViewLifecycleOwner(),x -> {
            if(x==null){
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_PLEBScanFragment_to_loginFragment);
            }
        });
    }

    @Override
    public void onDataReceived(DecodeResult decodeResult) {
        DecodeResult.Result result = decodeResult.getResult();
        String codeType = decodeResult.getCodeType();
        String data = decodeResult.getData();
        //Toast.makeText(this.getContext(), data, Toast.LENGTH_SHORT).show();
        System.out.println(data);
        if(decodeResult.getResult() == DecodeResult.Result.SUCCESS){
            String id = data.substring(0);
            plebViewModel.requestPlattenlager(id);
            //rtebViewModel.setFeedbackRestteil(id);
            //rtebViewModel.requestFeedback(id.split("%")[0]+"_RTEB.csv");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        scanManager.removeDataListener(this);
        scanManager.releaseScanManager();
    }
}
