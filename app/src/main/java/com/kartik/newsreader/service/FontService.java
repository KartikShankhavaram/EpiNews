package com.kartik.newsreader.service;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by kartik on 10/11/17.
 */

public class FontService {

    public static Typeface getProductSans(Context mContext) {
        return Typeface.createFromAsset(mContext.getAssets(), "fonts/product_sans_regular.ttf");
    }

    public static Typeface getProductSansBold(Context mContext) {
        return Typeface.createFromAsset(mContext.getAssets(), "fonts/product_sans_bold.ttf");
    }

    public static Typeface getProductSansItalic(Context mContext) {
        return Typeface.createFromAsset(mContext.getAssets(), "fonts/product_sans_italic.ttf");
    }

    public static Typeface getProductSansBoldItalic(Context mContext) {
        return Typeface.createFromAsset(mContext.getAssets(), "fonts/product_sans_bold_italic.ttf");
    }
}
