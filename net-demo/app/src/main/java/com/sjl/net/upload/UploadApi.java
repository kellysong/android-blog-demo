package com.sjl.net.upload;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename UploadApi
 * @time 2021/6/13 20:58
 * @copyright(C) 2021 song
 */
public interface UploadApi {

    @POST("http://192.168.120.65:8080/springmvc_demo/upload/fileChunk1.htmls")
    Observable<ResponseResult> upload1(@Body RequestBody requestBody);

    @POST("http://192.168.120.65:8080/springmvc_demo/upload/fileChunk2.htmls")
    Observable<ResponseResult> upload2(@Body RequestBody requestBody);
}
