package com.sjl.net.upload;

import java.io.File;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename FileChunkReq
 * @time 2021/6/14 13:28
 * @copyright(C) 2021 song
 */
public class FileChunkReq {
    //方案一
    public File file;
    public int chunkNum;
    public int totalChunk;

    /**
     * 公共
     */
    public String fileName;
    public long fileSize;
    public String uuid;

    //方案二
    public byte[] fileByte;
    public long offset;


    @Override
    public String toString() {
        return "FileChunkReq{" +
                ", chunkNum=" + chunkNum +
                ", totalChunk=" + totalChunk +
                ", fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                ", uuid='" + uuid + '\'' +
                ", offset=" + offset +
                '}';
    }

}
