package com.facebook.maciejprogramuje.fuel2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    WebView webView;
    TextView auchanTextView;
    TextView leclercTextView;
    ProgressBar progressBar;
    SwipeRefreshLayout refreshLayout;

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //CookieManager.getInstance().removeAllCookies(null);
        //CookieManager.getInstance().flush();

        refreshLayout = findViewById(R.id.refresh_layout);

        auchanTextView = findViewById(R.id.auchan_price_textview);
        leclercTextView = findViewById(R.id.leclerc_price_textview);
        progressBar = findViewById(R.id.progress_bar);

        webView = findViewById(R.id.webview);
        webView.setDrawingCacheEnabled(true);
        webView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64☺; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36");

        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Shop auchan = new Shop(
                "Auchan",
                "https://www.auchan.pl/pl/sklepy/Auchan-Lublin-Al-Witosa",
                "#app > div > main > div > div.store-page-container > div > div:nth-child(3) > div > div > div > div.d-flex.justify-space-around.align-stretch.flex-wrap.col-md-8.col-12 > div:nth-child(3)",
                "B7",
                "\\d[,]\\d{2}&nbsp;zł"
        );

        Shop leclerc = new Shop(
                "Leclerc",
                "https://lublin-turystyczna.leclerc.pl/",
                "#main > div.petrol-station > div > div.col-md-8 > ul > li.on.b7",
                null,
                "\\d[.]\\d{2}"
        );

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                auchanTextView.setText("n/a");
                leclercTextView.setText("n/a");
                progressBar.setVisibility(View.VISIBLE);
                
                parsePage(auchan, auchanTextView, leclerc);
                refreshLayout.setRefreshing(false);
            }
        });

        parsePage(auchan, auchanTextView, leclerc);
    }

    void parsePage(Shop shop, TextView textView, Shop nextShop) {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onLoadResource(WebView view, String url) {
                webView.evaluateJavascript(shop.getJsSelector(), new ValueCallback<String>() {
                    @SuppressLint({"SetTextI18n", "SetJavaScriptEnabled"})
                    @Override
                    public void onReceiveValue(String s) {
                        if (!s.equals("null")) {
                            shop.setPrice(s);
                            textView.setText(shop.getShopName() + ": " + shop.getPrice() + " zł");

                            if (nextShop != null) {
                                parsePage(nextShop, leclercTextView, null);
                            } else {
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    }
                });
            }
        });

        webView.post(() -> webView.loadUrl(shop.getUrl()));
    }
}