package de.glawleschkoff.scannerapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import de.glawleschkoff.scannerapp.databinding.FragmentInfo3Binding;

public class Info3Fragment extends Fragment {

    private FragmentInfo3Binding binding;

    public static Info3Fragment newInstance() {
        return new Info3Fragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentInfo3Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        binding.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireView()).navigate(R.id.action_info3Fragment_to_info1Fragment);
            }
        });

        return view;
    }

}