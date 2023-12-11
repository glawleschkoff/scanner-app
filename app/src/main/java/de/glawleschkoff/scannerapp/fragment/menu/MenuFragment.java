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
    private MenuRVAdapter menuRVAdapter;

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
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Menü");

        menuRVAdapter = new MenuRVAdapter(this.getContext(), Arrays.asList(
                new MenuRVItem("Bauteil Info"),
                new MenuRVItem("Bauteil Zurücksetzen"),
                new MenuRVItem("Restteil Hinzufügen"),
                new MenuRVItem("Restteil Bearbeiten"),
                new MenuRVItem("Restteil Auslagern"),
                new MenuRVItem("Qualitätskontrolle"),
                new MenuRVItem("Bauteiletikett Drucken A"),
                new MenuRVItem("Bauteiletikett Drucken B")));

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
                                                .navigate(R.id.action_menuFragment_to_infoScanFragment2);
                                        break;
                                    case 1:
                                        Navigation.findNavController(requireView())
                                                .navigate(R.id.action_menuFragment_to_BTZSScanFragment);
                                        break;
                                    case 2:
                                        Navigation.findNavController(requireView())
                                                .navigate(R.id.action_menuFragment_to_RTHFSelectFragment);
                                        break;
                                    case 3:
                                        Navigation.findNavController(requireView())
                                                .navigate(R.id.action_menuFragment_to_PLEBScanFragment);
                                        break;
                                    case 4:
                                        Navigation.findNavController(requireView())
                                                .navigate(R.id.action_menuFragment_to_RTABScanFragment);
                                        break;
                                    case 5:
                                        Navigation.findNavController(requireView())
                                                .navigate(R.id.action_menuFragment_to_PRQMScanFragment);
                                        break;
                                    case 6:
                                        Navigation.findNavController(requireView())
                                                .navigate(R.id.action_menuFragment_to_BTEDScanFragment);
                                        break;
                                    case 7:
                                        Navigation.findNavController(requireView())
                                                .navigate(R.id.action_menuFragment_to_BTEDBScanFragment);
                                        break;
                                    default:
                                }
                            }
                        });

        metaViewModel.getMitarbeiter().observe(getViewLifecycleOwner(),x->{
            if(x==null){
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_menuFragment_to_loginFragment);
            }
        });

        /*binding.bt1.setOnClickListener(x -> {
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_menuFragment_to_BTZSScanFragment);
        });

        binding.bt2.setOnClickListener(x -> {
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_menuFragment_to_infoScanFragment2);
        });
        binding.bt3.setOnClickListener(x -> {
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_menuFragment_to_RTHFSelectFragment);
        });
        binding.bt4.setOnClickListener(x -> {
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_menuFragment_to_PLEBScanFragment);
        });
        binding.bt5.setOnClickListener(x -> {
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_menuFragment_to_RTABScanFragment);
        });
        binding.bt6.setOnClickListener(x -> {
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_menuFragment_to_PRQMScanFragment);
        });

         */
    }
}