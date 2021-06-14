package com.sjl.net.upload;

import java.io.IOException;

import okio.BufferedSink;


public class ByteProgressRequestBody extends FileProgressRequestBody {

    protected final byte[] content;

    public ByteProgressRequestBody(byte[] content, String contentType, ProgressListener listener) {
        super(null,contentType,listener);
        this.content = content;

    }

    @Override

    public long contentLength() {

        return content.length;

    }

    @Override

    public void writeTo(BufferedSink sink) throws IOException {

        int offset = 0;
        //每一块，分多次输出文件上传流
        int count = (int) (content.length / SEGMENT_SIZE + (content.length % SEGMENT_SIZE != 0 ? 1 : 0));
        for (int i = 0; i < count; i++) {

            int chunk = i != count - 1 ? SEGMENT_SIZE : content.length - offset;

            sink.buffer().write(content, offset, chunk);//每次写入SEGMENT_SIZE 字节

            sink.buffer().flush();

            offset += chunk;

            listener.progress(chunk,offset);

        }

    }


}