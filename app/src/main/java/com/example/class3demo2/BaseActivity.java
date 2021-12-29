package com.example.class3demo2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class BaseActivity extends AppCompatActivity {
    NavController navCtrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        NavHostFragment nav_host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.base_navhost);
        navCtrl = nav_host.getNavController();
        NavigationUI.setupActionBarWithNavController(this,navCtrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.base_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (!super.onOptionsItemSelected(item)) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    navCtrl.navigateUp();
                    return true;
                case R.id.menu_settings:
//                    SettingsDialogFragment dialog = new SettingsDialogFragment();
                    DatePickerFragment dialog = new DatePickerFragment();
                    dialog.setOnDatePickerFragmentListener(new DatePickerFragment.DatePickerFragmentListener() {
                        @Override
                        public void onDateSet(int year, int month, int dayOfMonth) {
                            Log.d("TAG","date was set " + year+ " / " + month + " / " + dayOfMonth);
                        }
                    });
                    dialog.show(getSupportFragmentManager(), "TAG");
                    return true;
                default:
                    return NavigationUI.onNavDestinationSelected(item, navCtrl);
            }
        }
        return true;
    }
}