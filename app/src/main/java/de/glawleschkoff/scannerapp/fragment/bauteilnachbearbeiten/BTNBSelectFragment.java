package de.glawleschkoff.scannerapp.fragment.bauteilnachbearbeiten;

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
import java.util.stream.Collectors;

import de.glawleschkoff.scannerapp.R;
import de.glawleschkoff.scannerapp.databinding.FragmentBtnbselectBinding;
import de.glawleschkoff.scannerapp.databinding.FragmentRtbaselectBinding;
import de.glawleschkoff.scannerapp.fragment.restteilbearbeiten.RTBASelectFragment;
import de.glawleschkoff.scannerapp.model.USERPlattenlagerModel;
import de.glawleschkoff.scannerapp.util.AndLiveData;
import de.glawleschkoff.scannerapp.util.CardRVAdapter;
import de.glawleschkoff.scannerapp.util.RVItem;
import de.glawleschkoff.scannerapp.viewmodel.BTNBViewModel;
import de.glawleschkoff.scannerapp.viewmodel.RTBAViewModel;
import de.glawleschkoff.scannerapp.viewmodel.SuperViewModel;
import io.reactivex.annotations.NonNull;

public class BTNBSelectFragment extends Fragment implements ScanManager.DataListener {

    private FragmentBtnbselectBinding binding;
    private BTNBViewModel btnbViewModel;
    private SuperViewModel superViewModel;
    private CardRVAdapter cardRVAdapter;
    private ScanManager scanManager;

    public static BTNBSelectFragment newInstance() {
        return new BTNBSelectFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btnbViewModel = new ViewModelProvider(requireActivity()).get(BTNBViewModel.class);
        superViewModel = new ViewModelProvider(requireActivity()).get(SuperViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBtnbselectBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        //scanManager = ScanManager.createScanManager(this.getContext());
        //scanManager.addDataListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.text.setOnClickListener(x -> {
            //btnbViewModel.requestUSERALBDetails("4186643-001");
            btnbViewModel.requestUSERALBDetails("4132359-001");
        });

        btnbViewModel.getResponseBitmap().observe(getViewLifecycleOwner(), x -> {
            if(x.getResponse()!=null){
                Bitmap bitmap = x.getResponse();
                Matrix matrix = new Matrix();
                if(bitmap.getHeight() > bitmap.getWidth()){
                    matrix.postRotate(90);
                } else {
                    matrix.postRotate(0);
                }
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
                Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                binding.image.setImageBitmap(rotatedBitmap);
            }
        });

        AndLiveData.use(getViewLifecycleOwner())
                .add(btnbViewModel.getResponseUSERALBDetails())
                .observe(getViewLifecycleOwner(),x->{
                    if(btnbViewModel.getResponseUSERALBDetails().getValue().getErrorMessage() != null){
                        //scanManager.lockScanner();
                        new AlertDialog.Builder(getContext())
                                .setMessage("Bauteil nicht gefunden")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        //scanManager.unlockScanner();
                                        Navigation.findNavController(requireView()).navigate(R.id.action_BTNBSelectFragment_to_BTNBScanFragment);
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    } else {
                        Navigation.findNavController(requireView())
                                .navigate(R.id.action_BTNBSelectFragment_self);
                    }
                });

        List<RVItem> list1 = new ArrayList<>();
        list1.add(new RVItem("Lädt...",""));

        cardRVAdapter = new CardRVAdapter(list1,this.getContext(), x -> {});
        binding.rvoutside.setAdapter(cardRVAdapter);
        binding.rvoutside.setLayoutManager(new LinearLayoutManager(this.getContext()));

        btnbViewModel.getResponseUSERALBDetails().observe(getViewLifecycleOwner(), x -> {

            if(btnbViewModel.getResponseUSERALBDetails().getValue().getResponse()!=null){
                btnbViewModel.requestBitmap(btnbViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKundenAuftrag(),
                        btnbViewModel.getResponseUSERALBDetails()
                                .getValue().getResponse().getKundenPosition());

                if(!Arrays.stream(btnbViewModel.getResponseUSERALBDetails().getValue().getResponse()
                        .getScannerAntwort().split("#")).filter(y -> y.startsWith("BTNB")).collect(Collectors.toList()).isEmpty()) {
                    binding.button2.setVisibility(View.GONE);
                    binding.button2.setEnabled(false);
                    binding.text.setText("Bereits nachbearbeitet");
                    if(x!=null){
                        String fortschritt;
                        if(x.getResponse().getNb_fortschritt().length()!=0){
                            fortschritt = x.getResponse().getNb_fortschritt().replace(".","");
                        } else {
                            fortschritt = "qqqqqqqqq";
                        }
                        cardRVAdapter.setRvItemList(Arrays.asList(
                                new RVItem("WINKEL A",nbFortschrittFertigConverter(fortschritt.charAt(0))),
                                new RVItem("WINKEL B",nbFortschrittFertigConverter(fortschritt.charAt(1))),
                                new RVItem("WINKEL C",nbFortschrittFertigConverter(fortschritt.charAt(2))),
                                new RVItem("WINKEL D",nbFortschrittFertigConverter(fortschritt.charAt(3))),
                                new RVItem("SÄGEN A",nbFortschrittFertigConverter(fortschritt.charAt(4))),
                                new RVItem("SÄGEN B",nbFortschrittFertigConverter(fortschritt.charAt(5))),
                                new RVItem("SÄGEN C",nbFortschrittFertigConverter(fortschritt.charAt(6))),
                                new RVItem("SÄGEN D",nbFortschrittFertigConverter(fortschritt.charAt(7))),
                                new RVItem("SÄGEN Ecke",nbFortschrittFertigConverter(fortschritt.charAt(8)))
                        ));
                    }
                } else if(!Arrays.stream(btnbViewModel.getResponseUSERALBDetails().getValue().getResponse()
                        .getScannerAnweisung().split("#")).filter(y -> y.startsWith("BTNB=N")).collect(Collectors.toList()).isEmpty()){
                    binding.button2.setVisibility(View.GONE);
                    binding.button2.setEnabled(false);
                    binding.text.setText("Nicht freigegeben");
                    if(x!=null){
                        String fortschritt;
                        if(x.getResponse().getNb_fortschritt().length()!=0){
                            fortschritt = x.getResponse().getNb_fortschritt().replace(".","");
                        } else {
                            fortschritt = "qqqqqqqqq";
                        }
                        cardRVAdapter.setRvItemList(Arrays.asList(
                                new RVItem("WINKEL A",nbFortschrittConverter(fortschritt.charAt(0))),
                                new RVItem("WINKEL B",nbFortschrittConverter(fortschritt.charAt(1))),
                                new RVItem("WINKEL C",nbFortschrittConverter(fortschritt.charAt(2))),
                                new RVItem("WINKEL D",nbFortschrittConverter(fortschritt.charAt(3))),
                                new RVItem("SÄGEN A",nbFortschrittConverter(fortschritt.charAt(4))),
                                new RVItem("SÄGEN B",nbFortschrittConverter(fortschritt.charAt(5))),
                                new RVItem("SÄGEN C",nbFortschrittConverter(fortschritt.charAt(6))),
                                new RVItem("SÄGEN D",nbFortschrittConverter(fortschritt.charAt(7))),
                                new RVItem("SÄGEN Ecke",nbFortschrittConverter(fortschritt.charAt(8)))
                        ));
                    }
                } else {
                    if(x!=null){
                        String fortschritt;
                        if(x.getResponse().getNb_fortschritt().length()!=0){
                            fortschritt = x.getResponse().getNb_fortschritt().replace(".","");
                        } else {
                            fortschritt = "qqqqqqqqq";
                        }
                        cardRVAdapter.setRvItemList(Arrays.asList(
                                new RVItem("WINKEL A",nbFortschrittConverter(fortschritt.charAt(0))),
                                new RVItem("WINKEL B",nbFortschrittConverter(fortschritt.charAt(1))),
                                new RVItem("WINKEL C",nbFortschrittConverter(fortschritt.charAt(2))),
                                new RVItem("WINKEL D",nbFortschrittConverter(fortschritt.charAt(3))),
                                new RVItem("SÄGEN A",nbFortschrittConverter(fortschritt.charAt(4))),
                                new RVItem("SÄGEN B",nbFortschrittConverter(fortschritt.charAt(5))),
                                new RVItem("SÄGEN C",nbFortschrittConverter(fortschritt.charAt(6))),
                                new RVItem("SÄGEN D",nbFortschrittConverter(fortschritt.charAt(7))),
                                new RVItem("SÄGEN Ecke",nbFortschrittConverter(fortschritt.charAt(8)))
                        ));
                        if(!fortschritt.contains("1")){
                            binding.button2.setVisibility(View.GONE);
                            binding.button2.setEnabled(false);
                            binding.text.setText("Bereits nachbearbeitet");
                        }
                    }
                }
            }
        });

        binding.button.setOnClickListener(x -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_BTNBSelectFragment_to_BTNBScanFragment);
        });

        binding.button2.setOnClickListener(x -> {
            if(btnbViewModel.getResponseUSERALBDetails().getValue().getResponse()!=null && superViewModel.getMitarbeiter().getValue()!=null){
                String exemplarNr = btnbViewModel.getResponseUSERALBDetails().getValue().getResponse().getExemplarNr();

                String antwort = btnbViewModel.getResponseUSERALBDetails().getValue().getResponse().getScannerAntwort().equals("")?
                        "BTNB="+";;"+";"+ superViewModel.getMitarbeiter().getValue():
                        btnbViewModel.getResponseUSERALBDetails().getValue().getResponse().getScannerAntwort()+"#"+
                                "BTNB="+";;"+";"+ superViewModel.getMitarbeiter().getValue();
                btnbViewModel.updateUSERALBDetails(exemplarNr,antwort);
                Navigation.findNavController(requireView()).navigate(R.id.action_BTNBSelectFragment_to_BTNBScanFragment);
            }
        });

        superViewModel.getMitarbeiter().observe(getViewLifecycleOwner(), x -> {
            if(x==null){
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_BTNBSelectFragment_to_loginFragment);
            }
        });

    }

    private String nbFortschrittConverter(Character c){
        switch(c){
            case '0':
                return "-";
            case '1':
                return "offen";
            case '2':
                return "OK";
            default:
                return "";
        }
    }
    private String nbFortschrittFertigConverter(Character c){
        switch(c){
            case '0':
                return "-";
            case '1':
            case '2':
                return "OK";
            default:
                return "";
        }
    }

    @Override
    public void onDataReceived(DecodeResult decodeResult) {
        DecodeResult.Result result = decodeResult.getResult();
        String data = decodeResult.getData();
        if(result == DecodeResult.Result.SUCCESS){
            String id = data.startsWith(" ")?data.substring(1):data;
            btnbViewModel.requestUSERALBDetails(id);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //scanManager.removeDataListener(this);
        //scanManager.releaseScanManager();
    }
}
