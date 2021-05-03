package com.ametr1ne.overdiff;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Optional;

public class MainActivity extends AppCompatActivity {

    private static FileProperties properties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        System.out.println("REFRESH: "+UserFactory.getInstance().getCurrentUser().getRefreshToken());

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
    }


}