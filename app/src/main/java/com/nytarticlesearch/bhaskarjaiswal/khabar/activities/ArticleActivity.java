package com.nytarticlesearch.bhaskarjaiswal.khabar.activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nytarticlesearch.bhaskarjaiswal.khabar.R;
import com.nytarticlesearch.bhaskarjaiswal.khabar.models.Article;

import org.parceler.Parcels;

public class ArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ADD8E6")));
        bar.setTitle(Html.fromHtml("<font color='#000066'>Khabar </font>"));

        setContentView(R.layout.activity_article);

        Article article = (Article) Parcels.unwrap(getIntent().getParcelableExtra("article"));
        String url = article.getWebUrl();
        WebView wvArticle = (WebView) findViewById(R.id.wvArticle);

        wvArticle.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                return super.shouldOverrideUrlLoading(view, url);
                view.loadUrl(url);
                return true;
            }

        });

        wvArticle.loadUrl(url);
    }
}
