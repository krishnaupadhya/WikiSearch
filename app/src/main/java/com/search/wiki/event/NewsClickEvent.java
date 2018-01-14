package com.search.wiki.event;

import com.search.wiki.model.Pages;

public class NewsClickEvent {

    private Pages article;
    private int position;

    public NewsClickEvent(Pages info, int articlePosition) {
        this.article = info;
        this.position = articlePosition;
    }

    public Pages getArticle() {
        return article;
    }

}
