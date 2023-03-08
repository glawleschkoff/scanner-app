package de.glawleschkoff.scannerapp;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import de.glawleschkoff.scannerapp.old.BauteilRepository;

public class Reset1ViewModel extends ViewModel {

    private BauteilRepository bauteilRepository;
    private MutableLiveData<String> data;

    public Reset1ViewModel(){
        bauteilRepository = new BauteilRepository();
        data = bauteilRepository.bindData();
    }

    public void requestData(){
        bauteilRepository.getData();
    }
}