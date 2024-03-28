package de.glawleschkoff.scannerapp.fragment.bauteilinfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import de.glawleschkoff.scannerapp.databinding.FragmentBtinshow5Binding;
import de.glawleschkoff.scannerapp.util.CollapsableCardRVAdapter;
import de.glawleschkoff.scannerapp.util.CollapsableCardRVItem;
import de.glawleschkoff.scannerapp.util.RVItem;
import de.glawleschkoff.scannerapp.viewmodel.BTINViewModel;

public class BTINShow5Fragment extends Fragment {

    private FragmentBtinshow5Binding binding;
    private BTINViewModel btinViewModel;
    private CollapsableCardRVAdapter collapsableCardRVAdapter;

    public static BTINShow5Fragment newInstance(){
        return new BTINShow5Fragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btinViewModel = new ViewModelProvider(requireActivity()).get(BTINViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBtinshow5Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<RVItem> list1 = new ArrayList<>();
        list1.add(new RVItem("Name1","Wert1"));
        List<RVItem> list2 = new ArrayList<>();
        list2.add(new RVItem("VersteckterName1","VersteckterWert1"));

        collapsableCardRVAdapter = new CollapsableCardRVAdapter(Arrays.asList(new CollapsableCardRVItem(list1, list2)),this.getContext());
        binding.rv99.setAdapter(collapsableCardRVAdapter);
        binding.rv99.setLayoutManager(new LinearLayoutManager(this.getContext()));

        btinViewModel.getResponseUSERKntFeedback().observe(getViewLifecycleOwner(), response -> {

            if(response.getResponse()!=null){
                collapsableCardRVAdapter.setCardRVItemList(btinViewModel.getResponseUSERKntFeedback().getValue().getResponse().stream()
                        .map(x -> new CollapsableCardRVItem(
                                Arrays.asList(
                                        new RVItem("Datum",x.getDatum()),
                                        new RVItem("Uhrzeit",x.getUhrzeit()),
                                        new RVItem("LaufNr",x.getLaufNr()),
                                        new RVItem("Kante", x.getKante())),
                                Arrays.asList(
                                        new RVItem("KantenMat",x.getKantenMat()),
                                        new RVItem("KantenLng",x.getKantenLng())
                                ))).collect(Collectors.toList()));
            }
        });
    }
}
