package com.unbounds.trakt;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.unbounds.trakt.api.model.request.Code;
import com.unbounds.trakt.api.model.response.Token;

import rx.functions.Action1;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.progress_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final WebView webView = new WebView(LoginActivity.this);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadUrl(String.format("https://api-v2launch.trakt.tv/oauth/authorize?response_type=code&client_id=%s&redirect_uri=%s",
                        BuildConfig.CLIENT_ID,
                        "unbounds-trakt://oauth"));
                setContentView(webView);
            }
        });
    }

    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        parseResponse(intent.getData());
    }

    private void parseResponse(final Uri uri) {
        if (uri != null && uri.toString().startsWith("unbounds-trakt")) {
            final String authCode = uri.getQueryParameter("code");
            final Code code = new Code(authCode);
            ApiWrapper.getToken(code).subscribe(new Action1<Token>() {
                @Override
                public void call(final Token token) {
                    Toast.makeText(LoginActivity.this, String.format("Token: %s", token.getAccessToken()), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
