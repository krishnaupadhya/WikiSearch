package com.search.wiki.model;

import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sapna on 13-01-2018.
 */

public class Query extends RealmObject {

    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private RealmList<Pages> pages;

    public RealmList<Pages> getPages() {
        return pages;
    }

    public void setPages(RealmList<Pages> pages) {
        this.pages = pages;
    }
}
