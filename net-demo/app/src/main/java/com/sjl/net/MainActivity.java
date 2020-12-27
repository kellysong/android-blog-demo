package com.sjl.net;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "SIMPLE_LOGGER";
    @Bind(R.id.tv_progress)
    TextView mProgress;
    @Bind(R.id.tv_rate)
    TextView mRate;
    @Bind(R.id.tv_file_size)
    TextView mFileSize;
    @Bind(R.id.tv_msg)
    TextView tv_msg;

    public static final String ROOT_PATH = Environment.getExternalStorageDirectory() + File.separator;// sd路径
    /**
     * 更新apk 路径
     */
    public static final String DOWNLOAD_APK_PATH = ROOT_PATH + File.separator + "downloadApk";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }


    protected void initView() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    protected void initListener() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);//权限配置
    }

    protected void initData() {
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {

            }

            @Override
            public void onDenied(String permission) {

            }
        });
    }

    public void download1(View view) {
        DownloadApi apiService = RetrofitHelper.getInstance().getApiService(DownloadApi.class);
        String url = "https://imtt.dd.qq.com/16891/apk/AB92915374D251277B4EF3465ECB751E.apk?fsname=cn.gov.tax.its_1.5.5_10505.apk&csr=1bbd";
        Observable<ResponseBody> responseBodyObservable = apiService.downLoad(url);
        FileDownloader.downloadFile(responseBodyObservable, DOWNLOAD_APK_PATH, "test.apk", new DownloadProgressHandler() {


            @Override
            public void onProgress(int progress, long total, long speed) {
                mProgress.setText(progress + "%");
                mFileSize.setText(FileUtils.formatFileSize(total));
                mRate.setText(FileUtils.formatFileSize(speed) + "/s");
            }

            @Override
            public void onCompleted(File file) {
                showMsg("下载完成："+file.getAbsolutePath());
            }

            @Override
            public void onError(Throwable e) {
                showMsg(e.getMessage());
            }
        });
    }

    private void showMsg(String message) {
        if (isFinishing() || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && isDestroyed())){
            return;
        }
        tv_msg.setText("下载文件异常:"+message);
    }

    public void download2(View view) {
        DownloadApi apiService = RetrofitHelper.getInstance().getApiService(DownloadApi.class);
        String url = "https://cc849cacb0e96648f8dd4bb35ff8365b.dd.cdntips.com/imtt.dd.qq.com/16891/5BB89032B0755F5922C80DA8C2CAF735.apk?mkey=5c415b9fb711c35d&f=07b4&fsname=com.tencent.mobileqq_7.9.7_994.apk&csr=1bbd&cip=183.17.229.168&proto=https";
        Observable<ResponseBody> responseBodyObservable = apiService.downLoad(url);
        FileDownloader.downloadFile2(responseBodyObservable, DOWNLOAD_APK_PATH, "test2.apk", new DownloadProgressHandler() {


            @Override
            public void onProgress(int progress, long total, long speed) {
                mProgress.setText(progress + "%");
                mFileSize.setText(FileUtils.formatFileSize(total));
                mRate.setText(FileUtils.formatFileSize(speed) + "/s");
            }

            @Override
            public void onCompleted(File file) {
                showMsg("下载完成："+file.getAbsolutePath());
            }

            @Override
            public void onError(Throwable e) {
                //异常请检查路径，网络等因素
                showMsg(e.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileDownloader.cancelDownload();
    }

    public void download3(View view) {
        DownloadApi apiService = RetrofitHelper.getInstance().getApiService(DownloadApi.class);
        String url="https://cc849cacb0e96648f8dd4bb35ff8365b.dd.cdntips.com/imtt.dd.qq.com/16891/BEC5EEF53983300D9F0AB46166EC9EA7.apk?mkey=5c41a20bda11e60f&f=184b&fsname=com.tencent.pao_1.0.61.0_161.apk&csr=1bbd&cip=218.17.192.250&proto=https";
        Observable<ResponseBody> responseBodyObservable = apiService.downLoad(url);

        FileDownloader.downloadFile2(responseBodyObservable, DOWNLOAD_APK_PATH, "test3.apk", new DownloadProgressHandler() {


            @Override
            public void onProgress(int progress, long total, long speed) {
                mProgress.setText(progress + "%");
                mFileSize.setText(FileUtils.formatFileSize(total));
                mRate.setText(FileUtils.formatFileSize(speed) + "/s");
            }

            @Override
            public void onCompleted(File file) {
                showMsg("下载完成："+file.getAbsolutePath());
            }

            @Override
            public void onError(Throwable e) {
                showMsg(e.getMessage());
            }
        });
    }


}
