package com.search.wiki.model;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by sapna on 13-01-2018.
 */

public class Terms extends RealmObject {
    private RealmList<String> description;

    public RealmList<String> getDescription ()
    {
        return description;
    }

    public void setDescription (RealmList<String> description)
    {
        this.description = description;
    }

}
