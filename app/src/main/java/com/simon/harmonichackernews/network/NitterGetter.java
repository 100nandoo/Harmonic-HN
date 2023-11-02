package com.simon.harmonichackernews.network;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.simon.harmonichackernews.data.NitterInfo;
import com.simon.harmonichackernews.data.WikipediaInfo;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NitterGetter {

    public static boolean isValidNitterUrl(String url) {
        return url.contains("nitter.net");
    }

    public static void getInfo(WebView webView, Context ctx, NitterGetter.GetterCallback callback) {
        NitterInfo nitterInfo = new NitterInfo();

        webView.evaluateJavascript("(function() { " +
                "return JSON.stringify({" +
                "   text: document.querySelector('.tweet-content').textContent," +
                "   userName: document.querySelector('.fullname').textContent," +
                "   userTag: document.querySelector('.username').textContent," +
                "   date: document.querySelector('.tweet-date').textContent," +
                "   replyCount: document.querySelector('.icon-comment + *').textContent.trim()," +
                "   reposts: document.querySelector('.icon-retweet + *').textContent.trim()," +
                "   quotes: document.querySelector('.icon-quote + *').textContent.trim()," +
                "   likes: document.querySelector('.icon-heart + *').textContent.trim()" +
                "});" +
                "}) ();", resp -> {
            try {
                JSONObject jsonObject = new JSONObject(resp);
                nitterInfo.text = jsonObject.getString("text");
                nitterInfo.userName = jsonObject.getString("userName");
                nitterInfo.userTag = jsonObject.getString("userTag");
                nitterInfo.date = jsonObject.getString("date");
                nitterInfo.replyCount = Integer.parseInt(jsonObject.getString("replyCount"));
                nitterInfo.reposts = Integer.parseInt(jsonObject.getString("reposts"));
                nitterInfo.quotes = Integer.parseInt(jsonObject.getString("quotes"));
                nitterInfo.likes = Integer.parseInt(jsonObject.getString("likes"));

                callback.onSuccess(nitterInfo);
            } catch (Exception e) {
                e.printStackTrace();
                callback.onFailure("Failed at getting Nitter info");
            }
        });
    }

    public interface GetterCallback {
        void onSuccess(NitterInfo nitterInfo);
        void onFailure(String reason);
    }

}
