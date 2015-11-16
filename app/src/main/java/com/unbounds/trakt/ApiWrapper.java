package com.unbounds.trakt;

import com.unbounds.trakt.api.HttpRequest;
import com.unbounds.trakt.api.RxRequest;
import com.unbounds.trakt.api.model.request.Code;
import com.unbounds.trakt.api.model.response.Token;

import rx.Observable;

/**
 * Created by maclir on 11/7/15.
 */
public class ApiWrapper {
    public static Observable<Token> getToken(final Code code) {
        return new RxRequest() {
            @Override
            protected HttpRequest request() {
                return new HttpRequest("%s/oauth/token", BuildConfig.BASE_URL).post(code);
            }
        }.asObservable(Token.class);
    }
}
