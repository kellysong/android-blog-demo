package com.sjl.soloader.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.VersionInfo;
import com.getkeepsafe.relinker.ReLinker;
import com.sjl.jni.JniDemo;
import com.sjl.soloader.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import site.duqian.soloader.SoFileLoadManager;
import site.duqian.soloader.SoUtils;


public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private static final int ACTION_REQUEST_PERMISSIONS = 0x001;
    // 在线激活所需的权限
    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE
    };
    boolean libraryExists = true;
    // Demo 所需的动态库文件
    private static final String[] LIBRARIES = new String[]{
            // 人脸相关
            "JniDemo.so"
    };
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        textView = findViewById(R.id.tv_msg);
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};
            for (String str : permissions) {
                if (checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                } else {
                }
            }
        }


//        startLoadSoFromLocalPath();
//        libraryExists = checkSoFile(LIBRARIES);
        ApplicationInfo applicationInfo = getApplicationInfo();
        Log.i(TAG, "onCreate: " + applicationInfo.nativeLibraryDir);
        if (!libraryExists) {
            showToast("so没找到");
        } else {
            VersionInfo versionInfo = new VersionInfo();
            int code = FaceEngine.getVersion(versionInfo);
            Log.i(TAG, "onCreate: getVersion, code is: " + code + ", versionInfo is: " + versionInfo);
        }

    }

    public void sayHelloWorld() {
        String s = JniDemo.sayHelloWorld();
        textView.setText(s);
    }

    public void add() {
        int s = JniDemo.add(1, 3);
        textView.setText("a+b=" + s);
    }

    private void startLoadSoFromLocalPath() {
        List<String> out = new ArrayList<>();
        String cpuArchType = SoUtils.getCpuArchType();
        copy(Environment.getExternalStorageDirectory().toString() + "/temp2/" + cpuArchType, cpuArchType, out);
        for (String soPath : out) {
            //注入so路径，如果清除了的话。没有清除可以不用每次注入
            SoFileLoadManager.loadSoFile(this, cpuArchType);
            //加载so库
            if (new File(soPath).exists()) {
                final String soName = soPath.substring(soPath.lastIndexOf("/") + 1, soPath.lastIndexOf(".")).substring(3);
                System.loadLibrary(soName);
                //https://github.com/KeepSafe/ReLinker
                ReLinker.loadLibrary(this, soName, new ReLinker.LoadListener() {
                    @Override
                    public void success() {
                        Log.i(TAG, "加载成功:" + soName);
                    }

                    @Override
                    public void failure(Throwable t) {
                        Log.e(TAG, "加载异常:" + soName, t);
                    }
                });
            }

        }


    }


    /**
     * 拷贝到私有目录加载
     *
     * @param soPath
     * @param dirName
     * @param out
     */
    private void copy(String soPath, String dirName, List<String> out) {
        File file = new File(soPath);
        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            return;
        }
        for (int i = 0; i < files.length; i++) {
            File so = files[i];
            if (so.exists()) {
                File dir = getDir("libs", Context.MODE_PRIVATE);
                dir = new File(dir, dirName);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File target = new File(dir, so.getName());
                if (target.exists()) {
                    out.add(target.getAbsolutePath());
                    continue;
                }
                try {
                    if (!target.exists()) {
                        target.createNewFile();
                    }
                    fileCopy(so, target);
                    out.add(target.getAbsolutePath());
                } catch (Exception e) {
                    Log.e("SIMPLE_LOGGER", "拷贝异常：" + target.getAbsolutePath());
                }


            }
        }
    }

    public static void fileCopy(File source, File target) throws IOException {
        FileChannel inChannel = new FileInputStream(source).getChannel();
        FileChannel outChannel = new FileOutputStream(target).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
            int maxCount = (64 * 1024 * 1024) - (32 * 1024);
            long size = inChannel.size();
            long position = 0;
            while (position < size) {
                position += inChannel.transferTo(position, maxCount, outChannel);
            }
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }

    /**
     * 检查能否找到动态链接库，如果找不到，请修改工程配置
     *
     * @param libraries 需要的动态链接库
     * @return 动态库是否存在
     */
    private boolean checkSoFile(String[] libraries) {
        File dir = new File(getApplicationInfo().nativeLibraryDir);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.i("SIMPLE_LOGGER", "SUPPORTED ABI:" + Arrays.toString(Build.SUPPORTED_ABIS));
        }
        File file2 = new File(dir, "text.txt");
        if (!file2.exists()) {
            try {
                file2.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return false;
        }
        List<String> libraryNameList = new ArrayList<>();
        for (File file : files) {
            libraryNameList.add(file.getName());
        }
        boolean exists = true;
        for (String library : libraries) {
            exists &= libraryNameList.contains(library);
        }
        return exists;
    }


    @Override
    void afterRequestPermission(int requestCode, boolean isAllGranted) {
        if (requestCode == ACTION_REQUEST_PERMISSIONS) {
            if (isAllGranted) {

            } else {
                showToast("权限拒绝");
            }
        }
    }


}
