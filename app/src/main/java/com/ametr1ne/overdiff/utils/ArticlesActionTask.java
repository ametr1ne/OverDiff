package com.ametr1ne.overdiff.utils;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

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
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ArticlesActionTask extends AsyncTask<Void, Void, Article[]> {


    private Consumer<Article[]> action;

    public ArticlesActionTask(Consumer<Article[]> action) {
        this.action = action;
    }

    @Override
    protected Article[] doInBackground(Void... voids) {

        List<Article> articleList = new ArrayList<>();

        try {
            URL url = new URL("http://10.0.2.2:8081/api/articles");
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


                JSONArray jsonArray = jsonObject.getJSONArray("articles");

                for (int i = 0; !jsonArray.isNull(i); i++) {

                    JSONObject obj = jsonArray.getJSONObject(i);
                    articleList.add(Article.deserialize(obj));

                }


            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


        return articleList.toArray(new Article[0]);
    }

    @Override
    protected void onPostExecute(Article[] result) {
        super.onPostExecute(result);
        action.accept(result);
    }

}
