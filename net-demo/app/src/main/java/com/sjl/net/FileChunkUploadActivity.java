package com.sjl.net;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.sjl.net.upload.ByteProgressRequestBody;
import com.sjl.net.upload.FileChunkReq;
import com.sjl.net.upload.FileProgressRequestBody;
import com.sjl.net.upload.ResponseResult;
import com.sjl.net.upload.UploadApi;
import com.sjl.net.util.FileUtils;

import java.io.File;
import java.text.NumberFormat;
import java.util.List;

import butterknife.Bind;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;

/**
 * 分块上传示例
 *
 * @author Kelly
 * @version 1.0.0
 * @filename FileChunkUploadActivity
 * @time 2021/6/13 20:43
 * @copyright(C) 2021 song
 */
public class FileChunkUploadActivity extends BaseActivity {
    private static final String TAG = "FileChunkUploadActivity";

    @Bind(R.id.tv_progress)
    TextView mProgress;
    @Bind(R.id.tv_rate)
    TextView mRate;
    @Bind(R.id.tv_file_size)
    TextView mFileSize;
    @Bind(R.id.tv_msg)
    TextView tv_msg;
    @Bind(R.id.tv_time)
    TextView tv_time;

    @Override
    protected int getLayoutId() {
        return R.layout.file_chunk_upload_activity;
    }

    protected void initView() {

    }

    protected void initListener() {

    }

    private static final String UUID = "20210614001";
    private static final String TEST_APK = "test.apk";
    private File file;
    private UploadApi uploadApi;
    private long countUploadSize = 0;
    private String percent;
    private long totalSize = 0;
    private Object obj = new Object();
    private long startTime, usedTime;

    protected void initData() {
        uploadApi = RetrofitHelper.getInstance().getApiService(UploadApi.class);
        file = new File(Environment.getExternalStorageDirectory(), "test" + File.separator + TEST_APK);

    }


    public void uploadTest1(View view) {
        countUploadSize = 0;
        clearDir();
        totalSize = file.length();
        List<File> cut = FileUtils.split(file.getAbsolutePath(), 10);
        int totalChunk = cut.size();
        FileChunkReq[] items = new FileChunkReq[cut.size()];

        for (int i = 0; i < cut.size(); i++) {
            FileChunkReq fileChunkReq = new FileChunkReq();
            fileChunkReq.file = cut.get(i);
            fileChunkReq.fileName = file.getName();
            fileChunkReq.fileSize = totalSize;
            fileChunkReq.chunkNum = i + 1;
            fileChunkReq.totalChunk = totalChunk;
            fileChunkReq.uuid = UUID;
            items[i] = fileChunkReq;
        }
        startTime = System.currentTimeMillis();
        Observable.fromArray(items)
                .concatMap(new Function<FileChunkReq, ObservableSource<ResponseResult>>() {
                    @Override
                    public ObservableSource<ResponseResult> apply(FileChunkReq fcq) throws Exception {
                        return upload1(fcq);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseResult>() {
                    @Override
                    public void accept(ResponseResult responseResult) throws Exception {
                        showMsg("分块上传方案1responseResult：" + responseResult.toString());

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showMsg("分块上传方案1异常:" + throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        showMsg("分块上传方案1完毕");
                    }
                });

    }

    private long offset;

    private void clearDir() {
        final File apkDir = new File(Environment.getExternalStorageDirectory(), "test");
        File[] files = apkDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (!file.getName().equals(TEST_APK) && file.isFile()) {
                file.delete();
            }
        }
    }

    public void uploadTest2(View view) {
        countUploadSize = 0;
        offset = 0;
        long length = file.length();
        totalSize = length;
        startTime = System.currentTimeMillis();
        Observable.create(new ObservableOnSubscribe<FileChunkReq>() {

            @Override
            public void subscribe(ObservableEmitter<FileChunkReq> emitter) throws Exception {
                int blockSize = 1024 * 1024; //1M
                while (offset < totalSize) {
                    byte[] block = FileUtils.getBlock(offset, file, blockSize);
                    if (block != null) {
                        FileChunkReq fileChunkReq = new FileChunkReq();
                        fileChunkReq.fileByte = block;
                        fileChunkReq.fileName = file.getName();
                        fileChunkReq.fileSize = totalSize;
                        fileChunkReq.offset = offset;
                        fileChunkReq.uuid = UUID;
                        if (!emitter.isDisposed()){
                            emitter.onNext(fileChunkReq);
                            //下一个偏移量
                            offset += block.length;
                        }
                    }
                }
                if (!emitter.isDisposed()){
                    emitter.onComplete();
                }

            }
        }).concatMap(new Function<FileChunkReq, ObservableSource<ResponseResult>>() {
            @Override
            public ObservableSource<ResponseResult> apply(FileChunkReq fileChunkReq) throws Exception {
                return upload2(fileChunkReq);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseResult>() {
                    @Override
                    public void accept(ResponseResult responseResult) throws Exception {
                        showMsg("分块上传方案2responseResult：" + responseResult.toString());

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showMsg("分块上传方案2异常:" + throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        showMsg("分块上传方案2完毕");
                    }
                });
    }

    private void showMsg(String message) {
        if (isDestroy(this)) {
            return;
        }
        tv_msg.setText(message);
    }

    /**
     * 分块上传1
     *
     * @param fileChunkReq
     * @return
     */
    private Observable<ResponseResult> upload1(FileChunkReq fileChunkReq) {
        //"application/octet-stream"
        //"multipart/form-data"
        FileProgressRequestBody filePart = new FileProgressRequestBody(fileChunkReq.file, "application/octet-stream", new FileProgressRequestBody.ProgressListener() {
            @Override
            public void progress(long upload, long totalUpload) {
                synchronized (obj) {
                    showProgress(upload);
                    Log.w(TAG, "上传方案1,countUploadSize:" + countUploadSize + ",percent:" + percent);
                }
            }
        });


        final MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", fileChunkReq.file.getName(), filePart)
                .addFormDataPart("fileName", fileChunkReq.fileName)
                .addFormDataPart("fileSize", String.valueOf(fileChunkReq.fileSize))
                .addFormDataPart("uuid", fileChunkReq.uuid)
                .addFormDataPart("chunkNum", String.valueOf(fileChunkReq.chunkNum))
                .addFormDataPart("totalChunk", String.valueOf(fileChunkReq.totalChunk))
                .build();

        Observable<ResponseResult> observable = uploadApi.upload1(requestBody);
        return observable;
    }

    /**
     * 分块上传2
     *
     * @param fileChunkReq
     * @return
     */
    private ObservableSource<ResponseResult> upload2(FileChunkReq fileChunkReq) {
        ByteProgressRequestBody filePart = new ByteProgressRequestBody(fileChunkReq.fileByte, "application/octet-stream", new FileProgressRequestBody.ProgressListener() {
            @Override
            public void progress(long upload, long totalUpload) {
                synchronized (obj) {
                    showProgress(upload);
                    Log.w(TAG, "上传方案2,countUploadSize:" + countUploadSize + ",percent:" + percent);
                }
            }
        });


        final MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", fileChunkReq.fileName, filePart)
                .addFormDataPart("offset", String.valueOf(fileChunkReq.offset))
                .addFormDataPart("fileName", fileChunkReq.fileName)
                .addFormDataPart("fileSize", String.valueOf(fileChunkReq.fileSize))
                .addFormDataPart("uuid", fileChunkReq.uuid)
                .build();

        Observable<ResponseResult> observable = uploadApi.upload2(requestBody);
        return observable;
    }

    /**
     * @param upload 当前已经上传大小
     */
    private void showProgress(long upload) {
        countUploadSize += upload;
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        percent = numberFormat.format((float) countUploadSize / (float) totalSize * 100);
        usedTime = (System.currentTimeMillis() - startTime) / 1000;
        if (usedTime == 0) {
            usedTime = 1;
        }
        final long speed = (countUploadSize / usedTime); // 平均每秒下载速度
        if (isDestroy(this)) {
            return;
        }
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                mFileSize.setText(FileUtils.formatFileSize(totalSize));
                mProgress.setText(percent + "%");
                mRate.setText(FileUtils.formatFileSize(speed) + "/s");
                tv_time.setText(usedTime + "s");
            }
        });
    }

}
