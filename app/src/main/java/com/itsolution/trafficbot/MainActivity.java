package com.itsolution.trafficbot;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.webkit.ProxyConfig;
import androidx.webkit.ProxyController;
import androidx.webkit.WebViewFeature;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
//import android.net.Proxy;
import android.graphics.Path;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.ArrayMap;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.android.volley.Response;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.URL;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.Executor;

import retrofit2.Retrofit;
import retrofit2.http.GET;

public class MainActivity extends AppCompatActivity {
    public WebView host,ip_site;
    public Map<String, String> extraHeaders;
    public String url,IP,PORT;
    public boolean paid=true,click=true;
    SharedPreferences click_cmd;
    public String api_url="https://list.didsoft.com/get?email=baddddddest@gmail.com&pass=bkswpu&pid=socks4100&showcountry=no";


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ip_site = findViewById(R.id.ipsite);
        host = findViewById(R.id.mainsite);


        if(paid==true){
            collect_paid_ip();
        }
        else {
            collect_free_ip_by_scraping();
        }

    }




    private void collect_paid_ip(){


        host.setVisibility(View.GONE);
        ip_site.setVisibility(View.VISIBLE);

        WebSettings webSettings = ip_site.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setUseWideViewPort(true);
        HashMap<Integer,String> paid_ip_list = new HashMap<Integer, String>();
        ip_site.loadUrl(api_url);

        ip_site.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                for(int i=0;i<=2000;i++){
                    int I = i;
                    SharedPreferences sharedPreferences=getSharedPreferences("paid_proxies",MODE_PRIVATE);
                    SharedPreferences.Editor edit=sharedPreferences.edit();
                    ip_site.evaluateJavascript("(function() {return document.body.innerText.split(\"\\n\")["+i+"]; })();",
                            new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String ip) {
                                    IP=ip.replace("\"", "");
                                    Log.e("IP IS ",IP);
                                    edit.putString(String.valueOf(I), IP);
                                    edit.apply();
                                }
                            });

                    CountDownTimer cnt=new CountDownTimer(10000,1000) {
                        @Override
                        public void onTick(long l) {

                        }
                        @Override
                        public void onFinish() {
                            if(I==2000){
                                setProxy(0);
                            }
                        }
                    }.start();


                }

            }
        });



    }



    private void collect_free_ip_by_scraping(){


        host.setVisibility(View.GONE);
        ip_site.setVisibility(View.VISIBLE);
        url = "https://free-proxy-list.net/#list";
        WebSettings webSettings = ip_site.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setUseWideViewPort(true);
        HashMap<Integer,String> ipList = new HashMap<Integer, String>();
        ip_site.loadUrl(url);


        ip_site.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                for(int i=0;i<=299;i++){
                    int I = i;
                    SharedPreferences sharedPreferences=getSharedPreferences("proxies",MODE_PRIVATE);
                    SharedPreferences.Editor edit=sharedPreferences.edit();
                    ip_site.evaluateJavascript("(function() {return document.getElementsByClassName('table table-striped table-bordered')[0].children[1].children["+I+"].children[0].innerText; })();",
                            new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String ip) {
                                    IP=ip.replace("\"", "");
                                    ipList.put(I,IP);
                                }
                            });

                    ip_site.evaluateJavascript("(function() {return document.getElementsByClassName('table table-striped table-bordered')[0].children[1].children["+I+"].children[1].innerText; })();",
                            new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String port) {
                                    PORT=port.replace("\"", "");
                                    IP=ipList.get(I);
                                    Log.e("ip",IP);
                                    edit.putString(String.valueOf(I),IP+":"+PORT);
                                    edit.apply();

                                }
                            });

                    CountDownTimer cnt=new CountDownTimer(10000,1000) {
                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {
                            if(I==299){
                                setProxy(0);
                            }
                        }
                    }.start();
                }

            }
        });

    }


    private void setProxy(int i) {

        SharedPreferences settings=getSharedPreferences("settings",MODE_PRIVATE);
        int coordinates=settings.getInt("coordinates",200);
        int sleep_time=settings.getInt("sleep_time",1);
        int cycle_time=settings.getInt("cycle_time",1000);
        int interval_time=settings.getInt("interval_time",1);


        int min_now=Calendar.getInstance().getTime().getMinutes();
        SharedPreferences sharedPreferences=getSharedPreferences("time",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        int time_old=sharedPreferences.getInt("time_old",0);

        if(Math.abs(time_old-min_now)==9 && paid==false){
            editor.putInt("time_old",min_now);
            editor.apply();
            collect_free_ip_by_scraping();
        }
        else{


            String proxy="";
            SharedPreferences sharedPreferences1 ;

            if(paid==true){
                sharedPreferences1=getSharedPreferences("paid_proxies",MODE_PRIVATE);
            }
            else{
                sharedPreferences1=getSharedPreferences("proxies",MODE_PRIVATE);
            }


            proxy=sharedPreferences1.getString(String.valueOf(i),"");
            Log.e("new proxy is"," "+proxy);
            if(WebViewFeature.isFeatureSupported(WebViewFeature.PROXY_OVERRIDE)){
                ProxyConfig proxyConfig = new ProxyConfig.Builder().addProxyRule(proxy).addDirect().build();
                ProxyController.getInstance().setProxyOverride(proxyConfig, new Executor() {
                    @Override
                    public void execute(Runnable command) {

                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
            else{
                Log.e("proxy error","going for new proxy");
                setProxy(i+1);
            }
            make_request_to_site(i);

        }
    }

    private void make_request_to_site(int i){
        String[] list_of_useragent ={
                "Mozilla/5.0 (X11; Linux i686) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.63 Safari/537.31"
                ,"Mozilla/5.0 (Linux; Android 12; SM-S906N Build/QP1A.190711.020; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/80.0.3987.119 Mobile Safari/537.36"
                ,"Mozilla/5.0 (Linux; Android 10; SM-G996U Build/QP1A.190711.020; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Mobile Safari/537.36"
                ,"Mozilla/5.0 (Linux; Android 10; SM-G980F Build/QP1A.190711.020; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/78.0.3904.96 Mobile Safari/537.36",
                "Mozilla/5.0 (Linux; Android 9; SM-G973U Build/PPR1.180610.011) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Mobile Safari/537.36",
                "Mozilla/5.0 (Linux; Android 10; Google Pixel 4 Build/QD1A.190821.014.C2; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/78.0.3904.108 Mobile Safari/537.36",
                "Mozilla/5.0 (Linux; Android 10; Google Pixel 4 Build/QD1A.190821.014.C2; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/78.0.3904.108 Mobile Safari/537.36",
                "Mozilla/5.0 (Linux; Android 9; J8110 Build/55.0.A.0.552; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/71.0.3578.99 Mobile Safari/537.36",
                "Mozilla/5.0 (Linux; Android 6.0; HTC One M9 Build/MRA58K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.98 Mobile Safari/537.3",
                "Mozilla/5.0 (iPhone14,3; U; CPU iPhone OS 15_0 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) Version/10.0 Mobile/19A346 Safari/602.1",
                "Mozilla/5.0 (iPhone13,2; U; CPU iPhone OS 14_0 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) Version/10.0 Mobile/15E148 Safari/602.1",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 12_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/69.0.3497.105 Mobile/15E148 Safari/605.1",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.34 (KHTML, like Gecko) Version/11.0 Mobile/15A5341f Safari/604.1",
                "Mozilla/5.0 (Linux; Android 12; SM-S906N Build/QP1A.190711.020; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/80.0.3987.119 Mobile Safari/537.36",
                "Mozilla/5.0 (Linux; Android 10; SM-G996U Build/QP1A.190711.020; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Mobile Safari/537.36",
                "Mozilla/5.0 (Linux; Android 10; SM-G980F Build/QP1A.190711.020; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/78.0.3904.96 Mobile Safari/537.36",
                "Mozilla/5.0 (Linux; Android 9; SM-G973U Build/PPR1.180610.011) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Mobile Safari/537.36",
                "Mozilla/5.0 (Linux; Android 8.0.0; SM-G960F Build/R16NW) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.84 Mobile Safari/537.36",
                "Mozilla/5.0 (Linux; Android 7.0; SM-G892A Build/NRD90M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/60.0.3112.107 Mobile Safari/537.36",
                "Mozilla/5.0 (Linux; Android 7.0; SM-G930VC Build/NRD90M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/58.0.3029.83 Mobile Safari/537.36",
                "Mozilla/5.0 (Linux; Android 6.0.1; SM-G935S Build/MMB29K; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36",
                "Mozilla/5.0 (Linux; Android 6.0.1; SM-G920V Build/MMB29K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.98 Mobile Safari/537.36",
                "Mozilla/5.0 (Linux; Android 5.1.1; SM-G928X Build/LMY47X) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.83 Mobile Safari/537.36",
                "Mozilla/5.0 (Linux; Android 12; Pixel 6 Build/SD1A.210817.023; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/94.0.4606.71 Mobile Safari/537.36",
                "Mozilla/5.0 (Linux; Android 11; Pixel 5 Build/RQ3A.210805.001.A1; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/92.0.4515.159 Mobile Safari/537.36"
                ,"Mozilla/5.0 (Linux; Android 10; Google Pixel 4 Build/QD1A.190821.014.C2; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/78.0.3904.108 Mobile Safari/537.36",
                "Mozilla/5.0 (Linux; Android 10; Google Pixel 4 Build/QD1A.190821.014.C2; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/78.0.3904.108 Mobile Safari/537.36",
                "Mozilla/5.0 (Linux; Android 8.0.0; Pixel 2 Build/OPD1.170811.002; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/59.0.3071.125 Mobile Safari/537.36",
                "Mozilla/5.0 (Linux; Android 7.1.1; Google Pixel Build/NMF26F; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/54.0.2840.85 Mobile Safari/537.36",
                "Mozilla/5.0 (Linux; Android 6.0.1; Nexus 6P Build/MMB29P) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.83 Mobile Safari/537.36",
                "Mozilla/5.0 (Linux; Android 9; J8110 Build/55.0.A.0.552; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/71.0.3578.99 Mobile Safari/537.36",
                "Mozilla/5.0 (Linux; Android 7.1.1; G8231 Build/41.2.A.0.219; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/59.0.3071.125 Mobile Safari/537.36",
                "Mozilla/5.0 (Linux; Android 6.0.1; E6653 Build/32.2.A.0.253) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.98 Mobile Safari/537.36",
                "Mozilla/5.0 (Linux; Android 10; HTC Desire 21 pro 5G) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.127 Mobile Safari/537.36",
                "Mozilla/5.0 (Linux; Android 10; Wildfire U20 5G) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.136 Mobile Safari/537.36",
                "Mozilla/5.0 (Linux; Android 6.0; HTC One X10 Build/MRA58K; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/61.0.3163.98 Mobile Safari/537.36",
                "Mozilla/5.0 (Linux; Android 6.0; HTC One M9 Build/MRA58K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.98 Mobile Safari/537.3",
                "Mozilla/5.0 (iPhone14,6; U; CPU iPhone OS 15_4 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) Version/10.0 Mobile/19E241 Safari/602.1",
                "Mozilla/5.0 (iPhone14,3; U; CPU iPhone OS 15_0 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) Version/10.0 Mobile/19A346 Safari/602.1",
                "Mozilla/5.0 (iPhone13,2; U; CPU iPhone OS 14_0 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) Version/10.0 Mobile/15E148 Safari/602.1",
                "Mozilla/5.0 (iPhone12,1; U; CPU iPhone OS 13_0 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) Version/10.0 Mobile/15E148 Safari/602.1",
                "Mozilla/5.0 (iPhone12,1; U; CPU iPhone OS 13_0 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) Version/10.0 Mobile/15E148 Safari/602.1",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 12_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.0 Mobile/15E148 Safari/604.1",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 12_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/69.0.3497.105 Mobile/15E148 Safari/605.1",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 12_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) FxiOS/13.2b11866 Mobile/16A366 Safari/605.1.15",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.34 (KHTML, like Gecko) Version/11.0 Mobile/15A5341f Safari/604.1",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A5370a Safari/604.1",
                "Mozilla/5.0 (iPhone9,3; U; CPU iPhone OS 10_0_1 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) Version/10.0 Mobile/14A403 Safari/602.1",
                "Mozilla/5.0 (iPhone9,4; U; CPU iPhone OS 10_0_1 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) Version/10.0 Mobile/14A403 Safari/602.1",
                "Mozilla/5.0 (Apple-iPhone7C2/1202.466; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543 Safari/419.3"
        };

        Random rndm=new Random();
        ip_site.setVisibility(View.GONE);
        host.setVisibility(View.VISIBLE);
        url = "https://hindilinks4u.icu/";
        host.clearCache(true);
        WebStorage.getInstance().deleteAllData();
        CookieManager.getInstance().removeAllCookies(null);
        CookieManager.getInstance().flush();
        host.clearFormData();
        host.clearHistory();
        host.clearSslPreferences();

        int index_for_ua=rndm.nextInt(list_of_useragent.length);

        WebSettings webSettings = host.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDomStorageEnabled(true);
        //webSettings.setUseWideViewPort(true);
        extraHeaders = new HashMap<String, String>();
        extraHeaders.put("User-Agent",list_of_useragent[index_for_ua]);

        webSettings.setUserAgentString(list_of_useragent[index_for_ua]);

        host.loadUrl(url,extraHeaders);

        host.setWebViewClient(new WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onPageFinished(WebView view, String url) {
                Log.e("number of request is",String.valueOf(i));
                //random_move(i);



                CountDownTimer demo=new CountDownTimer(60000,1000) {
                    @Override
                    public void onTick(long l) {



                        CountDownTimer demo=new CountDownTimer(30000,1000) {
                            @Override
                            public void onTick(long l) {

                            }

                            @RequiresApi(api = Build.VERSION_CODES.R)
                            @Override
                            public void onFinish() {


                                //edit here


                                if(click==true){
                                    click_cmd=getSharedPreferences("click",MODE_PRIVATE);
                                    SharedPreferences.Editor editor=click_cmd.edit();
                                    editor.putBoolean("click",true);
                                    editor.apply();
                                    click=false;
                                }


                                //till here

                            }
                        }.start();




                    }

                    @Override
                    public void onFinish() {
                        setProxy(i+1);
                    }
                }.start();


            }
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {

                Log.e("header is>", ""+request.getRequestHeaders());
                return null;
            }
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                webView.loadUrl(url,extraHeaders);
                return false;
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void random_move(int i){

        Random rndm=new Random();
        int sleep_for=rndm.nextInt(180000+60000)-60000;
        Log.e("Sleeping for",String.valueOf(Math.abs(sleep_for/1000))+" seconds");




        CountDownTimer sleep=new CountDownTimer(sleep_for,1000) {
            @Override
            public void onTick(long l) {
                for(int j=0;j<=(rndm.nextInt(1+4)-4);j++){
                    int finalJ = j;
                    CountDownTimer cnt=new CountDownTimer(rndm.nextInt(4000+10000)-10000,1000) {
                        @Override
                        public void onTick(long l) {

                        }
                        @Override
                        public void onFinish() {
                            Toast.makeText(MainActivity.this, "making a click", Toast.LENGTH_SHORT).show();
                            host.performContextClick(100,250);
                            host.performClick();
                            host.performContextClick();
                            host.performLongClick();
                            host.performLongClick(100,250);
                            //Log.e("clicked or not?",String.valueOf(click));
                            host.loadUrl(" jQuery(document.elementFromPoint("+100+", "+250+")).click();");
                        }
                    }.start();

                    CountDownTimer cnt1=new CountDownTimer(rndm.nextInt(4000+10000)-10000,1000) {
                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {
                            host.setScrollY(rndm.nextInt(1000+1300)-1300);
                            Log.e("making scroll nmbr:",String.valueOf(finalJ));
                        }
                    }.start();


                }
            }

            @Override
            public void onFinish() {
                setProxy(i+1);

            }
        }.start();



    }

}