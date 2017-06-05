package com.jerry.zhoupro.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

/**
 * Created by wzl-pc on 2016/3/27.
 * StreamUtils工具类：对流的操作
 */
public class StreamUtils {

    public static String getProperties(String propertySrc, String key) {
        InputStream inputStream = null;
        String value = null;
        try {
            inputStream = StreamUtils.class.getClassLoader().getResourceAsStream(propertySrc);
            Properties p = new Properties();
            try {
                p.load(inputStream);
                value = p.getProperty(key);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {

        } finally {
            SafeCloseUtils.close(inputStream);
        }
        return value;
    }

    /**
     * 将输入流转化成字节数组
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static byte[] getBytesFromStream(InputStream is) throws IOException {
        byte[] data = null;
        Collection<byte[]> chunks = new ArrayList<byte[]>();
        byte[] buffer = new byte[1024 * 1000];
        int read = -1;
        int size = 0;

        while ((read = is.read(buffer)) != -1) {
            if (read > 0) {
                byte[] chunk = new byte[read];
                System.arraycopy(buffer, 0, chunk, 0, read);
                chunks.add(chunk);
                size += chunk.length;
            }
        }
        if (size > 0) {
            ByteArrayOutputStream bos = null;
            try {
                bos = new ByteArrayOutputStream(size);
                for (Iterator<byte[]> itr = chunks.iterator(); itr.hasNext(); ) {
                    byte[] chunk = itr.next();
                    bos.write(chunk);
                }
                data = bos.toByteArray();
            } finally {
                if (is != null) {
                    is.close();
                }
                if (bos != null) {
                    bos.flush();
                    bos.close();
                }
            }
        }
        return data;
    }

    /**
     * 将内容写到文本文件里
     *
     * @param text
     * @param path
     * @throws IOException
     */
    public static void writeTxt(String text, String path) throws IOException {
        final String mode = "rws";
        final String encoding = "utf-8";
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(path, mode);
            raf.write(text.getBytes(encoding));
        } finally {
            if (raf != null) {
                raf.close();
            }
        }
    }

    /**
     * 复制文件
     *
     * @param source      源文件路径
     * @param destination 目标文件路径
     * @throws IOException
     */
    public static void copyFile(String source, String destination)
            throws IOException {
        File sourceFile = null;
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            sourceFile = new File(source);
            fis = new FileInputStream(sourceFile);
            fos = new FileOutputStream(destination);
            byte[] buffer = new byte[1024];
            int c;
            while ((c = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, c);
            }
        } finally {
            SafeCloseUtils.close(fos);
            SafeCloseUtils.close(fis);
        }
    }

    /**
     * @param is
     * @param destination
     * @throws IOException
     */
    public static void writeFile(InputStream is, String destination)
            throws IOException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(destination);
            byte[] buffer = new byte[1024];
            int c;
            while ((c = is.read(buffer)) > 0) {
                fos.write(buffer, 0, c);
            }
        } finally {
            SafeCloseUtils.close(fos);
            SafeCloseUtils.close(is);
        }
    }

    /**
     * 根据路径创建文件夹
     *
     * @param path
     * @return
     */
    public static String createFolder(String path) {
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdir();
        }
        return path;
    }

    /**
     * 循环创建文件夹
     *
     * @param rootPath
     * @param folderPath
     * @return
     */
    public static String createFolder(String rootPath, String folderPath) {
        if (rootPath.endsWith("/")) {
            rootPath = rootPath.substring(0, rootPath.length() - 1);
        }
        folderPath = folderPath.replaceAll("\\\\", "/");
        if (!folderPath.startsWith("/")) {
            folderPath = "/" + folderPath;
        }
        if (!folderPath.endsWith("/")) {
            folderPath = folderPath + "/";
        }
        String[] folders = folderPath.split("/");
        for (int i = 0; i < folders.length; i++) {
            rootPath = rootPath + "/" + folders[i];
            StreamUtils.createFolder(rootPath);
        }
        return folderPath;
    }

    /**
     * 读取test文本内容
     *
     * @param path
     * @return
     * @throws IOException
     * @Description
     * @author 刘国山 lgs@yitong.com.cn
     * @version 1.0 2012-8-29
     */
    public static String readTxt(String path) throws IOException {
        StringBuffer resultBuffer = new StringBuffer();
        FileInputStream in = null;
        BufferedReader br = null;
        try {
            File file = new File(path);// 打开文件
            in = new FileInputStream(file);
            // 指定读取文件时以UTF-8的格式读取
            br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line;
            while (!StringUtils.isEmpty((line = br.readLine()))) {
                resultBuffer.append(line);
            }
        } finally {
            SafeCloseUtils.close(br);
            SafeCloseUtils.close(in);
        }
        return resultBuffer.toString();
    }
}
