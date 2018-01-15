package com.search.wiki.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.search.wiki.model.WikiList;

import org.json.JSONObject;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Krishna Upadhya on 9/11/2017.
 */

public interface WikiService {
    String SERVICE_ENDPOINT = "https://en.wikipedia.org/w";

    @GET("/api.php?action=opensearch&limit=8&namespace=0&format=json")
    Observable<JsonArray> getSearchSuggestions(@Query("search") String query);

    @GET("/api.php?action=query&format=json&prop=pageimages%7Cpageterms&generator=prefixsearch&redirects=1&formatversion=2&piprop=thumbnail&pithumbsize=50&pilimit=10&wbptterms=description&gpslimit=10")
    Observable<WikiList> getSearchResults(@Query("gpssearch") String query);

    @GET("/api.php?action=query&prop=info&inprop=url&format=json")
    Observable<JsonObject> getPageDetails(@Query("pageids") String pageId);

}
