package com.search.wiki.home.viewmodel;

import android.databinding.ObservableField;
import android.util.Log;

import com.search.wiki.app.DatabaseController;
import com.search.wiki.common.viewmodel.BaseViewModel;
import com.search.wiki.home.listener.HomeListener;
import com.search.wiki.model.WikiList;
import com.search.wiki.network.WikiService;
import com.search.wiki.network.ServiceFactory;
import com.search.wiki.utility.LogUtility;
import com.search.wiki.utility.NetworkUtility;

import io.realm.Realm;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class HomeActivityViewModel extends BaseViewModel {
    private final HomeListener homeListener;
    public ObservableField<Boolean> isProgressRingVisible;
    private String TAG = HomeActivityViewModel.class.getSimpleName();
    public ObservableField<Boolean> isNewsListListVisible;

    public HomeActivityViewModel(HomeListener homeListener) {
        this.isProgressRingVisible = new ObservableField<>(false);
        this.homeListener = homeListener;
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

        WikiService service = ServiceFactory.createRetrofitService(WikiService.class, WikiService.SERVICE_ENDPOINT);
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
