package de.glawleschkoff.scannerapp.fragment.login;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import de.glawleschkoff.scannerapp.util.LoginItemClickSupport;
import de.glawleschkoff.scannerapp.viewmodel.LoginViewModel;
import de.glawleschkoff.scannerapp.viewmodel.MetaViewModel;
import de.glawleschkoff.scannerapp.R;
import de.glawleschkoff.scannerapp.util.LoginRVAdapter;
import de.glawleschkoff.scannerapp.util.LoginRVItem;
import de.glawleschkoff.scannerapp.databinding.FragmentLoginBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private LoginViewModel loginViewModel;
    private MetaViewModel metaViewModel;
    private LoginRVAdapter loginRVAdapter;

   public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        metaViewModel = new ViewModelProvider(requireActivity()).get(MetaViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginRVAdapter = new LoginRVAdapter(this.getContext(),
                Arrays.asList(new LoginRVItem("Lädt...")));
        binding.rv2.setAdapter(loginRVAdapter);
        binding.rv2.setLayoutManager(new LinearLayoutManager(this.getContext()));

        getActivity().setTitle("Login");

        loginViewModel.requestMitarbeiter();

        loginViewModel.getResponseMitarbeiter().observe(getViewLifecycleOwner(), response -> {
            if(response.getResponse()!=null){
                loginRVAdapter.setRecyclerViewItems(response.getResponse().stream()
                        .map(x -> new LoginRVItem(x)).collect(Collectors.toList()));
            }
        });


        LoginItemClickSupport.addTo(binding.rv2)
                .setOnItemClickListener(new LoginItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        //Toast.makeText(getContext(),loginViewModel.getResponseMitarbeiter()
                        //       .getValue().get(position),Toast.LENGTH_SHORT).show();


                        new AlertDialog.Builder(getContext())
                                //.setTitle("Delete entry")
                                .setMessage("Wirklich als "+ loginViewModel.getResponseMitarbeiter()
                                        .getValue().getResponse().get(position)+ " anmelden?")

                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        new AlertDialog.Builder(getContext())
                                                .setSingleChoiceItems(new CharSequence[]{"Nach 5 min abmelden", "Nach 60 min abmelden"}, 0, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        if(which == 0){
                                                            metaViewModel.setFiveMinutes(true);
                                                        } else {
                                                            metaViewModel.setFiveMinutes(false);
                                                        }
                                                    }

                                                })
                                                .setPositiveButton("Weiter", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Boolean b = metaViewModel.getFiveMinutes().getValue();
                                                        metaViewModel.setMitarbeiter(loginViewModel.getResponseMitarbeiter()
                                                        .getValue().getResponse().get(position));
                                                        //getActivity().setTitle("Menü - angemeldet als "+metaViewModel.getMitarbeiter().getValue());
                                                        Navigation.findNavController(requireView())
                                                        .navigate(R.id.action_loginFragment_to_menuFragment);
                                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                metaViewModel.setTimer(b==true?300000:3600000);
                                                                metaViewModel.getTimer().getValue().start();
                                                            }
                                                        });

                                                    }
                                                })
                                                .create()
                                                .show();

                                    }
                                })

                                // A null listener allows the button to dismiss the dialog and take no further action.
                                .setNegativeButton("Nein", null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                });
    }
}