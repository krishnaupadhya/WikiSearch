package com.search.wiki.home.viewmodel;

import android.databinding.BaseObservable;
import android.text.TextUtils;
import android.view.View;

import com.search.wiki.R;
import com.search.wiki.app.WikiSearchApplication;
import com.search.wiki.event.PageItemClickEvent;
import com.search.wiki.model.Pages;
import com.search.wiki.utility.StringUtility;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

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


    public String getColorCode() {

        String[] colorList = WikiSearchApplication.getInstance().getResources().getStringArray(R.array.color_codes);
        if (colorList != null && colorList.length > 0) {
            int index = pagePosition % 10;
            return colorList[index];
        }
        return "#ff7200";

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

        EventBus.getDefault().post(new PageItemClickEvent(pageDetails, pagePosition));
    }

}
