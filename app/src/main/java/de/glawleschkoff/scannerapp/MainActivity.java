package de.glawleschkoff.scannerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import de.glawleschkoff.scannerapp.databinding.ActivityMainBinding;
import de.glawleschkoff.scannerapp.viewmodel.InfoViewModel;
import de.glawleschkoff.scannerapp.viewmodel.MetaViewModel;

public class MainActivity extends AppCompatActivity {

    public ActivityMainBinding binding;
    private MetaViewModel metaViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        metaViewModel = new ViewModelProvider(this).get(MetaViewModel.class);
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
                metaViewModel.resetMitarbeiter();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        metaViewModel.getMitarbeiter().observe(this,x->{
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

}