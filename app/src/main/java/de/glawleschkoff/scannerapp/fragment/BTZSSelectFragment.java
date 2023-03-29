package de.glawleschkoff.scannerapp.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.tabs.TabLayout;

import de.glawleschkoff.scannerapp.R;
import de.glawleschkoff.scannerapp.databinding.FragmentBtzsselectBinding;
import de.glawleschkoff.scannerapp.model.FeedbackModel;
import de.glawleschkoff.scannerapp.viewmodel.BTZSViewModel;
import de.glawleschkoff.scannerapp.viewmodel.MetaViewModel;

public class BTZSSelectFragment extends Fragment {

    private FragmentBtzsselectBinding binding;
    private BTZSViewModel btzsViewModel;
    private MetaViewModel metaViewModel;
    private TabLayout tabLayout;

    public static BTZSSelectFragment newInstance() {
        return new BTZSSelectFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btzsViewModel = new ViewModelProvider(requireActivity()).get(BTZSViewModel.class);
        metaViewModel = new ViewModelProvider(requireActivity()).get(MetaViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBtzsselectBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        btzsViewModel.getInfo2fragmentText().observe(getViewLifecycleOwner(), response -> {
            binding.text2.setText(response);
            if (!btzsViewModel.getInfo2fragmentText().getValue().equals("Bauteil wirklich zurücksetzen?")) {
                binding.button2.setEnabled(false);
                binding.button.setText("Zurück");
            }
        });

        binding.button.setOnClickListener(x -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_BTZSSelectFragment3_to_BTZSScanFragment);
        });

        binding.button2.setOnClickListener(x -> {
            String exemplarNr = btzsViewModel.getResponseBauteil().getValue().body().getExemplarNr();
            String scannerNr = "001";
            String mitarbeiter = metaViewModel.getMitarbeiter().getValue();
            String kurzbefehl = "BTZS";
            String optionen = "";

            btzsViewModel.createFeedback(new FeedbackModel(exemplarNr, scannerNr, kurzbefehl, mitarbeiter,optionen));
            Navigation.findNavController(requireView()).navigate(R.id.action_BTZSSelectFragment3_to_BTZSScanFragment);
        });

        tabLayout = binding.tabLayout;
        TabLayout.Tab firstTab = tabLayout.newTab();
        firstTab.setText("Bauteil");
        tabLayout.addTab(firstTab);
        /*TabLayout.Tab secondTab = tabLayout.newTab();
        secondTab.setText("2");
        tabLayout.addTab(secondTab);
        TabLayout.Tab thirdTab = tabLayout.newTab();
        thirdTab.setText("3");
        tabLayout.addTab(thirdTab);

         */
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(binding.frame.getId(), new BTZSSelect1Fragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch (tab.getPosition()){
                    case 0: fragment = new BTZSSelect1Fragment(); break;
                }
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(binding.frame.getId(), fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == 501){
                    Navigation.findNavController(requireView()).navigate(R.id.action_BTZSSelectFragment3_to_BTZSScanFragment);
                    return true;
                } else return false;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        btzsViewModel.getResponseBauteil().removeObservers(this);
        btzsViewModel.getInfo2fragmentText().removeObservers(this);
        btzsViewModel.resetResponseBauteil();
        btzsViewModel.resetResponseFeedback();
    }
}
