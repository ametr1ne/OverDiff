package com.ametr1ne.overdiff.utils;

import android.os.AsyncTask;

import com.ametr1ne.overdiff.models.Article;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.function.Consumer;

public class EvaluationArticleTask extends AsyncTask<Void, Void, JSONObject> {

    private String accessToken;

    private Article article;
    private boolean like;

    private Consumer<JSONObject> action;

    public EvaluationArticleTask(String accessToken, Article article,boolean like, Consumer<JSONObject> action) {
        this.accessToken = accessToken;
        this.article = article;
        this.like = like;
        this.action = action;
    }

    @Override
    protected JSONObject doInBackground(Void... params) {
        try {
            HttpURLConnection httpUrlConnection = null;
            String requestUrl = "http://"+GlobalProperties.KSITE_ADDRESS+"/api/evaluation?access_token=" + accessToken    +
                    "&article_id=" + article.getId()+
                    "&is_like="+like;
            String charset = "UTF-8";
            MultipartUtility multipart = new MultipartUtility(requestUrl, charset);


            List<String> response = multipart.finish();

            StringBuilder stringJson = new StringBuilder();
            for (String s : response) {
                stringJson.append(s);
            }

            JSONObject jsonObject = new JSONObject(stringJson.toString());
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        super.onPostExecute(result);
        action.accept(result);

        try {
            int status = result.getInt("status");

            if(status == ArticleStatus.SUCCESSFULLY){
                int likes = result.getInt("likes");
                int dislikes = result.getInt("dislikes");

                article.setLikes(likes);
                article.setDislikes(dislikes);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}