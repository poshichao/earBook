package com.hebut.earbook.util;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.hebut.earbook.R;

public class ResourceUtil {
    public static Drawable getDefaultImage(Context context, String cate) {
        Drawable defaultImg = null;
        switch (cate) {
            case "nlts":
                defaultImg = context.getDrawable(R.drawable.bg_nlts);
                break;
            case "shsy":
                defaultImg = context.getDrawable(R.drawable.bg_shsy);
                break;
            case "whyl":
                defaultImg = context.getDrawable(R.drawable.bg_whyl);
                break;
            default:
                defaultImg = context.getDrawable(R.drawable.bg_nlts);
                break;
        }
        return defaultImg;
    }
}
