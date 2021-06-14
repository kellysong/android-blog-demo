package com.sjl.net.upload;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename FileProgressRequestBody
 * @time 2021/5/31 18:30
 * @copyright(C) 2021 song
 */
public class FileProgressRequestBody extends RequestBody {

    public interface ProgressListener {
        /**
         *
         * @param upload 当前上传
         * @param totalUpload 总上传
         */
        void progress(long upload,long totalUpload);
    }

    public static final int SEGMENT_SIZE = 2*1024;

    protected File file;
    protected ProgressListener listener;
    protected String contentType;

    public FileProgressRequestBody(File file, String contentType, ProgressListener listener) {
        this.file = file;
        this.contentType = contentType;
        this.listener = listener;
    }

    protected FileProgressRequestBody() {}

    @Override
    public long contentLength() {
        return file.length();
    }

    @Override
    public MediaType contentType() {
        return MediaType.parse(contentType);
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        Source source = null;
        try {
            source = Okio.source(file);
            long total = 0;
            long read;
            //每一块，循环读取，写文件流
            while ((read = source.read(sink.buffer(), SEGMENT_SIZE)) != -1) {
                total += read;
                sink.flush();
                this.listener.progress(read,total);
            }
        } finally {
            Util.closeQuietly(source);
        }
    }

}