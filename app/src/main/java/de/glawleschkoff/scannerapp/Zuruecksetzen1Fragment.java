package de.glawleschkoff.scannerapp;

import androidx.fragment.app.FragmentTransaction;
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

public class Zuruecksetzen1Fragment extends Fragment implements ScanManager.DataListener {

    private FragmentZuruecksetzen1Binding binding;
    private Zuruecksetzen1ViewModel mViewModel;
    private ScanManager mScanManager;

    public static Zuruecksetzen1Fragment newInstance() {
        return new Zuruecksetzen1Fragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(Zuruecksetzen1ViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentZuruecksetzen1Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        mScanManager = ScanManager.createScanManager(this.getContext());
        mScanManager.addDataListener(this);

        return view;
    }

    @Override
    public void onDataReceived(DecodeResult decodeResult) {
        DecodeResult.Result result = decodeResult.getResult();
        String codeType = decodeResult.getCodeType();
        String data = decodeResult.getData();
        Toast.makeText(this.getContext(), data, Toast.LENGTH_SHORT).show();
        System.out.println(data);

        Navigation.findNavController(getView()).navigate(R.id.action_zuruecksetzen1Fragment_to_zuruecksetzen2Fragment);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mScanManager.removeDataListener(this);
        mScanManager.releaseScanManager();
    }
}