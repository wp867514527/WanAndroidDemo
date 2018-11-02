package com.zuo.wanandroidjava.model.api;

import com.zuo.wanandroidjava.bean.Article;
import com.zuo.wanandroidjava.bean.Banner;
import com.zuo.wanandroidjava.bean.Data;
import com.zuo.wanandroidjava.bean.ProjectList;
import com.zuo.wanandroidjava.bean.ProjectTab;
import com.zuo.wanandroidjava.bean.Tree;
import com.zuo.wanandroidjava.bean.TreeList;
import com.zuo.wanandroidjava.bean.User;

import java.util.List;

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

    public Observable<ProjectList> getProjectList(int page, int id) {

        return httpApi.getProjectList(page, id).compose(mainThread());
    }

    public Observable<TreeList> getTreeList(int page, int cid) {
        return httpApi.getTreeList(page, cid).compose(mainThread());
    }

    public Observable<Article> getSearchArticle(int page, String k) {
        return httpApi.getSearchArticle(page, k)
                .compose(handHttp());
    }

    private <T> ObservableTransformer<Data<T>, T> handHttp() {
        return upstream -> upstream.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(tHttpResult -> {
                    if (tHttpResult.getErrorCode() >= 0) {
                        return createData(tHttpResult.getData());
                    } else {
                        return Observable.error(new ApiException(tHttpResult.getErrorCode() + "", tHttpResult.getErrorMsg()));
                    }
                });
    }

    private <T> ObservableTransformer<T, T> mainThread() {
        return upstream -> upstream.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }


    private <T> Observable<T> createData(T data) {
        return Observable.create(emitter -> {
            emitter.onNext(data);
            emitter.onComplete();
        });
    }


    //注册
    public Observable<User> register(String userName, String password, String repassword) {
        return httpApi.register(userName, password, repassword).compose(handHttp());
    }


    //登陆
    public Observable<User> ogin(String userName, String password) {
        return httpApi.login(userName, password).compose(handHttp());
    }

    //退出登陆
    public Observable<User> logout() {
        return httpApi.logout().compose(mainThread());
    }

}
