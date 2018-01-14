package com.search.wiki.home.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.search.wiki.R;
import com.search.wiki.databinding.ItemPageBinding;
import com.search.wiki.home.viewmodel.ItemPageViewModel;
import com.search.wiki.model.Pages;

import java.util.ArrayList;
import java.util.List;

public class PageListAdapter extends RecyclerView.Adapter<PageListAdapter.PageAdapterViewHolder> {

    private static final String TAG = PageListAdapter.class.getSimpleName();
    private List<Pages> pageList;

    public PageListAdapter(ArrayList<Pages> pagesArrayList) {
        this.pageList = pagesArrayList;
    }

    @Override
    public PageAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemPageBinding itemPageBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_page,
                        parent, false);
        return new PageAdapterViewHolder(itemPageBinding);
    }

    @Override
    public void onBindViewHolder(PageAdapterViewHolder holder, int position) {
        holder.bindLocations(pageList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return pageList == null ? 0 : pageList.size();
    }

    public void updateNewsList(ArrayList<Pages> artliclesList) {
        this.pageList = artliclesList;
        notifyDataSetChanged();
    }

    public class PageAdapterViewHolder extends RecyclerView.ViewHolder {
        ItemPageBinding mItemPagesBinding;

        public PageAdapterViewHolder(ItemPageBinding itemPageBinding) {
            super(itemPageBinding.itemNews);
            this.mItemPagesBinding = itemPageBinding;
        }

        void bindLocations(Pages pagesData, int position) {
            if (mItemPagesBinding.getItemPageViewModel() == null) {
                mItemPagesBinding.setItemPageViewModel(
                        new ItemPageViewModel(pagesData, position));
            } else {
                mItemPagesBinding.getItemPageViewModel().setPageData(pagesData, position);
            }
        }
    }
}