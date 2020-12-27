package site.duqian.soloader;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Locale;

/**
 * Description:工具类
 */
public class SoUtils {


    public static boolean isX86Phone() {
        final String archType = getCpuArchType();
        return !TextUtils.isEmpty(archType) && "so/x86".equals(archType.toLowerCase());
    }

    public static String getCpuArchType() {
        String arch = "";
        try {
            Class<?> clazz = Class.forName("android.os.SystemProperties");
            Method get = clazz.getDeclaredMethod("get", new Class[]{String.class});
            arch = (String) get.invoke(clazz, new Object[]{"ro.product.cpu.abi"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(arch)) {
            arch = Build.CPU_ABI;//可能不准确？
        }
        Log.d("dq getCpuArchType", "arch " + arch);
        return arch;
    }

    /**
     * 把so文件从asset目录拷贝到私有目录
     *
     * @param context
     * @param assetSoPath
     * @param out
     * @return
     */
    public static void copyAssetsDirectory(Context context, String assetSoPath, List<String> out) throws Exception {
        AssetManager assetManager = context.getAssets();
        String[] files = context.getAssets().list(assetSoPath);
        if (files == null || files.length == 0) {
            return;
        }

        for (String path : files) {

            if (path.contains(".")) {
                //so/armeabi
                String dirName = getParentDir(assetSoPath);
                File dir = context.getDir("libs", Context.MODE_PRIVATE);
                dir = new File(dir, dirName);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                String targetSoFilePath = dir.getAbsolutePath() + "/" + path;
                boolean b = copyAssetFile(assetManager, assetSoPath + "/" + path, targetSoFilePath);
                if (b){
                    out.add(targetSoFilePath);
                }
            } else {
                copyAssetsDirectory(context, assetSoPath + "/" + path, out);
            }

            /**
             *    if (file.contains(".")) {
             *                     res &= copyAssetFile(assetManager, assetSoPath + "/" + file, toPath + "/" + file);
             *                 } else {
             *                     res &= copyAssetsDirectory(context, assetSoPath + "/" + file, toPath + "/" + file);
             *                 }
             */

        }
    }


    /**
     * 把so文件从sd目录拷贝到私有目录
     *
     * @param context
     * @param sdSoPath
     * @param out
     */
    public static void copySDDirectory(Context context, String sdSoPath, List<String> out) throws Exception {
        File[] files = new File(sdSoPath).listFiles();
        if (files == null || files.length == 0) {
            return;
        }
        for (int i = 0; i < files.length; i++) {
            File so = files[i];
            if (so.exists()) {
                File dir = context.getDir("libs", Context.MODE_PRIVATE);
                String dirName = getParentDir(so);

                dir = new File(dir, dirName);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File target = new File(dir, so.getName());
                if (target.exists()) {
                    out.add(target.getAbsolutePath());
                    continue;
                }
                if (!target.exists()) {
                    target.createNewFile();
                }
                fileCopy(so, target);
                out.add(target.getAbsolutePath());
            }
        }
    }

    public static String getParentDir(File file) {
        String strParentDirectory = file.getParent();
        return getParentDir(strParentDirectory);
    }
    public static String getParentDir(String path) {
        String resultParentDirectory=path.replaceAll("\\\\", "/");
        String arr[] = resultParentDirectory.split("/");
        return arr[arr.length-1];
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

    public static boolean isExist(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        boolean exist;
        try {
            File file = new File(path);
            exist = file.exists();
        } catch (Exception e) {
            exist = false;
        }
        return exist;
    }

    /**
     * 删除文件或文件夹(包括目录下的文件)
     *
     * @param filePath
     */
    public static boolean deleteFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        try {
            File f = new File(filePath);
            if (f.exists() && f.isDirectory()) {
                File[] delFiles = f.listFiles();
                if (delFiles != null && delFiles.length > 0) {
                    for (int i = 0; i < delFiles.length; i++) {
                        deleteFile(delFiles[i].getAbsolutePath());
                    }
                }
            }
            f.delete();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean copyAssetFile(AssetManager assetManager, String fromAssetPath, String toPath) {
        if (assetManager == null || TextUtils.isEmpty(fromAssetPath) || TextUtils.isEmpty(toPath)) {
            return false;
        }
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        try {
            final File file = new File(toPath);
            file.delete();
            file.getParentFile().mkdirs();
            InputStream inputStream = assetManager.open(fromAssetPath);
            bis = new BufferedInputStream(inputStream);
            fos = new FileOutputStream(toPath);
            byte[] buf = new byte[1024];
            int read;
            while ((read = bis.read(buf)) != -1) {
                fos.write(buf, 0, read);
            }
            fos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }

            } catch (Exception e) {
            }

            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
            }
        }
        return false;
    }

    /**
     * /system/lib64/libart.so
     */
    private static boolean isART64(Context context) {
        final String fileName = "art";
        try {
            ClassLoader classLoader = context.getClassLoader();
            Class<?> cls = ClassLoader.class;
            Method method = cls.getDeclaredMethod("findLibrary", String.class);
            Object object = method.invoke(classLoader, fileName);
            if (object != null) {
                return ((String) object).contains("lib64");
            }
        } catch (Exception e) {
            //如果发生异常就用方法②
            return is64bitCPU();
        }

        return false;
    }

    private static boolean is64bitCPU() {
        String CPU_ABI = null;
        if (Build.VERSION.SDK_INT >= 21) {
            String[] CPU_ABIS = Build.SUPPORTED_ABIS;
            if (CPU_ABIS.length > 0) {
                CPU_ABI = CPU_ABIS[0];
            }
        } else {
            CPU_ABI = Build.CPU_ABI;
        }
        return CPU_ABI != null && CPU_ABI.contains("arm64");
    }


    /**
     * ELF文件头 e_indent[]数组文件类标识索引
     */
    private static final int EI_CLASS = 4;
    /**
     * ELF文件头 e_indent[EI_CLASS]的取值：ELFCLASS32表示32位目标
     */
    private static final int ELFCLASS32 = 1;
    /**
     * ELF文件头 e_indent[EI_CLASS]的取值：ELFCLASS64表示64位目标
     */
    private static final int ELFCLASS64 = 2;

    /**
     * The system property key of CPU arch type
     */
    private static final String CPU_ARCHITECTURE_KEY_64 = "ro.product.cpu.abilist64";

    /**
     * The system libc.so file path
     */
    private static final String SYSTEM_LIB_C_PATH = "/system/lib/libc.so";
    private static final String SYSTEM_LIB_C_PATH_64 = "/system/lib64/libc.so";
    private static final String PROC_CPU_INFO_PATH = "/proc/cpuinfo";


    private static String getSystemProperty(String key, String defaultValue) {
        String value = defaultValue;
        try {
            Class<?> clazz = Class.forName("android.os.SystemProperties");
            Method get = clazz.getMethod("get", String.class, String.class);
            value = (String) (get.invoke(clazz, key, ""));
        } catch (Exception e) {
            Log.d("dq getSystemProperty", "key = " + key + ", error = " + e.getMessage());
        }
        Log.d("dq getSystemProperty", key + " = " + value);
        return value;
    }

    /**
     * Read the first line of "/proc/cpuinfo" file, and check if it is 64 bit.
     */
    private static boolean isCPUInfo64() {
        File cpuInfo = new File(PROC_CPU_INFO_PATH);
        if (cpuInfo.exists()) {
            InputStream inputStream = null;
            BufferedReader bufferedReader = null;
            try {
                inputStream = new FileInputStream(cpuInfo);
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream), 512);
                String line = bufferedReader.readLine();
                if (line != null && line.length() > 0 && line.toLowerCase(Locale.US).contains("arch64")) {
                    return true;
                }
            } catch (Throwable t) {
            } finally {
                try {
                    if (bufferedReader != null) {
                        bufferedReader.close();
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
        return false;
    }

    /**
     * Check if system libc.so is 32 bit or 64 bit
     */
    private static boolean isLibc64() {
        File libcFile = new File(SYSTEM_LIB_C_PATH);
        if (libcFile.exists()) {
            byte[] header = readELFHeadrIndentArray(libcFile);
            if (header != null && header[EI_CLASS] == ELFCLASS64) {
                Log.d("dq isLibc64()", SYSTEM_LIB_C_PATH + " is 64bit");
                return true;
            }
        }

        File libcFile64 = new File(SYSTEM_LIB_C_PATH_64);
        if (libcFile64.exists()) {
            byte[] header = readELFHeadrIndentArray(libcFile64);
            if (header != null && header[EI_CLASS] == ELFCLASS64) {
                Log.d("dq isLibc64()", SYSTEM_LIB_C_PATH_64 + " is 64bit");
                return true;
            }
        }

        return false;
    }

    /**
     * ELF文件头格式是固定的:文件开始是一个16字节的byte数组e_indent[16]
     * e_indent[4]的值可以判断ELF是32位还是64位
     */
    private static byte[] readELFHeadrIndentArray(File libFile) {
        if (libFile != null && libFile.exists()) {
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(libFile);
                byte[] tempBuffer = new byte[16];
                int count = inputStream.read(tempBuffer, 0, 16);
                if (count == 16) {
                    return tempBuffer;
                }
            } catch (Throwable t) {
                Log.e("readELFHeadrIndentArray", "Error:" + t.toString());
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

}
