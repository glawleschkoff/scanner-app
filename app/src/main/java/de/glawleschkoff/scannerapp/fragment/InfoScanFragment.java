package de.glawleschkoff.scannerapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.keyence.autoid.sdk.scan.DecodeResult;
import com.keyence.autoid.sdk.scan.ScanManager;

import de.glawleschkoff.scannerapp.R;
import de.glawleschkoff.scannerapp.databinding.FragmentInfoscanBinding;
import de.glawleschkoff.scannerapp.viewmodel.InfoViewModel;

public class InfoScanFragment extends Fragment implements ScanManager.DataListener {

    private FragmentInfoscanBinding binding;
    private InfoViewModel infoViewModel;
    private ScanManager scanManager;
    public static InfoScanFragment newInstance(){
        return new InfoScanFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        infoViewModel = new ViewModelProvider(requireActivity()).get(InfoViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInfoscanBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        //scanManager = ScanManager.createScanManager(this.getContext());
        //scanManager.addDataListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.text.setOnClickListener(x -> {
            infoViewModel.requestBauteil("3922780-001");
            infoViewModel.requestFeedback("3922780-001_BTZS.csv");
        });
        binding.bt1.setOnClickListener(x -> {
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_infoScanFragment2_to_menuFragment);
        });
        infoViewModel.getResponseCounter().observe(getViewLifecycleOwner(), response -> {
            if(getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle
                    .State.RESUMED
                    && infoViewModel.getResponseCounter().getValue() == 2){
                if(infoViewModel.getResponseBauteil().getValue().code() != 200){
                    Navigation.findNavController(requireView())
                            .navigate(R.id.action_infoScanFragment2_to_infoErrorFragment);
                } else if(infoViewModel.getResponseFeedback().getValue() != null){
                    infoViewModel.setBauteilDataDeprecated(true);
                    Navigation.findNavController(requireView())
                            .navigate(R.id.action_infoScanFragment2_to_infoShowFragment);
                } else {
                    Navigation.findNavController(requireView())
                            .navigate(R.id.action_infoScanFragment2_to_infoShowFragment);
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
            infoViewModel.requestBauteil(data.substring(1));
            infoViewModel.requestFeedback(data.substring(1)+"-001_BTZS.csv");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //mScanManager.removeDataListener(this);
        //mScanManager.releaseScanManager();
        infoViewModel.getResponseCounter().removeObservers(this);
        infoViewModel.resetResponseCounter();
    }
}
