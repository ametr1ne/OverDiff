package com.ametr1ne.overdiff.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.ametr1ne.overdiff.MainActivity;
import com.ametr1ne.overdiff.R;
import com.ametr1ne.overdiff.UserFactory;
import com.ametr1ne.overdiff.encryption.JWT;
import com.ametr1ne.overdiff.listener.AuthClickListener;
import com.ametr1ne.overdiff.listener.ExitClickListener;
import com.ametr1ne.overdiff.models.User;
import com.ametr1ne.overdiff.utils.AuthStatus;
import com.ametr1ne.overdiff.utils.RefreshTokenTask;

public class ProfileFragment extends Fragment {

    private MainActivity source;
    private User user;

    public ProfileFragment(MainActivity source) {
        this.source = source;
        this.user = UserFactory.getInstance().getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView dataView = (TextView) view.findViewById(R.id.profile_data);
        dataView.setVisibility(View.INVISIBLE);
        if (user!=null && user.getAccessToken()!=null && !JWT.isAlive(user.getAccessToken())) {
            UserFactory.getInstance().refreshCurrentUser(user1 -> {
                if (user1.getAuthStatus() != AuthStatus.SUCCESSFUL_AUTHORIZATION) {
                   source.openAuthorizationPage();
                }else{
                    loadView(view);
                }
            });
            return view;
        }

        loadView(view);
        view.findViewById(R.id.exit_button).setOnClickListener(new ExitClickListener(source));


        return view;
    }

    private void loadView(View view) {
        ((TextView) view.findViewById(R.id.user_name)).setText(user.getUsername());
        ((TextView) view.findViewById(R.id.user_email)).setText(user.getEmail());
    }
}