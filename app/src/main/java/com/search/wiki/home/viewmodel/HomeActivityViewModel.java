package com.search.wiki.home.viewmodel;

import android.databinding.ObservableField;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonObject;
import com.search.wiki.R;
import com.search.wiki.app.Constants;
import com.search.wiki.app.DatabaseController;
import com.search.wiki.app.WikiSearchApplication;
import com.search.wiki.common.viewmodel.BaseViewModel;
import com.search.wiki.home.listener.HomeListener;
import com.search.wiki.manager.SharedPreferenceManager;
import com.search.wiki.model.WikiList;
import com.search.wiki.network.WikiService;
import com.search.wiki.network.ServiceFactory;
import com.search.wiki.utility.LogUtility;
import com.search.wiki.utility.NetworkUtility;
import com.search.wiki.utility.StringUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class HomeActivityViewModel extends BaseViewModel {
    private final HomeListener homeListener;
    public ObservableField<Boolean> isProgressRingVisible;
    private String TAG = HomeActivityViewModel.class.getSimpleName();
    public ObservableField<Boolean> isNewsListListVisible;
    public ObservableField<String> recentSearchText;

    public HomeActivityViewModel(HomeListener homeListener) {
        this.isProgressRingVisible = new ObservableField<>(false);
        this.recentSearchText = new ObservableField<>(WikiSearchApplication.getInstance().getString(R.string.recent_search, SharedPreferenceManager.getRecentSearchKey()));
        this.homeListener = homeListener;
        this.isNewsListListVisible = new ObservableField<>(false);
    }

    public void setIsNewsListListVisible(Boolean isNewsListListVisible) {
        this.isNewsListListVisible.set(isNewsListListVisible);
    }

    public void setIsProgressRingVisible(Boolean isProgressRingVisible) {
        this.isProgressRingVisible.set(isProgressRingVisible);
    }


    public void setRecentSearchText(String recentSearchText) {
        this.recentSearchText.set(WikiSearchApplication.getInstance().getString(R.string.recent_search, recentSearchText));
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
                            SharedPreferenceManager.setRecentSearchKey(query);
                            setRecentSearchText(query);
                            homeListener.onSearchResultSuccess(response);
                            deleteRealmList();
                            saveSearchToRealmDb(response);
                        }
                        Log.e(TAG, "response");
                    }
                });
    }


    public void getPageDetails(String id) {

        if (NetworkUtility.isNetworkAvailable()) {
            setIsProgressRingVisible(true);
        } else {
            NetworkUtility.showNetworkError(getContext());
            return;
        }
        WikiService service = ServiceFactory.createRetrofitService(WikiService.class, WikiService.SERVICE_ENDPOINT);
        service.getPageDetails(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JsonObject>() {
                    @Override
                    public final void onCompleted() {
                        // do nothing
                        setIsProgressRingVisible(false);
                        Log.e(TAG, "onCompleted");
                    }

                    @Override
                    public final void onError(Throwable e) {
                        setIsProgressRingVisible(false);
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public final void onNext(JsonObject response) {
                        setIsProgressRingVisible(false);
                        if (response != null && !TextUtils.isEmpty(response.toString())) {
                            try {
                                JSONObject pageData = new JSONObject(response.toString());
                                Log.e(TAG, "response  " + response.toString());
                                JSONObject queryObject = pageData.getJSONObject(Constants.KEY_QUERY);
                                if (queryObject != null) {
                                    JSONObject pageObject = queryObject.getJSONObject(Constants.KEY_PAGES);
                                    if (pageObject != null) {
                                        JSONObject pageIdObject = pageObject.getJSONObject(id);
                                        if (pageIdObject != null) {
                                            String pageUrl = pageIdObject.getString(Constants.KEY_FULL_URL);
                                            if (!TextUtils.isEmpty(pageUrl))
                                                homeListener.onPageDetailsResultSuccess(pageUrl,pageIdObject.getString(Constants.KEY_TITLE));
                                        }
                                    }
                                }
                            } catch (JSONException e) {


                            }

                        }
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

    private void deleteRealmList() {
        final RealmResults<WikiList> results = DatabaseController.getInstance().getRealm().where(WikiList.class).findAll();
        if (results == null || results.size() == 0) return;
        DatabaseController.getInstance().getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                DatabaseController.getInstance().getRealm().delete(WikiList.class);
            }
        });
    }


}
