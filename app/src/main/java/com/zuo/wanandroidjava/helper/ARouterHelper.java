package com.zuo.wanandroidjava.helper;

import com.alibaba.android.arouter.launcher.ARouter;

public class ARouterHelper {
    public static String WEB_PAGE = "/zuo/webActivity";

    public static void jumpWeb(String url) {
        ARouter.getInstance().build(WEB_PAGE)
                .withString("url",url)
                .navigation();
    }
}
