package com.ametr1ne.overdiff.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ametr1ne.overdiff.MainActivity;
import com.ametr1ne.overdiff.R;
import com.ametr1ne.overdiff.UserFactory;

import java.util.Objects;


public class SettingFragment2 extends PreferenceFragmentCompat {


    private MainActivity source;

    public SettingFragment2() {
    }

    public SettingFragment2(MainActivity source) {
        this.source = source;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Preference button = findPreference(getString(R.string.apply_settings));
        if (button != null) {
            button.setOnPreferenceClickListener(preference -> {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
                String appTheme = prefs.getString("switch_theme", "Как в системе");
                if (appTheme.contains("Как в системе")) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    source.recreate();
                }
                if (appTheme.contains("Светлая")) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    source.recreate();
                }
                if (appTheme.contains("Темная")) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    source.recreate();
                }
                return true;

            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }








}