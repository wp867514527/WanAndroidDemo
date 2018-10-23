package com.zuo.wanandroidjava.presenter.contract;

import com.zuo.wanandroidjava.base.BaseView;
import com.zuo.wanandroidjava.bean.ProjectTab;

import java.util.List;

public interface ProjectContract {

    interface IView extends BaseView{

        void setDatas(List<ProjectTab.DataBean> data);
    }

    interface IPresenter{
        void getData();
    }
}
