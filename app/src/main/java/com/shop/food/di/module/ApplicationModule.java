package com.shop.food.di.module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.shop.food.di.scope.ApplicationContext;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Supriya A on 1/2/2018.
 */

@Module
public class ApplicationModule {

    private final Application mApplication;

    public ApplicationModule(Application app) {
        mApplication = app;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    SharedPreferences provideSharedPrefs() {
        return mApplication.getSharedPreferences("demo-prefs", Context.MODE_PRIVATE);
    }
}
