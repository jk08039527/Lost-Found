package com.feiying.breedapp.util;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by Administrator on 2016/3/27.
 */
public class StringUtils {
    // 随机种子
    private static final char[] CHAR_RANDOMS = {'1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
            'x', 'y', 'z'};

    public static String iso2utf8(String src) {
        try {
            if (isEmpty(src))
                return "";
            return new String(src.getBytes("iso-8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            return "?";
        }
    }

    public static String iso2gbk(String src) {
        try {
            if (isEmpty(src))
                return "";
            return new String(src.getBytes("iso-8859-1"), "gbk");
        } catch (UnsupportedEncodingException e) {
            return "?";
        }
    }

    public static String utf2gbk(String src) {
        try {
            if (isEmpty(src))
                return "";
            return new String(src.getBytes("utf-8"), "gbk");
        } catch (UnsupportedEncodingException e) {
            return "?";
        }
    }

    /**
     * 判断字符串是否为空值NULL、空格均认为空值
     *
     * @param value
     * @return
     */
    public static boolean isEmpty(String value) {
        return null == value || "".equals(value.trim());
    }

    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0)
            return true;
        for (int i = 0; i < strLen; i++)
            if (!Character.isWhitespace(str.charAt(i)))
                return false;

        return true;
    }

    /**
     * 内容不为空
     *
     * @param value
     * @return
     */
    public static boolean isNotEmpty(String value) {
        return null != value && !"".equals(value.trim());
    }

    /**
     * 重复字符串 如 repeatString("a",3) ==> "aaa"
     *
     * @param src
     * @param repeats
     * @return
     * @author uke
     */
    public static String repeatString(String src, int repeats) {
        if (null == src || repeats <= 0) {
            return src;
        } else {
            StringBuffer bf = new StringBuffer();
            for (int i = 0; i < repeats; i++) {
                bf.append(src);
            }
            return bf.toString();
        }
    }

    /**
     * 左对齐字符串 * lpadString("X",3); ==>" X" *
     *
     * @param src
     * @param length
     * @return
     */
    public static String lpadString(String src, int length) {
        return lpadString(src, length, " ");
    }

    /**
     * 以指定字符串填补空位，左对齐字符串 * lpadString("X",3,"0"); ==>"00X"
     *
     * @param src
     * @param length
     * @param single
     * @return
     */
    public static String lpadString(String src, int length, String single) {
        if (src == null || length <= src.getBytes().length) {
            return src;
        } else {
            return repeatString(single, length - src.getBytes().length) + src;
        }
    }

    /**
     * 右对齐字符串 * rpadString("9",3)==>"9 "
     *
     * @param src
     * @param byteLength
     * @return
     */
    public static String rpadString(String src, int byteLength) {
        return rpadString(src, byteLength, " ");
    }

    /**
     * 以指定字符串填补空位，右对齐字符串 rpadString("9",3,"0")==>"900"
     *
     * @param src
     * @param length
     * @param single
     * @return
     */
    public static String rpadString(String src, int length, String single) {
        if (src == null || length <= src.getBytes().length) {
            return src;
        } else {
            return src + repeatString(single, length - src.getBytes().length);
        }
    }

    /**
     * 去除,分隔符，用于金额数值去格式化
     *
     * @param value
     * @return
     */
    public static String decimal(String value) {
        if (null == value || "".equals(value.trim())) {
            return "0";
        } else {
            return value.replaceAll(",", "");
        }
    }

    /**
     * 在数组中查找字符串
     *
     * @param params
     * @param name
     * @param ignoreCase
     * @return
     */
    public static int indexOf(String[] params, String name, boolean ignoreCase) {
        if (params == null)
            return -1;
        for (int i = 0, j = params.length; i < j; i++) {
            if (ignoreCase && params[i].equalsIgnoreCase(name)) {
                return i;
            } else if (params[i].equals(name)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 将字符转数组
     *
     * @param str
     * @return
     */
    public static String[] toArr(String str) {
        String inStr = str;
        String a[] = null;
        try {
            if (null != inStr) {
                StringTokenizer st = new StringTokenizer(inStr, ",");
                if (st.countTokens() > 0) {
                    a = new String[st.countTokens()];
                    int i = 0;
                    while (st.hasMoreTokens()) {
                        a[i++] = st.nextToken();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return a;
    }

    /**
     * 字符串数组包装成字符串
     *
     * @param ary
     * @param s   包装符号如 ' 或 "
     * @return
     */
    public static String toStr(String[] ary, String s) {
        if (ary == null || ary.length < 1)
            return "";
        StringBuffer bf = new StringBuffer();
        bf.append(s);
        bf.append(ary[0]);
        for (int i = 1; i < ary.length; i++) {
            bf.append(s).append(",").append(s);
            bf.append(ary[i]);
        }
        bf.append(s);
        return bf.toString();
    }

    /**
     * 取整数值
     *
     * @param map
     * @param key
     * @param defValue
     * @return
     */
    public static int getInt(Map map, String key, int defValue) {
        if (null != map && isNotEmpty(key)) {
            try {
                return Integer.parseInt((String) map.get(key));
            } catch (Exception e) {
            }
        }
        return defValue;
    }

    /**
     * 取浮点值
     *
     * @param map
     * @param key
     * @param defValue
     * @return
     */
    public static float getFloat(Map map, String key, int defValue) {
        if (null != map && isNotEmpty(key)) {
            try {
                return Float.parseFloat(map.get(key).toString());
            } catch (Exception e) {
            }
        }
        return defValue;
    }

    public static boolean isNumber(String s) {
        if (s == null)
            return false;
        return s.matches("[0-9\\.]+");
    }

    /**
     * 转整数
     *
     * @param str
     * @return
     */
    public static int parseInt(String str) {
        if (!isNumber(str))
            return -1;
        return Integer.parseInt(str);
    }

    /**
     * @param str
     * @param def
     * @return
     */
    public static int parseInt(String str, int def) {
        int rst = parseInt(str);
        if (rst < 0) {
            return def;
        }
        return rst;
    }

    /**
     * 统计字符串长度,一个双字节字符长度计2，ASCII字符计1
     *
     * @param str 字符串
     */
    public static int getLength(String str) {
        return str.replaceAll("[^\\x00-\\xff]", "aa").length();
    }

    public static String getStringByBytes(byte[] bs) {
        return new String(bs);
    }


    /**
     * 生成xml格式
     *
     * @param bf
     * @param entry
     * @param value
     */
    public static void generyXmlEntry(StringBuffer bf, String entry, Object value) {
        bf.append("<").append(entry).append(">");
        if (null != value)
            bf.append(value.toString().trim());
        bf.append("</").append(entry).append(">");
    }

    /**
     * 类似于C#的String.format();
     *
     * @param msg
     * @param vars
     * @return
     */
    public static String getMessage(String msg, String[] vars) {
        for (int i = 0; i < vars.length; i++) {
            msg = msg.replaceAll("\\{" + i + "\\}", vars[i]);
        }
        return msg;
    }

    /**
     * @param msg
     * @param var
     * @return
     */
    public static String getMessage(String msg, String var) {
        return getMessage(msg, new String[]{var});
    }

    /**
     * @param msg
     * @param var
     * @param var2
     * @return
     */
    public static String getMessage(String msg, String var, String var2) {
        return getMessage(msg, new String[]{var, var2});
    }

    /**
     * 获取hashmap的值
     *
     * @param map
     * @param key
     * @return
     */
    public static Object getMapValue(Map map, Object key) {
        if (null == map || null == key)
            return "";

        if ((key instanceof String)) {
            String keystr = (String) key;
            keystr = keystr.toUpperCase();
            key = keystr;
        }
        Object value = map.get(key);
        return null == value ? "" : value;
    }

    /**
     * 生成一个图片的名称
     *
     * @param rootUrl
     * @param date
     * @param imgId
     * @param imgInfo
     * @return
     */
    public static String generyImgUrl(Object rootUrl, Object date, Object imgId, Object imgInfo) {
        StringBuffer bf = new StringBuffer();
        try {
            String ext = StringUtils.getFileExtName((String) imgInfo);
            bf.append(rootUrl).append("/");
            bf.append(date).append("/");
            bf.append(imgId).append(ext);
        } catch (Exception e) {
            bf.append("");
        }
        return bf.toString();
    }

    /**
     * 获取文件的扩展名
     *
     * @param oldName
     * @return
     */
    public static String getFileExtName(String oldName) {
        String ext = "";
        int lastIndex = oldName.lastIndexOf(".");
        if (lastIndex > 0) {
            ext = oldName.substring(lastIndex);
        }
        return ext;
    }

    public static void generyXmlEntryCData(StringBuffer bf, String entry, Object value) {
        bf.append("<").append(entry).append("><![CDATA[");
        if (null != value)
            bf.append(value);
        bf.append("]]></").append(entry).append(">");
    }

    /**
     * 截取固定超出长度,以"..."结尾
     *
     * @param str    需要截图的string
     * @param length 长度
     */
    public static String subStringLength(String str, int length) {
        if (!isEmpty(str) && str.length() > length) {
            str = str.substring(0, length) + "...";
        }
        return str;
    }

    /**
     * 生成长度为5到10的随机字符串. 随机字符串的内容包含[1-9 A-Z a-z]的字符.
     *
     * @return 随机字符串.
     */
    public static String buildRandomString() {
        return buildRandomString(5, 10);
    }

    /**
     * 生成随机字符串. 随机字符串的内容包含[1-9 A-Z a-z]的字符.
     *
     * @param length 必须为正整数 随机字符串的长度
     * @return 随机字符串.
     */
    public static synchronized String buildRandomString(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length只能是正整数!");
        }
        SecureRandom random = null;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (random == null) {
            return null;
        }
        StringBuffer ret = new StringBuffer();
        for (int i = 0; i < length; i++) {
            ret.append(CHAR_RANDOMS[random.nextInt(CHAR_RANDOMS.length)]);
        }
        random = null;
        return ret.toString();
    }

    /**
     * 生成随机字符串. 随机字符串的内容包含[1-9 A-Z a-z]的字符.
     *
     * @param min 必须为正整数 随机字符串的最小长度
     * @param max 必须为正整数 随机字符串的最大长度
     * @return 随机字符串.
     */
    public static synchronized String buildRandomString(int min, int max) {
        if (min <= 0) {
            throw new IllegalArgumentException("Min 只能是正整数!");
        } else if (max <= 0) {
            throw new IllegalArgumentException("Max 只能是正整数!");
        } else if (min > max) {
            throw new IllegalArgumentException("Min 必须小于或等于 Max!");
        }
        SecureRandom random = null;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (random == null) {
            return null;
        }
        int length = random.nextInt(max - min + 1) + min;
        StringBuffer ret = new StringBuffer();
        for (int i = 0; i < length; i++) {
            ret.append(CHAR_RANDOMS[random.nextInt(CHAR_RANDOMS.length)]);
        }
        random = null;
        return ret.toString();
    }


    /**
     * 将html占位符转化为正常字符
     *
     * @param str
     * @return
     */
    public static String replaceHtmlCharacters(String str) {
        String inputstr = str;
        String temptext = "";
        if (null != inputstr && !"".equals(inputstr)) {
            if (inputstr.indexOf("&amp;") > -1) {
                inputstr = inputstr.replaceAll("&amp;", "&");
            }
            if (inputstr.indexOf("&lt;") > -1) {
                inputstr = inputstr.replaceAll("&lt;", "<");
            }
            if (inputstr.indexOf("&gt;") > -1) {
                inputstr = inputstr.replaceAll("&gt;", ">");
            }
            if (inputstr.indexOf("&apos;") > -1) {
                inputstr = inputstr.replaceAll("&apos;", "'");
            }
            if (inputstr.indexOf("&quot;") > -1) {
                inputstr = inputstr.replaceAll("&quot;", "\"");
            }
            if (inputstr.indexOf("&#034;") > -1) {
                inputstr = inputstr.replaceAll("&#034;", "\"");
            }
            temptext = inputstr;
        }
        return temptext;
    }
}
