package com.search.wiki.event;

public class SearchSuggestionClicked {
    private String query;

    public SearchSuggestionClicked(String query) {
        this.query = query;

    }
    public String getQuery() {
        return this.query;
    }
}
