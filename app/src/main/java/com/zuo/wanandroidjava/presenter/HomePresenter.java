package com.zuo.wanandroidjava.presenter;

import android.util.Log;

import com.zuo.wanandroidjava.base.BasePresenter;
import com.zuo.wanandroidjava.bean.Article;
import com.zuo.wanandroidjava.bean.Banner;
import com.zuo.wanandroidjava.bean.Data;
import com.zuo.wanandroidjava.presenter.contract.HomeContract;
import com.zuo.wanandroidjava.model.api.BaseHttpObserver;

import java.util.List;

import javax.inject.Inject;

public class HomePresenter extends BasePresenter<HomeContract.IView> implements HomeContract.IPresenter {
    private int page = 0;
    @Inject
    public HomePresenter() {

    }

    //设置 banner 数据
    @Override
    public void getBannerDatas() {
        getHttpHelper()
                .getHomeBanners()
                .as(getDisposable())
                .subscribe(new BaseHttpObserver<Data<List<Banner>>>() {
                    @Override
                    public void onSuccess(Data<List<Banner>> listData) {
                        getView().setBannerData(listData.getData());
                    }

                    @Override
                    protected void onFail(Throwable e) {
                        Log.d(TAG, e.getMessage());
                    }
                });
    }
    // 获取数据列表
    @Override
    public void getHomeDatas(int page) {
        getHttpHelper().getHomeDatas(page)
                .as(getDisposable())
                .subscribe(new BaseHttpObserver<Data<Article>>(getView()) {
                    @Override
                    public void onSuccess(Data<Article> articleData) {
                        if (page == 0) {
                            getView().setHomeDatas(articleData.getData(),true);
                        }else{
                            getView().setHomeDatas(articleData.getData(),false);
                        }
                         getView().onUpdated(page==0 ,true);
                    }

                    @Override
                    protected void onFail(Throwable e) {
                        getView().onUpdated(page == 0, false);
                    }
                });
    }

    @Override
    public void onRefresh() {
        getBannerDatas();
        page=0;
        getHomeDatas(page);
    }

    @Override
    public void loadMore() {
        page++;
        getHomeDatas(page);
    }
}
