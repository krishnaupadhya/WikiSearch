package com.shop.food.event;


import com.shop.food.model.Pages;

/**
 * Created by Krishna Upadhya on 9/9/2017.
 */

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
