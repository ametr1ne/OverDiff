package com.ametr1ne.overdiff.utils;

import android.os.AsyncTask;

import com.ametr1ne.overdiff.UserFactory;
import com.ametr1ne.overdiff.models.User;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.function.Consumer;

public class RefreshTokenTask extends AsyncTask<Void, Void, User> {

    private User user;
    private Consumer<User> action;

    public RefreshTokenTask(User user,Consumer<User> action) {
        this.user = user;
        this.action = action;
    }

    @Override
    protected User doInBackground(Void... params) {
        try {
            HttpURLConnection httpUrlConnection = null;
            String requestUrl = "http://10.0.2.2:8081/api/refresh?id=" + user.getId() +
                    "&refresh_token=" + user.getRefreshToken();
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
        return null;
    }

    @Override
    protected void onPostExecute(User result) {
        super.onPostExecute(result);
        action.accept(result);
    }

}