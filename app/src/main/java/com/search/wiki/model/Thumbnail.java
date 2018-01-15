package com.search.wiki.model;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sapna on 13-01-2018.
 */

public class Thumbnail extends RealmObject {
    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private String source;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
