package de.glawleschkoff.scannerapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.glawleschkoff.scannerapp.viewmodel.MetaViewModel;
import de.glawleschkoff.scannerapp.R;
import de.glawleschkoff.scannerapp.databinding.FragmentMenuBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment {

    private FragmentMenuBinding binding;
    private MetaViewModel metaViewModel;

    public static MenuFragment newInstance() {
        return new MenuFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        metaViewModel = new ViewModelProvider(requireActivity()).get(MetaViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMenuBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        binding.btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                metaViewModel.setMitarbeiter("");
                getActivity().setTitle("ScannerApp");
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_menuFragment_to_loginFragment);
            }
        });

        binding.bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_menuFragment_to_info1Fragment);
            }
        });

        return view;
    }
}