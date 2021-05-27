package com.ametr1ne.overdiff;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.ametr1ne.overdiff.fragments.AuthFragment;
import com.ametr1ne.overdiff.fragments.ProfileFragment;
import com.ametr1ne.overdiff.fragments.TapesFragment;
import com.ametr1ne.overdiff.models.User;
import com.ametr1ne.overdiff.utils.FileProperties;
import com.ametr1ne.overdiff.utils.GlobalProperties;
import com.ametr1ne.overdiff.utils.RawProperties;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {

    private static FileProperties properties;

    private static final int PERMISSION_CALL = 127;
    private long backPressedTime;
    private Toast backTost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String deviceId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        GlobalProperties.DEVICE_ID = deviceId;
        GlobalProperties.IP = ip;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) {
            makeCall();
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_CALL);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALL) {

            boolean access = true;

            for (int grantResult : grantResults) {
                access &= grantResult == PackageManager.PERMISSION_GRANTED;
            }

            if (access)
                makeCall();
        }
    }

    public void makeCall(){
        RawProperties rawProperties = new RawProperties(getResources().openRawResource(R.raw.application));// getting XML
        rawProperties.getValue("ksite").ifPresent(s -> {
            GlobalProperties.KSITE_ADDRESS = s;
        });
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


    public void openAuthorizationPage() {
        this.runOnUiThread(() -> {
            this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new AuthFragment(this)).commit();
        });
    }

    public void update(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backTost.cancel();
            super.onBackPressed();
            return;
        } else {
            backTost = Toast.makeText(getBaseContext(), "Нажмите еще раз, чтобы выйти", Toast.LENGTH_SHORT);
            backTost.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}