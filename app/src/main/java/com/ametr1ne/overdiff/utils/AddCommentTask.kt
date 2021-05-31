package com.ametr1ne.overdiff.utils;

import android.os.AsyncTask;

import com.ametr1ne.overdiff.MainActivity;
import com.ametr1ne.overdiff.models.User;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.function.Consumer;

public class AddCommentTask extends AsyncTask<Void, Void, Integer> {

    private String accessToken;

    private long articleId;
    private String comment;

    private Consumer<Integer> action;

    public AddCommentTask(String accessToken, long articleId, String comment, Consumer<Integer> action) {
        this.accessToken = accessToken;
        this.articleId = articleId;
        this.comment = comment;
        this.action = action;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        try {
            HttpURLConnection httpUrlConnection = null;
            String requestUrl = "http://"+GlobalProperties.KSITE_ADDRESS+"/api/commentarticle?access_token=" + accessToken    +
                    "&article_id=" + articleId+
                    "&comment="+comment;
            String charset = "UTF-8";
            MultipartUtility multipart = new MultipartUtility(requestUrl, charset);


            List<String> response = multipart.finish();

            StringBuilder stringJson = new StringBuilder();
            for (String s : response) {
                stringJson.append(s);
            }

            JSONObject jsonObject = new JSONObject(stringJson.toString());
            return Integer.parseInt(jsonObject.getString("status"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        action.accept(result);
    }

}