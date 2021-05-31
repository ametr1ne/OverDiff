package com.ametr1ne.overdiff.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ametr1ne.overdiff.MainActivity;
import com.ametr1ne.overdiff.R;
import com.ametr1ne.overdiff.UserFactory;
import com.ametr1ne.overdiff.listener.AuthClickListener;
import com.ametr1ne.overdiff.utils.FileProperties;

import java.util.Optional;


public class AuthFragment extends Fragment  {

    private MainActivity source;

    public AuthFragment(MainActivity source) {
        this.source = source;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auth, container, false);

        FileProperties properties = MainActivity.getProperties();

        String refreshTokenOptional = properties.getValue("refresh_token");
        String userIdOptional = properties.getValue("user_id");

        if(refreshTokenOptional!=null && userIdOptional!=null){

            String refreshToken = refreshTokenOptional;
            long userId = Long.parseLong(userIdOptional);

            UserFactory.getInstance().refreshCurrentUser(user -> {
                if(user.isAuthorization()) {
                    source.runOnUiThread(() -> {
                        source.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new ProfileFragment(source)).commit();
                    });
                }
            });
        }

        view.findViewById(R.id.authButton).setOnClickListener(new AuthClickListener(
                ()->((TextView)view.findViewById(R.id.login)).getText().toString(),
                ()->((TextView)view.findViewById(R.id.password)).getText().toString(),
                source));
        return view;
    }


}