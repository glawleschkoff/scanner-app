package de.glawleschkoff.scannerapp.fragment.restteilauslagern;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.glawleschkoff.scannerapp.R;
import de.glawleschkoff.scannerapp.databinding.FragmentRtalselectBinding;
import de.glawleschkoff.scannerapp.util.CardRVAdapter;
import de.glawleschkoff.scannerapp.util.RVItem;
import de.glawleschkoff.scannerapp.viewmodel.SuperViewModel;
import de.glawleschkoff.scannerapp.viewmodel.RTALViewModel;
import io.reactivex.annotations.NonNull;

public class RTALSelectFragment extends Fragment {

    private FragmentRtalselectBinding binding;
    private RTALViewModel rtalViewModel;
    private SuperViewModel superViewModel;
    private CardRVAdapter cardRVAdapter;

    public static RTALSelectFragment newInstance() {
        return new RTALSelectFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rtalViewModel = new ViewModelProvider(requireActivity()).get(RTALViewModel.class);
        superViewModel = new ViewModelProvider(requireActivity()).get(SuperViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRtalselectBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rtalViewModel.getResponseBitmap().observe(getViewLifecycleOwner(), x -> {
            if(x.getResponse()!=null){
                Bitmap bitmap = x.getResponse();
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
                Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                binding.image.setImageBitmap(rotatedBitmap);
            }
        });


        rtalViewModel.getUSERPlattenlager().observe(getViewLifecycleOwner(), x -> {
            if(rtalViewModel.getUSERPlattenlager().getValue().getResponse()!=null){
                rtalViewModel.requestLager(rtalViewModel.getUSERPlattenlager().getValue().getResponse().getMatKurzzeichen());
            }
        });

        rtalViewModel.getLager().observe(getViewLifecycleOwner(), x -> {
            if(rtalViewModel.getLager().getValue().getResponse()!=null){
                rtalViewModel.requestBitmap(rtalViewModel.getLager().
                        getValue().getResponse().getMPTextur());
            }
        });

        List<RVItem> list1 = new ArrayList<>();
        list1.add(new RVItem("Lädt...",""));

        cardRVAdapter = new CardRVAdapter(list1,this.getContext(), x -> {});
        binding.rvoutside.setAdapter(cardRVAdapter);
        binding.rvoutside.setLayoutManager(new LinearLayoutManager(this.getContext()));

        rtalViewModel.getUSERPlattenlager().observe(getViewLifecycleOwner(), x -> {
            if(x!=null){
                cardRVAdapter.setRvItemList(Arrays.asList(
                        new RVItem("Material\nKurzzeichen1",String.valueOf(x.getResponse().getMatKurzzeichen())),
                        new RVItem("Länge1",String.valueOf(x.getResponse().getLng())),
                        new RVItem("Breite1",String.valueOf(x.getResponse().getBrt())),
                        new RVItem("Lagerplatz1",String.valueOf(x.getResponse().getLagerPlatz())),
                        new RVItem("PlattenID",String.format("%.0f", x.getResponse().getPlattenID()))
                ));
                binding.switch1.setChecked(rtalViewModel.getUSERPlattenlager().getValue().getResponse().getMz3().equals("J"));
                binding.switch1.setClickable(false);
            }
        });

        binding.button.setOnClickListener(x -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_rtalSelectFragment_to_rtalScanFragment);
        });

        binding.button2.setOnClickListener(x -> {
            rtalViewModel.updateUSERPlattenlager(superViewModel.getMitarbeiter().getValue());
            new AlertDialog.Builder(getContext())
                    .setMessage("Etikett entfernen")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Navigation.findNavController(requireView()).navigate(R.id.action_rtalSelectFragment_to_rtalScanFragment);
                        }
                    })
                    .create()
                    .show();
        });

        superViewModel.getMitarbeiter().observe(getViewLifecycleOwner(), x -> {
            if(x==null){
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_rtalSelectFragment_to_loginFragment);
            }
        });
    }
}
