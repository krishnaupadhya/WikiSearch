package com.search.wiki.common.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.search.wiki.R;
import com.search.wiki.app.Constants;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;
    private String url;
    private String pageTitle;
    private Toolbar mToolBar;
    private RelativeLayout webProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        getIntentData();
        initToolBar();
        initData();

    }


    private void initData() {
        webView = (WebView) findViewById(R.id.webView);
        webProgressBar = findViewById(R.id.web_prog_bar);
        webView.setWebViewClient(new MyBrowser());

        if (!TextUtils.isEmpty(url)) {
            webView.loadUrl(url);
        }
    }

    private void initToolBar() {
        mToolBar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(pageTitle);
    }

    private void getIntentData() {
        if (getIntent() != null) {
            url = getIntent().getStringExtra(Constants.KEY_INTENT_WEB_URL);
            pageTitle = getIntent().getStringExtra(Constants.KEY_INTENT_PAGE_TITLE);
        }
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            webProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            webProgressBar.setVisibility(View.GONE);

        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            webProgressBar.setVisibility(View.GONE);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
