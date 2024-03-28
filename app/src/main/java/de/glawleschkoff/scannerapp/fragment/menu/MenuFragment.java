package de.glawleschkoff.scannerapp.fragment.menu;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;

import de.glawleschkoff.scannerapp.util.LoginItemClickSupport;
import de.glawleschkoff.scannerapp.util.MenuRVAdapter;
import de.glawleschkoff.scannerapp.util.MenuRVItem;
import de.glawleschkoff.scannerapp.viewmodel.SuperViewModel;
import de.glawleschkoff.scannerapp.R;
import de.glawleschkoff.scannerapp.databinding.FragmentMenuBinding;

public class MenuFragment extends Fragment {

    private FragmentMenuBinding binding;
    private SuperViewModel superViewModel;
    private MenuRVAdapter menuRVAdapter;

    public static MenuFragment newInstance() {
        return new MenuFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        superViewModel = new ViewModelProvider(requireActivity()).get(SuperViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMenuBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Menü");

        menuRVAdapter = new MenuRVAdapter(this.getContext(), Arrays.asList(
                new MenuRVItem("Bauteil Info"),
                new MenuRVItem("Bauteil Zurücksetzen"),
                new MenuRVItem("Bauteil Nachbearbeiten"),
                new MenuRVItem("Bauteil Quali"),
                new MenuRVItem("Bauteil Etikett A"),
                new MenuRVItem("Bauteil Etikett B"),
                new MenuRVItem("Restteil Hinzufügen"),
                new MenuRVItem("Restteil Bearbeiten"),
                new MenuRVItem("Restteil Auslagern")));

        binding.rv2.setAdapter(menuRVAdapter);
        binding.rv2.setLayoutManager(new LinearLayoutManager(this.getContext()));

        LoginItemClickSupport.addTo(binding.rv2)
                        .setOnItemClickListener(new LoginItemClickSupport.OnItemClickListener() {
                            @Override
                            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                                System.out.println("Position: "+position);
                                switch (position){
                                    case 0:
                                        Navigation.findNavController(requireView())
                                                .navigate(R.id.action_menuFragment_to_btinScanFragment);
                                        break;
                                    case 1:
                                        Navigation.findNavController(requireView())
                                                .navigate(R.id.action_menuFragment_to_btzsScanFragment);
                                        break;
                                    case 2:
                                        Navigation.findNavController(requireView())
                                                .navigate(R.id.action_menuFragment_to_BTNBScanFragment);
                                        break;
                                    case 3:
                                        Navigation.findNavController(requireView())
                                                .navigate(R.id.action_menuFragment_to_btqlScanFragment);
                                        break;
                                    case 4:
                                        Navigation.findNavController(requireView())
                                                .navigate(R.id.action_menuFragment_to_bteaScanFragment);
                                        break;
                                    case 5:
                                        Navigation.findNavController(requireView())
                                                .navigate(R.id.action_menuFragment_to_btebScanFragment);
                                        break;
                                    case 6:
                                        Navigation.findNavController(requireView())
                                                .navigate(R.id.action_menuFragment_to_rthfSelectFragment);
                                        break;
                                    case 7:
                                        Navigation.findNavController(requireView())
                                                .navigate(R.id.action_menuFragment_to_rtbaScanFragment);
                                        break;
                                    case 8:
                                        Navigation.findNavController(requireView())
                                                .navigate(R.id.action_menuFragment_to_rtalScanFragment);
                                        break;
                                    default:
                                }
                            }
                        });

        superViewModel.getMitarbeiter().observe(getViewLifecycleOwner(), x->{
            if(x==null){
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_menuFragment_to_loginFragment);
            }
        });
    }
}