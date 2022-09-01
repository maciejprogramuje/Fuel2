package com.facebook.maciejprogramuje.fuel2;


import android.annotation.SuppressLint;
import android.util.Log;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Shop {
    private final String url;
    private final String shopName;
    private final String fuelType;
    private String price;
    private String jsSelector;
    private String pattern;

    public Shop(String shopName, String url, String jsSelector, String fuelType, String pattern) {
        this.url = url;
        this.fuelType = fuelType;
        this.shopName = shopName;
        setPattern(pattern);
        setPrice("n/a");
        setJsSelector(jsSelector);
    }

    public void setJsSelector(String mJsSelector) {
        mJsSelector = "javascript:HTMLOUT.processHTML("
                + "'<html>'+document.querySelector('" + mJsSelector + "').innerHTML+'</html>',"
                + "'" + getFuelType() + "',"
                + "'" + getPattern() + "'"
                + ");";

        this.jsSelector = mJsSelector;
    }

    public void setPrice(String mPrice) {
        this.price = mPrice;

        Matcher m = Pattern.compile("\\d[,]\\d{2}").matcher(mPrice.replaceAll("\\.", ","));
        while (m.find()) {
            price = m.group(0);
        }
    }

    public String getUrl() {
        return url;
    }

    public String getJsSelector() {
        return jsSelector;
    }

    public String getShopName() {
        return shopName;
    }

    public String getPrice() {
        return price;
    }

    public String getFuelType() {
        return fuelType;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String mPattern) {
        this.pattern = mPattern.replaceAll("\\\\", "\\\\\\\\");

    }
}
