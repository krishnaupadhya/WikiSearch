package com.search.wiki.model;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sapna on 13-01-2018.
 */


public class WikiList extends RealmObject {

    @PrimaryKey
    private String id = UUID.randomUUID().toString();

    @SerializedName("query")
    private Query query;

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }
}
