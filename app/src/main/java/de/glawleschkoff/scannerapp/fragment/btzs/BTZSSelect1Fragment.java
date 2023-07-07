package de.glawleschkoff.scannerapp.fragment.btzs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Arrays;

import de.glawleschkoff.scannerapp.databinding.FragmentBtzsselect1Binding;
import de.glawleschkoff.scannerapp.util.RVAdapter;
import de.glawleschkoff.scannerapp.util.RVItem;
import de.glawleschkoff.scannerapp.viewmodel.BTZSViewModel;

public class BTZSSelect1Fragment extends Fragment {

    private FragmentBtzsselect1Binding binding;
    private BTZSViewModel btzsViewModel;
    private RVAdapter rvAdapter;

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
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvAdapter = new RVAdapter(this.getContext(),
                Arrays.asList(new RVItem("Lädt...","")));
        binding.rv.setAdapter(rvAdapter);
        binding.rv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.rv.setHasFixedSize(true);

        btzsViewModel.getResponseBitmap().observe(getViewLifecycleOwner(),x -> {
            if(x.getResponse()!=null){
                binding.image.setImageBitmap(x.getResponse());
            }
        });

        binding.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView image = new ImageView(getContext());
                image.setImageBitmap(btzsViewModel.getResponseBitmap().getValue().getResponse());

                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getContext()).
                                setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).
                                setView(image);
                builder.create().show();
            }
        });

        btzsViewModel.getResponseBauteil().observe(getViewLifecycleOwner(),x -> {
            if(btzsViewModel.getResponseBauteil().getValue().getResponse()!=null){
                btzsViewModel.requestBitmap(btzsViewModel.getResponseBauteil()
                                .getValue().getResponse().getKundenAuftrag(),
                        btzsViewModel.getResponseBauteil()
                                .getValue().getResponse().getKundenPosition());
            }

            if(x.getResponse()!=null){
                rvAdapter.setRecyclerViewItems(Arrays.asList(
                        new RVItem("ExemplarNr",x.getResponse().getExemplarNr()),
                        new RVItem("KundenAuftrag",x.getResponse().getKundenAuftrag()),
                        new RVItem("KundenPosition",x.getResponse().getKundenPosition()),
                        new RVItem("Status",x.getResponse().getStatus())
                ));
            }
        });
    }

}
