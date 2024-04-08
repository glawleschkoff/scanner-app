package de.glawleschkoff.scannerapp.fragment.restteilhinzufügen;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.keyence.autoid.sdk.scan.DecodeResult;
import com.keyence.autoid.sdk.scan.ScanManager;

import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import de.glawleschkoff.scannerapp.R;
import de.glawleschkoff.scannerapp.databinding.FragmentRthfselectBinding;
import de.glawleschkoff.scannerapp.model.USERPlattenlagerModel;
import de.glawleschkoff.scannerapp.util.ClickInterface;
import de.glawleschkoff.scannerapp.util.MaterialienAdapter;
import de.glawleschkoff.scannerapp.util.CardRVAdapter;
import de.glawleschkoff.scannerapp.util.RVItem;
import de.glawleschkoff.scannerapp.viewmodel.SuperViewModel;
import de.glawleschkoff.scannerapp.viewmodel.RTHFViewModel;
import io.reactivex.annotations.NonNull;

public class RTHFSelectFragment extends Fragment implements ScanManager.DataListener {

    private FragmentRthfselectBinding binding;
    private RTHFViewModel rthfViewModel;
    private SuperViewModel superViewModel;
    private CardRVAdapter cardRVAdapter;
    private ScanManager scanManager;
    private boolean allowImage;

    public static RTHFSelectFragment newInstance() {
        return new RTHFSelectFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rthfViewModel = new ViewModelProvider(requireActivity()).get(RTHFViewModel.class);
        superViewModel = new ViewModelProvider(requireActivity()).get(SuperViewModel.class);
        allowImage = false;
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

        getActivity().setTitle("Restteil Hinzufügen");

        rthfViewModel.requestMaxPlattenID();

        rthfViewModel.requestMaterialien();

        rthfViewModel.getResponseBitmap().observe(getViewLifecycleOwner(),x -> {
            if(x.getResponse()!=null && allowImage){
                Bitmap bitmap = x.getResponse();
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
                Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                binding.image.setImageBitmap(rotatedBitmap);
            }
        });


        rthfViewModel.getLager().observe(getViewLifecycleOwner(),x -> {
            if(rthfViewModel.getLager().getValue().getResponse()!=null){
                rthfViewModel.requestBitmap(rthfViewModel.getLager().
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
                    case "Material\nKurzzeichen":
                        pickMaterialKurzzeichen();
                        break;
                    case "on":
                        rthfViewModel.setUSERPlattenlager(new USERPlattenlagerModel(
                                rthfViewModel.getUSERPlattenlager().getValue().getPlattenID(),
                                rthfViewModel.getUSERPlattenlager().getValue().getLagerPlatz(),
                                rthfViewModel.getUSERPlattenlager().getValue().getLng(),
                                rthfViewModel.getUSERPlattenlager().getValue().getBrt(),
                                rthfViewModel.getUSERPlattenlager().getValue().getMatKurzzeichen(),
                                "J"
                        ));
                        break;
                    case "off":
                        rthfViewModel.setUSERPlattenlager(new USERPlattenlagerModel(
                                rthfViewModel.getUSERPlattenlager().getValue().getPlattenID(),
                                rthfViewModel.getUSERPlattenlager().getValue().getLagerPlatz(),
                                rthfViewModel.getUSERPlattenlager().getValue().getLng(),
                                rthfViewModel.getUSERPlattenlager().getValue().getBrt(),
                                rthfViewModel.getUSERPlattenlager().getValue().getMatKurzzeichen(),
                                ""
                        ));
                        break;
                    default:
                }
            }
        });
        binding.rvoutside.setAdapter(cardRVAdapter);
        binding.rvoutside.setLayoutManager(new LinearLayoutManager(this.getContext()));

        rthfViewModel.getMaxPlattenID().observe(getViewLifecycleOwner(), x -> {
            if(x.getResponse()!=null){
                rthfViewModel.setUSERPlattenlager(new USERPlattenlagerModel(x.getResponse()+1,"",1400.,600.,"","J"));
            }
        });

        rthfViewModel.getUSERPlattenlager().observe(getViewLifecycleOwner(), x -> {
            if(x!=null){
                Date currentDate = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                cardRVAdapter.setRvItemList(Arrays.asList(
                        new RVItem("Material\nKurzzeichen",String.valueOf(x.getMatKurzzeichen())!=""?String.valueOf(x.getMatKurzzeichen()):"Wähle Material"),
                        new RVItem("Länge",String.valueOf(x.getLng())),
                        new RVItem("Breite",String.valueOf(x.getBrt())),
                        new RVItem("Lagerplatz",String.valueOf(x.getLagerPlatz())),
                        new RVItem("Einlagerdatum",dateFormat.format(currentDate)),
                        new RVItem("PlattenID",String.format("%.0f", x.getPlattenID()))
                ));
                binding.switch1.setChecked(rthfViewModel.getUSERPlattenlager().getValue().getMz3().equals("J"));
            }
        });

        binding.switch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                rthfViewModel.setUSERPlattenlager(new USERPlattenlagerModel(
                        rthfViewModel.getUSERPlattenlager().getValue().getPlattenID(),
                        rthfViewModel.getUSERPlattenlager().getValue().getLagerPlatz(),
                        rthfViewModel.getUSERPlattenlager().getValue().getLng(),
                        rthfViewModel.getUSERPlattenlager().getValue().getBrt(),
                        rthfViewModel.getUSERPlattenlager().getValue().getMatKurzzeichen(),
                        "J"
                ));
            } else {
                //only for testing
                rthfViewModel.setUSERPlattenlager(new USERPlattenlagerModel(
                        rthfViewModel.getUSERPlattenlager().getValue().getPlattenID(),
                        rthfViewModel.getUSERPlattenlager().getValue().getLagerPlatz(),
                        rthfViewModel.getUSERPlattenlager().getValue().getLng(),
                        rthfViewModel.getUSERPlattenlager().getValue().getBrt(),
                        rthfViewModel.getUSERPlattenlager().getValue().getMatKurzzeichen(),
                        ""
                ));
            }
        });

        binding.button.setOnClickListener(x -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_rthfSelectFragment_to_menuFragment);
        });


        binding.button2.setOnClickListener(x -> {

            if(rthfViewModel.getUSERPlattenlager().getValue().getMatKurzzeichen().equals("")){
                scanManager.lockScanner();
                new AlertDialog.Builder(getContext())
                        .setMessage("Material noch nicht gewählt")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                scanManager.unlockScanner();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                rthfViewModel.insertUSERPlattenlager(Integer.parseInt(superViewModel.getScannerNr().getValue())
                        ,rthfViewModel.getUSERPlattenlager().getValue().getMatKurzzeichen()
                        ,rthfViewModel.getUSERPlattenlager().getValue().getPlattenID()
                        ,rthfViewModel.getUSERPlattenlager().getValue().getLagerPlatz()
                        ,rthfViewModel.getUSERPlattenlager().getValue().getMz3()
                        ,rthfViewModel.getUSERPlattenlager().getValue().getLng()
                        ,rthfViewModel.getUSERPlattenlager().getValue().getBrt());
                Navigation.findNavController(requireView()).navigate(R.id.action_rthfSelectFragment_to_menuFragment);
            }


        });

        superViewModel.getMitarbeiter().observe(getViewLifecycleOwner(), x -> {
            if(x==null){
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_rthfSelectFragment_to_loginFragment);
            }
        });

        rthfViewModel.getTempLagerplatz().observe(getViewLifecycleOwner(), x -> {
            if(getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED){
                if(!validLagerplatz(rthfViewModel.getTempLagerplatz().getValue())){
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
                    rthfViewModel.setUSERPlattenlager(new USERPlattenlagerModel(
                            rthfViewModel.getUSERPlattenlager().getValue().getPlattenID(),
                            rthfViewModel.getTempLagerplatz().getValue(),
                            rthfViewModel.getUSERPlattenlager().getValue().getLng(),
                            rthfViewModel.getUSERPlattenlager().getValue().getBrt(),
                            rthfViewModel.getUSERPlattenlager().getValue().getMatKurzzeichen(),
                            rthfViewModel.getUSERPlattenlager().getValue().getMz3()
                    ));
                }
            }

        });
    }

    private boolean validLagerplatz(String s){
        return s.length()<10;
    }

    private void pickMaterialKurzzeichen() {
        scanManager.lockScanner();
        AlertDialog optionDialog = new AlertDialog.Builder(getContext()).create();
        optionDialog.setTitle("Wähle Material");
        optionDialog.setButton(Dialog.BUTTON_NEGATIVE, "Abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scanManager.unlockScanner();
            }
        });

        final View customLayout = getLayoutInflater().inflate(R.layout.alertdialog, null);
        optionDialog.setView(customLayout);

        EditText editText = (EditText) customLayout.findViewById(R.id.editText);
        editText.setHint("Suche");

        RecyclerView rv = (RecyclerView) customLayout.findViewById(R.id.list);

        List<String> list = new ArrayList<>();
        list.add("leer");
        MaterialienAdapter materialienAdapter = new MaterialienAdapter(getContext(), list, new ClickInterface() {
            @Override
            public void onClick(String name) {
                rthfViewModel.setUSERPlattenlager(new USERPlattenlagerModel(
                        rthfViewModel.getUSERPlattenlager().getValue().getPlattenID(),
                        rthfViewModel.getUSERPlattenlager().getValue().getLagerPlatz(),
                        rthfViewModel.getUSERPlattenlager().getValue().getLng(),
                        rthfViewModel.getUSERPlattenlager().getValue().getBrt(),
                        name,
                        rthfViewModel.getUSERPlattenlager().getValue().getMz3()
                ));
                allowImage = true;
                rthfViewModel.requestLager(name);
                optionDialog.dismiss();
            }
        });

        rv.setAdapter(materialienAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this.getContext()));

        materialienAdapter.setRecyclerViewItems(rthfViewModel.getMaterialien().getValue().getResponse().stream().sorted().filter(x -> !x.startsWith("DP_")).collect(Collectors.toList()));

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                materialienAdapter.setRecyclerViewItems(rthfViewModel.getMaterialien().getValue().getResponse()
                        .stream().filter(x -> x.toLowerCase().contains(s.toString().toLowerCase())).sorted().filter(x -> !x.startsWith("DP_")).collect(Collectors.toList()));
            }
        });

        optionDialog.show();
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
                return String.valueOf(value);
            }
        });
        np.setValue((rthfViewModel.getUSERPlattenlager().getValue().getLng().intValue()));
        np.setWrapSelectorWheel(false);
        d.setMessage("Länge wählen (mm)");
        d.setView(dialogView);
        d.setPositiveButton("Weiter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                rthfViewModel.setUSERPlattenlager(new USERPlattenlagerModel(
                        rthfViewModel.getUSERPlattenlager().getValue().getPlattenID(),
                        rthfViewModel.getUSERPlattenlager().getValue().getLagerPlatz(),
                        (double) (np.getValue()*1),
                        rthfViewModel.getUSERPlattenlager().getValue().getBrt(),
                        rthfViewModel.getUSERPlattenlager().getValue().getMatKurzzeichen(),
                        rthfViewModel.getUSERPlattenlager().getValue().getMz3()
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
                scanManager.unlockScanner();
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
        np.setValue((rthfViewModel.getUSERPlattenlager().getValue().getBrt().intValue()));
        np.setWrapSelectorWheel(false);
        d.setMessage("Breite wählen (mm)");
        d.setView(dialogView);
        d.setPositiveButton("Weiter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                rthfViewModel.setUSERPlattenlager(new USERPlattenlagerModel(
                        rthfViewModel.getUSERPlattenlager().getValue().getPlattenID(),
                        rthfViewModel.getUSERPlattenlager().getValue().getLagerPlatz(),
                        rthfViewModel.getUSERPlattenlager().getValue().getLng(),
                        (double) (np.getValue()*1),
                        rthfViewModel.getUSERPlattenlager().getValue().getMatKurzzeichen(),
                        rthfViewModel.getUSERPlattenlager().getValue().getMz3()
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
            System.out.println(data);
            String id = data.startsWith(" ")?data.substring(1):data;
            rthfViewModel.setTempLagerplatz(id);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        scanManager.removeDataListener(this);
        scanManager.releaseScanManager();
        rthfViewModel.setBitmap(null);
    }
}
