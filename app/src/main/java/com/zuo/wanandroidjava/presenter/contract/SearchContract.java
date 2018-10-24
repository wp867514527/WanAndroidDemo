package com.zuo.wanandroidjava.presenter.contract;

import com.zuo.wanandroidjava.base.BaseView;
import com.zuo.wanandroidjava.bean.Article;

import java.util.List;

public interface SearchContract {

    interface IView extends BaseView {

        void setDatas(List<Article.DatasBean> datas);
    }

    interface IPresenter {
        void getDatas(String k);
    }

}
