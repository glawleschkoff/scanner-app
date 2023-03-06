package de.glawleschkoff.scannerapp.ui.main;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.keyence.autoid.sdk.scan.DecodeResult;
import com.keyence.autoid.sdk.scan.ScanManager;

import java.util.Arrays;

import de.glawleschkoff.scannerapp.RecyclerViewBottomAdapter;
import de.glawleschkoff.scannerapp.RecyclerViewBottomItem;
import de.glawleschkoff.scannerapp.RecyclerViewTopAdapter;
import de.glawleschkoff.scannerapp.RecyclerViewTopItem;
import de.glawleschkoff.scannerapp.databinding.FragmentMainBinding;

public class MainFragment extends Fragment implements ScanManager.DataListener{

    private FragmentMainBinding binding;
    private MainViewModel mViewModel;
    private RecyclerViewTopAdapter recyclerViewTopAdapter;
    private RecyclerViewBottomAdapter recyclerViewBottomAdapter;
    private ScanManager mScanManager;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        mScanManager = ScanManager.createScanManager(this.getContext());
        mScanManager.addDataListener(this);

        recyclerViewTopAdapter = new RecyclerViewTopAdapter(this.getContext(),
                Arrays.asList(new RecyclerViewTopItem("Informationen ...")));
        recyclerViewBottomAdapter = new RecyclerViewBottomAdapter(this.getContext(),
                Arrays.asList(new RecyclerViewBottomItem("Das ist ein Knopf")));
        binding.rvTop.setAdapter(recyclerViewTopAdapter);
        binding.rvBottom.setAdapter(recyclerViewBottomAdapter);
        binding.rvTop.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.rvBottom.setLayoutManager(new LinearLayoutManager(this.getContext()));

        mViewModel.getTopItems().observe(getViewLifecycleOwner(),
                response -> recyclerViewTopAdapter.setRecyclerViewTopItems(response));
        mViewModel.getBottomItems().observe(getViewLifecycleOwner(),
                response -> recyclerViewBottomAdapter.setRecyclerViewBottomItems(response));
        return view;
    }

    @Override
    public void onDataReceived(DecodeResult decodeResult) {
        DecodeResult.Result result = decodeResult.getResult();
        String codeType = decodeResult.getCodeType();
        String data = decodeResult.getData();
        //Toast.makeText(this.getContext(), data, Toast.LENGTH_SHORT).show();
        System.out.println(data);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mScanManager.removeDataListener(this);
        mScanManager.releaseScanManager();
    }
}