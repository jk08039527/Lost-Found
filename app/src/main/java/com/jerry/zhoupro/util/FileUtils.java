package com.jerry.zhoupro.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.jerry.zhoupro.MLog.Mlog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 2016/3/27.
 * FileUtils工具类：文件管理读取
 */
public class FileUtils {
    public static final String TAG = "FileUtils";

    public static final String SDCARD = Environment
            .getExternalStorageDirectory() + "/";

    /**
     * 在SD卡上创建文件
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static boolean creatSDFile(String fileName) throws IOException {
        return createFile(SDCARD, fileName);
    }

    /**
     * 创建文件
     *
     * @param filePath
     * @return
     */
    public static boolean createFile(String filePath) {
        String pathName = getFilePath(filePath);
        String fileName = getFileName(filePath);
        return createFile(pathName, fileName);
    }

    /**
     * 创建文件
     *
     * @param path     文件路径
     * @param filename 文件名
     * @return
     */
    public static boolean createFile(String path, String filename) {
        Boolean createFlg = false;
        if (creatDir(path)) {
            File file = new File(path + File.separator + filename);
            if (!file.exists() || !file.isFile()) {
                try {
                    // 创建新文件
                    createFlg = file.createNewFile();
                } catch (Exception e) {
                    Mlog.d(TAG, e.getMessage());
                }
            } else
                return true;
        }
        return createFlg;
    }

    /**
     * 在SD卡上创建目录
     *
     * @param dirPath
     */
    public static boolean creatDir(String dirPath) {
        File file = new File(dirPath);
        // 按照指定的路径创建文件夹
        if (!file.exists() || !file.isDirectory()) {
            return file.mkdirs();
        }
        return true;
    }

    /**
     * 删除指定路径文件夹
     *
     * @param dirPath
     */
    public static void deleteDir(String dirPath) {
        File file = new File(dirPath);
        // 判断文件夹是否存在
        if (file.exists() && file.isDirectory()) {
            //删除
            file.delete();
        }
    }

    /**
     * 删除指定路径文件
     *
     * @param filePath
     */
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        // 判断文件是否存在
        if (file.exists() && file.isFile()) {
            //删除
            file.delete();
        }
    }

    /**
     * 将一个InputStream里面的数据写入到文件中
     *
     * @param path
     * @param fileName
     * @param input
     * @return
     */
    public File writeFromInput(String path, String fileName,
                               InputStream input) {
        File file = null;
        OutputStream output = null;
        try {
            creatDir(path);
            if (createFile(path + fileName)) {
                file = new File(path + fileName);
                output = new FileOutputStream(file);
                byte buffer[] = new byte[4 * 1024];
                while ((input.read(buffer)) != -1) {
                    output.write(buffer);
                }
                output.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null)
                    output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 将数据写入到缓存
     *
     * @param path
     * @param data
     * @throws IOException
     */
    public static void writeToCache(String path, byte[] data) throws IOException {
        OutputStream os = null;
        try {
            os = new FileOutputStream(path);
            os.write(data);
        } catch (IOException e) {
            Mlog.w(TAG, "file cache(" + path + ") error!");
        } finally {
            if (null != os)
                os.close();
            os = null;
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param path
     * @return
     */
    public static Boolean exist(String path) {
        File file = new File(path);
        Boolean exist = false;
        try {
            exist = file.exists();
            file = null;
        } catch (Exception e) {
            Mlog.w(TAG, "file exists(" + path + ") error!");
        }
        return exist;
    }

    /**
     * 文件重命名
     *
     * @param oldPath
     * @param newPath
     * @return
     */
    public static boolean renameTo(String oldPath, String newPath) {
        boolean renameFlg = false;
        File ofile = new File(oldPath);
        if (ofile.exists()) {
            File nfile = new File(newPath);
            renameFlg = ofile.renameTo(nfile);
        }
        return renameFlg;
    }

    /**
     * 判断文件是否为图片格式(png,jpg)
     *
     * @param path
     * @return
     */
    public static boolean isImgFile(String path) {
        boolean isImgFile = false;
        // 获取扩展名
        String fileEnd = path.substring(path.lastIndexOf(".") + 1,
                path.length()).toLowerCase();
        if (fileEnd.equals("png") || fileEnd.equals("jpg")) {
            isImgFile = true;
        } else {
            isImgFile = false;
        }
        return isImgFile;
    }

    /**
     * 判断文件是否为pdf格式(pdf)
     *
     * @param path
     * @return
     */
    public static boolean isPdfFile(String path) {
        boolean isPdfFile = false;
        // 获取扩展名
        String fileEnd = path.substring(path.lastIndexOf(".") + 1,
                path.length()).toLowerCase();
        if (fileEnd.equals("pdf")) {
            isPdfFile = true;
        } else {
            isPdfFile = false;
        }
        return isPdfFile;
    }

    /**
     * 判断文件是否为Vedio格式(mp4,3gp)
     *
     * @param path
     * @return
     */
    public static boolean isVideoFile(String path) {
        boolean isVideoFile = false;
        // 获取扩展名
        String fileEnd = path.substring(path.lastIndexOf(".") + 1,
                path.length()).toLowerCase();
        if (fileEnd.equals("mp4") || fileEnd.equals("3gp")) {
            isVideoFile = true;
        } else {
            isVideoFile = false;
        }
        return isVideoFile;
    }

    /**
     * 获取文件名称
     *
     * @param filePath
     * @return
     */
    public static String getFileName(String filePath) {
        String fileName = filePath.substring(filePath.lastIndexOf("/")).substring(1);
        return fileName;
    }

    /**
     * 获取文件的目录
     *
     * @param filePath
     * @return
     */
    public static String getFilePath(String filePath) {
        return filePath.substring(0, filePath.lastIndexOf(File.separator));
    }

    public static Bitmap getDiskBitmap(String pathString) {
        Bitmap bitmap = null;
        try {
            File file = new File(pathString);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(pathString);
            }
        } catch (Exception e) {
        }
        return bitmap;
    }

}
