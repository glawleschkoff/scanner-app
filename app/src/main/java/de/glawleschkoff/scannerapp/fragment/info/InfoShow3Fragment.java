package de.glawleschkoff.scannerapp.fragment.info;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.glawleschkoff.scannerapp.databinding.FragmentInfoshow2Binding;
import de.glawleschkoff.scannerapp.databinding.FragmentInfoshow3Binding;
import de.glawleschkoff.scannerapp.util.CardRVAdapter;
import de.glawleschkoff.scannerapp.util.CardRVItem;
import de.glawleschkoff.scannerapp.util.RVItem;
import de.glawleschkoff.scannerapp.viewmodel.InfoViewModel;

public class InfoShow3Fragment extends Fragment {

    private FragmentInfoshow3Binding binding;
    private InfoViewModel infoViewModel;
    private CardRVAdapter cardRVAdapter;

    public static InfoShow3Fragment newInstance(){
        return new InfoShow3Fragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        infoViewModel = new ViewModelProvider(requireActivity()).get(InfoViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInfoshow3Binding.inflate(getLayoutInflater());
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

        cardRVAdapter = new CardRVAdapter(Arrays.asList(new CardRVItem(list1, list2)),this.getContext());
        binding.rv99.setAdapter(cardRVAdapter);
        binding.rv99.setLayoutManager(new LinearLayoutManager(this.getContext()));

        infoViewModel.getResponseCNCFeedback().observe(getViewLifecycleOwner(),response -> {

            if(infoViewModel.getResponseCNCFeedback().getValue().getResponse()!=null){
                cardRVAdapter.setCardRVItemList(infoViewModel.getResponseCNCFeedback().getValue().getResponse().stream()
                        .map(x -> new CardRVItem(
                                Arrays.asList(
                                        new RVItem("Datum",x.getDatum()),
                                        new RVItem("Uhrzeit",x.getUhrzeit()),
                                        new RVItem("Fortschritt",x.getFortschritt())),
                                Arrays.asList(
                                        new RVItem("Maschine", x.getMaschine())
                                        //new RVItem("Bestandsbuchung", x.getBestandsBuchung())
                                ))).collect(Collectors.toList()));
            }
        });
    }
}
