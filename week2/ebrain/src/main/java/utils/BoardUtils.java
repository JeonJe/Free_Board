package utils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class BoardUtils {
    public static final String ERROR_MESSAGE_ATTRIBUTE = "errorMessage";
    /**
     * 게시글 등록 시 길이 등 유효성 검증
     *
     * @param writer
     * @param password
     * @param passwordConfirm
     * @param title
     * @param content
     * @return
     */
    public static boolean checkFormValidation(String writer, String password, String passwordConfirm, String title, String content) {
        try {

            if (writer.length() < 3 || writer.length() >= 5) {
                return false;
            }
            if (password.length() < 4 || password.length() >= 16 || !password.equals(passwordConfirm)) {
                return false;
            }
            String passwordRegex = "(?i)^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
            if (!password.matches(passwordRegex)) {
                return false;
            }
            if (title.length() < 4 || title.length() >= 100) {
                return false;
            }
            if (content.length() < 4 || content.length() >= 2000) {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 평문 패스워드 SHA256으로 해싱
     *
     * @param password
     * @return
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hashedPassword = new StringBuilder();
            for (byte hashByte : hashBytes) {
                hashedPassword.append(String.format("%02x", hashByte));
            }
            return hashedPassword.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void setErrorAlert(HttpServletRequest request, String errorMessage) {
        request.setAttribute(ERROR_MESSAGE_ATTRIBUTE, errorMessage);
    }



}
