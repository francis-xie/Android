package com.basic.log.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import com.basic.log.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件相关
 */
public final class FileUtils {
    /**
     * 压缩文件的扩展名.
     */
    public static final String ZIP_EXT = ".zip";

    private FileUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 判断文件是否存在.
     *
     * @param filePath 路径
     * @return true - 存在，false - 不存在
     */
    public static boolean isExist(@NonNull String filePath) {
        return isExist(new File(filePath));
    }

    /**
     * 判断文件是否存在.
     *
     * @param file 文件
     * @return true - 存在，false - 不存在
     */
    public static boolean isExist(@NonNull File file) {
        return file.exists();
    }

    /**
     * 创建目录，若目录已存在则不处理.
     *
     * @param dirPath 目录路径
     * @return true - 目录存在（创建成功或已存在），false - 目录不存在
     */
    public static boolean createDir(@NonNull String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            return file.mkdirs();
        }
        return file.exists();
    }

    /**
     * 计算文件的大小.
     *
     * @param dirPath 待测量文件的路径
     * @return 文件的大小，单位byte
     */
    public static long calSize(@NonNull String dirPath) {
        File directory = new File(dirPath);
        return calSize(directory);
    }

    /**
     * 计算文件的大小.
     *
     * @param directory 待测量文件
     * @return 文件的大小，单位byte
     */
    public static long calSize(@NonNull File directory) {
        long size = 0L;
        if (directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.isDirectory()) {
                    size += calSize(file);
                } else {
                    size += file.length();
                }
            }
        } else if (directory.isFile()) {
            size += directory.length();
        }
        return size;
    }

    /**
     * 压缩文件.
     *
     * @param sourcePath 源文件的路径
     * @param destPath   目标文件路径
     * @param isClean    压缩完毕后是否清理
     */
    public static void zip(@NonNull String sourcePath, @NonNull String destPath, boolean isClean)
            throws IOException {
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(destPath));
            File sourceFile = new File(sourcePath);
            zip(zos, sourceFile, null);
            if (isClean) {
                // 压缩完毕后删除
                boolean deleteResult = delete(sourceFile);
                if (!deleteResult) {
                    android.util.Log.e("FileUtils", "delete file failed");
                }
            }
        } finally {
            CloseUtils.closeQuietly(zos);
        }
    }

    /**
     * 压缩文件.
     *
     * @param zos        ZipOutputStream
     * @param fileToZip  待压缩的文件
     * @param folderPath 父路径，可以为null
     * @throws IOException 压缩失败
     */
    private static void zip(ZipOutputStream zos, File fileToZip, String folderPath)
            throws IOException {
        String zipEntryName = fileToZip.getName();
        if (!TextUtils.isEmpty(folderPath)) {
            zipEntryName = folderPath + File.separator + fileToZip.getName();
        }
        if (fileToZip.isDirectory()) {
            for (File file : fileToZip.listFiles()) {
                zip(zos, file, zipEntryName);
            }
        } else {
            BufferedInputStream bis = null;
            try {
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];
                FileInputStream fis = new FileInputStream(fileToZip);
                bis = new BufferedInputStream(fis, bufferSize);
                zos.putNextEntry(new ZipEntry(zipEntryName));
                int length;
                while ((length = bis.read(buffer, 0, bufferSize)) != -1) {
                    zos.write(buffer, 0, length);
                }
                zos.closeEntry();
            } finally {
                CloseUtils.closeQuietly(bis);
            }
        }
    }

    /**
     * 获取目录下所有的压缩文件.
     *
     * @param logDir 日志目录
     * @return 压缩文件数组
     */
    public static File[] getZipFiles(@NonNull File logDir) {
        FilenameFilter zipFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.endsWith(ZIP_EXT);
            }
        };
        return logDir.listFiles(zipFilter);
    }

    /**
     * 删除文件.
     *
     * @param file 将要删除的文件
     * @return true - 删除成功，false - 删除失败
     */
    public static boolean delete(File file) {
        boolean success = true;
        if (file.isDirectory()) {
            for (File subFile : file.listFiles()) {
                success &= delete(subFile);
            }
            return success;
        }
        return file.delete();
    }

    /**
     * 按文件的最后修改时间正序排序，越近的越靠前，越早的越靠后.
     *
     * @param files 待排序的文件
     */
    public static void sortByModifyDate(File[] files) {
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                long diff = f1.lastModified() - f2.lastModified();
                if (diff > 0) {
                    return 1;
                } else if (diff == 0) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
    }

    /**
     * 按文件的最后修改时间倒序排序，越近的越靠后，越早的越靠前.
     *
     * @param files 待排序的文件
     */
    public static void sortByModifyDateDesc(File[] files) {
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                long diff = f1.lastModified() - f2.lastModified();
                if (diff > 0) {
                    return 1;
                } else if (diff == 0) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
    }

    public static boolean isSDCardExist() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable();
    }

    /**
     * 获取磁盘的缓存目录
     *
     * @param context
     * @return
     */
    public static String getDiskCacheDir(Context context) {
        return isSDCardExist() && context.getExternalCacheDir() != null ? context.getExternalCacheDir().getPath() : context.getCacheDir().getPath();
    }

    /**
     * 获取磁盘的缓存目录
     *
     * @param context
     * @return
     */
    public static String getDiskCacheDir(Context context, String fileDir) {
        return getDiskCacheDir(context) + File.separator + fileDir;
    }

    /**
     * Return a content URI for a given file.
     *
     * @param file The file.
     * @return a content URI for a given file
     */
    public static Uri getUriForFile(final File file) {
        if (file == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String authority = Log.getContext().getPackageName() + ".log.provider";
            return FileProvider.getUriForFile(Log.getContext(), authority, file);
        } else {
            return Uri.fromFile(file);
        }
    }
}