package com.ametr1ne.overdiff.fragments;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ametr1ne.overdiff.MainActivity;
import com.ametr1ne.overdiff.R;
import com.ametr1ne.overdiff.UserFactory;
import com.ametr1ne.overdiff.models.Article;
import com.ametr1ne.overdiff.models.Comment;
import com.ametr1ne.overdiff.models.User;
import com.ametr1ne.overdiff.utils.AddCommentTask;
import com.ametr1ne.overdiff.utils.ArticleActionTask;
import com.ametr1ne.overdiff.utils.ArticleStatus;
import com.ametr1ne.overdiff.utils.EvaluationArticleTask;
import com.ametr1ne.overdiff.utils.ImageLoadTask;

import org.json.JSONException;

public class ArticleFragment extends Fragment {


    private MainActivity source;
    private String articleHash;
    private ImageButton like_button;
    private ImageButton dislike_button;

    public ArticleFragment(MainActivity mainActivity, String articleHash) {
        this.source = mainActivity;
        this.articleHash = articleHash;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_article, container, false);

        User currentUser = UserFactory.getInstance().getCurrentUser();
        new ArticleActionTask(articleHash, article -> {
            source.runOnUiThread(() -> {
                ((TextView) source.findViewById(R.id.articleText)).setText(article.getText());
                ((TextView) source.findViewById(R.id.articleName)).setText(article.getDescription());
                new ImageLoadTask(article.getIcon()
                        , source.findViewById(R.id.articleIcon)).execute();

                source.findViewById(R.id.article_newcomment).setVisibility(currentUser.isAuthorization() ? View.VISIBLE : View.INVISIBLE);

                ImageButton button = source.findViewById(R.id.new_comment_button);

                button.setOnClickListener(v -> {
                    TextView textView = (TextView) source.findViewById(R.id.new_comment_textview);

                    if (currentUser.isAuthorization())
                        new AddCommentTask(currentUser.getAccessToken(), article.getId(), textView.getText().toString(), integer -> {
                            source.runOnUiThread(() -> {
                                source.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                        new ArticleFragment(source, article.getHash())).commit();
                            });
                        }).execute();

                });

                ((TextView) source.findViewById(R.id.article_like_view)).setText(Integer.valueOf(article.getLikes()).toString());
                ((TextView) source.findViewById(R.id.article_dislikes_view)).setText(Integer.valueOf(article.getDislikes()).toString());


                LinearLayout layout = source.findViewById(R.id.comment_layout);

                for (Comment comment : article.getComment()) {
                    View view = LayoutInflater.from(source).inflate(R.layout.preview_comment, layout, false);
                    TextView text = view.findViewById(R.id.comment_title);
                    text.setText(comment.getComment());
                    layout.addView(view);
                }

            });



            source.findViewById(R.id.article_like_button).setOnClickListener(v -> {
                evaluationArticle(currentUser, article, true);
            });
            source.findViewById(R.id.article_dislike_button).setOnClickListener(v -> {
                evaluationArticle(currentUser, article, false);
            });



        }).execute();



        return inflate;
    }

    public void evaluationArticle(User currentUser, Article article,boolean like){
        if(currentUser.isAuthorization()) {
            new EvaluationArticleTask(currentUser.getAccessToken(), article, like, jsonObject -> {
                try {
                    int status = jsonObject.getInt("status");
                    if (status == ArticleStatus.SUCCESSFULLY) {

                        int likes = jsonObject.getInt("likes");
                        int dislikes = jsonObject.getInt("dislikes");

                        source.runOnUiThread(() -> {
                            ((TextView) source.findViewById(R.id.article_like_view)).setText(likes + "");
                            ((TextView) source.findViewById(R.id.article_dislikes_view)).setText(dislikes + "");

                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }).execute();
        }
    }

}