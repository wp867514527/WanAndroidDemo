package com.zuo.wanandroidjava.util;

import android.content.Context;

public class StatusUtil {



    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resultId = context.getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (resultId > 0) {
            result = context.getResources().getDimensionPixelSize(resultId);
        }
        return result;
    }
}
