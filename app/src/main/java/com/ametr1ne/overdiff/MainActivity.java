package com.ametr1ne.overdiff;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ametr1ne.overdiff.fragments.AuthFragment;
import com.ametr1ne.overdiff.fragments.ProfileFragment;
import com.ametr1ne.overdiff.fragments.TapesFragment;
import com.ametr1ne.overdiff.models.User;
import com.ametr1ne.overdiff.utils.ArticlesActionTask;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Optional;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new TapesFragment(this)).commit();
    }


    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.tapes:
                        selectedFragment = new TapesFragment(this);
                        break;
                    case R.id.profile:

                        Optional<User> optionalUser = UserFactory.getInstance().getCurrentUser();
                        if(optionalUser.isPresent()){
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
}