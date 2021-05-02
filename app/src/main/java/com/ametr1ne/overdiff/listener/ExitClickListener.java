package com.ametr1ne.overdiff.listener;

import android.view.View;

import com.ametr1ne.overdiff.MainActivity;
import com.ametr1ne.overdiff.R;
import com.ametr1ne.overdiff.UserFactory;
import com.ametr1ne.overdiff.fragments.AuthFragment;

public class ExitClickListener implements View.OnClickListener {

    private MainActivity source;

    public ExitClickListener(MainActivity mainActivity) {
        this.source = mainActivity;
    }

    @Override
    public void onClick(View v) {
        UserFactory.getInstance().exit();
        source.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new AuthFragment(source)).commit();
    }
}
