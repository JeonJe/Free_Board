package utils;

public class StringUtils {
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }
    public static boolean isNull(String str) {
        return str == null || str.equals("null");
    }
}
