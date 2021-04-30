package com.ametr1ne.overdiff.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ametr1ne.overdiff.MainActivity;
import com.ametr1ne.overdiff.R;
import com.ametr1ne.overdiff.listener.AuthClickListener;


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
        view.findViewById(R.id.authButton).setOnClickListener(new AuthClickListener(
                ()->((TextView)view.findViewById(R.id.login)).getText().toString(),
                ()->((TextView)view.findViewById(R.id.password)).getText().toString(),
                source));
        return view;
    }


}