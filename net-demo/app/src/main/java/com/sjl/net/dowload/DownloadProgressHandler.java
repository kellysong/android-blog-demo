package com.sjl.net.dowload;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * 下载进度Handler
 *
 * @author Kelly
 * @version 1.0.0
 * @filename DownloadProgressHandler.java
 * @time 2018/7/25 15:25
 * @copyright(C) 2018 song
 */
public abstract class DownloadProgressHandler implements DownloadCallBack {
    public static final int DOWNLOAD_SUCCESS = 0;
    public static final int DOWNLOAD_PROGRESS = 1;
    public static final int DOWNLOAD_FAIL = 2;

    protected ResponseHandler mHandler = new ResponseHandler(this, Looper.getMainLooper());

    /**
     * 发送消息，更新进度
     *
     * @param what
     * @param downloadInfo
     */
    public void sendMessage(int what, DownloadInfo downloadInfo) {
        mHandler.obtainMessage(what, downloadInfo).sendToTarget();
    }


    /**
     * 处理消息
     * @param message
     */
    protected void handleMessage(Message message) {
        DownloadInfo progressBean = (DownloadInfo) message.obj;
        switch (message.what) {
            case DOWNLOAD_SUCCESS://下载成功
                onCompleted(progressBean.getFile());
                removeMessage();
                break;
            case DOWNLOAD_PROGRESS://下载中
                onProgress(progressBean);
                break;
            case DOWNLOAD_FAIL://下载失败
                onError(progressBean.getErrorMsg());
                break;
            default:
                removeMessage();
                break;
        }
    }

    private void removeMessage() {
        if (mHandler != null){
            mHandler.removeCallbacksAndMessages(null);
        }
    }


    protected static class ResponseHandler extends Handler {

        private DownloadProgressHandler mProgressHandler;

        public ResponseHandler(DownloadProgressHandler mProgressHandler, Looper looper) {
            super(looper);
            this.mProgressHandler = mProgressHandler;
        }

        @Override
        public void handleMessage(Message msg) {
            mProgressHandler.handleMessage(msg);
        }
    }
}
