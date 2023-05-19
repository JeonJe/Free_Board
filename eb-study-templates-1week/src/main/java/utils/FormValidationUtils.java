package utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class FormValidationUtils {
    public static boolean checkFormValidation(String writer, String password, String passwordConfirm, String title, String content) {
        try {

          if (writer.length() < 3 || writer.length() >= 5){
              return false;
          }
          if (password.length() < 4 || password.length() >= 16 || !password.equals(passwordConfirm)){
              return false;
          }
            String passwordRegex = "(?i)^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
            if (!password.matches(passwordRegex)) {
                return false;
            }
          if (title.length() < 4 || title.length() >= 100){
              return false;
          }
          if (content.length() < 4 || content.length() >= 2000){
              return false;
          }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
