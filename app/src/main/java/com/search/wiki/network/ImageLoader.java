package com.search.wiki.network;

import android.webkit.URLUtil;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


public class ImageLoader {
    public static void loadImage(ImageView imageView, String imageUrl) {
        if (imageView != null && URLUtil.isValidUrl(imageUrl)) {
            Glide.with(imageView.getContext()).load(imageUrl).into(imageView);
        }
    }
}
