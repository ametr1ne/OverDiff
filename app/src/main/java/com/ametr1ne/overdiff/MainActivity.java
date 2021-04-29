package com.ametr1ne.overdiff;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ametr1ne.overdiff.fragments.ArticleFragment;
import com.ametr1ne.overdiff.fragments.AuthFragment;
import com.ametr1ne.overdiff.fragments.ProfileFragment;
import com.ametr1ne.overdiff.fragments.TapesFragment;
import com.ametr1ne.overdiff.utils.ImageLoadTask;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new TapesFragment(this)).commit();


    }


    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.tapes:
                        selectedFragment = new TapesFragment(this);
                        break;
                    case R.id.profile:
                        selectedFragment = new ProfileFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();
                return true;
            };




    public void authClick(View view) {



        new Thread(() -> {

            try {
                URL url = new URL("http://10.0.2.2:8081/api/auth");

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();



                try {
                   /* urlConnection.setReadTimeout(15000   );
                    urlConnection.setConnectTimeout(15000   );*/

                    urlConnection.setRequestMethod("POST");

                    System.out.println("TEST::: "+urlConnection.getRequestMethod());

                  /*  urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);*/

                    /*urlConnection.setRequestProperty("login","undframe");
                    urlConnection.setRequestProperty("password","pass");*/


                    //urlConnection.getOutputStream().write("/api/auth".getBytes());

                    InputStream inputStream;



                    System.out.println(urlConnection.getRequestMethod());
                    System.out.println("URL: "+urlConnection.getURL().toString());

                    int status = urlConnection.getResponseCode();

                    if(status != HttpURLConnection.HTTP_OK)
                        inputStream = urlConnection.getErrorStream();
                    else
                        inputStream = urlConnection.getInputStream();

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));


                    String line;
                    StringBuilder stringJson = new StringBuilder();

                    while ((line = bufferedReader.readLine()) != null) {
                        stringJson.append(line);
                    }

                    System.out.println(stringJson.toString());

                    JSONObject jsonObject = new JSONObject(stringJson.toString());

                    System.out.println(jsonObject.toString());

                    runOnUiThread(() -> {
                        try {
                            ((TextView) findViewById(R.id.articleText)).setText(jsonObject.getString("text"));
                            ((TextView) findViewById(R.id.articleName)).setText(jsonObject.getString("description"));
                            ((TextView) findViewById(R.id.articleAutor)).setText(jsonObject.getString("author"));

                            System.out.println(jsonObject.getString("text"));

                            new ImageLoadTask(jsonObject.getString("icon")
                                    .replaceAll("localhost","10.0.2.2:8081")
                                    .replaceAll("\\/","/")
                                    , ((ImageView) findViewById(R.id.articleImage))).execute();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });


                } finally {
                    urlConnection.disconnect();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();

    }

    public void authButton(View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new AuthFragment()).commit();
    }
}