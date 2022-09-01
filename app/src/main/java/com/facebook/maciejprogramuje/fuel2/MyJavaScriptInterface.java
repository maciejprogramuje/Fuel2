package com.facebook.maciejprogramuje.fuel2;

import android.util.Log;
import android.webkit.JavascriptInterface;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class MyJavaScriptInterface {
    @SuppressWarnings("unused")
    @JavascriptInterface
    public String processHTML(String html, String fuelType, String pattern) {
        Log.w("fuel33", html);

        if (!fuelType.equals("null") && !html.contains(fuelType)) {
            return null;
        }

        String result = null;

        Matcher m = Pattern.compile(pattern).matcher(html);
        while (m.find()) {
            result = m.group(0);
        }

        return result;
    }
}
