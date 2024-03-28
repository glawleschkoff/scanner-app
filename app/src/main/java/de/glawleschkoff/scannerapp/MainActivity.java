package de.glawleschkoff.scannerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;

import de.glawleschkoff.scannerapp.databinding.ActivityMainBinding;
import de.glawleschkoff.scannerapp.viewmodel.SuperViewModel;

public class MainActivity extends AppCompatActivity {

    public ActivityMainBinding binding;
    private SuperViewModel superViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        superViewModel = new ViewModelProvider(this).get(SuperViewModel.class);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        menu.setGroupEnabled(0,false);
        menu.setGroupVisible(0,false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                superViewModel.resetMitarbeiter();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        superViewModel.getMitarbeiter().observe(this, x->{
            System.out.println(x);
            if(x == null){
                menu.setGroupEnabled(0,false);
                menu.setGroupVisible(0,false);
                binding.kRzel.setText("");
            } else {
                menu.setGroupEnabled(0,true);
                menu.setGroupVisible(0,true);
                menu.findItem(R.id.action_settings).setTitle("Abmelden");
                binding.kRzel.setText(x);
            }
        });
        return true;
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        if(superViewModel.getTimer().getValue()!=null){
            superViewModel.getTimer().getValue().cancel();
            superViewModel.getTimer().getValue().start();
        }
    }

}