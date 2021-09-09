package com.sjl.net.dowload;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Streaming;
import retrofit2.http.Url;


public interface DownloadApi {


    /**
     * 下载文件
     *
     */
    @Streaming
    @GET
    Observable<ResponseBody> downLoad(@Url String url);


    /**
     *下载文件
     * @param range Range表示断点续传的请求头参数
     * @param url 下载url
     * @return
     */
    @Streaming
    @GET
    Observable<ResponseBody> download(@Header("Range") String range, @Url String url);
}
