package com.zuo.wanandroidjava.util;

import android.view.Gravity;
import android.widget.Toast;

import com.zuo.wanandroidjava.BaseApp;
import com.zuo.wanandroidjava.base.BaseActivity;

/**
 *  toast
 */
public class ToastUtils {


    private static Toast toast;
    private static Toast customToast;

    public static void showToast(String text) {
        show(text);
        toast.setText(text);
        toast.show();
    }

    public static void showCenterToast(String text) {
        show(text);
        toast.setText(text);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private static void show(String text) {
        if (toast == null)
            toast = Toast.makeText(BaseApp.getInstance(), text, Toast.LENGTH_SHORT);

    }


    public static void get() {
        if (customToast == null)
            customToast = new Toast(BaseApp.getInstance());
    }

    public static void  showImageToast() {
        get();
       // LayoutInflater.from(MyApplication.).inflate(R.layout.item_toast)
       // customToast.setView();
    }


    public static void showNetErrorToast() {
        ToastUtils.showCenterToast("网络异常");

    }

    public static void showNoNetwork() {
        ToastUtils.showCenterToast("网络未连接");
    }


    public static void showUnfinished() {
        showToast("该功能尚未完成,敬请期待");
    }
}
