package com.feiying.breedapp.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;

/**
 * Created by Administrator on 2016/3/27.
 * 安全关闭句柄工具类
 */
public class SafeCloseUtils {

    public static void close(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (Exception e) {
            }
        }
    }

    public static void close(OutputStream outputStream) {
        if (outputStream != null) {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (Exception e) {
            }
        }
    }

    public static void close(Reader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (Exception e) {
            }
        }
    }
}
