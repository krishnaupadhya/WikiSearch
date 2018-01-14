package com.shop.food.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by sapna on 13-01-2018.
 */


public class WikiList extends RealmObject {
    @SerializedName("query")
    private Query query;

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }
}
