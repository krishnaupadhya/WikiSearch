package com.shop.food.network;

import com.google.gson.JsonArray;
import com.shop.food.model.WikiList;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Krishna Upadhya on 9/11/2017.
 */

public interface HackerNewsService {
    String SERVICE_ENDPOINT = "https://en.wikipedia.org/w";

    @GET("/topstories.json?print=pretty")
    Observable<JsonArray> getTopStoriesNewsId();



    @GET("/api.php?action=opensearch&limit=8&namespace=0&format=json")
    Observable<JsonArray> getSearchSuggestions(@Query("search") String query);

    @GET("/api.php?action=query&format=json&prop=pageimages%7Cpageterms&generator=prefixsearch&redirects=1&formatversion=2&piprop=thumbnail&pithumbsize=50&pilimit=10&wbptterms=description&gpslimit=10")
    Observable<WikiList> getSearchResults(@Query("gpssearch") String query);

}
