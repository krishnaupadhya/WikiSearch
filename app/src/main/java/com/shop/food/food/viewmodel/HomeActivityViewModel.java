package com.shop.food.food.viewmodel;

import android.app.Activity;
import android.databinding.ObservableField;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.shop.food.app.Constants;
import com.shop.food.app.DatabaseController;
import com.shop.food.common.viewmodel.BaseViewModel;
import com.shop.food.food.listener.HomeListener;
import com.shop.food.model.WikiList;
import com.shop.food.network.HackerNewsService;
import com.shop.food.network.ServiceFactory;
import com.shop.food.utility.LogUtility;
import com.shop.food.utility.NetworkUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Krishna Upadhya on 9/6/2017.
 */

public class HomeActivityViewModel extends BaseViewModel {
    private final HomeListener homeListener;
    public ObservableField<Boolean> isProgressRingVisible;
    private String TAG = HomeActivityViewModel.class.getSimpleName();
    private Activity mContext;
    public ObservableField<Boolean> isNewsListListVisible;

    public HomeActivityViewModel(HomeListener homeListener, Activity activity) {
        this.isProgressRingVisible = new ObservableField<>(false);
        this.homeListener = homeListener;
        this.mContext = activity;
        this.isNewsListListVisible = new ObservableField<>(false);
    }

    public void setIsNewsListListVisible(Boolean isNewsListListVisible) {
        this.isNewsListListVisible.set(isNewsListListVisible);
    }

    public void setIsProgressRingVisible(Boolean isProgressRingVisible) {
        this.isProgressRingVisible.set(isProgressRingVisible);
    }

    public void getSearchResults(String query) {

        if (NetworkUtility.isNetworkAvailable()) {
            setIsProgressRingVisible(true);
        } else {
            NetworkUtility.showNetworkError(getContext());
            return;
        }

        HackerNewsService service = ServiceFactory.createRetrofitService(HackerNewsService.class, HackerNewsService.SERVICE_ENDPOINT);
        service.getSearchResults(query)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WikiList>() {
                    @Override
                    public final void onCompleted() {
                        // do nothing
                        Log.e(TAG, "onCompleted");
                        setIsProgressRingVisible(false);
                    }

                    @Override
                    public final void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                        setIsProgressRingVisible(false);
                    }

                    @Override
                    public final void onNext(WikiList response) {
                        setIsProgressRingVisible(false);
                        if (response != null && response.getQuery() != null && response.getQuery().getPages() != null
                                && response.getQuery().getPages().size() > 0) {
                            homeListener.onSearchResultSuccess(response);
                            saveSearchToRealmDb(response);
                        }
                        Log.e(TAG, "response");
                    }
                });
    }








    private void saveSearchToRealmDb(WikiList searchResult) {
        //save  the response into realm database
        DatabaseController.getInstance().getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                try {
                    realm.copyToRealmOrUpdate(searchResult);

                } catch (Exception e) {
                    LogUtility.e(TAG, "Error getting news ");
                }
            }
        });
    }



}
