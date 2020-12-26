package com.sjl.net;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit帮助类
 *
 * @author Kelly
 * @version 1.0.0
 * @filename RetrofitHelper.java
 * @time 2018/1/29 15:09
 * @copyright(C) 2018 song
 */
public class RetrofitHelper {
    private volatile static RetrofitHelper sInstance;//使线程共享一个实例
    private Retrofit mRetrofit;



    private RetrofitHelper() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl("https://49d2147716ff75a9dc3c984f02381780.dd.cdntips.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static RetrofitHelper getInstance() {
        if (sInstance == null) {
            synchronized (RetrofitHelper.class) {
                if (sInstance == null) {
                    sInstance = new RetrofitHelper();
                }
            }
        }
        return sInstance;
    }





    /**
     * 返回接口服务实例
     *
     * @param clz
     * @param <T>
     * @return
     */
    public <T> T getApiService(Class<T> clz) {
        return mRetrofit.create(clz);
    }


}
