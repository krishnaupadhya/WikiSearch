package com.search.wiki.home.view.fragment;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.search.wiki.R;
import com.search.wiki.app.Constants;
import com.search.wiki.app.DatabaseController;
import com.search.wiki.common.view.BaseFragment;
import com.search.wiki.common.view.WebViewActivity;
import com.search.wiki.databinding.HomeListFragmentBinding;
import com.search.wiki.event.PageItemClickEvent;
import com.search.wiki.event.SearchSuggestionClicked;
import com.search.wiki.home.adapters.PageListAdapter;
import com.search.wiki.home.listener.HomeListener;
import com.search.wiki.home.viewmodel.HomeActivityViewModel;
import com.search.wiki.model.Pages;
import com.search.wiki.model.WikiList;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeListFragment extends BaseFragment implements HomeListener {

    HomeListFragmentBinding homeListFragmentBinding;
    private HomeActivityViewModel homeTabViewModel;
    private PageListAdapter adapter;
    ArrayList<Pages> searchResultList;

    public HomeListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        homeListFragmentBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.home_list_fragment, null, false);
        homeTabViewModel = new HomeActivityViewModel(this);
        homeListFragmentBinding.setHomeViewModel(homeTabViewModel);
        EventBus.getDefault().register(this);
        fetchCachedData();
        return homeListFragmentBinding.getRoot();
    }


    private void fetchCachedData() {
        RealmResults<WikiList> recentSearches = DatabaseController.getInstance().getPagesFromDb();
        if (recentSearches != null && recentSearches.size() > 0 && recentSearches.get(0) != null
                && recentSearches.get(0).getQuery() != null && recentSearches.get(0).getQuery().getPages() != null) {
            searchResultList = new ArrayList<Pages>();
            searchResultList.addAll(recentSearches.get(0).getQuery().getPages());
            setSearchAdapter(searchResultList);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public static Fragment newInstance() {
        return new HomeListFragment();
    }

    private void setSearchAdapter(ArrayList<Pages> pagesArrayList) {
        if (pagesArrayList == null || pagesArrayList.size() == 0) {
            homeTabViewModel.setIsNewsListListVisible(false);
        } else {
            homeTabViewModel.setIsNewsListListVisible(true);
            adapter = new PageListAdapter(pagesArrayList);
            homeListFragmentBinding.listNews.setAdapter(adapter);
            homeListFragmentBinding.listNews.setLayoutManager(new LinearLayoutManager(getActivity()));
            homeListFragmentBinding.listNews.setItemAnimator(new DefaultItemAnimator());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSearchEvent(SearchSuggestionClicked event) {
        if (getActivity() != null && !isDetached())
            if (event != null && homeTabViewModel != null) {
                homeTabViewModel.getSearchResults(event.getQuery());
            }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPageItemEvent(PageItemClickEvent event) {
        if (getActivity() != null && !isDetached())
            if (event != null && homeTabViewModel != null) {
                homeTabViewModel.getPageDetails(event.getPages().getPageid());
            }
    }

    @Override
    public void onSearchResultSuccess(WikiList wikiList) {
        ArrayList<Pages> pages = new ArrayList<>();
        pages.addAll(wikiList.getQuery().getPages());
        setSearchAdapter(pages);
    }

    @Override
    public void onPageDetailsResultSuccess(String pageUrl, String title) {
        if (getActivity() == null || isDetached()) return;
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        intent.putExtra(Constants.KEY_INTENT_WEB_URL, pageUrl);
        intent.putExtra(Constants.KEY_INTENT_PAGE_TITLE, title);
        startActivity(intent);
    }
}
