package com.kartik.newsreader.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kartik.newsreader.R;
import com.kartik.newsreader.adapter.NewsAdapter;
import com.kartik.newsreader.data.NewsInfo;
import com.kartik.newsreader.data.PublicationInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.Predicate;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.os.AsyncTask.THREAD_POOL_EXECUTOR;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.my_toolbar) Toolbar toolbar;
    @BindView(R.id.swiperef) SwipeRefreshLayout refreshLayout;

    NewsAdapter newsAdapter;
    ArrayList<NewsInfo> newsInfoList = new ArrayList<>();
    NewsInfo newsInfo;
    URL url2;
    HttpURLConnection urlConnection;
    int counter = 0, asyncCounter = 0;
    SharedPreferences sharedPreferences;
    ArrayList<PublicationInfo> sources;
    HashMap<String, Boolean> pref;
    Point size;

    @SuppressLint("StaticFieldLeak")
    public class GetNews extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                url2 = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url2.openConnection();

                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                String result = "";
                int data = reader.read();
                char current;

                Log.i("Getting", "true");

                while(data != -1) {

                    current = (char) data;
                    result += current;
                    data = reader.read();

                }
                Log.i("JSON", result);
                return result;



            } catch (java.io.IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s == null) {
                Toast.makeText(MainActivity.this, "Couldn't fetch news", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    int i;
                    JSONObject news = new JSONObject(s);
                    JSONArray newsArray = news.getJSONArray("articles");
                    Log.i("Decoding", "Now!");
                    for (i = 0; i < newsArray.length(); i++) {
                        newsInfo = new NewsInfo();
                        final JSONObject newsObject = newsArray.getJSONObject(i);
                        newsInfo.setAuthor(newsObject.getString("author"));
                        newsInfo.setTitle(newsObject.getString("title"));
                        if(hasTitle(newsObject.getString("title"))) {
                            continue;
                        }
                        newsInfo.setSource(newsObject.getJSONObject("source").getString("name"));
                        newsInfo.setUrl(newsObject.getString("url"));
                        newsInfo.setThumbNailURL(newsObject.getString("urlToImage"));
                        newsInfo.setDesc(newsObject.getString("description"));
                        if (newsObject.getString("description").equals("null")) {
                            continue;
                        }
                        Log.i("newsInfo", newsInfo.toString());
                        newsInfoList.add(newsInfo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                asyncCounter++;
                if (asyncCounter == counter) {
                    Collections.shuffle(newsInfoList);
                    newsAdapter = new NewsAdapter(newsInfoList, MainActivity.this, size);
                    recyclerView.setAdapter(newsAdapter);
                    Log.i("Adapter", "Set!");
                    refreshLayout.setRefreshing(false);
                }

            }

        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        Display display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        refreshLayout.setOnRefreshListener(() -> {
            loadNews();
            refreshLayout.setRefreshing(true);
        });

        sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        String sourcesPrefJson = sharedPreferences.getString("sources_pref", null);
        String sourcesListJson = sharedPreferences.getString("sources_list", null);
        if(sourcesPrefJson == null) {
            Toast.makeText(getApplicationContext(), "Please select atleast 1 news source", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, PublicationSelectionActivity.class).putExtra("status", 0).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            MainActivity.this.finish();
        } else {

            pref = new Gson().fromJson(sourcesPrefJson, new TypeToken<HashMap<String, Boolean>>() {
            }.getType());
            sources = new Gson().fromJson(sourcesListJson, new TypeToken<ArrayList<PublicationInfo>>() {
            }.getType());

            if(pref != null) {
                loadNews();
            } else {
                Toast.makeText(getApplicationContext(), "Please select atleast 1 news source", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, PublicationSelectionActivity.class).putExtra("status", 0).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                MainActivity.this.finish();
            }
        }



    }

    private void loadNews() {
        int i;
        String sourcesId;
        refreshLayout.setRefreshing(true);
        for(i = 0; i < pref.size(); i++) {
            sourcesId = sources.get(i).id;
            if(pref.get(sourcesId)) {
                String url = "https://newsapi.org/v2/top-headlines?sources=" + sources.get(i).id + "&apiKey=" + getString(R.string.api_key);
                new GetNews()
                        .executeOnExecutor(THREAD_POOL_EXECUTOR, url);
                counter++;
                Log.i("Counter", counter + "");
                Log.i("URL", url);
            }
        }
    }

    private boolean hasTitle(final String title) {
        for(NewsInfo info: newsInfoList) {
            if(info.getTitle().contains(title)) {
                return true;
            }
        }
        return false;
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

            case R.id.select_publ:
                startActivity(new Intent(this, PublicationSelectionActivity.class).putExtra("status", 1));

            default:
                return false;

        }
    }


}
