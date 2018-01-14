package com.search.wiki.common.viewmodel;

import android.databinding.ObservableField;

public class CustomToastViewModel {
    public ObservableField<String> message;

    public CustomToastViewModel(String message) {
        this.message = new ObservableField<>(message);
    }


}
