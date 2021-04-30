package com.ametr1ne.overdiff.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ametr1ne.overdiff.ListAdapter;
import com.ametr1ne.overdiff.MainActivity;
import com.ametr1ne.overdiff.R;
import com.ametr1ne.overdiff.utils.ArticlesActionTask;

import java.util.Arrays;


public class TapesFragment extends Fragment {

    private MainActivity mainActivity;

    public TapesFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        new ArticlesActionTask(articles -> {
            mainActivity.runOnUiThread(() -> {
                ListAdapter listAdapter = new ListAdapter(mainActivity,Arrays.asList(articles));
            recyclerView.setAdapter(listAdapter);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            });
        }).execute();

        return view;
    }
}