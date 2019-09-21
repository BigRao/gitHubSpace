package com.inspur.plugins.kaoshi.util;

public class DataUtil {
    public static Integer O2I(Object object) {
        return Integer.parseInt(String.valueOf(object));
    }

    public static String O2S(Object object) {
        return String.valueOf(object);
    }

    /**
     * 字符转ASC
     *
     * @param st
     * @return
     */
    public static int getAsc(String st) {
        int ascNum;
        byte[] gc = st.getBytes();
        ascNum = (int) gc[0];
        return ascNum;
    }

    /**
     * ASC转字符
     *
     * @param backNum
     * @return
     */
    public static char backChar(int backNum) {
        char strChar;
        strChar = (char) backNum;
        return strChar;
    }
}
