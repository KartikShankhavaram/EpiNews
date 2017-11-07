package com.kartik.newsreader.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kartik.newsreader.R;
import com.kartik.newsreader.card_view.NewsAdapter;
import com.kartik.newsreader.card_view.NewsInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    NewsAdapter newsAdapter;
    ArrayList<NewsInfo> newsInfoList = new ArrayList<NewsInfo>();
    NewsInfo newsInfo;
    URL url1, url2;
    HttpURLConnection httpURLConnection, urlConnection;
    String textIDs;
    String[] storyIDs = new String[] {};
    int index = 0;
    Boolean transactionComplete = false;
    int listSize = 30;
    ProgressBar progressBar;
    int currentProgress = 0;

    @SuppressLint("StaticFieldLeak")
    public class GetNews extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                url2 = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url2.openConnection();

                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                publishProgress(++currentProgress);

                String result = "";
                int data = reader.read();
                char current;

                while(data != -1) {

                    current = (char) data;
                    result += current;
                    data = reader.read();

                }
                return result;



            } catch (java.io.IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                newsInfo = new NewsInfo();
                JSONObject newsJSON = new JSONObject(s);
                newsInfo.setTitle(newsJSON.getString("title"));
                newsInfo.setUrl(newsJSON.getString("url"));
                newsInfo.setAuthor(newsJSON.getString("by"));
                newsInfoList.add(index, newsInfo);
                index++;
                if(newsInfoList != null && newsInfoList.size() == listSize) {
                    findViewById(R.id.loadingPane1).setVisibility(View.GONE);
                    newsAdapter = new NewsAdapter(newsInfoList);
                    recyclerView.setAdapter(newsAdapter);
                    findViewById(R.id.recycler_view).setVisibility(View.VISIBLE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }
    }


    @SuppressLint("StaticFieldLeak")
    public class GetID extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {

            Looper.prepare();
            try {

                url1 = new URL(urls[0]);
                httpURLConnection = (HttpURLConnection) url1.openConnection();

                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);

                int data = reader.read();
                char current;

                while(data != -1) {

                    current = (char) data;
                    textIDs += current;
                    data = reader.read();

                }


                return textIDs;

            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Couldn't get the current news", Toast.LENGTH_SHORT).show();
                return null;
            }


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            if(s != null) {
                storyIDs = s.substring(s.indexOf("[") + 2, s.indexOf("]")).split(", ");
                Log.i("ID", storyIDs[0]);
                transactionComplete = true;
                progressBar.setIndeterminate(false);
                for(int i = 0; i < listSize; i++) {
                    int id = Integer.parseInt(storyIDs[i]);
                    new GetNews().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "https://hacker-news.firebaseio.com/v0/item/" + id + ".json?print=pretty");
                }

            }


        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*setTheme(R.style.SplashTheme);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Log.i("Splash", "Failed");
        }
        setTheme(R.style.AppTheme);*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.loadingPane1).setVisibility(View.VISIBLE);
        findViewById(R.id.splash).setVisibility(View.GONE);
        GetID getID = new GetID();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setVisibility(View.GONE);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgress(0);
        progressBar.setMax(listSize);
        progressBar.setIndeterminate(true);

        getID.execute("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case R.id.menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;

            default:
                return false;

        }
    }
}
