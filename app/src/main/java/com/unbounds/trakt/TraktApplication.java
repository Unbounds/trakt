package com.unbounds.trakt;

import android.app.Application;

import com.squareup.okhttp.MediaType;
import com.unbounds.trakt.api.HttpRequest;

/**
 * Created by maclir on 2015-11-16.
 */
public class TraktApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        HttpRequest.HEADERS.put("Content-type", MediaType.parse("application/json").toString());
        HttpRequest.HEADERS.put("trakt-api-key", BuildConfig.CLIENT_ID);
        HttpRequest.HEADERS.put("trakt-api-version", "2");
    }
}
