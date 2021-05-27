package com.ametr1ne.overdiff.utils;

import android.os.AsyncTask;

import com.ametr1ne.overdiff.models.User;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.function.Consumer;

public class RefreshTokenTask extends AsyncTask<Void, Void, User> {

    private String refreshToken;
    private long userId;
    private Consumer<User> action;

    public RefreshTokenTask(String getRefreshToken, long userId, Consumer<User> action) {
        this.refreshToken = getRefreshToken;
        this.userId = userId;
        this.action = action;
    }

    @Override
    protected User doInBackground(Void... params) {
        try {
            String requestUrl = "http://"+GlobalProperties.KSITE_ADDRESS+"/api/refresh?id=" + userId +
                    "&device_id=1"+
                    "&refresh_token=" + refreshToken;
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