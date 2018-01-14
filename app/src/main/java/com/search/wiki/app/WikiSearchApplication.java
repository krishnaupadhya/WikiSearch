package com.search.wiki.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.search.wiki.data.DataManager;
import com.search.wiki.di.component.ApplicationComponent;
import com.search.wiki.di.component.DaggerApplicationComponent;
import com.search.wiki.di.module.ApplicationModule;
import com.search.wiki.utility.LogUtility;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Supriya A on 1/2/2018.
 */
public class WikiSearchApplication extends Application {

    private final String TAG = WikiSearchApplication.class.getSimpleName();
    private static WikiSearchApplication instance;
    private RealmConfiguration realmConfigurationDNation;
    protected ApplicationComponent applicationComponent;

    @Inject
    DataManager dataManager;

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtility.i(TAG, "onCreate");
        instance = this;
        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        applicationComponent.inject(this);
        initializeRealmData();
        MultiDex.install(this);

    }

    private void initializeRealmData() {
        Realm.init(this);
        realmConfigurationDNation = new RealmConfiguration.Builder()
                .name(Constants.REALM_TYPE_HACKER_NEWS)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();

    }


    public static WikiSearchApplication getInstance() {
        return instance;
    }


    public void initialiseDefaultConfiguration(String country) {
        DatabaseController.resetRealm();
        Realm.removeDefaultConfiguration();
        Realm.setDefaultConfiguration(realmConfigurationDNation);
    }

    public static WikiSearchApplication get(Context context) {
        return (WikiSearchApplication) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        return applicationComponent;
    }
}
