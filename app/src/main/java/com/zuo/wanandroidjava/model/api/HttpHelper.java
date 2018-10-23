package com.zuo.wanandroidjava.model.api;

import com.zuo.wanandroidjava.bean.Article;
import com.zuo.wanandroidjava.bean.Banner;
import com.zuo.wanandroidjava.bean.Data;
import com.zuo.wanandroidjava.bean.ProjectList;
import com.zuo.wanandroidjava.bean.ProjectTab;
import com.zuo.wanandroidjava.bean.Tree;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HttpHelper {


    private final HttpApi httpApi;
    public HttpHelper(HttpApi httpApi) {
        this.httpApi = httpApi;
    }

    public Observable<Data<List<Banner>>> getHomeBanners() {
        return httpApi.getHomeBanners().compose(mainThread());
    }


    private <T> ObservableTransformer<T, T> mainThread() {
        return upstream -> upstream.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Data<Article>> getHomeDatas(int page) {
        return httpApi.getHomeDatas(page)
                .compose(mainThread());
    }
    public Observable<Tree> getTree() {

        return httpApi.getTree().compose(mainThread());
    }
    //获取项目的标题
    public Observable<ProjectTab> getProjectTab() {
        return httpApi.getProjectTab().compose(mainThread());
    }

    public Observable<ProjectList> getProjectList(int page, int id ) {

        return httpApi.getProjectList(page, id).compose(mainThread());
    }
}
