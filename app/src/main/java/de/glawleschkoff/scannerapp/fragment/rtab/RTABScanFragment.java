package de.glawleschkoff.scannerapp.fragment.rtab;

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
import de.glawleschkoff.scannerapp.databinding.FragmentRtabscanBinding;
import de.glawleschkoff.scannerapp.viewmodel.MetaViewModel;
import de.glawleschkoff.scannerapp.viewmodel.RTABViewModel;
import io.reactivex.annotations.NonNull;

public class RTABScanFragment extends Fragment implements ScanManager.DataListener {

    private FragmentRtabscanBinding binding;
    private RTABViewModel rtabViewModel;
    private MetaViewModel metaViewModel;
    private ScanManager scanManager;

    public static RTABScanFragment newInstance() {
        return new RTABScanFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rtabViewModel = new ViewModelProvider(requireActivity()).get(RTABViewModel.class);
        metaViewModel = new ViewModelProvider(requireActivity()).get(MetaViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRtabscanBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        scanManager = ScanManager.createScanManager(this.getContext());
        scanManager.addDataListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Restteil auslagern");

        /*binding.text.setOnClickListener(x -> {
            rtabViewModel.requestPlattenlager("5065105101");
        });

         */

        rtabViewModel.getPlattenlagerModel().observe(getViewLifecycleOwner(), x -> {
            if(getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED){
                if(rtabViewModel.getPlattenlagerModel().getValue().getErrorMessage() != null){
                    System.out.println("Fehler: "+rtabViewModel.getPlattenlagerModel().getValue().getErrorMessage());
                    new AlertDialog.Builder(getContext())
                            //.setTitle("Delete entry")
                            .setMessage("Bauteil nicht gefunden")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Navigation.findNavController(requireView()).navigate(R.id.action_RTABScanFragment_self);
                                }
                            })
                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } else if(rtabViewModel.getPlattenlagerModel().getValue().getResponse().getMenge()==0) {
                    new AlertDialog.Builder(getContext())
                            //.setTitle("Delete entry")
                            .setMessage("Ausgelagert von "+rtabViewModel.getPlattenlagerModel().getValue().getResponse().getAuslagerInfo())

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Navigation.findNavController(requireView()).navigate(R.id.action_RTABScanFragment_self);
                                }
                            })
                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } else if(rtabViewModel.getPlattenlagerModel().getValue().getResponse().getOptimiert()==0 &&
                        rtabViewModel.getPlattenlagerModel().getValue().getResponse().getProduktion()==0 &&
                        rtabViewModel.getPlattenlagerModel().getValue().getResponse().getMenge()!=0){
                    Navigation.findNavController(requireView())
                            .navigate(R.id.action_RTABScanFragment_to_RTABSelectFragment);
                } else {
                    new AlertDialog.Builder(getContext())
                            //.setTitle("Delete entry")
                            .setMessage("Bauteil ist reserviert")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Navigation.findNavController(requireView()).navigate(R.id.action_RTABScanFragment_self);
                                }
                            })
                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }

        });

        binding.bt1.setOnClickListener(x -> {
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_RTABScanFragment_to_menuFragment);
        });

        metaViewModel.getMitarbeiter().observe(getViewLifecycleOwner(),x -> {
            if(x==null){
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_RTABScanFragment_to_loginFragment);
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
            rtabViewModel.requestPlattenlager(id);
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
