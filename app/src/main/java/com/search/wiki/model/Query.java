package com.search.wiki.model;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by sapna on 13-01-2018.
 */

public class Query extends RealmObject {
    private RealmList<Pages> pages;

    public RealmList<Pages> getPages() {
        return pages;
    }

    public void setPages(RealmList<Pages> pages) {
        this.pages = pages;
    }
}
