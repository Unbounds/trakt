package com.unbounds.trakt.api.model.request;

import com.unbounds.trakt.BuildConfig;

/**
 * Created by maclir on 2015-11-08.
 */
public class Code {
    private final String code;
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final String grantType;

    public Code(String code) {
        this.code = code;
        this.clientId = BuildConfig.CLIENT_ID;
        this.clientSecret = BuildConfig.CLIENT_SECRET;
        this.redirectUri = "unbounds-trakt://oauth";
        this.grantType = "authorization_code";
    }
}
