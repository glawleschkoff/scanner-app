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

import de.glawleschkoff.scannerapp.viewmodel.InfoViewModel;
import de.glawleschkoff.scannerapp.viewmodel.MetaViewModel;
import de.glawleschkoff.scannerapp.R;
import de.glawleschkoff.scannerapp.databinding.FragmentInfo1Binding;

public class Info1Fragment extends Fragment implements ScanManager.DataListener {

    private FragmentInfo1Binding binding;
    private InfoViewModel mViewModel;
    private MetaViewModel metaViewModel;
    private ScanManager mScanManager;
    public static Info1Fragment newInstance() {
        return new Info1Fragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(InfoViewModel.class);
        metaViewModel = new ViewModelProvider(requireActivity()).get(MetaViewModel.class);

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
        binding.text.setOnClickListener(x -> {
            mViewModel.requestBauteil("3922780-001");
            mViewModel.requestFeedback("3922780-001_BTZS.csv");
            System.out.println(metaViewModel.getMitarbeiter().getValue());
        });
        binding.bt1.setOnClickListener(x -> {
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_info1Fragment_to_menuFragment);
        });
        mViewModel.getResponseCounter().observe(getViewLifecycleOwner(), response -> {
            if(getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle
                    .State.RESUMED
            && mViewModel.getResponseCounter().getValue() == 2){
                if(mViewModel.getResponseBauteil().getValue().code() != 200){
                    Navigation.findNavController(requireView())
                            .navigate(R.id.action_info1Fragment_to_info3Fragment);
                } else if(mViewModel.getResponseFeedback().getValue() != null){
                    mViewModel.getInfo2fragmentText().setValue("Bereits zurückgesetzt");
                    Navigation.findNavController(requireView())
                            .navigate(R.id.action_info1Fragment_to_info2Fragment);
                } else if(!mViewModel.getResponseBauteil().getValue().body()
                        .getScannerAnweisung().equals("BTZS=J")){
                    mViewModel.getInfo2fragmentText().setValue("Zurücksetzen nicht möglich");
                    Navigation.findNavController(requireView())
                            .navigate(R.id.action_info1Fragment_to_info2Fragment);
                } else {
                    mViewModel.getInfo2fragmentText().setValue("Bauteil wirklich zurücksetzen?");
                    Navigation.findNavController(requireView())
                            .navigate(R.id.action_info1Fragment_to_info2Fragment);
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
            mViewModel.requestBauteil(data.substring(1));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //mScanManager.removeDataListener(this);
        //mScanManager.releaseScanManager();
        mViewModel.getResponseCounter().removeObservers(this);
        mViewModel.resetResponseCounter();
    }

}