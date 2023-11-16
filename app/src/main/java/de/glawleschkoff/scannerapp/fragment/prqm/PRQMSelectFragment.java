package de.glawleschkoff.scannerapp.fragment.prqm;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import java.util.Random;
import java.util.stream.Collectors;

import de.glawleschkoff.scannerapp.R;
import de.glawleschkoff.scannerapp.databinding.FragmentPrqmscanBinding;
import de.glawleschkoff.scannerapp.databinding.FragmentPrqmselectBinding;
import de.glawleschkoff.scannerapp.databinding.FragmentRtabselectBinding;
import de.glawleschkoff.scannerapp.util.AndLiveData;
import de.glawleschkoff.scannerapp.util.PLEBCardRVAdapter;
import de.glawleschkoff.scannerapp.util.RVAdapter;
import de.glawleschkoff.scannerapp.util.RVItem;
import de.glawleschkoff.scannerapp.viewmodel.MetaViewModel;
import de.glawleschkoff.scannerapp.viewmodel.PRQMViewModel;
import io.reactivex.annotations.NonNull;

public class PRQMSelectFragment extends Fragment implements ScanManager.DataListener{

    private FragmentPrqmselectBinding binding;
    private PRQMViewModel prqmViewModel;
    private MetaViewModel metaViewModel;
    private PLEBCardRVAdapter plebCardRVAdapter;
    private ScanManager scanManager;
    private Notification notification;

    public static PRQMSelectFragment newInstance() {
        return new PRQMSelectFragment();
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
        binding = FragmentPrqmselectBinding.inflate(getLayoutInflater());
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
            switch(new Random().nextInt(2 - 0 + 1) + 0){
                case 0:
                    // case 3
                    //prqmViewModel.requestBauteil("4083499-002"); break;
                case 1:
                    // case 4
                    //prqmViewModel.requestBauteil("4059942-001"); break;
                case 2:
                    // case 1
                    prqmViewModel.requestBauteil("4078332-001"); break;
                default:
            }
        });

         */

        AndLiveData.use(getViewLifecycleOwner())
                .add(prqmViewModel.getResponseBauteil())
                .observe(getViewLifecycleOwner(),x->{
                    if(prqmViewModel.getResponseBauteil().getValue().getErrorMessage() != null){
                        new AlertDialog.Builder(getContext())
                                .setMessage("Bauteil nicht gefunden")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Navigation.findNavController(requireView()).navigate(R.id.action_PRQMSelectFragment_to_PRQMScanFragment);
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    } else {
                        Navigation.findNavController(requireView())
                                .navigate(R.id.action_PRQMSelectFragment_self);
                    }
                        });

        List<RVItem> list1 = new ArrayList<>();
        list1.add(new RVItem("Lädt...", ""));

        plebCardRVAdapter = new PLEBCardRVAdapter(list1,this.getContext(), x -> {});
        binding.rv.setAdapter(plebCardRVAdapter);
        binding.rv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        //binding.rv.setHasFixedSize(true);

        prqmViewModel.getResponseBauteil().observe(getViewLifecycleOwner(), x -> {
            System.out.println(x.getResponse());
            if(x.getResponse() != null){
                if(!Arrays.stream(prqmViewModel.getResponseBauteil().getValue().getResponse()
                        .getScannerAnweisung().split("#")).filter(y -> y.startsWith("BTQM=J")).collect(Collectors.toList()).isEmpty()){
                    if(!Arrays.stream(prqmViewModel.getResponseBauteil().getValue().getResponse()
                            .getScannerAntwort().split("#")).filter(y -> y.startsWith("BTQM")).collect(Collectors.toList()).isEmpty()){
                        // C
                        plebCardRVAdapter.setRvItemList(Arrays.asList(
                                new RVItem("ExemplarNr", prqmViewModel.getResponseBauteil().getValue().getResponse().getExemplarNr()),
                                new RVItem("Kunden-\nAuftrag", prqmViewModel.getResponseBauteil().getValue().getResponse().getKundenAuftrag()),
                                new RVItem("Kunden-\nPosition",prqmViewModel.getResponseBauteil().getValue().getResponse().getKundenPosition()),
                                new RVItem("Wagen-\nkennung", "keine")
                        ));
                        binding.text.setText("Bereits gescannt + keine Wagenkennung");
                        binding.text.setTextColor(Color.parseColor("#ff0000"));
                        notification.startBuzzer(12,0,1000,1);
                    } else {
                        prqmViewModel.requestKommWagen(prqmViewModel.getResponseBauteil().getValue().getResponse().getKundenAuftrag());
                    }
                } else if(!Arrays.stream(prqmViewModel.getResponseBauteil().getValue().getResponse()
                        .getScannerAnweisung().split("#")).filter(y -> y.startsWith("BTQM=N")).collect(Collectors.toList()).isEmpty()){
                    // B
                    plebCardRVAdapter.setRvItemList(Arrays.asList(
                            new RVItem("ExemplarNr", prqmViewModel.getResponseBauteil().getValue().getResponse().getExemplarNr()),
                            new RVItem("Kunden-\nAuftrag", prqmViewModel.getResponseBauteil().getValue().getResponse().getKundenAuftrag()),
                            new RVItem("Kunden-\nPosition",prqmViewModel.getResponseBauteil().getValue().getResponse().getKundenPosition()),
                            new RVItem("Wagen-\nkennung", "keine")
                    ));
                    binding.text.setText("Bauteil unvollständig");
                    binding.text.setTextColor(Color.parseColor("#ff0000"));
                    notification.startBuzzer(12,0,1000,1);
                } else {
                    // A
                    plebCardRVAdapter.setRvItemList(Arrays.asList(
                            new RVItem("ExemplarNr", prqmViewModel.getResponseBauteil().getValue().getResponse().getExemplarNr()),
                            new RVItem("Kunden-\nAuftrag", prqmViewModel.getResponseBauteil().getValue().getResponse().getKundenAuftrag()),
                            new RVItem("Kunden-\nPosition",prqmViewModel.getResponseBauteil().getValue().getResponse().getKundenPosition()),
                            new RVItem("Wagen-\nkennung", "keine")
                    ));
                    binding.text.setText("Datensatz nicht vorhanden");
                    binding.text.setTextColor(Color.parseColor("#ff0000"));
                    notification.startBuzzer(12,0,1000,1);
                }
            }
        });



        prqmViewModel.getResponseKommWagen().observe(getViewLifecycleOwner(), x -> {
            if(getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED){
                if(x.getResponse() != null){
                    // D, D
                    if(metaViewModel.getPrqmLetzterAuftrag().getValue().equals(prqmViewModel.getResponseBauteil().getValue().getResponse().getKundenAuftrag())){
                        // 2
                        plebCardRVAdapter.setRvItemList(Arrays.asList(
                                new RVItem("ExemplarNr", prqmViewModel.getResponseBauteil().getValue().getResponse().getExemplarNr()),
                                new RVItem("Kunden-\nAuftrag", prqmViewModel.getResponseBauteil().getValue().getResponse().getKundenAuftrag()),
                                new RVItem("Kunden-\nPosition",prqmViewModel.getResponseBauteil().getValue().getResponse().getKundenPosition()),
                                new RVItem("Wagen-\nkennung", prqmViewModel.getResponseKommWagen().getValue().getResponse().getWagenKennung())
                        ));
                        binding.text.setText("OK + Wagenkennung");
                        binding.text.setTextColor(Color.parseColor("#ffd500"));
                        notification.startBuzzer(12,0,1000,1);

                        String antwort = prqmViewModel.getResponseBauteil().getValue().getResponse().getScannerAntwort().equals("")?
                                "BTQM="+new SimpleDateFormat("yyyyMMddHHmmss").format(new Timestamp(System.currentTimeMillis() + 3600000))+
                                        ";"+metaViewModel.getMitarbeiter().getValue():
                                prqmViewModel.getResponseBauteil().getValue().getResponse().getScannerAntwort()+"#"+
                                        prqmViewModel.getResponseBauteil().getValue().getResponse().getScannerAntwort()+"#"+"BTQM="+
                                        new SimpleDateFormat("yyyyMMddHHmmss").format(new Timestamp(System.currentTimeMillis() + 3600000))+
                                        ";"+metaViewModel.getMitarbeiter().getValue();
                        prqmViewModel.updateBauteil(prqmViewModel.getResponseBauteil().getValue().getResponse().getExemplarNr(),antwort);
                        System.out.println(prqmViewModel.getResponseBauteil().getValue().getResponse().getExemplarNr());
                        System.out.println(antwort);

                    } else {
                        // 1
                        plebCardRVAdapter.setRvItemList(Arrays.asList(
                                new RVItem("ExemplarNr", prqmViewModel.getResponseBauteil().getValue().getResponse().getExemplarNr()),
                                new RVItem("Kunden-\nAuftrag", prqmViewModel.getResponseBauteil().getValue().getResponse().getKundenAuftrag()),
                                new RVItem("Kunden-\nPosition",prqmViewModel.getResponseBauteil().getValue().getResponse().getKundenPosition()),
                                new RVItem("Wagen-\nkennung", prqmViewModel.getResponseKommWagen().getValue().getResponse().getWagenKennung())
                        ));
                        binding.text.setText("OK + Wagenkennung");
                        binding.text.setTextColor(Color.parseColor("#00ff00"));

                        String antwort = prqmViewModel.getResponseBauteil().getValue().getResponse().getScannerAntwort().equals("")?
                                "BTQM="+new SimpleDateFormat("yyyyMMddHHmmss").format(new Timestamp(System.currentTimeMillis() + 3600000))+
                                        ";"+metaViewModel.getMitarbeiter().getValue():
                                prqmViewModel.getResponseBauteil().getValue().getResponse().getScannerAntwort()+"#"+
                                        prqmViewModel.getResponseBauteil().getValue().getResponse().getScannerAntwort()+"#"+"BTQM="+
                                        new SimpleDateFormat("yyyyMMddHHmmss").format(new Timestamp(System.currentTimeMillis() + 3600000))+
                                        ";"+metaViewModel.getMitarbeiter().getValue();
                        prqmViewModel.updateBauteil(prqmViewModel.getResponseBauteil().getValue().getResponse().getExemplarNr(),antwort);
                        System.out.println(prqmViewModel.getResponseBauteil().getValue().getResponse().getExemplarNr());
                        System.out.println(antwort);
                    }
                } else {
                    // E
                    plebCardRVAdapter.setRvItemList(Arrays.asList(
                            new RVItem("ExemplarNr", prqmViewModel.getResponseBauteil().getValue().getResponse().getExemplarNr()),
                            new RVItem("Kunden-\nAuftrag", prqmViewModel.getResponseBauteil().getValue().getResponse().getKundenAuftrag()),
                            new RVItem("Kunden-\nPosition",prqmViewModel.getResponseBauteil().getValue().getResponse().getKundenPosition()),
                            new RVItem("Wagen-\nkennung", "keine")
                    ));
                    binding.text.setText("Wagen nicht vorhanden + keine Wagenkennung");
                    binding.text.setTextColor(Color.parseColor("#ff0000"));
                    notification.startBuzzer(12,0,1000,1);
                }
            }
        });


        binding.button.setOnClickListener(x -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_PRQMSelectFragment_to_PRQMScanFragment);
        });

        metaViewModel.getMitarbeiter().observe(getViewLifecycleOwner(),x -> {
            if(x==null){
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_PRQMSelectFragment_to_loginFragment);
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
            prqmViewModel.requestBauteil(id);
        }
    }

    @Override
    public void onDestroyView() {
        metaViewModel.setPrqmLetzterAuftrag(prqmViewModel.getResponseBauteil().getValue().getResponse().getKundenAuftrag());
        System.out.println("destroy");
        super.onDestroyView();
        scanManager.removeDataListener(this);
        scanManager.releaseScanManager();
        notification.releaseNotification();
    }

}
