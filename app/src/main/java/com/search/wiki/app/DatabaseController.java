package com.search.wiki.app;


import android.app.Activity;
import android.app.Application;
import android.app.Fragment;

import com.search.wiki.model.Pages;
import com.search.wiki.model.WikiList;

import java.util.Hashtable;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmResults;

public class DatabaseController {
    private static DatabaseController instance;
    private Realm realm = null;
    // shared cache for primary keys
    private static Hashtable<Class<? extends RealmModel>, String> primaryKeyMap = new Hashtable<>();

    public DatabaseController(Application application) {
        try {
            if (Realm.getDefaultInstance() != null) {
                realm = Realm.getDefaultInstance();
            } else {
                realm = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DatabaseController with(Fragment fragment) {

        if (instance == null) {
            instance = new DatabaseController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static DatabaseController with(Activity activity) {

        if (instance == null) {
            instance = new DatabaseController(activity.getApplication());
        }
        return instance;
    }

    public static DatabaseController with(Application application) {

        if (instance == null) {
            instance = new DatabaseController(application);
        }
        return instance;
    }

    public static DatabaseController getInstance() {
        if (instance == null) {
            instance = new DatabaseController(WikiSearchApplication.getInstance());
        }
        return instance;
    }

    public Realm getRealm() {

        return realm;
    }


    public static void resetRealm() {
        if (instance != null) {
            instance = null;
        }
    }


    public RealmResults<WikiList> getPagesFromDb() {
        return realm.where(WikiList.class).findAll();

    }
}
