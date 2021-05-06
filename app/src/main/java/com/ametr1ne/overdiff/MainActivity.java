package com.ametr1ne.overdiff;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ametr1ne.overdiff.settings.SettingsActivity;
import com.ametr1ne.overdiff.settings.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private Button bt_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_settings = findViewById(R.id.bt_settings);

        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        //String appTheme = prefs.getString("@string/switch_theme", "Как в системе");


        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new TapesFragment()).commit();
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.tape:
                            selectedFragment = new TapesFragment();
                            setTitle(R.string.title_tape);
                            break;
                        case R.id.profile:
                            selectedFragment = new ProfileFragment();
                            setTitle(R.string.title_profile);
                            break;
                    }
                    assert selectedFragment != null;
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };

    public void onClickSettings (View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
       startActivity(intent);
    }

    public void onClickLogout (View view) {

    }
}