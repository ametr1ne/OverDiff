package com.ametr1ne.overdiff.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.ametr1ne.overdiff.UserFactory;
import com.ametr1ne.overdiff.models.Article;
import com.ametr1ne.overdiff.models.User;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class CreateArticleTask extends AsyncTask<Void, Void, String> {

    String attachmentName;
    String attachmentFileName;
    String crlf = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";

    private String description;
    private String text;
    private Bitmap bitmap;

    private String path;
    private File file;


    public CreateArticleTask(File file, String path, Bitmap bitmap, String description, String text) {
        this.file = file;
        this.path = path;
        this.bitmap = bitmap;
        this.description = description;
        this.text = text;

        String[] split = path.split("/");

        String fileName = split[split.length - 1];

        this.attachmentName = fileName.split("\\.")[0];
        this.attachmentFileName = fileName;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {


            User user = UserFactory.getInstance().getCurrentUser().get();

            HttpURLConnection httpUrlConnection = null;
            String requestUrl = "http://10.0.2.2:8081/api/createarticle?access_token=" + user.getAccessToken() +
                    "&description=" + description +
                    "&text=" + text;
            URL url = new URL(requestUrl);
            String charset = "UTF-8";
            MultipartUtility multipart = new MultipartUtility(requestUrl, charset);

            multipart.addFilePart("icon", file);

            List<String> response = multipart.finish();

            StringBuilder stringJson = new StringBuilder();

            for (String s : response) {
                stringJson.append(s);
            }

            JSONObject jsonObject = new JSONObject(stringJson.toString());

            return stringJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        System.out.println("RESULT: " + result);

        //  imageView.setImageBitmap(result);
    }

}