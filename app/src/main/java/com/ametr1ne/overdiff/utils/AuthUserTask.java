package com.ametr1ne.overdiff.utils;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.ametr1ne.overdiff.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
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
            String requestUrl = "http://"+GlobalProperties.KSITE_ADDRESS+"/api/auth?login="+username+
                    "&password="+password+
                    "&device_id="+DeviceId.getDeviceId()+
                    "&service_id="+GlobalProperties.SERVICE_NAME;
            String charset = "UTF-8";
            MultipartUtility multipart = new MultipartUtility(requestUrl, charset);
            List<String> response = multipart.finish();

            StringBuilder stringJson = new StringBuilder();
            for (String s : response) {
                stringJson.append(s);
            }

            JSONObject jsonObject = new JSONObject(stringJson.toString());
            return User.deserialize(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return User.getInstance();

    }

    @Override
    protected void onPostExecute(User result) {
        super.onPostExecute(result);
        action.accept(result);
    }

}
