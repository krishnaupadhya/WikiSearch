package com.search.wiki.model;

import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sapna on 13-01-2018.
 */

public class Terms extends RealmObject {
    @PrimaryKey
    private String id = UUID.randomUUID().toString();
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
