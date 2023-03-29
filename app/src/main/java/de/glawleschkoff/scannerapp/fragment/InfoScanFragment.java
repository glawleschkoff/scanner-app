package de.glawleschkoff.scannerapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.keyence.autoid.sdk.scan.DecodeResult;
import com.keyence.autoid.sdk.scan.ScanManager;

import de.glawleschkoff.scannerapp.databinding.FragmentInfoscanBinding;

public class InfoScanFragment extends Fragment implements ScanManager.DataListener {

    private FragmentInfoscanBinding binding;
    //private InfoViewModel infoViewModel;
    private ScanManager scanManager;
    public static InfoScanFragment newInstance(){
        return new InfoScanFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //infoViewModel = new ViewModelProvider(requireActivity()).get(InfoViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInfoscanBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        //mScanManager = ScanManager.createScanManager(this.getContext());
        //mScanManager.addDataListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.text.setOnClickListener(x -> {
            //infoViewModel.requestBauteil("3922780-001");
            //infoViewModel.requestFeedback("3922780-001_BTZS.csv");
        });
        binding.bt1.setOnClickListener(x -> {

        });
    }

    @Override
    public void onDataReceived(DecodeResult decodeResult) {

    }
}
