package de.glawleschkoff.scannerapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.squareup.picasso.Picasso;

import de.glawleschkoff.scannerapp.databinding.FragmentInfoshow1Binding;
import de.glawleschkoff.scannerapp.model.BauteilModel;
import de.glawleschkoff.scannerapp.viewmodel.InfoViewModel;

public class InfoShow1Fragment extends Fragment {

    private FragmentInfoshow1Binding binding;
    private InfoViewModel infoViewModel;

    public static InfoShow1Fragment newInstance(){
        return new InfoShow1Fragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        infoViewModel = new ViewModelProvider(requireActivity()).get(InfoViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInfoshow1Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        infoViewModel.getResponseBauteil().observe(getViewLifecycleOwner(),x -> {
            if(infoViewModel.getResponseBauteil().getValue()!=null){
                String id = infoViewModel.getResponseBauteil().getValue().body().getKundenAuftrag();
                String name = infoViewModel.getResponseBauteil().getValue().body().getKundenPosition();
                Picasso.get().load("http://192.168.1.34:8080/api/v1/image/?id="+id+"&name="+name+".jpg").into(binding.image);
                binding.t00.setText("ExemplarNr");
                binding.t01.setText(infoViewModel.getResponseBauteil().getValue().body().getExemplarNr());
                binding.t10.setText("KundenAuftrag");
                binding.t11.setText(infoViewModel.getResponseBauteil().getValue().body().getKundenAuftrag());
                binding.t20.setText("KundenPosition");
                binding.t21.setText(infoViewModel.getResponseBauteil().getValue().body().getKundenPosition());
                binding.t30.setText("ProdFreigabe");
                binding.t31.setText(infoViewModel.getResponseBauteil().getValue().body().getProdFreigabe());
                binding.t40.setText("Status");
                binding.t41.setText(infoViewModel.getResponseBauteil().getValue().body().getStatus());
                binding.t50.setText("ProdDatum");
                binding.t51.setText(BauteilModel.ddmToMap(infoViewModel.getResponseBauteil()
                        .getValue().body().getRowDDMFields()).get("ProdDatum"));
                binding.t60.setText("FertigDatum");
                binding.t61.setText(BauteilModel.ddmToMap(infoViewModel.getResponseBauteil()
                        .getValue().body().getRowDDMFields()).get("FertigDatum"));
                binding.t70.setText("ScannerAnweisung");
                binding.t71.setText(infoViewModel.getResponseBauteil().getValue().body().getScannerAnweisung());
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        infoViewModel.getResponseBauteil().removeObservers(this);
    }
}
