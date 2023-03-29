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

import de.glawleschkoff.scannerapp.databinding.FragmentBtzserrorBinding;
import de.glawleschkoff.scannerapp.viewmodel.BTZSViewModel;
import de.glawleschkoff.scannerapp.viewmodel.MetaViewModel;
import de.glawleschkoff.scannerapp.R;

public class BTZSErrorFragment extends Fragment {

    private FragmentBtzserrorBinding binding;
    private BTZSViewModel mViewModel;
    private MetaViewModel metaViewModel;

    public static BTZSErrorFragment newInstance() {
        return new BTZSErrorFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(BTZSViewModel.class);
        metaViewModel = new ViewModelProvider(requireActivity()).get(MetaViewModel.class);

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentBtzserrorBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.button3.setOnClickListener(x -> Navigation.findNavController(requireView()).navigate(R.id.action_BTZSErrorFragment_to_BTZSScanFragment));

    }

    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == 501){
                    Navigation.findNavController(requireView()).navigate(R.id.action_BTZSErrorFragment_to_BTZSScanFragment);
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