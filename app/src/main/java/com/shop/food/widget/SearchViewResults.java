package com.shop.food.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.shop.food.interfaces.onSearchActionsListener;
import com.shop.food.network.HackerNewsService;
import com.shop.food.network.ServiceFactory;
import com.shop.food.utility.NetworkUtility;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by shahroz on 1/8/2016.
 */
public class SearchViewResults implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {
    private static final int TRIGGER_SEARCH = 1;
    private static final long SEARCH_TRIGGER_DELAY_IN_MS = 400;
    private ListView mListView;
    private String sequence;
    private int mPage;
    private Handler mHandler;
    private boolean isLoadMore;
    private ArrayAdapter mAdapter;
    private onSearchActionsListener mListener;
    private ArrayList<String> searchList;
    private String TAG = SearchViewResults.class.getSimpleName();
    /*
    * Used Handler in case implement Search remotely
    * */

    public SearchViewResults(Context context, String searchQuery) {
        sequence = searchQuery;
        searchList = new ArrayList<>();
        mAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, searchList);
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_SEARCH) {
                    clearAdapter();
                    String sequence = (String) msg.obj;
                    // mSearch.execute(sequence);
                    getSearchSuggestions(sequence);
                }
                return false;
            }
        });
    }

    public void getSearchSuggestions(String query) {

        if (NetworkUtility.isNetworkAvailable()) {
        } else {
            return;
        }

        HackerNewsService service = ServiceFactory.createRetrofitService(HackerNewsService.class, HackerNewsService.SERVICE_ENDPOINT);
        service.getSearchSuggestions(query)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JsonArray>() {
                    @Override
                    public final void onCompleted() {
                        // do nothing
                        Log.e(TAG, "onCompleted");
                    }

                    @Override
                    public final void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public final void onNext(JsonArray response) {
                        JsonArray res = response;
                        Log.e(TAG, "response  " + res.toString());
                        if(response!=null && !TextUtils.isEmpty(response.toString())){
                            JSONArray jArray = null;
                            try {
                                jArray = new JSONArray(response.toString());
                                ArrayList<String> result = new ArrayList<>();
                                for (int i = 0; i < jArray.getJSONArray(1).length(); i++) {
                                    String SuggestKey = jArray.getJSONArray(1).getString(i);
                                    result.add(SuggestKey);
                                }
                                if (result != null && result.size() > 0) {
                                    mListener.showProgress(false);
                                    if (mPage == 0 && result.isEmpty()) {
                                        mListener.listEmpty();
                                    } else {
                                        mAdapter.notifyDataSetInvalidated();
                                        mAdapter.addAll(result);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });


    }

    public void setListView(ListView listView) {
        mListView = listView;
        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(this);
        mListView.setAdapter(mAdapter);
        updateSequence();
    }

    public void updateSequence(String s) {
        sequence = s;
        updateSequence();
    }

    private void updateSequence() {
        mPage = 0;
        isLoadMore = true;
        if (mHandler != null) {
            mHandler.removeMessages(TRIGGER_SEARCH);
        }
        if (!sequence.isEmpty()) {
            Message searchMessage = new Message();
            searchMessage.what = TRIGGER_SEARCH;
            searchMessage.obj = sequence;
            mHandler.sendMessageDelayed(searchMessage, SEARCH_TRIGGER_DELAY_IN_MS);
        } else {
            isLoadMore = false;
            clearAdapter();
        }
    }

    private void clearAdapter() {
        mAdapter.clear();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mListener.onItemClicked((String) mAdapter.getItem(position));

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_TOUCH_SCROLL || scrollState == SCROLL_STATE_FLING) {
            mListener.onScroll();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        boolean loadMore = totalItemCount > 0 && firstVisibleItem + visibleItemCount >= totalItemCount;
        if (loadMore && isLoadMore) {
            mPage++;
            isLoadMore = false;
            getSearchSuggestions(sequence);
        }
    }

    public void setSearchProvidersListener(onSearchActionsListener listener) {
        this.mListener = listener;
    }

}


