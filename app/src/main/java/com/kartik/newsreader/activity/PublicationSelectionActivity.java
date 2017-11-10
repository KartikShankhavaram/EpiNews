package com.kartik.newsreader.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kartik.newsreader.R;
import com.kartik.newsreader.adapter.ListviewAdapter;
import com.kartik.newsreader.data.PublicationInfo;
import com.kartik.newsreader.service.ConnectionService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PublicationSelectionActivity extends AppCompatActivity {

    @BindView(R.id.listView) ListView listView;
    @BindView(R.id.swref) SwipeRefreshLayout refreshLayout;
    @BindView(R.id.my_toolbar) Toolbar toolbar;

    PublicationInfo publicationInfo;
    ArrayList<PublicationInfo> publicationList;
    ListviewAdapter adapter;
    URL url1;
    HttpURLConnection httpURLConnection;
    ArrayList<Boolean> preList;
    SharedPreferences savedList;
    boolean setSaved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.NoActionBarTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publication_selection);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        savedList = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        Intent a = getIntent();
        if(a.getIntExtra("status", 0) == 1) {
            String preListString = savedList.getString("sources_pref", null);
            if(preListString != null) {
                preList = new Gson().fromJson(preListString, new TypeToken<ArrayList<Boolean>>(){}.getType());
            }
        }
        if(preList != null) {
            setSaved = true;
        }

        if(ConnectionService.getConnectionStatus(this)) {
            onRefreshAction();
        } else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(ConnectionService.getConnectionStatus(PublicationSelectionActivity.this)) {
                    onRefreshAction();
                } else {
                    Toast.makeText(PublicationSelectionActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                    refreshLayout.setRefreshing(false);
                }
            }
        });



    }

    @SuppressLint("StaticFieldLeak")
    public class GetPublication extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {

            try {

                url1 = new URL(urls[0]);
                httpURLConnection = (HttpURLConnection) url1.openConnection();

                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);

                String result = "";

                int data = reader.read();
                char current;

                while(data != -1) {

                    current = (char) data;
                    result += current;
                    data = reader.read();

                }


                return result;

            } catch (Exception e) {
                Toast.makeText(PublicationSelectionActivity.this, "Couldn't fetch publications.", Toast.LENGTH_SHORT).show();
                return null;
            }


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            String crappyPrefix = "null";

            if(s.startsWith(crappyPrefix)){
                s = s.substring(crappyPrefix.length(), s.length());
            }

            if(s != null) {
                try {
                    JSONArray sources = (new JSONObject(s)).getJSONArray("sources");
                    for(int i = 0; i < sources.length(); i++) {
                        publicationInfo = new PublicationInfo();
                        publicationInfo.id = sources.getJSONObject(i).getString("id");
                        publicationInfo.name = sources.getJSONObject(i).getString("name");
                        publicationInfo.url = sources.getJSONObject(i).getString("url");
                        publicationInfo.desc = sources.getJSONObject(i).getString("description");
                        publicationInfo.category = sources.getJSONObject(i).getString("category");
                        publicationList.add(i, publicationInfo);
                    }
                    Log.i("Array", publicationList.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter = new ListviewAdapter(PublicationSelectionActivity.this, R.layout.listview, publicationList, publicationList.size());
                if(setSaved) {
                    adapter.setSelectedList(preList);
                }
                listView.setAdapter(adapter);
                refreshLayout.setRefreshing(false);
            }

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void onRefreshAction() {
        GetPublication g = new GetPublication();
        publicationList = new ArrayList<>();
        if(adapter != null) {
            adapter.notifyDataSetChanged();
        }
        refreshLayout.setRefreshing(true);
        g.execute("https://newsapi.org/v1/sources?language=en");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sources_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int ID = item.getItemId();

        switch(ID) {
            case R.id.sources_menu_settings:
                startActivity(new Intent(PublicationSelectionActivity.this, SettingsActivity.class));
                break;

            case R.id.save_sources:
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                String sourceInfo = new Gson().toJson(publicationList);
                String sourcePref = new Gson().toJson(adapter.getSelectedList());
                this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE)
                        .edit()
                        .putString("sources_list", sourceInfo)
                        .putString("sources_pref", sourcePref)
                        .apply();
                startActivity(new Intent(this, MainActivity.class));
                break;

            default:
                return true;
        }

        return true;
    }
}
