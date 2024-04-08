package de.glawleschkoff.scannerapp.fragment.bauteilquali;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.keyence.autoid.sdk.scan.DecodeResult;
import com.keyence.autoid.sdk.scan.ScanManager;
import com.keyence.autoid.sdk.notification.Notification;

import org.jetbrains.annotations.Nullable;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import de.glawleschkoff.scannerapp.R;
import de.glawleschkoff.scannerapp.databinding.FragmentBtqlselectBinding;
import de.glawleschkoff.scannerapp.util.AndLiveData;
import de.glawleschkoff.scannerapp.util.RVAdapter;
import de.glawleschkoff.scannerapp.util.RVItem;
import de.glawleschkoff.scannerapp.viewmodel.SuperViewModel;
import de.glawleschkoff.scannerapp.viewmodel.BTQLViewModel;
import io.reactivex.annotations.NonNull;

public class BTQLSelectFragment extends Fragment implements ScanManager.DataListener{

    private FragmentBtqlselectBinding binding;
    private BTQLViewModel btqlViewModel;
    private SuperViewModel superViewModel;
    private RVAdapter rvAdapter;
    private ScanManager scanManager;
    private Notification notification;

    public static BTQLSelectFragment newInstance() {
        return new BTQLSelectFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btqlViewModel = new ViewModelProvider(requireActivity()).get(BTQLViewModel.class);
        superViewModel = new ViewModelProvider(requireActivity()).get(SuperViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBtqlselectBinding.inflate(getLayoutInflater());
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
            metaViewModel.setPrqmLetzterAuftrag(prqmViewModel.getResponseBauteil().getValue().getResponse().getKundenAuftrag());
            switch(new Random().nextInt(1 - 0 + 1) + 0){
                case 0:
                    // case 3
                    prqmViewModel.requestBauteil("4107047-001"); break;
                case 1:
                    // case 4
                    prqmViewModel.requestBauteil("4107015-001"); break;
                case 2:
                    // case 1
                    //prqmViewModel.requestBauteil("4078332-001"); break;
                default:
            }
        });

         */

        AndLiveData.use(getViewLifecycleOwner())
                .add(btqlViewModel.getResponseUSERALBDetails())
                .observe(getViewLifecycleOwner(),x->{
                    if(btqlViewModel.getResponseUSERALBDetails().getValue().getErrorMessage() != null){
                        scanManager.lockScanner();
                        new AlertDialog.Builder(getContext())
                                .setMessage("Bauteil nicht gefunden")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        scanManager.unlockScanner();
                                        Navigation.findNavController(requireView()).navigate(R.id.action_btqlSelectFragment_to_btqlScanFragment);
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    } else {
                        Navigation.findNavController(requireView())
                                .navigate(R.id.action_btqlSelectFragment_self);
                    }
                        });

        List<RVItem> list1 = new ArrayList<>();
        list1.add(new RVItem("Lädt...", ""));

        rvAdapter = new RVAdapter(this.getContext(),list1);
        binding.rv.setAdapter(rvAdapter);
        binding.rv.setLayoutManager(new LinearLayoutManager(this.getContext()));

        btqlViewModel.getResponseBitmap().observe(getViewLifecycleOwner(), x -> {
            if(x.getResponse()!=null){
                binding.image.setImageBitmap(x.getResponse());
            }
        });

        btqlViewModel.getResponseUSERALBDetails().observe(getViewLifecycleOwner(), x -> {
            if(btqlViewModel.getResponseUSERALBDetails().getValue().getResponse()!=null){
                btqlViewModel.requestBitmap(btqlViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKundenAuftrag(),
                        btqlViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKundenPosition());
            }

            if(x.getResponse() != null){
                if(!Arrays.stream(btqlViewModel.getResponseUSERALBDetails().getValue().getResponse()
                        .getScannerAnweisung().split("#")).filter(y -> y.startsWith("BTQM=J")).collect(Collectors.toList()).isEmpty() ||
                        !Arrays.stream(btqlViewModel.getResponseUSERALBDetails().getValue().getResponse()
                                .getScannerAnweisung().split("#")).filter(y -> y.startsWith("BTQM=F")).collect(Collectors.toList()).isEmpty()){
                    btqlViewModel.requestUSERKommWagen(btqlViewModel.getResponseUSERALBDetails().getValue().getResponse().getKundenAuftrag());
                } else if(!Arrays.stream(btqlViewModel.getResponseUSERALBDetails().getValue().getResponse()
                        .getScannerAnweisung().split("#")).filter(y -> y.startsWith("BTQM=N")).collect(Collectors.toList()).isEmpty()) {
                    // (3)
                    rvAdapter.setRecyclerViewItems(Arrays.asList(
                            new RVItem("ExemplarNr", btqlViewModel.getResponseUSERALBDetails().getValue().getResponse().getExemplarNr()),
                            new RVItem("Auftrag", btqlViewModel.getResponseUSERALBDetails().getValue().getResponse().getKundenAuftrag()),
                            new RVItem("Position", btqlViewModel.getResponseUSERALBDetails().getValue().getResponse().getKundenPosition())
                    ));
                    binding.text.setText("Bearbeitung unvollständig");
                    binding.frameWagenkennung.setText("!!!");
                    binding.frameWagenkennung.setTextColor(Color.parseColor("#ff0000"));
                } else {
                    // (6)
                    rvAdapter.setRecyclerViewItems(Arrays.asList(
                            new RVItem("ExemplarNr", btqlViewModel.getResponseUSERALBDetails().getValue().getResponse().getExemplarNr()),
                            new RVItem("Auftrag", btqlViewModel.getResponseUSERALBDetails().getValue().getResponse().getKundenAuftrag()),
                            new RVItem("Position", btqlViewModel.getResponseUSERALBDetails().getValue().getResponse().getKundenPosition())
                    ));
                    binding.text.setText("Anweisung nicht vorhanden");
                    binding.frameWagenkennung.setText("!!!");
                    binding.frameWagenkennung.setTextColor(Color.parseColor("#ff0000"));
                }
            }
        });



        btqlViewModel.getResponseUSERKommWagen().observe(getViewLifecycleOwner(), x -> {
            if(getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED){
                if(x.getResponse() != null){
                    if(!Arrays.stream(btqlViewModel.getResponseUSERALBDetails().getValue().getResponse()
                            .getScannerAntwort().split("#")).filter(y -> y.startsWith("BTQM")).collect(Collectors.toList()).isEmpty() ||
                            !Arrays.stream(btqlViewModel.getResponseUSERALBDetails().getValue().getResponse()
                                    .getScannerAnweisung().split("#")).filter(y -> y.startsWith("BTQM=F")).collect(Collectors.toList()).isEmpty()){
                        // (5)
                        rvAdapter.setRecyclerViewItems(Arrays.asList(
                                new RVItem("ExemplarNr", btqlViewModel.getResponseUSERALBDetails().getValue().getResponse().getExemplarNr()),
                                new RVItem("Auftrag", btqlViewModel.getResponseUSERALBDetails().getValue().getResponse().getKundenAuftrag()),
                                new RVItem("Position", btqlViewModel.getResponseUSERALBDetails().getValue().getResponse().getKundenPosition())
                        ));
                        binding.text.setText("Qualitätskontrolle bereits erfolgt");
                        binding.frameWagenkennung.setText(btqlViewModel.getResponseUSERKommWagen().getValue().getResponse().getWagenKennung());
                    } else {
                        if(!superViewModel.getPrqmLetzterAuftrag().getValue().equals(btqlViewModel.getResponseUSERALBDetails().getValue().getResponse().getKundenAuftrag()) &&
                        !superViewModel.getPrqmLetzterAuftrag().getValue().equals("")){
                            // (2)
                            rvAdapter.setRecyclerViewItems(Arrays.asList(
                                    new RVItem("ExemplarNr", btqlViewModel.getResponseUSERALBDetails().getValue().getResponse().getExemplarNr()),
                                    new RVItem("Auftrag", btqlViewModel.getResponseUSERALBDetails().getValue().getResponse().getKundenAuftrag()),
                                    new RVItem("Position", btqlViewModel.getResponseUSERALBDetails().getValue().getResponse().getKundenPosition())
                            ));
                            binding.text.setText("Auftragswechsel");
                            binding.frameWagenkennung.setText(btqlViewModel.getResponseUSERKommWagen().getValue().getResponse().getWagenKennung());
                            binding.frameWagenkennung.setTextColor(Color.parseColor("#ffd500"));
                            notification.startBuzzer(16,200,50,10);
                            notification.startLed(Notification.Led.YELLOW,200,50,10);

                            String antwort = btqlViewModel.getResponseUSERALBDetails().getValue().getResponse().getScannerAntwort().equals("")?
                                    "BTQM="+";;"+";"+ superViewModel.getMitarbeiter().getValue():
                                    btqlViewModel.getResponseUSERALBDetails().getValue().getResponse().getScannerAntwort()+"#"+
                                            "BTQM="+";;"+";"+ superViewModel.getMitarbeiter().getValue();
                            btqlViewModel.updateUSERALBDetails(btqlViewModel.getResponseUSERALBDetails().getValue().getResponse().getExemplarNr(),antwort);

                        } else {
                            // (1)
                            rvAdapter.setRecyclerViewItems(Arrays.asList(
                                    new RVItem("ExemplarNr", btqlViewModel.getResponseUSERALBDetails().getValue().getResponse().getExemplarNr()),
                                    new RVItem("Auftrag", btqlViewModel.getResponseUSERALBDetails().getValue().getResponse().getKundenAuftrag()),
                                    new RVItem("Position", btqlViewModel.getResponseUSERALBDetails().getValue().getResponse().getKundenPosition())
                            ));
                            binding.text.setText("");
                            binding.frameWagenkennung.setText(btqlViewModel.getResponseUSERKommWagen().getValue().getResponse().getWagenKennung());
                            binding.frameWagenkennung.setTextColor(Color.parseColor("#00ff00"));

                            String antwort = btqlViewModel.getResponseUSERALBDetails().getValue().getResponse().getScannerAntwort().equals("")?
                                    "BTQM="+";;"+";"+ superViewModel.getMitarbeiter().getValue():
                                    btqlViewModel.getResponseUSERALBDetails().getValue().getResponse().getScannerAntwort()+"#"+"BTQM="+
                                            ";;"+";"+ superViewModel.getMitarbeiter().getValue();
                            btqlViewModel.updateUSERALBDetails(btqlViewModel.getResponseUSERALBDetails().getValue().getResponse().getExemplarNr(),antwort);
                        }
                    }
                } else {
                    // (4)
                    rvAdapter.setRecyclerViewItems(Arrays.asList(
                            new RVItem("ExemplarNr", btqlViewModel.getResponseUSERALBDetails().getValue().getResponse().getExemplarNr()),
                            new RVItem("KundenAuftrag", btqlViewModel.getResponseUSERALBDetails().getValue().getResponse().getKundenAuftrag()),
                            new RVItem("Position", btqlViewModel.getResponseUSERALBDetails().getValue().getResponse().getKundenPosition())
                    ));
                    binding.text.setText("Wagenkennung nicht vorhanden");
                    binding.frameWagenkennung.setText("!!!");
                    binding.frameWagenkennung.setTextColor(Color.parseColor("#ff0000"));
                }
            }
        });

        binding.button.setOnClickListener(x -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_btqlSelectFragment_to_btqlScanFragment);
        });

        superViewModel.getMitarbeiter().observe(getViewLifecycleOwner(), x -> {
            if(x==null){
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_btqlSelectFragment_to_loginFragment);
            }
        });
    }

    @Override
    public void onDataReceived(DecodeResult decodeResult) {
        superViewModel.setPrqmLetzterAuftrag(btqlViewModel.getResponseUSERALBDetails().getValue().getResponse().getKundenAuftrag());
        DecodeResult.Result result = decodeResult.getResult();
        String data = decodeResult.getData();
        if(result == DecodeResult.Result.SUCCESS){
            String id = data.startsWith(" ")?data.substring(1):data;
            btqlViewModel.requestUSERALBDetails(id);
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
