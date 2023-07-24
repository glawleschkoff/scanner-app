package de.glawleschkoff.scannerapp.fragment.pleb;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import de.glawleschkoff.scannerapp.databinding.FragmentPlebselectBinding;
import de.glawleschkoff.scannerapp.model.PlattenlagerModel;
import de.glawleschkoff.scannerapp.model.RestteilModel;
import de.glawleschkoff.scannerapp.util.PLEBCardRVAdapter;
import de.glawleschkoff.scannerapp.util.RTEBCardRVAdapter;
import de.glawleschkoff.scannerapp.util.RVItem;
import de.glawleschkoff.scannerapp.viewmodel.MetaViewModel;
import de.glawleschkoff.scannerapp.viewmodel.PLEBViewModel;
import io.reactivex.annotations.NonNull;

public class PLEBSelectFragment extends Fragment implements ScanManager.DataListener {

    private FragmentPlebselectBinding binding;
    private PLEBViewModel plebViewModel;
    private MetaViewModel metaViewModel;
    private PLEBCardRVAdapter plebCardRVAdapter;
    private ScanManager scanManager;

    public static PLEBSelectFragment newInstance() {
        return new PLEBSelectFragment();
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
        binding = FragmentPlebselectBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        scanManager = ScanManager.createScanManager(this.getContext());
        scanManager.addDataListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        plebViewModel.getResponseBitmap().observe(getViewLifecycleOwner(),x -> {
            if(x.getResponse()!=null){
                Bitmap bitmap = x.getResponse();
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
                Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                binding.image.setImageBitmap(rotatedBitmap);
            }
        });


        plebViewModel.getPlattenlagerModel().observe(getViewLifecycleOwner(),x -> {
            if(plebViewModel.getPlattenlagerModel().getValue().getResponse()!=null){
                System.out.println("Request Lager");
                System.out.println(plebViewModel.getPlattenlagerModel().getValue().getResponse().getMatKurzzeichen());
                plebViewModel.requestLager(plebViewModel.getPlattenlagerModel().getValue().getResponse().getMatKurzzeichen());
            }
        });

        plebViewModel.getLagerModel().observe(getViewLifecycleOwner(),x -> {
            if(plebViewModel.getLagerModel().getValue().getResponse()!=null){
                plebViewModel.requestBitmap(plebViewModel.getLagerModel().
                        getValue().getResponse().getMPTextur());
                System.out.println("Material Bild");

            }
        });

        List<RVItem> list1 = new ArrayList<>();
        list1.add(new RVItem("Lädt...",""));

        plebCardRVAdapter = new PLEBCardRVAdapter(list1,this.getContext(), x -> {
            if(x!=null){
                switch(x){
                    case "Länge":
                        pickLänge(false); break;
                    case "Breite":
                        pickBreite(); break;
                    case "on":
                        plebViewModel.setPlattenlagerModel(new PlattenlagerModel(
                                plebViewModel.getPlattenlagerModel().getValue().getResponse().getPlattenID(),
                                plebViewModel.getPlattenlagerModel().getValue().getResponse().getLagerPlatz(),
                                plebViewModel.getPlattenlagerModel().getValue().getResponse().getLng(),
                                plebViewModel.getPlattenlagerModel().getValue().getResponse().getBrt(),
                                plebViewModel.getPlattenlagerModel().getValue().getResponse().getMatKurzzeichen(),
                                "J",
                                plebViewModel.getPlattenlagerModel().getValue().getResponse().getAuslagerId(),
                                plebViewModel.getPlattenlagerModel().getValue().getResponse().getAuslagerInfo(),
                                plebViewModel.getPlattenlagerModel().getValue().getResponse().getAuslagerDatum(),
                                plebViewModel.getPlattenlagerModel().getValue().getResponse().getMenge()
                        ));
                        break;
                    case "off":
                        plebViewModel.setPlattenlagerModel(new PlattenlagerModel(
                                plebViewModel.getPlattenlagerModel().getValue().getResponse().getPlattenID(),
                                plebViewModel.getPlattenlagerModel().getValue().getResponse().getLagerPlatz(),
                                plebViewModel.getPlattenlagerModel().getValue().getResponse().getLng(),
                                plebViewModel.getPlattenlagerModel().getValue().getResponse().getBrt(),
                                plebViewModel.getPlattenlagerModel().getValue().getResponse().getMatKurzzeichen(),
                                "",
                                plebViewModel.getPlattenlagerModel().getValue().getResponse().getAuslagerId(),
                                plebViewModel.getPlattenlagerModel().getValue().getResponse().getAuslagerInfo(),
                                plebViewModel.getPlattenlagerModel().getValue().getResponse().getAuslagerDatum(),
                                plebViewModel.getPlattenlagerModel().getValue().getResponse().getMenge()
                        ));
                        break;
                    default:
                }
            }
        });
        binding.rvoutside.setAdapter(plebCardRVAdapter);
        binding.rvoutside.setLayoutManager(new LinearLayoutManager(this.getContext()));

        plebViewModel.getPlattenlagerModel().observe(getViewLifecycleOwner(), x -> {
            if(x!=null){
                plebCardRVAdapter.setRvItemList(Arrays.asList(
                        new RVItem("PlattenID",String.format("%.0f", x.getResponse().getPlattenID())),
                        new RVItem("Material\nKurzzeichen1",String.valueOf(x.getResponse().getMatKurzzeichen())),
                        new RVItem("Länge",String.valueOf(x.getResponse().getLng())),
                        new RVItem("Breite",String.valueOf(x.getResponse().getBrt())),
                        new RVItem("Lagerplatz",String.valueOf(x.getResponse().getLagerPlatz()))
                        //new RVItem("switch",String.valueOf(x.getResponse().getMz3()))
                ));
                binding.switch1.setChecked(plebViewModel.getPlattenlagerModel().getValue().getResponse().getMz3().equals("J"));
            }
        });

        binding.switch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                plebViewModel.setPlattenlagerModel(new PlattenlagerModel(
                        plebViewModel.getPlattenlagerModel().getValue().getResponse().getPlattenID(),
                        plebViewModel.getPlattenlagerModel().getValue().getResponse().getLagerPlatz(),
                        plebViewModel.getPlattenlagerModel().getValue().getResponse().getLng(),
                        plebViewModel.getPlattenlagerModel().getValue().getResponse().getBrt(),
                        plebViewModel.getPlattenlagerModel().getValue().getResponse().getMatKurzzeichen(),
                        "J",
                        plebViewModel.getPlattenlagerModel().getValue().getResponse().getAuslagerId(),
                        plebViewModel.getPlattenlagerModel().getValue().getResponse().getAuslagerInfo(),
                        plebViewModel.getPlattenlagerModel().getValue().getResponse().getAuslagerDatum(),
                        plebViewModel.getPlattenlagerModel().getValue().getResponse().getMenge()
                ));
            } else {
                plebViewModel.setPlattenlagerModel(new PlattenlagerModel(
                        plebViewModel.getPlattenlagerModel().getValue().getResponse().getPlattenID(),
                        plebViewModel.getPlattenlagerModel().getValue().getResponse().getLagerPlatz(),
                        plebViewModel.getPlattenlagerModel().getValue().getResponse().getLng(),
                        plebViewModel.getPlattenlagerModel().getValue().getResponse().getBrt(),
                        plebViewModel.getPlattenlagerModel().getValue().getResponse().getMatKurzzeichen(),
                        "",
                        plebViewModel.getPlattenlagerModel().getValue().getResponse().getAuslagerId(),
                        plebViewModel.getPlattenlagerModel().getValue().getResponse().getAuslagerInfo(),
                        plebViewModel.getPlattenlagerModel().getValue().getResponse().getAuslagerDatum(),
                        plebViewModel.getPlattenlagerModel().getValue().getResponse().getMenge()
                ));
            }
        });

        binding.button.setOnClickListener(x -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_PLEBSelectFragment_to_PLEBScanFragment);
        });

        binding.button2.setOnClickListener(x -> {
            plebViewModel.updatePlattenlager();
            Navigation.findNavController(requireView()).navigate(R.id.action_PLEBSelectFragment_to_PLEBScanFragment);
        });

        metaViewModel.getMitarbeiter().observe(getViewLifecycleOwner(),x -> {
            if(x==null){
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_PLEBSelectFragment_to_loginFragment);
            }
        });

        plebViewModel.getTempLagerplatz().observe(getViewLifecycleOwner(), x -> {
            if(getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED){
                if(!validLagerplatz(plebViewModel.getTempLagerplatz().getValue())){
                    new AlertDialog.Builder(getContext())
                            //.setTitle("Delete entry")
                            .setMessage("Ungültiger QR-Code")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } else {
                    plebViewModel.setPlattenlagerModel(new PlattenlagerModel(
                            plebViewModel.getPlattenlagerModel().getValue().getResponse().getPlattenID(),
                            plebViewModel.getTempLagerplatz().getValue(),
                            plebViewModel.getPlattenlagerModel().getValue().getResponse().getLng(),
                            plebViewModel.getPlattenlagerModel().getValue().getResponse().getBrt(),
                            plebViewModel.getPlattenlagerModel().getValue().getResponse().getMatKurzzeichen(),
                            plebViewModel.getPlattenlagerModel().getValue().getResponse().getMz3(),
                            plebViewModel.getPlattenlagerModel().getValue().getResponse().getAuslagerId(),
                            plebViewModel.getPlattenlagerModel().getValue().getResponse().getAuslagerInfo(),
                            plebViewModel.getPlattenlagerModel().getValue().getResponse().getAuslagerDatum(),
                            plebViewModel.getPlattenlagerModel().getValue().getResponse().getMenge()
                    ));
                }
            }

        });
    }

    private boolean validLagerplatz(String s){
        return s.matches("[a-z][0-9]");
    }

    private void pickLänge(boolean b){
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
        np.setValue((plebViewModel.getPlattenlagerModel().getValue().getResponse().getLng().intValue()));
        np.setWrapSelectorWheel(false);
        d.setMessage("Länge wählen (mm)");
        d.setView(dialogView);
        d.setPositiveButton("Weiter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                plebViewModel.setPlattenlagerModel(new PlattenlagerModel(
                        plebViewModel.getPlattenlagerModel().getValue().getResponse().getPlattenID(),
                        plebViewModel.getPlattenlagerModel().getValue().getResponse().getLagerPlatz(),
                        (double) (np.getValue()*1),
                        plebViewModel.getPlattenlagerModel().getValue().getResponse().getBrt(),
                        plebViewModel.getPlattenlagerModel().getValue().getResponse().getMatKurzzeichen(),
                        plebViewModel.getPlattenlagerModel().getValue().getResponse().getMz3(),
                        plebViewModel.getPlattenlagerModel().getValue().getResponse().getAuslagerId(),
                        plebViewModel.getPlattenlagerModel().getValue().getResponse().getAuslagerInfo(),
                        plebViewModel.getPlattenlagerModel().getValue().getResponse().getAuslagerDatum(),
                        plebViewModel.getPlattenlagerModel().getValue().getResponse().getMenge()
                ));
                if(b){
                    pickBreite();
                }
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
        np.setValue((plebViewModel.getPlattenlagerModel().getValue().getResponse().getBrt().intValue()));
        np.setWrapSelectorWheel(false);
        d.setMessage("Breite wählen (mm)");
        d.setView(dialogView);
        d.setPositiveButton("Weiter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                plebViewModel.setPlattenlagerModel(new PlattenlagerModel(
                        plebViewModel.getPlattenlagerModel().getValue().getResponse().getPlattenID(),
                        plebViewModel.getPlattenlagerModel().getValue().getResponse().getLagerPlatz(),
                        plebViewModel.getPlattenlagerModel().getValue().getResponse().getLng(),
                        (double) (np.getValue()*1),
                        plebViewModel.getPlattenlagerModel().getValue().getResponse().getMatKurzzeichen(),
                        plebViewModel.getPlattenlagerModel().getValue().getResponse().getMz3(),
                        plebViewModel.getPlattenlagerModel().getValue().getResponse().getAuslagerId(),
                        plebViewModel.getPlattenlagerModel().getValue().getResponse().getAuslagerInfo(),
                        plebViewModel.getPlattenlagerModel().getValue().getResponse().getAuslagerDatum(),
                        plebViewModel.getPlattenlagerModel().getValue().getResponse().getMenge()
                ));
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

    @Override
    public void onDataReceived(DecodeResult decodeResult) {
        DecodeResult.Result result = decodeResult.getResult();
        String codeType = decodeResult.getCodeType();
        String data = decodeResult.getData();
        //Toast.makeText(this.getContext(), data, Toast.LENGTH_SHORT).show();
        System.out.println(data);
        if(decodeResult.getResult() == DecodeResult.Result.SUCCESS){
            String id = data.startsWith(" ")?data.substring(1):data;
            plebViewModel.setTempLagerplatz(id);
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
