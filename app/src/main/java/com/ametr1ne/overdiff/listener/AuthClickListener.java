package com.ametr1ne.overdiff.listener;

import android.view.View;

import com.ametr1ne.overdiff.MainActivity;
import com.ametr1ne.overdiff.R;
import com.ametr1ne.overdiff.UserFactory;
import com.ametr1ne.overdiff.fragments.ProfileFragment;
import com.ametr1ne.overdiff.utils.AuthUserTask;

import java.util.function.Consumer;
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

    @Override
    public void onClick(View v) {
        UserFactory.getInstance().authCurrentUser(username.get(),password.get(),user -> {
            source.runOnUiThread(() -> {
                source.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
            });
        });
    }
}
