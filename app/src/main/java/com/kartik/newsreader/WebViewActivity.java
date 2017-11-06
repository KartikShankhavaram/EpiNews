package com.kartik.newsreader;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.MalformedURLException;
import java.net.URL;

public class WebViewActivity extends AppCompatActivity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = (WebView) findViewById(R.id.webview);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportMultipleWindows(true);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                findViewById(R.id.progressBar1).setVisibility(View.VISIBLE);
                findViewById(R.id.webview).setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                findViewById(R.id.progressBar1).setVisibility(View.GONE);

                findViewById(R.id.webview).setVisibility(View.VISIBLE);
            }
        });



        Intent page = getIntent();

        String url = page.getStringExtra("url");
        Log.i("URL", url);
        webView.loadUrl(url);


    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
