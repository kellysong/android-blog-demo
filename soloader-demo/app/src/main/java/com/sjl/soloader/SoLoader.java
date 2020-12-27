package com.sjl.soloader;

import android.content.Context;
import android.util.Log;

import java.io.File;

import site.duqian.soloader.LoadLibraryUtil;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename SoLoader
 * @time 2020/12/27 12:35
 * @copyright(C) 2020 song
 */
public class SoLoader {
    /**
     * 加载 so 目录下的so文件
     *
     * @param dirName 下载的so
     * @return
     */
    public static boolean loadSoFile(Context context, String dirName) {
        try {
            File dir = context.getDir("libs", Context.MODE_PRIVATE);
            dir = new File(dir, dirName);

            if (!dir.exists()) {
                return false;
            }
            File[] files = dir.listFiles();
            if (files == null || files.length == 0) {
                return false;
            }
           return LoadLibraryUtil.installNativeLibraryPath(context.getApplicationContext().getClassLoader(), dir);
        } catch (Throwable throwable) {
            Log.e("SoLoader", "加载so文件异常", throwable);
        }
        return false;
    }
}