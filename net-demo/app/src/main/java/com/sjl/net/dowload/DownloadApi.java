package com.sjl.net.dowload;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
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

}
