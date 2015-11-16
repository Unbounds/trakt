package com.unbounds.trakt;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.unbounds.trakt.api.HttpRequest;
import com.unbounds.trakt.api.model.request.Code;
import com.unbounds.trakt.api.model.response.Token;

import rx.functions.Action1;

public class LoginActivity extends AppCompatActivity {

    public static Intent createIntent(final Activity activity) {
        return new Intent(activity, LoginActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final WebView webView = (WebView) findViewById(R.id.login_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(String.format("%s/oauth/authorize?response_type=code&client_id=%s&redirect_uri=%s",
                BuildConfig.BASE_URL,
                BuildConfig.CLIENT_ID,
                "unbounds-trakt://oauth"));
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
                    HttpRequest.HEADERS.put("Authorization", String.format("Bearer %s", token.getAccessToken()));
                    startActivity(MainActivity.createIntent(LoginActivity.this));
                }
            });
        }
    }
}
