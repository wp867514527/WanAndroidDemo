package com.zuo.wanandroidjava.model.api;

import com.zuo.wanandroidjava.bean.Article;
import com.zuo.wanandroidjava.bean.Banner;
import com.zuo.wanandroidjava.bean.Data;
import com.zuo.wanandroidjava.bean.ProjectList;
import com.zuo.wanandroidjava.bean.ProjectTab;
import com.zuo.wanandroidjava.bean.Tree;
import com.zuo.wanandroidjava.bean.TreeList;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HttpApi {
    @GET("/banner/json")
    Observable<Data<List<Banner>>> getHomeBanners();

    //首页数据
    @GET("/article/list/{page}/json")
    Observable<Data<Article>> getHomeDatas(@Path("page") int page);

    //http://www.wanandroid.com/tree/json
    // 体系数据
    @GET("/tree/json")
    Observable<Tree> getTree();

    /* /project/tree/json*/
    @GET("/project/tree/json")
    Observable<ProjectTab> getProjectTab();

    // http://www.wanandroid.com/project/list/1/json?cid=294
    // 项目列表数据
    @GET("/project/list/{page}/json")
    Observable<ProjectList> getProjectList(@Path("page") int page, @Query("cid") int id);

    //体系列表数据 http://www.wanandroid.com/article/list/0/json?cid=60
    @GET("/article/list/{page}/json")
    Observable<TreeList> getTreeList(@Path("page") int page, @Query("cid") int cid);

    //搜索
    //http://www.wanandroid.com/article/query/0/json  // 从0开始
    @POST("/article/query/{page}/json")
    @FormUrlEncoded
    Observable<Data<Article>> getSearchArticle(@Path("page") int page, @Field("k") String k);

}
