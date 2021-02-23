package javacloud.shared.utils;

public class StringUtils {
    public static boolean IsEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static String NullIfEmpty(String str) {
        return IsEmpty(str) ? null : str;
    }
}
