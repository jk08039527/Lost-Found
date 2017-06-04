package com.jerry.zhoupro.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

/**
 * Created by Administrator on 2016/3/27.
 * FileUtils工具类：文件管理读取
 */
public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();

    /**
     * 获取文件名称
     *
     * @param filePath
     * @return
     */
    private static String getFileName(String filePath) {
        return filePath.substring(filePath.lastIndexOf("/")).substring(1);
    }

    /**
     * 获取文件的目录
     *
     * @param filePath
     * @return
     */
    private static String getFilePath(String filePath) {
        return filePath.substring(0, filePath.lastIndexOf(File.separator));
    }

    /**
     * 创建文件
     *
     * @param filePath
     * @return
     */
    public static boolean createFile(String filePath) {
        String dirPath = getFilePath(filePath);
        String fileName = getFileName(filePath);
        return createFile(dirPath, fileName);
    }

    /**
     * 创建文件
     *
     * @param dirPath  文件路径
     * @param filename 文件名
     * @return
     */
    private static boolean createFile(String dirPath, String filename) {
        File dir = new File(dirPath);
        // 按照指定的路径创建文件夹
        if (dir.exists() || dir.mkdirs()) {
            File file = new File(dirPath + File.separator + filename);
            if (!file.exists()) {
                // 创建新文件
                try {
                    return file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    /**
     * 在SD卡上创建目录
     *
     * @param dirPath
     */
    public static boolean creatDir(String dirPath) {
        File file = new File(dirPath);
        // 按照指定的路径创建文件夹
        return file.exists() || file.mkdirs();
    }

    /**
     * 删除指定路径文件夹
     *
     * @param dirPath
     */
    public static boolean deleteDir(String dirPath) {
        File file = new File(dirPath);
        // 判断文件夹是否存在
        //删除
        return !(file.exists() && file.isDirectory()) || file.delete();
    }

    /**
     * 删除指定路径文件
     *
     * @param filePath
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        // 判断文件是否存在
        //删除
        return !file.exists() || file.delete();
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
                if (output != null) { output.close(); }
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
            if (null != os) { os.close(); }
        }
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
        // 获取扩展名
        String fileEnd = path.substring(path.lastIndexOf(".") + 1,
                path.length()).toLowerCase();
        return fileEnd.equals("png") || fileEnd.equals("jpg");
    }

    /**
     * 判断文件是否为pdf格式(pdf)
     *
     * @param path
     * @return
     */
    public static boolean isPdfFile(String path) {
        // 获取扩展名
        String fileEnd = path.substring(path.lastIndexOf(".") + 1,
                path.length()).toLowerCase();
        return fileEnd.equals("pdf");
    }

    /**
     * 判断文件是否为Vedio格式(mp4,3gp)
     *
     * @param path
     * @return
     */
    public static boolean isVideoFile(String path) {
        // 获取扩展名
        String fileEnd = path.substring(path.lastIndexOf(".") + 1,
                path.length()).toLowerCase();
        return fileEnd.equals("mp4") || fileEnd.equals("3gp");
    }

    public static Bitmap getLocalBitmap(String pathString) {
        Bitmap bitmap = null;
        File file = new File(pathString);
        if (file.exists()) {
            bitmap = BitmapFactory.decodeFile(pathString);
        }
        return bitmap;
    }

    public static Uri saveLocalBitmap(final Bitmap bitmap, final String path) {
        File img = new File(path);
        if (!createFile(path)) { return null; }
        try {
            FileOutputStream fos = new FileOutputStream(img);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            return Uri.fromFile(img);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
