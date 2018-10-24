package com.zuo.wanandroidjava.helper;

import com.alibaba.android.arouter.launcher.ARouter;

public class ARouterHelper {
    public final static String WEB_PAGE = "/zuo/webActivity";
    public final static String SEARCH_PAGE = "/zuo/searchActivity";

    public static void jumpWeb(String url) {
        ARouter.getInstance().build(WEB_PAGE)
                .withString("url",url)
                .navigation();
    }

    public static void jumpSearch() {
        ARouter.getInstance().build(SEARCH_PAGE)
                .navigation();
    }
}
