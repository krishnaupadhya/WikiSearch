package com.shop.food.model;

import io.realm.RealmObject;

/**
 * Created by sapna on 13-01-2018.
 */

public class Thumbnail extends RealmObject {
    private String source;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
