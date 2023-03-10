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

import de.glawleschkoff.scannerapp.databinding.FragmentZuruecksetzen1Binding;

public class Reset1Fragment extends Fragment implements ScanManager.DataListener {

    private FragmentZuruecksetzen1Binding binding;
    private ResetViewModel mViewModel;
    private ScanManager mScanManager;

    public static Reset1Fragment newInstance() {
        return new Reset1Fragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(ResetViewModel.class);
        //mViewModel = new ViewModelProvider(this).get(Reset1ViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentZuruecksetzen1Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        //mViewModel.data.setValue(new BauteilModel("test44"));
        //binding.text.setText(mViewModel.data.getValue().getRowDDMFields());
        //mScanManager = ScanManager.createScanManager(this.getContext());
        //mScanManager.addDataListener(this);

        //mViewModel.data.observe(getViewLifecycleOwner(), response -> binding.text.setText(response.getRowDDMFields()));

        binding.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("Click");
                mViewModel.requestData("3818233-001");
                //System.out.println("fragment: " + mViewModel.data.getValue().getRowDDMFields());
                //System.out.println("text: " + binding.text.getText());
                //Navigation.findNavController(getView()).navigate(R.id.action_zuruecksetzen1Fragment_to_zuruecksetzen2Fragment);
            }
        });


        mViewModel.responseSuccessful().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    Navigation.findNavController(getView()).navigate(R.id.action_zuruecksetzen1Fragment_to_zuruecksetzen2Fragment);
                } else {
                    Navigation.findNavController(getView()).navigate(R.id.action_zuruecksetzen1Fragment_to_zuruecksetzen3Fragment);
                }
            }
        });



        return view;
    }

    @Override
    public void onDataReceived(DecodeResult decodeResult) {
        DecodeResult.Result result = decodeResult.getResult();
        String codeType = decodeResult.getCodeType();
        String data = decodeResult.getData();
        Toast.makeText(this.getContext(), data, Toast.LENGTH_SHORT).show();
        System.out.println(data);
        mViewModel.requestData(data);

        Navigation.findNavController(getView()).navigate(R.id.action_zuruecksetzen1Fragment_to_zuruecksetzen2Fragment);
    }



    @Override
    public void onDestroy(){
        super.onDestroy();
        mScanManager.removeDataListener(this);
        mScanManager.releaseScanManager();
    }
}