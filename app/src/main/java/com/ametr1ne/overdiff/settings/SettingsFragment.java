package com.ametr1ne.overdiff.settings;


import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;
import com.ametr1ne.overdiff.R;


public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings);
    }
}
