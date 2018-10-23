package com.zuo.wanandroidjava.presenter.contract;

import com.zuo.wanandroidjava.base.BaseView;
import com.zuo.wanandroidjava.bean.Article;
import com.zuo.wanandroidjava.bean.Banner;

import java.util.List;

public interface HomeContract {

    interface IPresenter{
        void getBannerDatas();

        void getHomeDatas(int page);

        void onRefresh();

        void loadMore();

    }

    interface IView extends BaseView{
        void setBannerData(List<Banner> bannerData);

        void setHomeDatas(Article article,boolean isRefresh);
        void onUpdated(boolean refresh, boolean success);
    }
}
