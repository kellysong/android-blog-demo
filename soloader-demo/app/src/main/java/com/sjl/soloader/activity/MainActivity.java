package com.sjl.soloader.activity;

import android.Manifest;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.getkeepsafe.relinker.ReLinker;
import com.sjl.jni.JniDemo;
import com.sjl.soloader.R;
import com.sjl.soloader.SoLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import site.duqian.soloader.SoUtils;


public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int ACTION_REQUEST_PERMISSIONS = 0x001;
    boolean libraryExists = true;
    // Demo 所需的动态库文件
    private static final String[] LIBRARIES = new String[]{
            // 测试so
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.i(TAG, "SUPPORTED ABI:" + Arrays.toString(Build.SUPPORTED_ABIS));
        }
    }

    public void btnSoLoad(View view) {
        loadSoFromAssetPathCopy();
        libraryExists = checkSoFile(LIBRARIES);
        //原生so库默认存储路径
        ApplicationInfo applicationInfo = getApplicationInfo();
        Log.i(TAG, "onCreate: " + applicationInfo.nativeLibraryDir);
        if (!libraryExists) {
            Log.w(TAG, "so没找到");
        }
    }

    public void btnTestSo(View view) {
        String str = JniDemo.sayHelloWorld();
        textView.setText(str);
        int s = JniDemo.add(1, 3);
        textView.setText(textView.getText() + ",a+b=" + s);
    }


    private void loadSoFromAssetPathCopy() {
        List<String> out = new ArrayList<>();
        try {
            SoUtils.copyAssetsDirectory(this, "so", out);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //arm64-v8a, armeabi-v7a, armeabi
        Log.d(TAG, "supported api:" + Build.CPU_ABI + " " + Build.CPU_ABI2);

        if (Build.VERSION.SDK_INT >= 21) {
            String[] abis = Build.SUPPORTED_ABIS;
            for (String abi : abis) {
                if (loadSoFile(abi, out)) {
                    break;
                }
            }
        } else {
            if (TextUtils.isEmpty(Build.CPU_ABI)) {
                if (loadSoFile(Build.CPU_ABI, out)) {
                    return;
                }
            }
            if (TextUtils.isEmpty(Build.CPU_ABI2)) {
                if (loadSoFile(Build.CPU_ABI2, out)) {
                    return;
                }
            }
            if (loadSoFile("armeabi", out)) {
                return;
            }
        }

    }

    private boolean loadSoFile(String abi, List<String> out) {
        boolean success = false;
        for (final String soPath : out) {
            if (soPath.contains(abi)) {
                String parentDir = SoUtils.getParentDir(new File(soPath));
                //注入so路径，如果清除了的话。没有清除可以不用每次注入
                boolean b = SoLoader.loadSoFile(this, parentDir);
                //加载so库
                if (b && new File(soPath).exists()) {
                    final String soName = soPath.substring(soPath.lastIndexOf("/") + 1, soPath.lastIndexOf(".")).substring(3);
                /*    final String soName = soPath.substring(soPath.lastIndexOf("/") + 1, soPath.lastIndexOf(".")).substring(3);
                    System.loadLibrary(soName); //加载有可能直接崩掉
                    //System.load(soPath); //load使用的是文件绝对路径
                    */
                    //or 使用第三方库加载，这个加载报错回调failure，不会直接崩溃，底层也是 System.load实现，只不过加载之前做了一些验证
                    ReLinker.loadLibrary(this, soName, new ReLinker.LoadListener() {
                        @Override
                        public void success() {
                            Log.i(TAG, "加载成功:" + soPath);
                        }

                        @Override
                        public void failure(Throwable t) {
                            Log.e(TAG, "加载异常:" + soPath, t);
                        }
                    });
                    success = true;
                    break;
                }
            }
        }
        return success;
    }


    /**
     * 检查能否找到动态链接库，如果找不到，请修改工程配置
     *
     * @param libraries 需要的动态链接库
     * @return 动态库是否存在
     */
    private boolean checkSoFile(String[] libraries) {
        File dir = new File(getApplicationInfo().nativeLibraryDir);

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
