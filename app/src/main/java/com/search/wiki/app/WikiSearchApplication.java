package com.search.wiki.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;


import com.search.wiki.utility.LogUtility;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class WikiSearchApplication extends Application {

    private final String TAG = WikiSearchApplication.class.getSimpleName();
    private static WikiSearchApplication instance;
    private RealmConfiguration realmConfigurationDNation;


    @Override
    public void onCreate() {
        super.onCreate();
        LogUtility.i(TAG, "onCreate");
        instance = this;

        initializeRealmData();
        MultiDex.install(this);

    }

    private void initializeRealmData() {
        Realm.init(this);
        realmConfigurationDNation = new RealmConfiguration.Builder()
                .name(Constants.REALM_TYPE_WIKI_PAGE)
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

}
