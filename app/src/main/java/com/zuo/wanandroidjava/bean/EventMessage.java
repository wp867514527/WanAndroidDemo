package com.zuo.wanandroidjava.bean;

/**
 * 描述:
 * 作者:wp
 * 时间:2018/11/2
 */

public class EventMessage {
    private boolean isSuccess;

    public EventMessage(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
