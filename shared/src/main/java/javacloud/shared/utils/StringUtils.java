package javacloud.shared.utils;

public class StringUtils {
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static String nullIfEmpty(String str) {
        return isNullOrEmpty(str) ? null : str;
    }
}
