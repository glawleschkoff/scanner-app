package de.glawleschkoff.scannerapp.fragment;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.keyence.autoid.sdk.scan.DecodeResult;
import com.keyence.autoid.sdk.scan.ScanManager;

import de.glawleschkoff.scannerapp.databinding.FragmentBtzsscanBinding;
import de.glawleschkoff.scannerapp.viewmodel.BTZSViewModel;
import de.glawleschkoff.scannerapp.viewmodel.MetaViewModel;
import de.glawleschkoff.scannerapp.R;

public class BTZSScanFragment extends Fragment implements ScanManager.DataListener {

    private FragmentBtzsscanBinding binding;
    private BTZSViewModel btzsViewModel;
    private ScanManager mScanManager;
    public static BTZSScanFragment newInstance() {
        return new BTZSScanFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btzsViewModel = new ViewModelProvider(requireActivity()).get(BTZSViewModel.class);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentBtzsscanBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        //mScanManager = ScanManager.createScanManager(this.getContext());
        //mScanManager.addDataListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.text.setOnClickListener(x -> {
            btzsViewModel.requestBauteil("3400510-005");
            btzsViewModel.requestFeedback("3400510-005_BTZS.csv");
        });
        binding.bt1.setOnClickListener(x -> {
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_BTZSScanFragment_to_menuFragment);
        });
        btzsViewModel.getResponseCounter().observe(getViewLifecycleOwner(), response -> {
            if(getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle
                    .State.RESUMED
            && btzsViewModel.getResponseCounter().getValue() == 2){
                if(btzsViewModel.getResponseBauteil().getValue().code() != 200){
                    Navigation.findNavController(requireView())
                            .navigate(R.id.action_BTZSScanFragment_to_BTZSErrorFragment);
                } else if(btzsViewModel.getResponseFeedback().getValue() != null){
                    btzsViewModel.getInfo2fragmentText().setValue("Bereits zurückgesetzt");
                    Navigation.findNavController(requireView())
                            .navigate(R.id.action_BTZSScanFragment_to_BTZSSelectFragment3);
                } else if(!btzsViewModel.getResponseBauteil().getValue().body()
                        .getScannerAnweisung().equals("BTZS=J")){
                    btzsViewModel.getInfo2fragmentText().setValue("Zurücksetzen nicht möglich");
                    Navigation.findNavController(requireView())
                            .navigate(R.id.action_BTZSScanFragment_to_BTZSSelectFragment3);
                } else {
                    btzsViewModel.getInfo2fragmentText().setValue("Bauteil wirklich zurücksetzen?");
                    Navigation.findNavController(requireView())
                            .navigate(R.id.action_BTZSScanFragment_to_BTZSSelectFragment3);
                }
            }
        });
    }

    @Override
    public void onDataReceived(DecodeResult decodeResult) {
        DecodeResult.Result result = decodeResult.getResult();
        String codeType = decodeResult.getCodeType();
        String data = decodeResult.getData();
        //Toast.makeText(this.getContext(), data, Toast.LENGTH_SHORT).show();
        System.out.println(data);
        if(decodeResult.getResult() == DecodeResult.Result.SUCCESS){
            btzsViewModel.requestBauteil(data.substring(1));
            btzsViewModel.requestFeedback(data.substring(1)+"-001_BTZS.csv");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //mScanManager.removeDataListener(this);
        //mScanManager.releaseScanManager();
        btzsViewModel.getResponseCounter().removeObservers(this);
        btzsViewModel.resetResponseCounter();
    }

}