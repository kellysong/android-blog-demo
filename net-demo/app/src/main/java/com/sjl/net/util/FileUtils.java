package com.sjl.net.util;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    private static final String TAG = "FileUtils";

    /**
     * 格式化文件大小
     *
     * @param size
     * @return
     */
    public static String formatFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"b", "kb", "M", "G", "T"};
        //计算单位的，原理是利用lg,公式是 lg(1024^n) = nlg(1024)，最后 nlg(1024)/lg(1024) = n。
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        //计算原理是，size/单位值。单位值指的是:比如说b = 1024,KB = 1024^2
        return new DecimalFormat("#,##0.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    /**
     * 文件分块工具
     *
     * @param offset    起始偏移位置
     * @param file      文件
     * @param blockSize 分块大小
     * @return 分块数据
     */

    public static byte[] getBlock(long offset, File file, int blockSize) {
        byte[] result = new byte[blockSize];

        RandomAccessFile accessFile = null;

        try {
            accessFile = new RandomAccessFile(file, "r");

            accessFile.seek(offset);

            int readSize = accessFile.read(result);

            if (readSize == -1) {
                return null;

            } else if (readSize == blockSize) {
                return result;

            } else {
                byte[] tmpByte = new byte[readSize];

                System.arraycopy(result, 0, tmpByte, 0, readSize);

                return tmpByte;

            }

        } catch (IOException e) {
            Log.e(TAG, "", e);

        } finally {
            if (accessFile != null) {
                try {
                    accessFile.close();

                } catch (IOException e1) {
                }

            }

        }

        return null;

    }

    /**
     * 分割文件
     *
     * @param filePath 要分割的文件路径
     * @param num      要分割成多少份
     * @return
     */
    public static List<File> split(String filePath, int num) {
        File file = new File(filePath);
        long lon = file.length() / num;
        List<File> chunk = new ArrayList<>();
        try {
            RandomAccessFile src = new RandomAccessFile(file, "r");
            byte[] bytes = new byte[1024];
            int len;
            for (int i = 0; i < num; i++) {
                //分割添加索引，后台根据索引顺序依次合并
                String name = getFileBegin(filePath) + "_" + (i + 1) + getFileSuff(filePath);
                File temp = new File(name);
                RandomAccessFile target = new RandomAccessFile(temp, "rw");
                while ((len = src.read(bytes)) != -1) {
                    target.write(bytes, 0, len);
                    if (target.length() > lon)// 当生成的新文件字节数大于lon时，结束循环
                        break;
                }
                chunk.add(temp);
                target.close();
            }
            src.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chunk;
    }


    /**
     * 获取文件除后缀名外的名字
     *
     * @param filePath
     */
    public static String getFileBegin(String filePath) {
        String i = getFileSuff(filePath);
        int index = filePath.indexOf(i);
        return filePath.substring(0, index);
    }


    /**
     * 获取文件后缀名
     *
     * @param filePath
     */
    public static String getFileSuff(String filePath) {
        int start = filePath.lastIndexOf(".");
        int end = filePath.length();
        return filePath.substring(start, end);
    }

    public static String formatTime(long time) {
        String temp;
        if (time >= 60 && time <= 3600) {
            temp = Long.valueOf(time / 60) + "分" + time % 60 + "秒";
        } else {
            if (time > 3600) {
                temp = Long.valueOf(time / 3600) + "小时" + Long.valueOf(((time % 3600) / 60)) + "分" + time % 60 + "秒";
            } else {
                temp = time + "秒";
            }
        }
        return temp;
    }
}
