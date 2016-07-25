package xzheng2.cmu.edu.hw3.ViewActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import xzheng2.cmu.edu.hw3.R;

public class ViewImgActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_img);
        Intent intent = getIntent();
        String urlString = intent.getStringExtra("urlString");
//        textView.setText(urlString);
        WebView wv = (WebView) findViewById(R.id.webview);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(true);
        WebSettings webSettings = wv.getSettings();
        webSettings.setJavaScriptEnabled(true);
        wv.loadUrl(urlString);


    }
}
