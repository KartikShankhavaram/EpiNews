package com.kartik.newsreader;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> titleList = new ArrayList<>();
    ArrayList<String> urlList = new ArrayList<>();
    ArrayList<String> htmlList = new ArrayList<>();
    URL url1, url2;
    HttpURLConnection httpURLConnection, urlConnection;
    String textIDs;
    String[] storyIDs = new String[] {};
    int index = 0;
    Boolean transactionComplete = false;
    int listSize = 100;
    ProgressBar progressBar;
    int currentProgress = 0;

    /*public class GetHTML extends AsyncTask<String, Void, String> {

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




        }
    }*/

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

                JSONObject newsJSON = new JSONObject(s);
                titleList.add(index, newsJSON.getString("title"));
                urlList.add(index, newsJSON.getString("url"));
                index++;
                Log.i("URLlist", urlList.toString());
                Log.i("Titles", titleList.toString());
                if(titleList != null && titleList.size() == listSize) {
                    findViewById(R.id.loadingPane1).setVisibility(View.GONE);
                    arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, titleList);
                    listView.setAdapter(arrayAdapter);
                    Log.i("URL", urlList.toString());
                    findViewById(R.id.listview).setVisibility(View.VISIBLE);
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listview);
        findViewById(R.id.listview).setVisibility(View.GONE);
        findViewById(R.id.loadingPane1).setVisibility(View.VISIBLE);
        GetID getID = new GetID();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setProgress(0);
        progressBar.setMax(listSize);
        progressBar.setIndeterminate(true);

        getID.execute("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent openWebpage = new Intent(MainActivity.this, WebViewActivity.class);
                openWebpage.putExtra("url", urlList.get(position));
                startActivity(openWebpage);
            }
        });








    }
}
