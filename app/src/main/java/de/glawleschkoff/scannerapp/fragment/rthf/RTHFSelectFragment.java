package de.glawleschkoff.scannerapp.fragment.rthf;

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
import de.glawleschkoff.scannerapp.databinding.FragmentPlebselectBinding;
import de.glawleschkoff.scannerapp.databinding.FragmentRthfselectBinding;
import de.glawleschkoff.scannerapp.model.PlattenlagerModel;
import de.glawleschkoff.scannerapp.model.RestteilModel;
import de.glawleschkoff.scannerapp.util.PLEBCardRVAdapter;
import de.glawleschkoff.scannerapp.util.RTEBCardRVAdapter;
import de.glawleschkoff.scannerapp.util.RVItem;
import de.glawleschkoff.scannerapp.viewmodel.MetaViewModel;
import de.glawleschkoff.scannerapp.viewmodel.PLEBViewModel;
import de.glawleschkoff.scannerapp.viewmodel.RTHFViewModel;
import io.reactivex.annotations.NonNull;

public class RTHFSelectFragment extends Fragment implements ScanManager.DataListener {

    private FragmentRthfselectBinding binding;
    private RTHFViewModel rthfViewModel;
    private MetaViewModel metaViewModel;
    private PLEBCardRVAdapter plebCardRVAdapter;
    private ScanManager scanManager;

    public static RTHFSelectFragment newInstance() {
        return new RTHFSelectFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rthfViewModel = new ViewModelProvider(requireActivity()).get(RTHFViewModel.class);
        metaViewModel = new ViewModelProvider(requireActivity()).get(MetaViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRthfselectBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        scanManager = ScanManager.createScanManager(this.getContext());
        scanManager.addDataListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rthfViewModel.requestMaxPlattenID();

        rthfViewModel.getResponseBitmap().observe(getViewLifecycleOwner(),x -> {
            if(x.getResponse()!=null){
                Bitmap bitmap = x.getResponse();
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
                Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                binding.image.setImageBitmap(rotatedBitmap);


            }
        });

        rthfViewModel.getLagerModel().observe(getViewLifecycleOwner(),x -> {
            if(rthfViewModel.getLagerModel().getValue().getResponse()!=null){
                rthfViewModel.requestBitmap(rthfViewModel.getLagerModel().
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
                    default:
                }
            }
        });
        binding.rvoutside.setAdapter(plebCardRVAdapter);
        binding.rvoutside.setLayoutManager(new LinearLayoutManager(this.getContext()));

        rthfViewModel.getMaxPlattenID().observe(getViewLifecycleOwner(), x -> {
            if(x.getResponse()!=null){
                rthfViewModel.setPlattenlagerModel(new PlattenlagerModel(x.getResponse()+1,"",1400.,600.,"",""));
            }
        });

        rthfViewModel.getPlattenlagerModel().observe(getViewLifecycleOwner(), x -> {
            if(x!=null){
                plebCardRVAdapter.setRvItemList(Arrays.asList(
                        new RVItem("PlattenID",String.format("%.0f", x.getPlattenID())),
                        new RVItem("Material\nKurzzeichen",String.valueOf(x.getMatKurzzeichen())),
                        new RVItem("Länge",String.valueOf(x.getLng())),
                        new RVItem("Breite",String.valueOf(x.getBrt())),
                        new RVItem("Lagerplatz",String.valueOf(x.getLagerPlatz()))
                ));
            }
        });

        binding.switch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                rthfViewModel.setPlattenlagerModel(new PlattenlagerModel(
                        rthfViewModel.getPlattenlagerModel().getValue().getPlattenID(),
                        rthfViewModel.getPlattenlagerModel().getValue().getLagerPlatz(),
                        rthfViewModel.getPlattenlagerModel().getValue().getLng(),
                        rthfViewModel.getPlattenlagerModel().getValue().getBrt(),
                        rthfViewModel.getPlattenlagerModel().getValue().getMatKurzzeichen(),
                        "J"
                ));
            } else {
                //only for testing
                rthfViewModel.setPlattenlagerModel(new PlattenlagerModel(
                        rthfViewModel.getPlattenlagerModel().getValue().getPlattenID(),
                        rthfViewModel.getPlattenlagerModel().getValue().getLagerPlatz(),
                        rthfViewModel.getPlattenlagerModel().getValue().getLng(),
                        rthfViewModel.getPlattenlagerModel().getValue().getBrt(),
                        rthfViewModel.getPlattenlagerModel().getValue().getMatKurzzeichen(),
                        ""
                ));
            }
        });

        binding.button.setOnClickListener(x -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_RTHFSelectFragment_to_menuFragment);
        });

        binding.button2.setOnClickListener(x -> {
            rthfViewModel.insertPlattenlager(Integer.parseInt(metaViewModel.getScannerNr().getValue())
                    ,rthfViewModel.getPlattenlagerModel().getValue().getMatKurzzeichen()
                    ,rthfViewModel.getPlattenlagerModel().getValue().getPlattenID()
                    ,rthfViewModel.getPlattenlagerModel().getValue().getLagerPlatz()
                    ,rthfViewModel.getPlattenlagerModel().getValue().getMz3()
                    ,rthfViewModel.getPlattenlagerModel().getValue().getLng()
                    ,rthfViewModel.getPlattenlagerModel().getValue().getBrt());
            Navigation.findNavController(requireView()).navigate(R.id.action_RTHFSelectFragment_to_menuFragment);
        });

        metaViewModel.getMitarbeiter().observe(getViewLifecycleOwner(),x -> {
            if(x==null){
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_RTHFSelectFragment_to_loginFragment);
            }
        });

        rthfViewModel.getTempLagerplatz().observe(getViewLifecycleOwner(), x -> {
            if(getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED){
                if(!validLagerplatz(rthfViewModel.getTempLagerplatz().getValue())){
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
                    rthfViewModel.setPlattenlagerModel(new PlattenlagerModel(
                            rthfViewModel.getPlattenlagerModel().getValue().getPlattenID(),
                            rthfViewModel.getTempLagerplatz().getValue(),
                            rthfViewModel.getPlattenlagerModel().getValue().getLng(),
                            rthfViewModel.getPlattenlagerModel().getValue().getBrt(),
                            rthfViewModel.getPlattenlagerModel().getValue().getMatKurzzeichen(),
                            rthfViewModel.getPlattenlagerModel().getValue().getMz3()
                    ));
                }
            }

        });
    }

    private boolean validLagerplatz(String s){
        return s.matches("[0-9][0-9]|[0-9]");
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
                return String.valueOf(value*5);
            }
        });
        np.setValue((rthfViewModel.getPlattenlagerModel().getValue().getLng().intValue() -
                rthfViewModel.getPlattenlagerModel().getValue().getLng().intValue() % 5)/5);
        np.setWrapSelectorWheel(false);
        d.setMessage("Länge wählen (mm)");
        d.setView(dialogView);
        d.setPositiveButton("Weiter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                rthfViewModel.setPlattenlagerModel(new PlattenlagerModel(
                        rthfViewModel.getPlattenlagerModel().getValue().getPlattenID(),
                        rthfViewModel.getPlattenlagerModel().getValue().getLagerPlatz(),
                        (double) (np.getValue()*5),
                        rthfViewModel.getPlattenlagerModel().getValue().getBrt(),
                        rthfViewModel.getPlattenlagerModel().getValue().getMatKurzzeichen(),
                        rthfViewModel.getPlattenlagerModel().getValue().getMz3()
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
                return String.valueOf(value*5);
            }
        });
        np.setValue((rthfViewModel.getPlattenlagerModel().getValue().getBrt().intValue() -
                rthfViewModel.getPlattenlagerModel().getValue().getBrt().intValue() % 5)/5);
        np.setWrapSelectorWheel(false);
        d.setMessage("Breite wählen (mm)");
        d.setView(dialogView);
        d.setPositiveButton("Weiter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                rthfViewModel.setPlattenlagerModel(new PlattenlagerModel(
                        rthfViewModel.getPlattenlagerModel().getValue().getPlattenID(),
                        rthfViewModel.getPlattenlagerModel().getValue().getLagerPlatz(),
                        rthfViewModel.getPlattenlagerModel().getValue().getLng(),
                        (double) (np.getValue()*5),
                        rthfViewModel.getPlattenlagerModel().getValue().getMatKurzzeichen(),
                        rthfViewModel.getPlattenlagerModel().getValue().getMz3()
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
            String id = data.substring(0);
            rthfViewModel.setTempLagerplatz(id);
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
