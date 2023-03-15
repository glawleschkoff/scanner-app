package de.glawleschkoff.scannerapp;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.keyence.autoid.sdk.scan.DecodeResult;
import com.keyence.autoid.sdk.scan.ScanManager;

import java.util.Objects;

import de.glawleschkoff.scannerapp.databinding.FragmentInfo1Binding;

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
        mScanManager = ScanManager.createScanManager(this.getContext());
        mScanManager.addDataListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.text.setOnClickListener(x -> mViewModel.getBauteil("3818233-001"));
        mViewModel.responseSuccessful().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer aInteger) {
                if(aInteger==1){
                    Navigation.findNavController(requireView()).navigate(R.id.action_info1Fragment_to_info2Fragment);
                } else if(aInteger==-1){
                    Navigation.findNavController(requireView()).navigate(R.id.action_info1Fragment_to_info3Fragment);
                    mViewModel.resetResponseState();
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.out.println("destroy");
        mScanManager.removeDataListener(this);
        mScanManager.releaseScanManager();
        mViewModel.responseSuccessful().removeObservers(this);
    }

}