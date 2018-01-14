package com.shop.food.food.view.fragment;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shop.food.R;
import com.shop.food.app.DatabaseController;
import com.shop.food.common.view.BaseFragment;
import com.shop.food.databinding.HomeTabFragmentBinding;
import com.shop.food.event.SearchSuggestionClicked;
import com.shop.food.food.adapters.PageListAdapter;
import com.shop.food.food.listener.HomeListener;
import com.shop.food.food.viewmodel.HomeActivityViewModel;
import com.shop.food.model.Pages;
import com.shop.food.model.WikiList;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeTabFragment extends BaseFragment implements HomeListener {

    HomeTabFragmentBinding homeActivityBinding;
    private HomeActivityViewModel homeTabViewModel;
    private PageListAdapter adapter;
    ArrayList<Pages> searchResultList;

    public HomeTabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        homeActivityBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.home_tab_fragment, null, false);
        homeTabViewModel = new HomeActivityViewModel(this);
        homeActivityBinding.setHomeViewModel(homeTabViewModel);
        EventBus.getDefault().register(this);
        return homeActivityBinding.getRoot();
    }


    private void fetchCachedData() {
        RealmResults<Pages> pagesList = DatabaseController.getInstance().getPagesFromDb();
        searchResultList = new ArrayList<Pages>();
        searchResultList.addAll(pagesList);
        setSearchAdapter(searchResultList);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        fetchCachedData();
    }

    public static Fragment newInstance() {
        return new HomeTabFragment();
    }

    private void setSearchAdapter(ArrayList<Pages> pagesArrayList) {
        if (pagesArrayList == null || pagesArrayList.size() == 0) {
            homeTabViewModel.setIsNewsListListVisible(false);
        } else {
            homeTabViewModel.setIsNewsListListVisible(true);
            adapter = new PageListAdapter(pagesArrayList);
            homeActivityBinding.listNews.setAdapter(adapter);
            homeActivityBinding.listNews.setLayoutManager(new LinearLayoutManager(getActivity()));
            homeActivityBinding.listNews.setItemAnimator(new DefaultItemAnimator());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSearchEvent(SearchSuggestionClicked event) {
        if (homeTabViewModel != null) {
            homeTabViewModel.getSearchResults(event.getQuery());
        }
    }

    @Override
    public void onSearchResultSuccess(WikiList wikiList) {
        ArrayList<Pages> pages = new ArrayList<>();
        pages.addAll(wikiList.getQuery().getPages());
        setSearchAdapter(pages);
    }
}
