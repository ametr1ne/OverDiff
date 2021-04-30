package com.ametr1ne.overdiff.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ametr1ne.overdiff.MainActivity;
import com.ametr1ne.overdiff.R;
import com.ametr1ne.overdiff.utils.ArticleActionTask;
import com.ametr1ne.overdiff.utils.ImageLoadTask;

public class ArticleFragment extends Fragment {


    private MainActivity mainActivity;
    private String articleHash;

    public ArticleFragment(MainActivity mainActivity, String articleHash) {
        this.mainActivity = mainActivity;
        this.articleHash = articleHash;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_article, container, false);

        new ArticleActionTask(articleHash, article -> {
            mainActivity.runOnUiThread(() -> {
                ((TextView) mainActivity.findViewById(R.id.articleText)).setText(article.getText());
                ((TextView) mainActivity.findViewById(R.id.articleName)).setText(article.getDescription());
                new ImageLoadTask(article.getIcon()
                        , mainActivity.findViewById(R.id.articleIcon)).execute();
            });
        }).execute();

        return inflate;
    }
}