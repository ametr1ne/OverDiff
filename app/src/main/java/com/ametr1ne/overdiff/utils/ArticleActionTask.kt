package com.ametr1ne.overdiff.utils;

import android.os.AsyncTask;

import com.ametr1ne.overdiff.MainActivity;
import com.ametr1ne.overdiff.models.Article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ArticleActionTask extends AsyncTask<Void, Void, Article> {

    private String articleHash;
    private Consumer<Article> action;

    public ArticleActionTask(String articleHash, Consumer<Article> action) {
        this.articleHash = articleHash;
        this.action = action;
    }

    @Override
    protected Article doInBackground(Void... voids) {


        try {
            URL url = new URL("http://"+GlobalProperties.KSITE_ADDRESS+"/api/article?id=" + articleHash);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream inputStream;
                int status = urlConnection.getResponseCode();

                if (status != HttpURLConnection.HTTP_OK)
                    inputStream = urlConnection.getErrorStream();
                else
                    inputStream = urlConnection.getInputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));


                String line;
                StringBuilder stringJson = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null) {
                    stringJson.append(line);
                }


                JSONObject jsonObject = new JSONObject(stringJson.toString());
                return  Article.deserialize(jsonObject);


            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(Article result) {
        super.onPostExecute(result);
        if(result!=null) {
            action.accept(result);
        }
    }

}
