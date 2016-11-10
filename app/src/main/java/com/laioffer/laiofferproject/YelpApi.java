package com.laioffer.laiofferproject;

/**
 * Created by PaiGe on 2016/11/3.
 */

import android.util.Log;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

public class YelpApi {
    private static final String API_HOST = "api.yelp.com";
    private static final String SEARCH_PATH = "/v2/search";


    private static final String CONSUMER_KEY = "AgDmabsO1n6JDTffbd06Lg";
    private static final String CONSUMER_SECRET = "NR4hobTSABW4Rcaa8EGiLb2dFD0";
    private static final String TOKEN = "L2OhFZvKr3mHEtNWo3GvWoeO3y_fSHqP";
    private static final String TOKEN_SECRET = "vP1b47xDCs9l8r50ZW1cGZJjg1Q";


    private OAuthService service;
    private Token accessToken;


    /**
     * Setup the Yelp API OAuth credentials.
     */
    public YelpApi() {
        this.service = new ServiceBuilder().provider(TwoStepOAuth.class).apiKey(CONSUMER_KEY)
                .apiSecret(CONSUMER_SECRET).build();
        this.accessToken = new Token(TOKEN, TOKEN_SECRET);
    }


    /**
     * Fire a search request to Yelp API.
     */
    public String searchForBusinessesByLocation(String term, String location, int searchLimit) {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://" + API_HOST + SEARCH_PATH);
        request.addQuerystringParameter("term", term);
        request.addQuerystringParameter("location", location);
        request.addQuerystringParameter("limit", String.valueOf(searchLimit));
        this.service.signRequest(this.accessToken, request);
        Response response = request.send();
        Log.i("message", response.getBody());
        return response.getBody();
    }

}
