package com.ametr1ne.overdiff.listener;

import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.ametr1ne.overdiff.MainActivity;
import com.ametr1ne.overdiff.R;
import com.ametr1ne.overdiff.UserFactory;
import com.ametr1ne.overdiff.fragments.ProfileFragment;
import com.ametr1ne.overdiff.utils.AuthStatus;

import java.util.function.Supplier;

public class AuthClickListener implements View.OnClickListener {

    private Supplier<String> username;
    private Supplier<String> password;

    private MainActivity source;


    public AuthClickListener(Supplier<String> username, Supplier<String> password, MainActivity source) {
        this.username = username;
        this.password = password;
        this.source = source;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {

        String userName = username.get();
        String password = this.password.get();

        TextView authErrorLogin = (TextView)source.findViewById(R.id.auth_error_login);
        authErrorLogin.setVisibility(View.INVISIBLE);
        TextView authErrorPassword = (TextView)source.findViewById(R.id.auth_error_password);
        authErrorPassword.setVisibility(View.INVISIBLE);
      /*  TextView authError = (TextView)source.findViewById(R.id.auth_error_info);
        authError.setVisibility(View.INVISIBLE);*/

/*
        TextView authError = (TextView)source.findViewById(R.id.auth_error);
        authError.setVisibility(View.VISIBLE);
        authError.setText("Неверный логин или пароль");
*/


        if(userName.isEmpty()){
            authErrorLogin.setVisibility(View.VISIBLE);
            authErrorLogin.setText("Пустой логин");
        }
        if(password.isEmpty()){
            authErrorPassword.setVisibility(View.VISIBLE);
            authErrorPassword.setText("Пустой пароль");
        }
        UserFactory.getInstance().authCurrentUser(userName, password, source.findViewById(R.id.auth_savepassword).isActivated(), user -> {
            if(user.getAuthStatus()!= AuthStatus.SUCCESSFUL_AUTHORIZATION) {
                TextView authError = (TextView)source.findViewById(R.id.auth_error_info);
                authError.setVisibility(View.VISIBLE);
            }else
            source.runOnUiThread(() -> {
                source.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment(source)).commit();
            });
        });
    }
}
