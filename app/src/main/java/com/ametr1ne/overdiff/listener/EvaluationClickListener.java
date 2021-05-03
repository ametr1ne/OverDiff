package com.ametr1ne.overdiff.listener;

import android.view.View;

import com.ametr1ne.overdiff.UserFactory;
import com.ametr1ne.overdiff.models.Article;
import com.ametr1ne.overdiff.models.User;
import com.ametr1ne.overdiff.utils.EvaluationArticleTask;

import org.json.JSONObject;

import java.util.function.Consumer;

public class EvaluationClickListener implements View.OnClickListener {

    private boolean like;
    private Article article;
    private User user;

    private Consumer<JSONObject> action;

    public EvaluationClickListener(boolean like, Article article, User user, Consumer<JSONObject> action) {
        this.like = like;
        this.article = article;
        this.user = user;
        this.action = action;
    }

    @Override
    public void onClick(View v) {
        if(user.isAuthorization()){
            new EvaluationArticleTask(user.getAccessToken(), article, like, action);
        }

    }
}
