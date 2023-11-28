package de.glawleschkoff.scannerapp.fragment.prqm;

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
import de.glawleschkoff.scannerapp.databinding.FragmentPrqmscanBinding;
import de.glawleschkoff.scannerapp.databinding.FragmentRtabscanBinding;
import de.glawleschkoff.scannerapp.fragment.rtab.RTABScanFragment;
import de.glawleschkoff.scannerapp.viewmodel.MetaViewModel;
import de.glawleschkoff.scannerapp.viewmodel.PRQMViewModel;
import de.glawleschkoff.scannerapp.viewmodel.RTABViewModel;
import io.reactivex.annotations.NonNull;

public class PRQMScanFragment extends Fragment implements ScanManager.DataListener {

    private FragmentPrqmscanBinding binding;
    private PRQMViewModel prqmViewModel;
    private MetaViewModel metaViewModel;
    private ScanManager scanManager;

    public static PRQMScanFragment newInstance() {
        return new PRQMScanFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prqmViewModel = new ViewModelProvider(requireActivity()).get(PRQMViewModel.class);
        metaViewModel = new ViewModelProvider(requireActivity()).get(MetaViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPrqmscanBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        scanManager = ScanManager.createScanManager(this.getContext());
        scanManager.addDataListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Qualitätskontrolle");

        binding.text.setOnClickListener(x -> {
            //prqmViewModel.requestBauteil("4101777-001");
            //btzsViewModel.requestBauteil("3914986-001");
            //btzsViewModel.requestFeedback("3914986-001_BTZS.csv");
        });



        prqmViewModel.getResponseBauteil().observe(getViewLifecycleOwner(), x -> {
            if(getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED){
                if(prqmViewModel.getResponseBauteil().getValue().getErrorMessage() != null){
                    new AlertDialog.Builder(getContext())
                            .setMessage("Bauteil nicht gefunden")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Navigation.findNavController(requireView()).navigate(R.id.action_BTZSScanFragment_self);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } else {
                    System.out.println("hier123");
                    Navigation.findNavController(requireView())
                            .navigate(R.id.action_PRQMScanFragment_to_PRQMSelectFragment);
                }
            }
        });



        binding.bt1.setOnClickListener(x -> {
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_PRQMScanFragment_to_menuFragment);
        });

        metaViewModel.getMitarbeiter().observe(getViewLifecycleOwner(),x -> {
            if(x==null){
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_PRQMScanFragment_to_loginFragment);
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
            String id = data.startsWith(" ")?data.substring(1):data;
            prqmViewModel.requestBauteil(id);
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
