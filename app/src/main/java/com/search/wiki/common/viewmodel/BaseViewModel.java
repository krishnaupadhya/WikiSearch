package com.search.wiki.common.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;

import com.search.wiki.app.WikiSearchApplication;

public class BaseViewModel extends BaseObservable {

    protected Context getContext() {
        return WikiSearchApplication.getInstance().getApplicationContext();
    }



}
