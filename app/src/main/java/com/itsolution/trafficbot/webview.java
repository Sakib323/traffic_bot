package com.itsolution.trafficbot;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class webview extends AppCompatActivity {
    WebView webView;
    Boolean click=true;
    SharedPreferences click_cmd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        webView = findViewById(R.id.webview);

        webView.loadUrl("https://hindilinks4u.icu/");


        if(click==true){

            click_cmd=getSharedPreferences("click",MODE_PRIVATE);
            SharedPreferences.Editor editor=click_cmd.edit();
            editor.putBoolean("click",true);
            editor.apply();
            click=false;


        }

        webView.setWebViewClient(new WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onPageFinished(WebView view, String url) {
                Log.e("page has","finished loading");


            }


        });

    }
}