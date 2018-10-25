package com.zuo.wanandroidjava.di.module;

import android.content.Context;
import android.util.Log;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.zuo.wanandroidjava.model.api.Filed;
import com.zuo.wanandroidjava.model.api.HttpApi;
import com.zuo.wanandroidjava.model.api.HttpHelper;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class HttpModule {
    /**
     * 缓存大小.
     */
    private static final int CACHE_MAX = 50 * 1024 * 1024;
    private static final String HTTP_TAG = "http";
    private final Context context;

    public HttpModule(final Context context) {
        this.context = context;
    }

    @Provides
    public final PersistentCookieJar providerCookieJar() {
        return new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
    }

    @Provides
    @Singleton
    public OkHttpClient.Builder providerOkHttpBuild(PersistentCookieJar cookieJar) {

        return new OkHttpClient.Builder()
                .cache(new Cache(new File(context.getCacheDir(), HTTP_TAG), CACHE_MAX))
                .cookieJar(cookieJar)
                .addInterceptor(new CacheInterceptor())
                .addInterceptor(new HttpLoggingInterceptor(message -> Log.d(HTTP_TAG, message)).setLevel(HttpLoggingInterceptor.Level.BODY));
    }

    @Provides
    @Singleton
    public Retrofit.Builder providerRetrofitBuild() {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    }

    @Provides
    @Singleton
    public HttpApi providerHttpApi(Retrofit.Builder builder, OkHttpClient.Builder httpBuilder) {

        return builder
                .client(httpBuilder.build())
                .baseUrl(Filed.BASE_URL)
                .build().create(HttpApi.class);
    }

    @Provides
    @Singleton
    public HttpHelper providerHttpHeplper(HttpApi httpApi) {
        return new HttpHelper(httpApi);
    }

}
