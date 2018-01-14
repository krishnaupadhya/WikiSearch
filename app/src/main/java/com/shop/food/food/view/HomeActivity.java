package com.shop.food.food.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.shop.food.R;
import com.shop.food.common.view.BaseActivity;
import com.shop.food.databinding.HomeActivityBinding;
import com.shop.food.event.NewsClickEvent;
import com.shop.food.food.adapters.PageListAdapter;
import com.shop.food.food.listener.HomeListener;
import com.shop.food.food.viewmodel.HomeActivityViewModel;
import com.shop.food.interfaces.onSearchListener;
import com.shop.food.interfaces.onSimpleSearchActionsListener;
import com.shop.food.model.Pages;
import com.shop.food.model.WikiList;
import com.shop.food.utility.DialogUtility;
import com.shop.food.widget.MaterialSearchView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;


/**
 * Created by Krishna Upadhya on 9/9/2017.
 */

public class HomeActivity extends BaseActivity implements onSimpleSearchActionsListener, onSearchListener, HomeListener {

    private HomeActivityViewModel homeViewModel;
    private PageListAdapter adapter;
    private HomeActivityBinding homeActivityBinding;

    private boolean mSearchViewAdded = false;
    private MaterialSearchView mSearchView;
    private WindowManager mWindowManager;

    private MenuItem searchItem;
    private boolean searchActive = false;
    private View rootView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBinding();
        fetchCachedData();
        initToolBar();
        initView();
        EventBus.getDefault().register(this);
    }

    private void initView() {
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mSearchView = new MaterialSearchView(this);
        mSearchView.setOnSearchListener(this);
        mSearchView.setSearchResultsListener(this);
        mSearchView.setHintText(getString(R.string.search));

        if (homeActivityBinding.appBarHome.toolbar != null) {
            // Delay adding SearchView until Toolbar has finished loading
            homeActivityBinding.appBarHome.toolbar.post(new Runnable() {
                @Override
                public void run() {
                    if (!mSearchViewAdded && mWindowManager != null) {
                        mWindowManager.addView(mSearchView,
                                MaterialSearchView.getSearchViewLayoutParams(HomeActivity.this));
                        mSearchViewAdded = true;
                    }
                }
            });
        }
    }

    private void fetchCachedData() {

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initBinding() {
        homeActivityBinding = DataBindingUtil.setContentView(this, R.layout.home_activity);
        homeViewModel = new HomeActivityViewModel(this, this);
        homeActivityBinding.setHomeViewModel(homeViewModel);
    }

    private void initToolBar() {
        setSupportActionBar(homeActivityBinding.appBarHome.toolbar);
    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }


    private void setSearchAdapter(ArrayList<Pages> pagesArrayList) {
        if (pagesArrayList == null || (pagesArrayList != null && pagesArrayList.size() == 0)) {
            homeViewModel.setIsNewsListListVisible(false);
        } else {
            homeViewModel.setIsNewsListListVisible(true);
            if (adapter == null) {
                adapter = new PageListAdapter(pagesArrayList);
                homeActivityBinding.listNews.setAdapter(adapter);
                homeActivityBinding.listNews.setLayoutManager(new LinearLayoutManager(this));
                homeActivityBinding.listNews.setItemAnimator(new DefaultItemAnimator());
            } else {
                adapter.updateNewsList(pagesArrayList);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        searchItem = menu.findItem(R.id.search);
        searchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mSearchView.display();
                openKeyboard();
                return true;
            }
        });
        if (searchActive)
            mSearchView.display();
        return true;

    }

    private void openKeyboard() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                mSearchView.getSearchView().dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
                mSearchView.getSearchView().dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
            }
        }, 200);
    }


    @Override
    public void onSearchResultSuccess(WikiList wikiList) {
        ArrayList<Pages> pages = new ArrayList<>();
        pages.addAll(wikiList.getQuery().getPages());
        setSearchAdapter(pages);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewsClickEvent(NewsClickEvent event) {
        if (event != null) {
//            Intent intent = new Intent(this, DetailsActivity.class);
//            intent.putExtra(Constants.KEY_INTENT_NEWS_POSITION, event.getArticle().getArticleId());
//            startActivity(intent);
        }
    }


    @Override
    public void onSearch(String query) {

    }

    @Override
    public void searchViewOpened() {

    }

    @Override
    public void searchViewClosed() {

    }

    @Override
    public void onCancelSearch() {
        searchActive = false;
        mSearchView.hide();
    }

    @Override
    public void onItemClicked(String item) {
        mSearchView.hide();
        DialogUtility.showToastMessage(this, getString(R.string.searching_for, item), Toast.LENGTH_SHORT);
        homeViewModel.getSearchResults(item);
    }

    @Override
    public void onScroll() {

    }

    @Override
    public void error(String localizedMessage) {

    }
}
