package com.ametr1ne.overdiff;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ametr1ne.overdiff.fragments.AuthFragment;
import com.ametr1ne.overdiff.fragments.ProfileFragment;
import com.ametr1ne.overdiff.fragments.TapesFragment;
import com.ametr1ne.overdiff.models.User;
import com.ametr1ne.overdiff.settings.SettingsFragment;
import com.ametr1ne.overdiff.utils.FileProperties;
import com.ametr1ne.overdiff.utils.GlobalProperties;
import com.ametr1ne.overdiff.utils.RawProperties;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static FileProperties properties;

    private static final int PERMISSION_CALL = 127;
    private Button bt_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) {
            makeCall();
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_CALL);
        }



        System.out.println("REFRESH: "+UserFactory.getInstance().getCurrentUser().getRefreshToken());

    }

    public void onClickSettings (View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new SettingsFragment()).commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CALL) {

            boolean access = true;

            for (int grantResult : grantResults) {
                access &= grantResult == PackageManager.PERMISSION_GRANTED;
            }

            if(access)
                makeCall();
        }
    }

    public void makeCall(){
        RawProperties rawProperties = new RawProperties(getResources().openRawResource(R.raw.application));// getting XML
        rawProperties.getValue("ksite").ifPresent(s -> {
            GlobalProperties.KSITE_ADDRESS = s;
        });


        System.out.println("Connect to "+GlobalProperties.KSITE_ADDRESS);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        properties = new FileProperties(new File(getFilesDir(), "config.data"));
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new TapesFragment(this)).commit();





        UserFactory.getInstance().refreshSavedUser(user -> {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new TapesFragment(this)).commit();
        });
    }

    public static FileProperties getProperties(){
        return properties;
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.tapes:
                        selectedFragment = new TapesFragment(this);
                        break;
                    case R.id.profile:

                        User currentUser = UserFactory.getInstance().getCurrentUser();
                        if(currentUser.isAuthorization()){
                            selectedFragment = new ProfileFragment(this);
                        }else{
                            selectedFragment = new AuthFragment(this);
                        }

                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();
                return true;
            };


    public void openAuthorizationPage(){
        this.runOnUiThread(() -> {
            this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new AuthFragment(this)).commit();
        });
    public void onClickSettings (View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
       startActivity(intent);
    }

    public void onClickLogout (View view) {

    }
}