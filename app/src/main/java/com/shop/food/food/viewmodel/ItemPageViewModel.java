package com.shop.food.food.viewmodel;

import android.databinding.BaseObservable;
import android.text.TextUtils;
import android.view.View;

import com.shop.food.model.Pages;
import com.shop.food.utility.StringUtility;

/**
 * Created by Krishna Upadhya on 3/2/2018.
 */
public class ItemPageViewModel extends BaseObservable {

    private Pages pageDetails;
    private int pagePosition;
    private String TAG = ItemPageViewModel.class.getName();

    public ItemPageViewModel(Pages languagesData, int position) {
        this.pageDetails = languagesData;
        this.pagePosition = position;
        if (this.pageDetails == null) {
            this.pageDetails = new Pages();
        }
    }


    public String getDescription() {
        if (pageDetails.getTerms() != null && pageDetails.getTerms().getDescription() != null
                && pageDetails.getTerms().getDescription().size() > 0
                && !TextUtils.isEmpty(pageDetails.getTerms().getDescription().get(0)))
            return pageDetails.getTerms().getDescription().get(0);
        else
            return StringUtility.EMPTY;
    }

    public String getTitle() {
        if (!TextUtils.isEmpty(pageDetails.getTitle()))
            return pageDetails.getTitle();
        else
            return StringUtility.EMPTY;
    }

    public String getImage() {
        if (pageDetails.getThumbnail() != null && !TextUtils.isEmpty(pageDetails.getThumbnail().getSource()))
            return pageDetails.getThumbnail().getSource();
        else
            return StringUtility.EMPTY;
    }

    public void setPageData(Pages pageData, int position) {
        this.pageDetails = pageData;
        this.pagePosition = position;
        notifyChange();
    }

    public void onItemClick(View view) {
//        if (pageDetails != null && !TextUtils.isEmpty(pageDetails.getType()) && pageDetails.getType().equals(Constants.TITLE_COMMENT))
//            return;
//        EventBus.getDefault().post(new NewsClickEvent(pageDetails, pagePosition));
    }

}
