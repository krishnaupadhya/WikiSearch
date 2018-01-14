package com.search.wiki.model;

import io.realm.RealmObject;

/**
 * Created by sapna on 13-01-2018.
 */

public class Pages extends RealmObject {
    private String title;

    private Thumbnail thumbnail;

    private Terms terms;

    private String pageid;

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public Thumbnail getThumbnail ()
    {
        return thumbnail;
    }

    public void setThumbnail (Thumbnail thumbnail)
    {
        this.thumbnail = thumbnail;
    }

    public Terms getTerms ()
    {
        return terms;
    }

    public void setTerms (Terms terms)
    {
        this.terms = terms;
    }

    public String getPageid ()
    {
        return pageid;
    }

    public void setPageid (String pageid)
    {
        this.pageid = pageid;
    }

}
