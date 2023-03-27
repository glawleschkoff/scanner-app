package de.glawleschkoff.scannerapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.glawleschkoff.scannerapp.viewmodel.InfoViewModel;
import de.glawleschkoff.scannerapp.viewmodel.MetaViewModel;
import de.glawleschkoff.scannerapp.R;
import de.glawleschkoff.scannerapp.databinding.FragmentInfo3Binding;

public class Info3Fragment extends Fragment {

    private FragmentInfo3Binding binding;
    private InfoViewModel mViewModel;
    private MetaViewModel metaViewModel;

    public static Info3Fragment newInstance() {
        return new Info3Fragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(InfoViewModel.class);
        metaViewModel = new ViewModelProvider(requireActivity()).get(MetaViewModel.class);

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentInfo3Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.button3.setOnClickListener(x -> Navigation.findNavController(requireView()).navigate(R.id.action_info3Fragment_to_info1Fragment));

    }

    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == 501){
                    Navigation.findNavController(requireView()).navigate(R.id.action_info3Fragment_to_info1Fragment);
                    return true;
                } else return false;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewModel.resetResponseBauteil();
        mViewModel.resetResponseFeedback();
    }
}