package de.glawleschkoff.scannerapp.fragment.bauteiletiketta;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.keyence.autoid.sdk.notification.Notification;
import com.keyence.autoid.sdk.scan.DecodeResult;
import com.keyence.autoid.sdk.scan.ScanManager;

import org.jetbrains.annotations.Nullable;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import de.glawleschkoff.scannerapp.R;
import de.glawleschkoff.scannerapp.databinding.FragmentBteaselectBinding;
import de.glawleschkoff.scannerapp.util.AndLiveData;
import de.glawleschkoff.scannerapp.util.RVAdapter;
import de.glawleschkoff.scannerapp.util.RVItem;
import de.glawleschkoff.scannerapp.viewmodel.BTEAViewModel;
import de.glawleschkoff.scannerapp.viewmodel.SuperViewModel;
import io.reactivex.annotations.NonNull;

public class BTEASelectFragment extends Fragment implements ScanManager.DataListener {

    private FragmentBteaselectBinding binding;
    private BTEAViewModel bteaViewModel;
    private SuperViewModel superViewModel;
    private RVAdapter rvAdapter;
    private ScanManager scanManager;
    private Notification notification;

    public static BTEASelectFragment newInstance() {
        return new BTEASelectFragment();
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
        binding = FragmentBteaselectBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        scanManager = ScanManager.createScanManager(this.getContext());
        scanManager.addDataListener(this);
        notification = Notification.createNotification(this.getContext());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*binding.text.setOnClickListener(x -> {
            metaViewModel.setPrqmLetzterAuftrag(btedViewModel.getResponseBauteil().getValue().getResponse().getKundenAuftrag());
            switch(new Random().nextInt(1 - 0 + 1) + 0){
                case 0:
                    // case 3
                    btedViewModel.requestBauteil("4107047-001"); break;
                case 1:
                    // case 4
                    btedViewModel.requestBauteil("4107015-001"); break;
                case 2:
                    // case 1
                    //prqmViewModel.requestBauteil("4078332-001"); break;
                default:
            }
        });

         */


        AndLiveData.use(getViewLifecycleOwner())
                .add(bteaViewModel.getResponseUSERALBDetails())
                .observe(getViewLifecycleOwner(),x->{
                    if(bteaViewModel.getResponseUSERALBDetails().getValue().getErrorMessage() != null){
                        scanManager.lockScanner();
                        new AlertDialog.Builder(getContext())
                                .setMessage("Bauteil nicht gefunden")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        scanManager.unlockScanner();
                                        Navigation.findNavController(requireView()).navigate(R.id.action_bteaSelectFragment_to_bteaScanFragment);
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    } else {
                        Navigation.findNavController(requireView())
                                .navigate(R.id.action_bteaSelectFragment_self);
                    }
                });

        List<RVItem> list1 = new ArrayList<>();
        list1.add(new RVItem("Lädt...", ""));

        rvAdapter = new RVAdapter(this.getContext(),list1);
        binding.rv.setAdapter(rvAdapter);
        binding.rv.setLayoutManager(new LinearLayoutManager(this.getContext()));

        bteaViewModel.getResponseBitmap().observe(getViewLifecycleOwner(), x -> {
            if(x.getResponse()!=null){
                binding.image.setImageBitmap(x.getResponse());
            }
        });

        bteaViewModel.getResponseUSERALBDetails().observe(getViewLifecycleOwner(), x -> {
            if(bteaViewModel.getResponseUSERALBDetails().getValue().getResponse()!=null){
                bteaViewModel.requestBitmap(bteaViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKundenAuftrag(),
                        bteaViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKundenPosition());
            }

            if(x.getResponse() != null){
                if(!Arrays.stream(bteaViewModel.getResponseUSERALBDetails().getValue().getResponse()
                        .getScannerAnweisung().split("#")).filter(y -> y.startsWith("BTETI=J")).collect(Collectors.toList()).isEmpty()){
                    if(!Arrays.stream(bteaViewModel.getResponseUSERALBDetails().getValue().getResponse()
                            .getScannerAntwort().split("#")).filter(y -> y.startsWith("BTETI")).collect(Collectors.toList()).isEmpty()){
                        rvAdapter.setRecyclerViewItems(Arrays.asList(
                                new RVItem("ExemplarNr", bteaViewModel.getResponseUSERALBDetails().getValue().getResponse().getExemplarNr()),
                                new RVItem("Auftrag", bteaViewModel.getResponseUSERALBDetails().getValue().getResponse().getKundenAuftrag()),
                                new RVItem("Position", bteaViewModel.getResponseUSERALBDetails().getValue().getResponse().getKundenPosition())
                        ));
                        binding.text.setText("Bereits ausgeführt");
                        binding.frameWagenkennung.setText("gedruckt");
                    } else {
                        rvAdapter.setRecyclerViewItems(Arrays.asList(
                                new RVItem("ExemplarNr", bteaViewModel.getResponseUSERALBDetails().getValue().getResponse().getExemplarNr()),
                                new RVItem("Auftrag", bteaViewModel.getResponseUSERALBDetails().getValue().getResponse().getKundenAuftrag()),
                                new RVItem("Position", bteaViewModel.getResponseUSERALBDetails().getValue().getResponse().getKundenPosition())
                        ));
                        binding.text.setText("");
                        binding.frameWagenkennung.setText("gedruckt");
                        binding.frameWagenkennung.setTextColor(Color.parseColor("#00ff00"));

                        String antwort = bteaViewModel.getResponseUSERALBDetails().getValue().getResponse().getScannerAntwort().equals("")?
                                "BTETI="+";;"+";"+ superViewModel.getMitarbeiter().getValue()+";A":
                                bteaViewModel.getResponseUSERALBDetails().getValue().getResponse().getScannerAntwort()+"#"+"BTETI="+
                                        ";;"+";"+ superViewModel.getMitarbeiter().getValue()+";A";
                        bteaViewModel.updateUSERALBDetails(bteaViewModel.getResponseUSERALBDetails().getValue().getResponse().getExemplarNr(),antwort);
                    }
                } else if(!Arrays.stream(bteaViewModel.getResponseUSERALBDetails().getValue().getResponse()
                        .getScannerAnweisung().split("#")).filter(y -> y.startsWith("BTETI=N")).collect(Collectors.toList()).isEmpty()) {
                    // (3)
                    rvAdapter.setRecyclerViewItems(Arrays.asList(
                            new RVItem("ExemplarNr", bteaViewModel.getResponseUSERALBDetails().getValue().getResponse().getExemplarNr()),
                            new RVItem("Auftrag", bteaViewModel.getResponseUSERALBDetails().getValue().getResponse().getKundenAuftrag()),
                            new RVItem("Position", bteaViewModel.getResponseUSERALBDetails().getValue().getResponse().getKundenPosition())
                    ));
                    binding.text.setText("Bauteil nicht vorhanden");
                    binding.frameWagenkennung.setText("!!!");
                    binding.frameWagenkennung.setTextColor(Color.parseColor("#ff0000"));
                } else {
                    // (6)
                    rvAdapter.setRecyclerViewItems(Arrays.asList(
                            new RVItem("ExemplarNr", bteaViewModel.getResponseUSERALBDetails().getValue().getResponse().getExemplarNr()),
                            new RVItem("Auftrag", bteaViewModel.getResponseUSERALBDetails().getValue().getResponse().getKundenAuftrag()),
                            new RVItem("Position", bteaViewModel.getResponseUSERALBDetails().getValue().getResponse().getKundenPosition())
                    ));
                    binding.text.setText("Anweisung nicht vorhanden");
                    binding.frameWagenkennung.setText("!!!");
                    binding.frameWagenkennung.setTextColor(Color.parseColor("#ff0000"));
                }
            }
        });

        binding.button.setOnClickListener(x -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_bteaSelectFragment_to_bteaScanFragment);
        });

        superViewModel.getMitarbeiter().observe(getViewLifecycleOwner(), x -> {
            if(x==null){
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_bteaSelectFragment_to_loginFragment);
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
            bteaViewModel.requestUSERALBDetails(id);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        scanManager.removeDataListener(this);
        scanManager.releaseScanManager();
        notification.releaseNotification();
    }
}
