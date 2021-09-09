package com.sjl.net.dowload;

import com.sjl.net.RetrofitHelper;
import com.sjl.net.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename FileDownloadObservable
 * @time 2021/9/6 17:37
 * @copyright(C) 2021 song
 */
public class FileDownloadObservable {

    /**
     * 下载url
     */
    private String url;
    /**
     * 缓存的FIle
     */
    private File file;
    /**
     * 开始位置
     */
    private int startPosition;
    /**
     * 结束位置
     */
    private int endPosition;
    /**
     * 当前位置
     */
    private int curPosition;
    /**
     * 完成
     */
    private boolean finished = false;
    /**
     * 已经下载多少
     */
    private int downloadSize = 0;
    private Disposable disposable;
    private String name;

    public FileDownloadObservable(String url, File file, int startPosition,
                                  int endPosition) {
        this.url = url;
        this.file = file;
        this.startPosition = startPosition;
        this.curPosition = startPosition;
        this.endPosition = endPosition;
        this.name = "";
    }

    public void download() {
        DownloadApi apiService = RetrofitHelper.getInstance().getApiService(DownloadApi.class);
        String range = "bytes=" + (startPosition) + "-" + endPosition;

        apiService.download(range, url)
                .flatMap(new Function<ResponseBody, ObservableSource<Integer>>() {

                    @Override
                    public ObservableSource<Integer> apply(final ResponseBody responseBody) throws Exception {

                        return Observable.create(new ObservableOnSubscribe<Integer>() {
                            @Override
                            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                                InputStream inputStream = null;
                                long contentLength;

                                RandomAccessFile randomAccessFile = null;
                                byte[] buf = new byte[1024 * 8];
                                name = Thread.currentThread().getName();
                                try {
                                    inputStream = responseBody.byteStream();
                                    contentLength = responseBody.contentLength();
                                    System.out.println(name + ",startPosition " + startPosition + ",endPosition " + endPosition);
                                    randomAccessFile = new RandomAccessFile(file, "rwd");
                                    //设置开始写入位置
                                    randomAccessFile.seek(startPosition);
                                    System.out.println(name + "连接成功,读取长度：" + FileUtils.formatFileSize(contentLength));
                                    while (curPosition < endPosition) {
                                        //当前位置小于结束位置  继续下载
                                        int len = inputStream.read(buf);
                                        if (len == -1) {
                                            //下载完成
                                            System.out.println(len);
                                            break;
                                        }
                                        randomAccessFile.write(buf, 0, len);
                                        curPosition = curPosition + len;
                                        if (curPosition > endPosition) {    //如果下载多了，则减去多余部分
                                            System.out.println(name + "curPosition > endPosition  !!!!");
                                            int extraLen = curPosition - endPosition;
                                            downloadSize += (len - extraLen + 1);
                                        } else {
                                            downloadSize += len;
                                        }
//                                        emitter.onNext(downloadSize);
                                    }
                                    finished = true;  //当前阶段下载完成
                                    System.out.println("当前" + name + "下载完成");

                                    if (!emitter.isDisposed()) {
                                        emitter.onComplete();
                                    }
                                } catch (Exception e) {
                                    if (!emitter.isDisposed()) {
                                        emitter.onError(e);
                                    }
                                } finally {
                                    //关闭流
                                    if (inputStream != null) {
                                        try {
                                            inputStream.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    try {
                                        randomAccessFile.close();
                                    } catch (IOException e) {
                                        System.out.println("AccessFile IOException " + e.getMessage());
                                    }
                                }

                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.newThread())//新的线程下载
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Integer downloadSize) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println(name + "download error Exception " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * 是否完成当前段下载完成
     *
     * @return
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * 已经下载多少
     *
     * @return
     */
    public int getDownloadSize() {
        return downloadSize;
    }

    public Disposable getDisposable() {
        return disposable;
    }
}
