package utils;

public class StringUtils {

    /**
     * Check if parameter is null or empty
     * @param str
     * @return
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * Check if parameter is null
     * @param str
     * @return
     */
    public static boolean isNull(String str) {
        return str == null || str.equals("null");
    }
}
