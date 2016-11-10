package com.laioffer.laiofferproject;


import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;

/**
 * Created by PaiGe on 2016/11/3.
 */

public class TwoStepOAuth extends DefaultApi10a {
    @Override
    public String getAccessTokenEndpoint() { return null; }


    @Override
    public String getAuthorizationUrl(Token unused) {
        return null;
    }


    @Override
    public String getRequestTokenEndpoint() {
        return null;
    }
}
