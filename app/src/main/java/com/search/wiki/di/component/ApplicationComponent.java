package com.search.wiki.di.component;

import android.app.Application;
import android.content.Context;

import com.search.wiki.app.WikiSearchApplication;
import com.search.wiki.data.DataManager;
import com.search.wiki.data.SharedPrefsHelper;
import com.search.wiki.di.module.ApplicationModule;
import com.search.wiki.di.scope.ApplicationContext;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(WikiSearchApplication urbanPiperApplication);

    @ApplicationContext
    Context getContext();

    Application getApplication();

    DataManager getDataManager();

    SharedPrefsHelper getPreferenceHelper();


}
