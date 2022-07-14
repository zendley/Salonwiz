package com.speckpro.salonwiz.ui.user;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.speckpro.salonwiz.R;

public class ContactUs extends AppCompatActivity {

    private ProgressBar progressBar;
    private static final int TIME_OUT = 3000;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        WebView webViewContact = new WebView(this);
        webViewContact.getSettings().setJavaScriptEnabled(true);
        final Activity activity = this;

        webViewContact.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }
            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }
        });

        new Handler().postDelayed(() -> {
            webViewContact.loadUrl("https://mysalonmanager.co.uk/contact-us/");
            setContentView(webViewContact);
        }, TIME_OUT);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}