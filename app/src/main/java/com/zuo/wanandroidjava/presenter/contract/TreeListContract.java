package com.zuo.wanandroidjava.presenter.contract;

import com.zuo.wanandroidjava.base.BaseView;
import com.zuo.wanandroidjava.bean.TreeList;

public interface TreeListContract {

    interface IPresenter{
       void getDatas(int cid);
    }

    interface IView extends BaseView{

        void setDatas(TreeList.DataBean data, boolean b);

        void onUpdate(boolean isRresh, boolean success);
    }

}
