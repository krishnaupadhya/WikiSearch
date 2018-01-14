package com.shop.food.event;

/**
 * Created by sapna on 14-01-2018.
 */

public class SearchSuggestionClicked {
    private String query;

    public SearchSuggestionClicked(String query) {
        this.query = query;

    }
    public String getQuery() {
        return this.query;
    }
}
