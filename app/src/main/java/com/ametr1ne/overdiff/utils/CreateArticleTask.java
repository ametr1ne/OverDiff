package com.ametr1ne.overdiff.utils;

import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.ametr1ne.overdiff.MainActivity;
import com.ametr1ne.overdiff.R;
import com.ametr1ne.overdiff.UserFactory;
import com.ametr1ne.overdiff.models.User;

import org.json.JSONObject;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.function.Consumer;

public class CreateArticleTask extends AsyncTask<Void, Void, Integer> {


    private MainActivity mainActivity;
    private String description;
    private String text;
    private File file;

    private Consumer<Integer> action;


    public CreateArticleTask(MainActivity mainActivity, File file, String description, String text, Consumer<Integer> action) {
        this.mainActivity = mainActivity;
        this.file = file;
        this.description = description;
        this.text = text;


        this.action = action;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        try {


            User user = UserFactory.getInstance().getCurrentUser();

            if(user.isAuthorization()) {
                String requestUrl = "http://"+GlobalProperties.KSITE_ADDRESS+"/api/createarticle?access_token=" + user.getAccessToken() +
                        "&description=" + description +
                        "&text=" + text;
                MultipartUtility multipart = new MultipartUtility(requestUrl, Charset.defaultCharset().name());

                multipart.addFilePart("icon", file);

                List<String> response = multipart.finish();

                StringBuilder stringJson = new StringBuilder();

                for (String s : response) {
                    stringJson.append(s);
                }

                JSONObject jsonObject = new JSONObject(stringJson.toString());
                return jsonObject.getInt("status");
            }
        } catch (Exception e) {
            e.printStackTrace();
            mainActivity.runOnUiThread(() -> {
                TextView viewById = (TextView) mainActivity.findViewById(R.id.new_article_logger);

                viewById.setText("ERROR: " + e.toString());
                viewById.setVisibility(View.VISIBLE);

            });
        }
        return ArticleStatus.TOKEN_DAMAGED;
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        action.accept(result);
    }

}