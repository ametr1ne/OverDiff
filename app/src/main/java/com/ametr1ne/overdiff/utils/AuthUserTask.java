package com.ametr1ne.overdiff.utils;

import android.os.AsyncTask;

import com.ametr1ne.overdiff.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Consumer;

public class AuthUserTask extends AsyncTask<Void, Void, User> {


    private String username;
    private String password;

    private Consumer<User> action;

    public AuthUserTask(String username, String password, Consumer<User> action) {
        this.username = username;
        this.password = password;
        this.action = action;
    }

    @Override
    protected User doInBackground(Void... voids) {


        try {
            URL url = new URL("http://10.0.2.2:8081/api/auth?login="+username+
                    "&password="+password);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {

                urlConnection.setRequestMethod("POST");

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

                inputStream.close();
                JSONObject jsonObject = new JSONObject(stringJson.toString());
                return  User.deserialize(jsonObject);


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
    protected void onPostExecute(User result) {
        super.onPostExecute(result);
        if(result!=null && result.isAuthorization()) {
            action.accept(result);
        }
    }

}
