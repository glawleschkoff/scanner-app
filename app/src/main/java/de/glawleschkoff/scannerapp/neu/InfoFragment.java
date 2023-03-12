package de.glawleschkoff.scannerapp.neu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.glawleschkoff.scannerapp.R;
import de.glawleschkoff.scannerapp.databinding.FragmentInfoBinding;

public class InfoFragment extends Fragment {

    private FragmentInfoBinding binding;

    public static InfoFragment newInstance() {
        return new InfoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInfoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
    }
}