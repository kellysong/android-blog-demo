package com.sjl.net;

import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import com.sjl.net.dowload.DownloadApi;
import com.sjl.net.dowload.DownloadInfo;
import com.sjl.net.dowload.DownloadProgressHandler;
import com.sjl.net.dowload.FileDownloader;
import com.sjl.net.util.FileUtils;

import java.io.File;

import butterknife.Bind;
import io.reactivex.Observable;
import okhttp3.ResponseBody;

public class FileDownloadActivity extends BaseActivity {
    private static final String TAG = "SIMPLE_LOGGER";
    @Bind(R.id.tv_progress)
    TextView mProgress;
    @Bind(R.id.tv_rate)
    TextView mRate;
    @Bind(R.id.tv_file_size)
    TextView mFileSize;
    @Bind(R.id.tv_msg)
    TextView tv_msg;

    @Bind(R.id.tv_time)
    TextView mTime;

    public static final String ROOT_PATH = Environment.getExternalStorageDirectory() + File.separator;// sd路径
    /**
     * 更新apk 路径
     */
    public static final String DOWNLOAD_APK_PATH = ROOT_PATH + File.separator + "downloadApk";


    @Override
    protected int getLayoutId() {
        return R.layout.file_dowload_activity;
    }


    protected void initView() {

    }

    protected void initListener() {

    }


    protected void initData() {

    }

    public void download1(View view) {
        FileDownloader.cancelDownload();
        DownloadApi apiService = RetrofitHelper.getInstance().getApiService(DownloadApi.class);
        String url = "https://imtt.dd.qq.com/16891/apk/AB92915374D251277B4EF3465ECB751E.apk?fsname=cn.gov.tax.its_1.5.5_10505.apk&csr=1bbd";
        Observable<ResponseBody> responseBodyObservable = apiService.downLoad(url);
        FileDownloader.downloadFile(responseBodyObservable, DOWNLOAD_APK_PATH, "test.apk", new DownloadProgressHandler() {


            @Override
            public void onProgress(DownloadInfo downloadInfo) {
                int progress = downloadInfo.getProgress();
                long fileSize = downloadInfo.getFileSize();
                long speed = downloadInfo.getSpeed();
                long usedTime = downloadInfo.getUsedTime();
                mProgress.setText(progress + "%");
                mFileSize.setText(FileUtils.formatFileSize(fileSize));
                mRate.setText(FileUtils.formatFileSize(speed) + "/s");
                mTime.setText(FileUtils.formatTime(usedTime));
            }

            @Override
            public void onCompleted(File file) {
                showMsg("下载完成：" + file.getAbsolutePath());
            }

            @Override
            public void onError(Throwable e) {
                showMsg("下载文件异常:" + e.getMessage());
            }
        });
    }

    private void showMsg(String message) {
        if (isDestroy(this)) {
            return;
        }
        tv_msg.setText(message);
    }

    public void download2(View view) {
        FileDownloader.cancelDownload();
        DownloadApi apiService = RetrofitHelper.getInstance().getApiService(DownloadApi.class);
        String url = "https://imtt.dd.qq.com/16891/apk/AB92915374D251277B4EF3465ECB751E.apk?fsname=cn.gov.tax.its_1.5.5_10505.apk&csr=1bbd";
        Observable<ResponseBody> responseBodyObservable = apiService.downLoad(url);
        FileDownloader.downloadFile2(responseBodyObservable, DOWNLOAD_APK_PATH, "test2.apk", new DownloadProgressHandler() {


            @Override
            public void onProgress(DownloadInfo downloadInfo) {
                int progress = downloadInfo.getProgress();
                long fileSize = downloadInfo.getFileSize();
                long speed = downloadInfo.getSpeed();
                long usedTime = downloadInfo.getUsedTime();
                mProgress.setText(progress + "%");
                mFileSize.setText(FileUtils.formatFileSize(fileSize));
                mRate.setText(FileUtils.formatFileSize(speed) + "/s");
                mTime.setText(FileUtils.formatTime(usedTime));
            }

            @Override
            public void onCompleted(File file) {
                showMsg("下载完成：" + file.getAbsolutePath());
            }

            @Override
            public void onError(Throwable e) {
                //异常请检查路径，网络等因素
                showMsg("下载文件异常:" + e.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileDownloader.cancelDownload();
    }

    public void download3(View view) {
        FileDownloader.cancelDownload();
        DownloadApi apiService = RetrofitHelper.getInstance().getApiService(DownloadApi.class);
        String url = "https://imtt.dd.qq.com/16891/apk/B168BCBBFBE744DA4404C62FD18FFF6F.apk?fsname=com.tencent.tmgp.sgame_1.61.1.6_61010601.apk&csr=1bbd";
        Observable<ResponseBody> responseBodyObservable = apiService.downLoad(url);

        FileDownloader.downloadFile2(responseBodyObservable, DOWNLOAD_APK_PATH, "test3.apk", new DownloadProgressHandler() {


            @Override
            public void onProgress(DownloadInfo downloadInfo) {
                int progress = downloadInfo.getProgress();
                long fileSize = downloadInfo.getFileSize();
                long speed = downloadInfo.getSpeed();
                long usedTime = downloadInfo.getUsedTime();
                mProgress.setText(progress + "%");
                mFileSize.setText(FileUtils.formatFileSize(fileSize));
                mRate.setText(FileUtils.formatFileSize(speed) + "/s");
                mTime.setText(FileUtils.formatTime(usedTime));
            }

            @Override
            public void onCompleted(File file) {
                showMsg("下载完成：" + file.getAbsolutePath());
            }

            @Override
            public void onError(Throwable e) {
                showMsg("下载文件异常:" + e.getMessage());
            }
        });
    }


}
