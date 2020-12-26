package com.sjl.net;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;


public interface DownloadApi {


    /**
     * 下载Apk1文件
     *
     */
    @Streaming
    @GET("https://imtt.dd.qq.com/16891/apk/AB92915374D251277B4EF3465ECB751E.apk?fsname=cn.gov.tax.its_1.5.5_10505.apk&csr=1bbd")
    Observable<ResponseBody> downloadApkFile1();

    /**
     * 下载Apk2文件
     *
     */
    @Streaming
    @GET("https://cc849cacb0e96648f8dd4bb35ff8365b.dd.cdntips.com/imtt.dd.qq.com/16891/5BB89032B0755F5922C80DA8C2CAF735.apk?mkey=5c415b9fb711c35d&f=07b4&fsname=com.tencent.mobileqq_7.9.7_994.apk&csr=1bbd&cip=183.17.229.168&proto=https")
    Observable<ResponseBody> downloadApkFile2();

    /**
     * 下载Apk3文件
     *
     */
    @Streaming
    @GET("https://cc849cacb0e96648f8dd4bb35ff8365b.dd.cdntips.com/imtt.dd.qq.com/16891/BEC5EEF53983300D9F0AB46166EC9EA7.apk?mkey=5c41a20bda11e60f&f=184b&fsname=com.tencent.pao_1.0.61.0_161.apk&csr=1bbd&cip=218.17.192.250&proto=https")
    Observable<ResponseBody> downloadApkFile3();
}
