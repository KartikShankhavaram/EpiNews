package com.kartik.newsreader.activity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.kartik.newsreader.R;
import com.kartik.newsreader.adapter.ListviewAdapter;
import com.kartik.newsreader.data.PublicationInfo;

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

    PublicationInfo publicationInfo;
    ArrayList<PublicationInfo> publicationList = new ArrayList<>();
    ListviewAdapter adapter;

    URL url1;
    HttpURLConnection httpURLConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publication_selection);

        ButterKnife.bind(this);

        GetPublication getPublication = new GetPublication();
        getPublication.execute("https://newsapi.org/v1/sources?language=en");



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

                adapter = new ListviewAdapter(PublicationSelectionActivity.this, R.layout.listview, publicationList, 60);
                listView.setAdapter(adapter);
            }


        }

    }
}
