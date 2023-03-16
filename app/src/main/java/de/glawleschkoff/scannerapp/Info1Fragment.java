package de.glawleschkoff.scannerapp;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.JsonToken;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.keyence.autoid.sdk.scan.DecodeResult;
import com.keyence.autoid.sdk.scan.ScanManager;

import java.util.List;
import java.util.Objects;

import de.glawleschkoff.scannerapp.databinding.FragmentInfo1Binding;
import retrofit2.Response;

public class Info1Fragment extends Fragment implements ScanManager.DataListener {

    private FragmentInfo1Binding binding;
    private InfoViewModel mViewModel;
    private ScanManager mScanManager;
    public static Info1Fragment newInstance() {
        return new Info1Fragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(InfoViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentInfo1Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        //mScanManager = ScanManager.createScanManager(this.getContext());
        //mScanManager.addDataListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.text.setOnClickListener(x -> mViewModel.getBauteil("3818233-001"));
        mViewModel.getResponseLivedata().observe(getViewLifecycleOwner(), response -> {
            if(getViewLifecycleOwner().getLifecycle().getCurrentState()== Lifecycle.State.RESUMED){
                if(response.code() == 200){
                    Navigation.findNavController(requireView()).navigate(R.id.action_info1Fragment_to_info2Fragment);
                } else {
                    Navigation.findNavController(requireView()).navigate(R.id.action_info1Fragment_to_info3Fragment);
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
            mViewModel.getBauteil(data.substring(1));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //mScanManager.removeDataListener(this);
        //mScanManager.releaseScanManager();
        mViewModel.getResponseLivedata().removeObservers(this);
    }

}