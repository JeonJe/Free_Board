package ebrain.board.utils;

/**
 * 스트링 관련 유틸리티 클래스
 */
public class StringUtils {

    /**
     * 문자열이 null 또는 빈 문자열인지 확인하는 메서드
     *
     * @param str 확인할 문자열
     * @return 문자열이 null이거나 빈 문자열이면 true, 그렇지 않으면 false
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

}
