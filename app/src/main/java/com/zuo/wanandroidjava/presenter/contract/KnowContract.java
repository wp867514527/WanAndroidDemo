package com.zuo.wanandroidjava.presenter.contract;

import com.zuo.wanandroidjava.base.BaseView;
import com.zuo.wanandroidjava.bean.Tree;

public interface KnowContract {

    interface IView extends BaseView {
        void setDatas(Tree tree, boolean isRefresh);

        void onUpdate(boolean isRefresh,boolean success);

    }
    interface IPresenter{
        void onRefresh();
        void loadMore();

        void getDatas();
    }
}
