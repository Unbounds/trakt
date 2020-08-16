package com.unbounds.trakt.login;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.customtabs.CustomTabsServiceConnection;
import androidx.appcompat.app.AppCompatActivity;

import com.unbounds.trakt.ApiWrapper;
import com.unbounds.trakt.BuildConfig;
import com.unbounds.trakt.R;
import com.unbounds.trakt.api.model.request.Code;
import com.unbounds.trakt.api.model.response.Token;

import rx.functions.Action1;

public class LoginActivity extends AppCompatActivity {

    public static Intent createIntent(final Activity activity) {
        return new Intent(activity, LoginActivity.class);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final CustomTabsServiceConnection connection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(final ComponentName componentName, final CustomTabsClient client) {
                final CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                final CustomTabsIntent customTabsIntent = builder.build();
                client.warmup(0L); // This prevents backgrounding after redirection
                customTabsIntent.launchUrl(LoginActivity.this, Uri.parse(String.format("%s/oauth/authorize?response_type=code&client_id=%s&redirect_uri=%s",
                        BuildConfig.BASE_URL,
                        BuildConfig.CLIENT_ID,
                        getString(R.string.oauth_referrer))));
            }

            @Override
            public void onServiceDisconnected(final ComponentName name) {
            }
        };
        CustomTabsClient.bindCustomTabsService(this, "com.android.chrome", connection);
    }

    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        parseResponse(intent.getData());
    }

    private void parseResponse(final Uri uri) {
        if (uri != null && uri.toString().startsWith(getString(R.string.oauth_referrer))) {
            final String authCode = uri.getQueryParameter("code");
            final Code code = new Code(authCode, getString(R.string.oauth_referrer));
            ApiWrapper.getToken(code).subscribe(new Action1<Token>() {
                @Override
                public void call(final Token token) {
                    LoginManager.getInstance().setToken(token);
                    setResult(RESULT_OK);
                    finish();
                }
            });
        }
    }
}
