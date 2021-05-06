package com.ametr1ne.overdiff.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Consumer;

public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {


    private String url;
    private ImageView imageView;
    private Consumer<Exception> exceptionHandler;

    public ImageLoadTask(String url, ImageView imageView) {
        this.url = url;
        this.imageView = imageView;
    }

    public ImageLoadTask exceptionHandler(Consumer<Exception> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            URL urlConnection = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlConnection
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            if(input!=null)
            return BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            if(exceptionHandler!=null)
                exceptionHandler.accept(e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        if (result != null)
            imageView.setImageBitmap(result);
    }

}