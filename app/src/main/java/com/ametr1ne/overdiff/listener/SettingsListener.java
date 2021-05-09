package com.ametr1ne.overdiff.listener;

import android.view.View;

import com.ametr1ne.overdiff.MainActivity;
import com.ametr1ne.overdiff.R;
import com.ametr1ne.overdiff.settings.SettingFragment2;

public class SettingsListener implements View.OnClickListener {

    private MainActivity source;

    public SettingsListener(MainActivity source) {
        this.source = source;
    }

    @Override
    public void onClick(View v) {
        source.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new SettingFragment2(source)).addToBackStack("tag").commit();
    }
}
