package com.zuo.wanandroidjava.helper;

import com.alibaba.android.arouter.launcher.ARouter;

public class ARouterHelper {
    public final static String WEB_PAGE = "/zuo/webActivity";
    public final static String SEARCH_PAGE = "/zuo/searchActivity";
    public final static String LOGIN_REGIST = "/zuo/Login_RegistActivity";
    public final static String SHOUCANG = "/zuo/ShoucangActivity";
    public final static String ABOUT = "/zuo/aboutActivity";

    public static void jumpWeb(String url) {
        ARouter.getInstance().build(WEB_PAGE)
                .withString("url",url)
                .navigation();
    }

    public static void jumpSearch() {
        ARouter.getInstance().build(SEARCH_PAGE)
                .navigation();
    }

    public static void jumpLogin() {
        ARouter.getInstance().build(LOGIN_REGIST)
                .navigation();
    }
    public static void jumpShoucang() {
        ARouter.getInstance().build(SHOUCANG)
                .navigation();
    }
    public static void jumpAbout() {
        ARouter.getInstance().build(ABOUT)
                .navigation();
    }
}
