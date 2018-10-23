package com.zuo.wanandroidjava.presenter.contract;

import com.zuo.wanandroidjava.base.BaseView;
import com.zuo.wanandroidjava.bean.ProjectList;

public interface ProjectListContract {

    interface IPresenter{
        void onRefresh(int id);
        void loadMore(int id);
    }
    interface IView extends BaseView{

        void onUpdate(boolean isRefresh, boolean success);
        void setDatas(ProjectList.DataBean data, boolean b);
    }

}
