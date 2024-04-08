package de.glawleschkoff.scannerapp.fragment.restteilbearbeiten;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.keyence.autoid.sdk.scan.DecodeResult;
import com.keyence.autoid.sdk.scan.ScanManager;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.glawleschkoff.scannerapp.R;
import de.glawleschkoff.scannerapp.databinding.FragmentRtbaselectBinding;
import de.glawleschkoff.scannerapp.model.USERPlattenlagerModel;
import de.glawleschkoff.scannerapp.util.CardRVAdapter;
import de.glawleschkoff.scannerapp.util.RVItem;
import de.glawleschkoff.scannerapp.viewmodel.SuperViewModel;
import de.glawleschkoff.scannerapp.viewmodel.RTBAViewModel;
import io.reactivex.annotations.NonNull;

public class RTBASelectFragment extends Fragment implements ScanManager.DataListener {

    private FragmentRtbaselectBinding binding;
    private RTBAViewModel rtbaViewModel;
    private SuperViewModel superViewModel;
    private CardRVAdapter cardRVAdapter;
    private ScanManager scanManager;
    private boolean listenerFirstTime = true;

    public static RTBASelectFragment newInstance() {
        return new RTBASelectFragment();
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
        binding = FragmentRtbaselectBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        scanManager = ScanManager.createScanManager(this.getContext());
        scanManager.addDataListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rtbaViewModel.getResponseBitmap().observe(getViewLifecycleOwner(), x -> {
            if(x.getResponse()!=null){
                Bitmap bitmap = x.getResponse();
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
                Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                binding.image.setImageBitmap(rotatedBitmap);
            }
        });


        rtbaViewModel.getUSERPlattenlager().observe(getViewLifecycleOwner(), x -> {
            if(rtbaViewModel.getUSERPlattenlager().getValue().getResponse()!=null){
                rtbaViewModel.requestLager(rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getMatKurzzeichen());
            }
        });

        rtbaViewModel.getLager().observe(getViewLifecycleOwner(), x -> {
            if(rtbaViewModel.getLager().getValue().getResponse()!=null){
                rtbaViewModel.requestBitmap(rtbaViewModel.getLager().
                        getValue().getResponse().getMPTextur());

            }
        });

        List<RVItem> list1 = new ArrayList<>();
        list1.add(new RVItem("Lädt...",""));

        cardRVAdapter = new CardRVAdapter(list1,this.getContext(), x -> {
            if(x!=null){
                switch(x){
                    case "Länge":
                        pickLänge(false); break;
                    case "Breite":
                        pickBreite(); break;
                    case "on":
                        rtbaViewModel.setUSERPlattenlager(new USERPlattenlagerModel(
                                rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getPlattenID(),
                                rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getLagerPlatz(),
                                rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getLng(),
                                rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getBrt(),
                                rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getMatKurzzeichen(),
                                "J",
                                rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getAuslagerId(),
                                rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getAuslagerInfo(),
                                rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getAuslagerDatum(),
                                rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getMenge(),
                                rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getEinlagerDatum()
                        ));
                        break;
                    case "off":
                        rtbaViewModel.setUSERPlattenlager(new USERPlattenlagerModel(
                                rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getPlattenID(),
                                rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getLagerPlatz(),
                                rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getLng(),
                                rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getBrt(),
                                rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getMatKurzzeichen(),
                                "",
                                rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getAuslagerId(),
                                rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getAuslagerInfo(),
                                rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getAuslagerDatum(),
                                rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getMenge(),
                                rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getEinlagerDatum()
                        ));
                        break;
                    default:
                }
            }
        });
        binding.rvoutside.setAdapter(cardRVAdapter);
        binding.rvoutside.setLayoutManager(new LinearLayoutManager(this.getContext()));

        rtbaViewModel.getUSERPlattenlager().observe(getViewLifecycleOwner(), x -> {
            if(x!=null){
                cardRVAdapter.setRvItemList(Arrays.asList(
                        new RVItem("Material\nKurzzeichen1",String.valueOf(x.getResponse().getMatKurzzeichen())),
                        new RVItem("Länge",String.valueOf(x.getResponse().getLng())),
                        new RVItem("Breite",String.valueOf(x.getResponse().getBrt())),
                        new RVItem("Lagerplatz",String.valueOf(x.getResponse().getLagerPlatz())),
                        new RVItem("Einlagerdatum",String.valueOf(x.getResponse().getEinlagerDatum())),
                        new RVItem("PlattenID",String.format("%.0f", x.getResponse().getPlattenID()))
                ));
                binding.switch1.setChecked(rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getMz3().equals("J"));
                if(x.getResponse().getAuslagerInfo().contains("ist") && listenerFirstTime){
                    scanManager.lockScanner();
                    new AlertDialog.Builder(getContext())
                            .setMessage(x.getResponse().getAuslagerInfo())
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    scanManager.unlockScanner();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    listenerFirstTime = false;
                }
            }
        });

        binding.switch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                rtbaViewModel.setUSERPlattenlager(new USERPlattenlagerModel(
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getPlattenID(),
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getLagerPlatz(),
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getLng(),
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getBrt(),
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getMatKurzzeichen(),
                        "J",
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getAuslagerId(),
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getAuslagerInfo(),
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getAuslagerDatum(),
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getMenge(),
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getEinlagerDatum()
                ));
            } else {
                rtbaViewModel.setUSERPlattenlager(new USERPlattenlagerModel(
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getPlattenID(),
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getLagerPlatz(),
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getLng(),
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getBrt(),
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getMatKurzzeichen(),
                        "",
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getAuslagerId(),
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getAuslagerInfo(),
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getAuslagerDatum(),
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getMenge(),
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getEinlagerDatum()
                ));
            }
        });

        binding.button.setOnClickListener(x -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_rtbaSelectFragment_to_rtbaScanFragment);
        });

        binding.button2.setOnClickListener(x -> {
            rtbaViewModel.updateUSERPlattenlager();
            Navigation.findNavController(requireView()).navigate(R.id.action_rtbaSelectFragment_to_rtbaScanFragment);
        });

        superViewModel.getMitarbeiter().observe(getViewLifecycleOwner(), x -> {
            if(x==null){
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_rtbaSelectFragment_to_loginFragment);
            }
        });

        rtbaViewModel.getTempLagerplatz().observe(getViewLifecycleOwner(), x -> {
            if(getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED){
                if(!validLagerplatz(rtbaViewModel.getTempLagerplatz().getValue())){
                    scanManager.lockScanner();
                    new AlertDialog.Builder(getContext())
                            .setMessage("Ungültiger QR-Code")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    scanManager.unlockScanner();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } else {
                    rtbaViewModel.setUSERPlattenlager(new USERPlattenlagerModel(
                            rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getPlattenID(),
                            rtbaViewModel.getTempLagerplatz().getValue(),
                            rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getLng(),
                            rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getBrt(),
                            rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getMatKurzzeichen(),
                            rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getMz3(),
                            rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getAuslagerId(),
                            rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getAuslagerInfo(),
                            rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getAuslagerDatum(),
                            rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getMenge(),
                            rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getEinlagerDatum()
                    ));
                }
            }
        });
    }

    private boolean validLagerplatz(String s){
        return s.length()<10;
    }

    private void pickLänge(boolean b){
        scanManager.lockScanner();
        final AlertDialog.Builder d = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog, null);
        final NumberPicker np = (NumberPicker) dialogView.findViewById(R.id.numberPicker1);
        np.setMaxValue(100000);
        np.setMinValue(0);
        np.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.valueOf(value*1);
            }
        });
        np.setValue((rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getLng().intValue()));
        np.setWrapSelectorWheel(false);
        d.setMessage("Länge wählen (mm)");
        d.setView(dialogView);
        d.setPositiveButton("Weiter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                rtbaViewModel.setUSERPlattenlager(new USERPlattenlagerModel(
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getPlattenID(),
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getLagerPlatz(),
                        (double) (np.getValue()*1),
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getBrt(),
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getMatKurzzeichen(),
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getMz3(),
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getAuslagerId(),
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getAuslagerInfo(),
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getAuslagerDatum(),
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getMenge(),
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getEinlagerDatum()
                ));
                if(b){
                    pickBreite();
                }
                scanManager.unlockScanner();
            }
        });
        d.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = d.create();
        alertDialog.show();
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void pickBreite(){
        scanManager.lockScanner();
        final AlertDialog.Builder d = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog, null);
        final NumberPicker np = (NumberPicker) dialogView.findViewById(R.id.numberPicker1);
        np.setMaxValue(100000);
        np.setMinValue(0);
        np.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.valueOf(value*1);
            }
        });
        np.setValue((rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getBrt().intValue()));
        np.setWrapSelectorWheel(false);
        d.setMessage("Breite wählen (mm)");
        d.setView(dialogView);
        d.setPositiveButton("Weiter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                rtbaViewModel.setUSERPlattenlager(new USERPlattenlagerModel(
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getPlattenID(),
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getLagerPlatz(),
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getLng(),
                        (double) (np.getValue()*1),
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getMatKurzzeichen(),
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getMz3(),
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getAuslagerId(),
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getAuslagerInfo(),
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getAuslagerDatum(),
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getMenge(),
                        rtbaViewModel.getUSERPlattenlager().getValue().getResponse().getEinlagerDatum()
                ));
                scanManager.unlockScanner();
            }
        });
        d.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scanManager.unlockScanner();
            }
        });

        AlertDialog alertDialog = d.create();
        alertDialog.show();
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    @Override
    public void onDataReceived(DecodeResult decodeResult) {
        DecodeResult.Result result = decodeResult.getResult();
        String data = decodeResult.getData();
        if(result == DecodeResult.Result.SUCCESS){
            String id = data.startsWith(" ")?data.substring(1):data;
            rtbaViewModel.setTempLagerplatz(id);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        scanManager.removeDataListener(this);
        scanManager.releaseScanManager();
    }
}
