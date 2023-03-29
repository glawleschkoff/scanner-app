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

import de.glawleschkoff.scannerapp.databinding.FragmentBtzsselect1Binding;
import de.glawleschkoff.scannerapp.viewmodel.BTZSViewModel;

public class BTZSSelect1Fragment extends Fragment {

    private FragmentBtzsselect1Binding binding;
    private BTZSViewModel btzsViewModel;

    public static BTZSSelect1Fragment newInstance() {
        return new BTZSSelect1Fragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btzsViewModel = new ViewModelProvider(requireActivity()).get(BTZSViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBtzsselect1Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        btzsViewModel.getResponseBauteil().observe(getViewLifecycleOwner(),x -> {
            if(btzsViewModel.getResponseBauteil().getValue()!=null){
                binding.t01.setText(btzsViewModel.getResponseBauteil().getValue().body().getExemplarNr());
                binding.t11.setText(btzsViewModel.getResponseBauteil().getValue().body().getKundenAuftrag());
                binding.t21.setText(btzsViewModel.getResponseBauteil().getValue().body().getKundenPosition());
                binding.t31.setText(btzsViewModel.getResponseBauteil().getValue().body().getStatus());
                String id = btzsViewModel.getResponseBauteil().getValue().body().getKundenAuftrag();
                String name = btzsViewModel.getResponseBauteil().getValue().body().getKundenPosition();
                Picasso.get().load("http://192.168.1.34:8080/api/v1/image/?id="+id+"&name="+name+".jpg").into(binding.image);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        btzsViewModel.getResponseBauteil().removeObservers(this);
    }
}
