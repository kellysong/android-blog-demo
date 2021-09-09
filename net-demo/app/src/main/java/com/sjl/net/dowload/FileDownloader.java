package com.sjl.net.dowload;


import android.os.SystemClock;

import com.sjl.net.RetrofitHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * 文件下载器
 *
 * @author Kelly
 * @version 1.0.0
 * @filename FileDownloader.java
 * @time 2018/7/25 16:04
 * @copyright(C) 2018 song
 */
public class FileDownloader {
    static CompositeDisposable mDisposable = new CompositeDisposable();


    /**
     * 下载文件法1(使用Handler更新UI)
     *
     * @param observable      下载被观察者
     * @param destDir         下载目录
     * @param fileName        文件名
     * @param progressHandler 进度handler
     */
    public static void downloadFile(Observable<ResponseBody> observable, final String destDir, final String fileName, final DownloadProgressHandler progressHandler) {
        final DownloadInfo downloadInfo = new DownloadInfo();
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        InputStream inputStream = null;
                        long total = 0;
                        long responseLength;
                        FileOutputStream fos = null;
                        try {
                            byte[] buf = new byte[1024 * 8];
                            int len;
                            responseLength = responseBody.contentLength();
                            inputStream = responseBody.byteStream();
                            File dir = new File(destDir);
                            if (!dir.exists()) {
                                dir.mkdirs();
                            }
                            final File file = new File(destDir, fileName);
                            downloadInfo.setFile(file);
                            downloadInfo.setFileSize(responseLength);
                            fos = new FileOutputStream(file);
                            int progress = 0;
                            int lastProgress;
                            long startTime = System.currentTimeMillis(); // 开始下载时获取开始时间
                            while ((len = inputStream.read(buf)) != -1) {
                                fos.write(buf, 0, len);
                                total += len;
                                lastProgress = progress;
                                progress = (int) (total * 100 / responseLength);
                                long curTime = System.currentTimeMillis();
                                long usedTime = (curTime - startTime) / 1000;
                                if (usedTime == 0) {
                                    usedTime = 1;
                                }
                                long speed = (total / usedTime); // 平均每秒下载速度
                                // 如果进度与之前进度相等，则不更新，如果更新太频繁，则会造成界面卡顿
                                if (progress > 0 && progress != lastProgress) {
                                    downloadInfo.setSpeed(speed);
                                    downloadInfo.setProgress(progress);
                                    downloadInfo.setCurrentSize(total);
                                    downloadInfo.setUsedTime(usedTime);
                                    progressHandler.sendMessage(DownloadProgressHandler.DOWNLOAD_PROGRESS, downloadInfo);
                                }
                            }
                            fos.flush();
                            downloadInfo.setFile(file);
                            progressHandler.sendMessage(DownloadProgressHandler.DOWNLOAD_SUCCESS, downloadInfo);

                        } catch (final Exception e) {
                            downloadInfo.setErrorMsg(e);
                            progressHandler.sendMessage(DownloadProgressHandler.DOWNLOAD_FAIL, downloadInfo);
                        } finally {
                            try {
                                if (fos != null) {
                                    fos.close();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                if (inputStream != null) {
                                    inputStream.close();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {//new Consumer<Throwable>
                        downloadInfo.setErrorMsg(e);
                        progressHandler.sendMessage(DownloadProgressHandler.DOWNLOAD_FAIL, downloadInfo);

                    }

                    @Override
                    public void onComplete() {// new Action()

                    }
                });
    }


    /**
     * 下载文件法2(使用RXJava更新UI)
     *
     * @param observable
     * @param destDir
     * @param fileName
     * @param progressHandler
     */
    public static void downloadFile2(Observable<ResponseBody> observable, final String destDir, final String fileName, final DownloadProgressHandler progressHandler) {
        final DownloadInfo downloadInfo = new DownloadInfo();
        observable
                .flatMap(new Function<ResponseBody, ObservableSource<DownloadInfo>>() {

                    @Override
                    public ObservableSource<DownloadInfo> apply(final ResponseBody responseBody) throws Exception {

                        return Observable.create(new ObservableOnSubscribe<DownloadInfo>() {
                            @Override
                            public void subscribe(ObservableEmitter<DownloadInfo> emitter) throws Exception {
                                InputStream inputStream = null;
                                long total = 0;
                                long responseLength = 0;
                                FileOutputStream fos = null;
                                try {
                                    byte[] buf = new byte[1024 * 8];
                                    int len = 0;
                                    responseLength = responseBody.contentLength();
                                    inputStream = responseBody.byteStream();

                                    File dir = new File(destDir);
                                    if (!dir.exists()) {
                                        dir.mkdirs();
                                    }
                                    final File file = new File(destDir, fileName);
                                    downloadInfo.setFile(file);
                                    downloadInfo.setFileSize(responseLength);

                                    fos = new FileOutputStream(file);
                                    int progress = 0;
                                    int lastProgress = 0;
                                    long startTime = System.currentTimeMillis(); // 开始下载时获取开始时间
                                    while ((len = inputStream.read(buf)) != -1) {
                                        fos.write(buf, 0, len);
                                        total += len;
                                        lastProgress = progress;
                                        progress = (int) (total * 100 / responseLength);
                                        long curTime = System.currentTimeMillis();
                                        long usedTime = (curTime - startTime) / 1000;
                                        if (usedTime == 0) {
                                            usedTime = 1;
                                        }
                                        long speed = (total / usedTime); // 平均每秒下载速度
                                        // 如果进度与之前进度相等，则不更新，如果更新太频繁，则会造成界面卡顿
                                        if (progress > 0 && progress != lastProgress) {
                                            downloadInfo.setSpeed(speed);
                                            downloadInfo.setProgress(progress);
                                            downloadInfo.setCurrentSize(total);
                                            downloadInfo.setUsedTime(usedTime);
                                            if (!emitter.isDisposed()) {
                                                emitter.onNext(downloadInfo);
                                            }
                                        }
                                    }
                                    fos.flush();
                                    downloadInfo.setFile(file);
                                    if (!emitter.isDisposed()) {
                                        emitter.onComplete();
                                    }
                                } catch (Exception e) {
                                    downloadInfo.setErrorMsg(e);
                                    if (!emitter.isDisposed()) {
                                        emitter.onError(e);
                                    }
                                } finally {
                                    try {
                                        if (fos != null) {
                                            fos.close();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        if (inputStream != null) {
                                            inputStream.close();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DownloadInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(DownloadInfo downloadInfo) {
                        progressHandler.onProgress(downloadInfo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressHandler.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        progressHandler.onCompleted(downloadInfo.getFile());
                    }
                });
    }


    /**
     * 下载文件法3(多线程文件下载，使用RXJava更新UI)
     *
     * @param threadNum       下载线程数
     * @param url
     * @param destDir
     * @param fileName
     * @param progressHandler
     */
    public static void downloadFile3(final int threadNum, final String url, final String destDir, final String fileName, final DownloadProgressHandler progressHandler) {
        DownloadApi apiService = RetrofitHelper.getInstance().getApiService(DownloadApi.class);
        final DownloadInfo downloadInfo = new DownloadInfo();
        Observable<ResponseBody> getFileSizeObservable = apiService.downLoad(url);
        final FileDownloadObservable[] fileDownloadObservables = new FileDownloadObservable[threadNum];//设置线程数量
        //获取文件大小，分割文件下载
        getFileSizeObservable
                .flatMap(new Function<ResponseBody, ObservableSource<DownloadInfo>>() {

                    @Override
                    public ObservableSource<DownloadInfo> apply(final ResponseBody responseBody) throws Exception {

                        return Observable.create(new ObservableOnSubscribe<DownloadInfo>() {
                            @Override
                            public void subscribe(ObservableEmitter<DownloadInfo> emitter) throws Exception {
                                long fileSize = responseBody.contentLength();


                                File dir = new File(destDir);
                                if (!dir.exists()) {
                                    dir.mkdirs();
                                }
                                final File file = new File(destDir, fileName);
                                if (file.exists()){
                                    file.delete();
                                }
                                downloadInfo.setFile(file);
                                downloadInfo.setFileSize(fileSize);
                                RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");
                                //设置本地文件的长度和下载文件相同
                                accessFile.setLength(fileSize);
                                accessFile.close();
                                //每条线程下载数据
                                long blockSize = ((fileSize % threadNum) == 0) ? (fileSize / threadNum) : (fileSize / threadNum + 1);
                                //指定每条线程下载的区间
                                for (int i = 0; i < threadNum; i++) {
                                    int curThreadEndPosition = (int) ((i + 1) != threadNum ? ((i + 1) * blockSize - 1) : fileSize);
                                    FileDownloadObservable fileDownloadObservable = new FileDownloadObservable(url, file, (int) (i * blockSize), curThreadEndPosition);
                                    fileDownloadObservable.download();
                                    fileDownloadObservables[i] = fileDownloadObservable;
                                    mDisposable.add(fileDownloadObservable.getDisposable());

                                }
                                boolean finished = false;
                                long startTime = System.currentTimeMillis();
                                int downloadSize;
                                int progress;
                                long usedTime;
                                long curTime;
                                boolean completed;
                                int speed;
                                //根据所有线程下载大小，计算下载进度、下载速率，下载用时
                                while (!finished && !emitter.isDisposed()) {
                                    downloadSize = 0;
                                    finished = true;
                                    for (int i = 0; i < fileDownloadObservables.length; i++) {
                                        downloadSize += fileDownloadObservables[i].getDownloadSize();
                                        if (!fileDownloadObservables[i].isFinished()) {
                                            finished = false;
                                        }
                                    }
                                    progress = (int) ((downloadSize * 1.0 / fileSize) * 100);
                                    curTime = System.currentTimeMillis();
                                    usedTime = (curTime - startTime) / 1000;
                                    if (usedTime == 0) usedTime = 1;
                                    speed = (int) (downloadSize / usedTime);
                                    downloadInfo.setSpeed(speed);
                                    downloadInfo.setProgress(progress);
                                    downloadInfo.setCurrentSize(downloadSize);
                                    downloadInfo.setUsedTime(usedTime);
                                    //回显下载信息
                                    if (!emitter.isDisposed()) {
                                        emitter.onNext(downloadInfo);
                                    }
                                    SystemClock.sleep(1000);
                                }
                                completed = true;
                                if (!emitter.isDisposed()) {
                                    if (completed) {
                                        emitter.onComplete();
                                    } else {
                                        emitter.onError(new RuntimeException("下载失败"));
                                    }
                                }

                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DownloadInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(DownloadInfo downloadInfo) {
                        progressHandler.onProgress(downloadInfo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressHandler.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        progressHandler.onCompleted(downloadInfo.getFile());
                    }
                });

    }


    public static void cancelDownload() {
        mDisposable.clear();

    }


}
