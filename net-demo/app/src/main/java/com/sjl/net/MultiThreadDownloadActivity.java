package com.sjl.net;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.sjl.net.dowload.DownloadInfo;
import com.sjl.net.dowload.DownloadProgressHandler;
import com.sjl.net.dowload.FileDownloader;
import com.sjl.net.util.FileUtils;

import java.io.File;
import java.text.NumberFormat;

import butterknife.Bind;

/**
 * 多线程文件下载示例
 *
 * @author Kelly
 * @version 1.0.0
 * @filename MultiThreadDownloadActivity
 * @time 2021/9/6 16:56
 * @copyright(C) 2021 song
 */
public class MultiThreadDownloadActivity extends BaseActivity {
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
    @Bind(R.id.spinner)
    Spinner spinner;
    @Override
    protected int getLayoutId() {
        return R.layout.multi_thread_download_activity;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }
    int threadNum;
    @Override
    protected void initData() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object selectedItem = spinner.getSelectedItem();
                threadNum = Integer.parseInt(selectedItem.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setSelection(2,false);
    }

    public void multiThreadDownloadTest(View view) {

        String url = "https://imtt.dd.qq.com/16891/apk/B168BCBBFBE744DA4404C62FD18FFF6F.apk?fsname=com.tencent.tmgp.sgame_1.61.1.6_61010601.apk&csr=1bbd";
        final NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        FileDownloader.downloadFile3(threadNum, url, FileDownloadActivity.DOWNLOAD_APK_PATH, "multi_test.apk", new DownloadProgressHandler() {

            @Override
            public void onProgress(DownloadInfo downloadInfo) {

                long fileSize = downloadInfo.getFileSize();
                long speed = downloadInfo.getSpeed();
                long usedTime = downloadInfo.getUsedTime();
                long currentSize = downloadInfo.getCurrentSize();
                String percent = numberFormat.format((float) currentSize / (float) fileSize * 100);
                mProgress.setText(percent + "%");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileDownloader.cancelDownload();
    }
}
