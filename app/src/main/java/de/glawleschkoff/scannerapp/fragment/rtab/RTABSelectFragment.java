package de.glawleschkoff.scannerapp.fragment.rtab;

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
import de.glawleschkoff.scannerapp.databinding.FragmentRtabselectBinding;
import de.glawleschkoff.scannerapp.model.PlattenlagerModel;
import de.glawleschkoff.scannerapp.model.RestteilModel;
import de.glawleschkoff.scannerapp.util.PLEBCardRVAdapter;
import de.glawleschkoff.scannerapp.util.RTEBCardRVAdapter;
import de.glawleschkoff.scannerapp.util.RVItem;
import de.glawleschkoff.scannerapp.viewmodel.MetaViewModel;
import de.glawleschkoff.scannerapp.viewmodel.RTABViewModel;
import io.reactivex.annotations.NonNull;

public class RTABSelectFragment extends Fragment {

    private FragmentRtabselectBinding binding;
    private RTABViewModel rtabViewModel;
    private MetaViewModel metaViewModel;
    private PLEBCardRVAdapter plebCardRVAdapter;

    public static RTABSelectFragment newInstance() {
        return new RTABSelectFragment();
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
        binding = FragmentRtabselectBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rtabViewModel.getResponseBitmap().observe(getViewLifecycleOwner(),x -> {
            if(x.getResponse()!=null){
                Bitmap bitmap = x.getResponse();
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
                Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                binding.image.setImageBitmap(rotatedBitmap);
            }
        });


        rtabViewModel.getPlattenlagerModel().observe(getViewLifecycleOwner(),x -> {
            if(rtabViewModel.getPlattenlagerModel().getValue().getResponse()!=null){
                System.out.println("Request Lager");
                System.out.println(rtabViewModel.getPlattenlagerModel().getValue().getResponse().getMatKurzzeichen());
                rtabViewModel.requestLager(rtabViewModel.getPlattenlagerModel().getValue().getResponse().getMatKurzzeichen());
            }
        });

        rtabViewModel.getLagerModel().observe(getViewLifecycleOwner(),x -> {
            if(rtabViewModel.getLagerModel().getValue().getResponse()!=null){
                rtabViewModel.requestBitmap(rtabViewModel.getLagerModel().
                        getValue().getResponse().getMPTextur());
                System.out.println("Material Bild");

            }
        });

        List<RVItem> list1 = new ArrayList<>();
        list1.add(new RVItem("Lädt...",""));

        plebCardRVAdapter = new PLEBCardRVAdapter(list1,this.getContext(), x -> {});
        binding.rvoutside.setAdapter(plebCardRVAdapter);
        binding.rvoutside.setLayoutManager(new LinearLayoutManager(this.getContext()));

        rtabViewModel.getPlattenlagerModel().observe(getViewLifecycleOwner(), x -> {
            if(x!=null){
                plebCardRVAdapter.setRvItemList(Arrays.asList(
                        new RVItem("PlattenID",String.format("%.0f", x.getResponse().getPlattenID())),
                        new RVItem("Material\nKurzzeichen1",String.valueOf(x.getResponse().getMatKurzzeichen())),
                        new RVItem("Länge1",String.valueOf(x.getResponse().getLng())),
                        new RVItem("Breite1",String.valueOf(x.getResponse().getBrt())),
                        new RVItem("Lagerplatz1",String.valueOf(x.getResponse().getLagerPlatz()))
                        //new RVItem("switch",String.valueOf(x.getResponse().getMz3()))
                ));
                binding.switch1.setChecked(rtabViewModel.getPlattenlagerModel().getValue().getResponse().getMz3().equals("J"));
                binding.switch1.setClickable(false);
            }
        });

        binding.button.setOnClickListener(x -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_RTABSelectFragment_to_RTABScanFragment);
        });

        binding.button2.setOnClickListener(x -> {
            rtabViewModel.updatePlattenlager(metaViewModel.getMitarbeiter().getValue());
            new AlertDialog.Builder(getContext())
                    .setMessage("Etikett entfernen")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Navigation.findNavController(requireView()).navigate(R.id.action_RTABSelectFragment_to_RTABScanFragment);
                        }
                    })
                    .create()
                    .show();
        });

        metaViewModel.getMitarbeiter().observe(getViewLifecycleOwner(),x -> {
            if(x==null){
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_RTABSelectFragment_to_loginFragment);
            }
        });

    }

}
