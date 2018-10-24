package com.zuo.wanandroidjava.presenter;

import com.zuo.wanandroidjava.base.BasePresenter;
import com.zuo.wanandroidjava.bean.Article;
import com.zuo.wanandroidjava.model.api.BaseHttpObserver;
import com.zuo.wanandroidjava.presenter.contract.SearchContract;

import javax.inject.Inject;

public class SearchPresneter extends BasePresenter<SearchContract.IView> implements SearchContract.IPresenter {
    int page = 0;

    @Inject
    public SearchPresneter() {
    }

    @Override
    public void getDatas(String k) {
        System.out.println(Thread.currentThread().getName());
        getHttpHelper().getSearchArticle(page, k)
                .as(getDisposable())
                .subscribe(new BaseHttpObserver<Article>() {
                    @Override
                    public void onSuccess(Article article) {
                        getView().showContentView();
                        getView().setDatas(article.getDatas());

                    }

                    @Override
                    protected void onFail(Throwable e) {
                        getView().showEmptyView();
                    }
                });
    }
}
