package com.search.wiki.home.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.search.wiki.R;
import com.search.wiki.common.view.BaseActivity;
import com.search.wiki.databinding.HomeActivityBinding;
import com.search.wiki.event.SearchSuggestionClicked;
import com.search.wiki.home.view.fragment.HomeTabFragment;
import com.search.wiki.interfaces.onSearchListener;
import com.search.wiki.interfaces.onSimpleSearchActionsListener;
import com.search.wiki.widget.MaterialSearchView;

import org.greenrobot.eventbus.EventBus;

public class HomeActivity extends BaseActivity implements onSimpleSearchActionsListener, onSearchListener {

    private HomeActivityBinding homeActivityBinding;

    private boolean mSearchViewAdded = false;
    private MaterialSearchView mSearchView;
    private WindowManager mWindowManager;

    private MenuItem searchItem;
    private boolean searchActive = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBinding();
        initToolBar();
        initView();
       // EventBus.getDefault().register(this);
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
        addHomeFragment();
    }

    @Override
    protected void onDestroy() {
        //EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initBinding() {
        homeActivityBinding = DataBindingUtil.setContentView(this, R.layout.home_activity);
    }

    private void initToolBar() {
        setSupportActionBar(homeActivityBinding.appBarHome.toolbar);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

    public void addHomeFragment() {
        if (homeActivityBinding.homeFrame != null) {


            // Create a new Fragment to be placed in the activity layout
            Fragment firstFragment = HomeTabFragment.newInstance();

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.home_frame, firstFragment, HomeTabFragment.class.getSimpleName()).commit();
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
//        DialogUtility.showToastMessage(this, getString(R.string.searching_for, item), Toast.LENGTH_SHORT);
        //homeViewModel.getSearchResults(item);
        EventBus.getDefault().post(new SearchSuggestionClicked(item));
    }

    @Override
    public void onScroll() {

    }

    @Override
    public void error(String localizedMessage) {

    }
}
