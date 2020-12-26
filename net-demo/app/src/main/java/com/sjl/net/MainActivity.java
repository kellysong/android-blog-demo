package com.sjl.net;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "SIMPLE_LOGGER";
    @Bind(R.id.tv_progress)
    TextView mProgress;
    @Bind(R.id.tv_rate)
    TextView mRate;
    @Bind(R.id.tv_file_size)
    TextView mFileSize;

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

        FileDownloader.downloadFile(apiService.downloadApkFile1(), DOWNLOAD_APK_PATH, "test.apk", new DownloadProgressHandler() {


            @Override
            public void onProgress(int progress, long total, long speed) {
                mProgress.setText(progress + "%");
                mFileSize.setText(FileUtils.formatFileSize(total));
                mRate.setText(FileUtils.formatFileSize(speed) + "/s");
            }

            @Override
            public void onCompleted(File file) {

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "下载文件异常", e);
            }
        });
    }

    public void download2(View view) {
        DownloadApi apiService = RetrofitHelper.getInstance().getApiService(DownloadApi.class);

        FileDownloader.downloadFile2(apiService.downloadApkFile2(), DOWNLOAD_APK_PATH, "test2.apk", new DownloadProgressHandler() {


            @Override
            public void onProgress(int progress, long total, long speed) {
                mProgress.setText(progress + "%");
                mFileSize.setText(FileUtils.formatFileSize(total));
                mRate.setText(FileUtils.formatFileSize(speed) + "/s");
            }

            @Override
            public void onCompleted(File file) {
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "下载文件异常", e);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void download3(View view) {
        DownloadApi apiService = RetrofitHelper.getInstance().getApiService(DownloadApi.class);

        FileDownloader.downloadFile2(apiService.downloadApkFile3(), DOWNLOAD_APK_PATH, "test3.apk", new DownloadProgressHandler() {


            @Override
            public void onProgress(int progress, long total, long speed) {
                mProgress.setText(progress + "%");
                mFileSize.setText(FileUtils.formatFileSize(total));
                mRate.setText(FileUtils.formatFileSize(speed) + "/s");
            }

            @Override
            public void onCompleted(File file) {

            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }
}
