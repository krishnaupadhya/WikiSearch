package com.search.wiki.event;

import com.search.wiki.model.Pages;

public class PageItemClickEvent {

    private Pages pages;
    private int position;

    public PageItemClickEvent(Pages info, int articlePosition) {
        this.pages = info;
        this.position = articlePosition;
    }

    public Pages getPages() {
        return pages;
    }

}
