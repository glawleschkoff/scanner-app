package de.glawleschkoff.scannerapp.fragment.rthf;

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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import de.glawleschkoff.scannerapp.R;
import de.glawleschkoff.scannerapp.databinding.FragmentPlebselectBinding;
import de.glawleschkoff.scannerapp.databinding.FragmentRthfselectBinding;
import de.glawleschkoff.scannerapp.model.PlattenlagerModel;
import de.glawleschkoff.scannerapp.model.ResponseWrapper;
import de.glawleschkoff.scannerapp.model.RestteilModel;
import de.glawleschkoff.scannerapp.util.ClickInterface;
import de.glawleschkoff.scannerapp.util.MaterialienAdapter;
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
    private boolean allowImage;

    public static RTHFSelectFragment newInstance() {
        return new RTHFSelectFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rthfViewModel = new ViewModelProvider(requireActivity()).get(RTHFViewModel.class);
        metaViewModel = new ViewModelProvider(requireActivity()).get(MetaViewModel.class);
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

        getActivity().setTitle("Restteil einbuchen");

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
                System.out.println("biiiiiild");
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
                    case "Material\nKurzzeichen":
                        pickMaterialKurzzeichen();
                        break;
                    case "on":
                        rthfViewModel.setPlattenlagerModel(new PlattenlagerModel(
                                rthfViewModel.getPlattenlagerModel().getValue().getPlattenID(),
                                rthfViewModel.getPlattenlagerModel().getValue().getLagerPlatz(),
                                rthfViewModel.getPlattenlagerModel().getValue().getLng(),
                                rthfViewModel.getPlattenlagerModel().getValue().getBrt(),
                                rthfViewModel.getPlattenlagerModel().getValue().getMatKurzzeichen(),
                                "J"
                        ));
                        System.out.println("ooon");
                        break;
                    case "off":
                        rthfViewModel.setPlattenlagerModel(new PlattenlagerModel(
                                rthfViewModel.getPlattenlagerModel().getValue().getPlattenID(),
                                rthfViewModel.getPlattenlagerModel().getValue().getLagerPlatz(),
                                rthfViewModel.getPlattenlagerModel().getValue().getLng(),
                                rthfViewModel.getPlattenlagerModel().getValue().getBrt(),
                                rthfViewModel.getPlattenlagerModel().getValue().getMatKurzzeichen(),
                                ""
                        ));
                        System.out.println("offff");
                        break;
                    default:
                }
            }
        });
        binding.rvoutside.setAdapter(plebCardRVAdapter);
        binding.rvoutside.setLayoutManager(new LinearLayoutManager(this.getContext()));

        rthfViewModel.getMaxPlattenID().observe(getViewLifecycleOwner(), x -> {
            if(x.getResponse()!=null){
                rthfViewModel.setPlattenlagerModel(new PlattenlagerModel(x.getResponse()+1,"",1400.,600.,"","J"));
            }
        });

        rthfViewModel.getPlattenlagerModel().observe(getViewLifecycleOwner(), x -> {
            if(x!=null){
                plebCardRVAdapter.setRvItemList(Arrays.asList(
                        new RVItem("PlattenID",String.format("%.0f", x.getPlattenID())),
                        new RVItem("Material\nKurzzeichen",String.valueOf(x.getMatKurzzeichen())!=""?String.valueOf(x.getMatKurzzeichen()):"Wähle Material"),
                        new RVItem("Länge",String.valueOf(x.getLng())),
                        new RVItem("Breite",String.valueOf(x.getBrt())),
                        new RVItem("Lagerplatz",String.valueOf(x.getLagerPlatz()))
                        //new RVItem("switch","new")
                ));
                binding.switch1.setChecked(rthfViewModel.getPlattenlagerModel().getValue().getMz3().equals("J"));
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

            if(rthfViewModel.getPlattenlagerModel().getValue().getMatKurzzeichen().equals("")){
                new AlertDialog.Builder(getContext())
                        //.setTitle("Delete entry")
                        .setMessage("Material noch nicht gewählt")

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
                rthfViewModel.insertPlattenlager(Integer.parseInt(metaViewModel.getScannerNr().getValue())
                        ,rthfViewModel.getPlattenlagerModel().getValue().getMatKurzzeichen()
                        ,rthfViewModel.getPlattenlagerModel().getValue().getPlattenID()
                        ,rthfViewModel.getPlattenlagerModel().getValue().getLagerPlatz()
                        ,rthfViewModel.getPlattenlagerModel().getValue().getMz3()
                        ,rthfViewModel.getPlattenlagerModel().getValue().getLng()
                        ,rthfViewModel.getPlattenlagerModel().getValue().getBrt());
                Navigation.findNavController(requireView()).navigate(R.id.action_RTHFSelectFragment_to_menuFragment);
            }


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
        return s.length()<10;
        //return s.toLowerCase().matches("[a-z][0-9]");
    }

    private void pickMaterialKurzzeichen() {
        // Create an alert builder
        AlertDialog optionDialog = new AlertDialog.Builder(getContext()).create();
        //AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        optionDialog.setTitle("Wähle Material");
        optionDialog.setButton(Dialog.BUTTON_NEGATIVE, "Abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        // set the custom layout
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
                rthfViewModel.setPlattenlagerModel(new PlattenlagerModel(
                        rthfViewModel.getPlattenlagerModel().getValue().getPlattenID(),
                        rthfViewModel.getPlattenlagerModel().getValue().getLagerPlatz(),
                        rthfViewModel.getPlattenlagerModel().getValue().getLng(),
                        rthfViewModel.getPlattenlagerModel().getValue().getBrt(),
                        name,
                        rthfViewModel.getPlattenlagerModel().getValue().getMz3()
                ));
                allowImage = true;
                rthfViewModel.requestLager(name);
                System.out.println("hier55");
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

        // add a button
        /*optionDialog.set
        optionDialog.setPositiveButton("OK", (dialog1, which) -> {

        });*/
        optionDialog.show();
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
                return String.valueOf(value);
            }
        });
        np.setValue((rthfViewModel.getPlattenlagerModel().getValue().getLng().intValue()));
        np.setWrapSelectorWheel(false);
        d.setMessage("Länge wählen (mm)");
        d.setView(dialogView);
        d.setPositiveButton("Weiter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                rthfViewModel.setPlattenlagerModel(new PlattenlagerModel(
                        rthfViewModel.getPlattenlagerModel().getValue().getPlattenID(),
                        rthfViewModel.getPlattenlagerModel().getValue().getLagerPlatz(),
                        (double) (np.getValue()*1),
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
                return String.valueOf(value*1);
            }
        });
        np.setValue((rthfViewModel.getPlattenlagerModel().getValue().getBrt().intValue()));
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
                        (double) (np.getValue()*1),
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
            System.out.println(data);
            String id = data.startsWith(" ")?data.substring(1):data;
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
        rthfViewModel.setBitmap(null);
    }
}
