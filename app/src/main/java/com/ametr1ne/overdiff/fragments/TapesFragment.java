package com.ametr1ne.overdiff.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ametr1ne.overdiff.MainActivity;
import com.ametr1ne.overdiff.R;
import com.ametr1ne.overdiff.utils.ImageLoadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class TapesFragment extends Fragment {


    private MainActivity mainActivity;

    public TapesFragment(MainActivity mainActivity) {
        // Required empty public constructor
        this.mainActivity = mainActivity;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tapes, container, false);
    }




    public void click(View view) {
        mainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new ArticleFragment()).commit();


        new Thread(() -> {

            try {
                URL url = new URL("http://10.0.2.2:8081/greeting?id=7d37dc41-814c-423b-ba1e-9cbcaa445657");

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {


                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());


                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));


                    String line;
                    StringBuilder stringJson = new StringBuilder();

                    while ((line = bufferedReader.readLine()) != null) {
                        stringJson.append(line);
                    }


                    JSONObject jsonObject = new JSONObject(stringJson.toString());

                    System.out.println(jsonObject.toString());

                    mainActivity.runOnUiThread(() -> {
                        try {
                            ((TextView) mainActivity.findViewById(R.id.articleText)).setText(jsonObject.getString("text"));
                            ((TextView) mainActivity.findViewById(R.id.articleName)).setText(jsonObject.getString("description"));
                            ((TextView) mainActivity.findViewById(R.id.articleAutor)).setText(jsonObject.getString("author"));

                            System.out.println(jsonObject.getString("text"));

                            new ImageLoadTask(jsonObject.getString("icon")
                                    .replaceAll("localhost","10.0.2.2:8081")
                                    .replaceAll("\\/","/")
                                    , ((ImageView) mainActivity.findViewById(R.id.articleImage))).execute();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });


                } finally {
                    urlConnection.disconnect();
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

        }).start();
    }

}