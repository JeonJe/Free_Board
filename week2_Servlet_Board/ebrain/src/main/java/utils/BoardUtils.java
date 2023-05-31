package utils;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class BoardUtils {
    /**
     * Error Message literal
     */

    public static final String ERROR_MESSAGE_ATTRIBUTE = "errorMessage";
    /**
     * Check the condition of the form
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
     * Hashing plaintext passwords with the SHA256 hash function
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

    /**
     * Upload Attachment Items to Server
     * @param attachmentItem
     * @param uploadPath
     * @return
     * @throws Exception
     */
    public static String uploadFile(FileItem attachmentItem, String uploadPath) throws Exception {
            String fileName = attachmentItem.getName();

            // Duplicate File Name Handling
            File uploadedFile = new File(uploadPath);
            String baseName = FilenameUtils.getBaseName(fileName);
            String extension = FilenameUtils.getExtension(fileName);

            int count = 1;
            String numberedFileName = null;
            while (uploadedFile.exists()) {
                // Add number to duplicate file name
                numberedFileName = baseName + "_" + count + "." + extension;
                uploadedFile = new File(uploadPath, numberedFileName);
                count++;
            }

            // File Upload to Server's upload folder
            attachmentItem.write(uploadedFile);

            return numberedFileName;
    }


    /**
     * Set Error Message into request
     * @param request
     * @param errorMessage
     */
    public static void setErrorAlert(HttpServletRequest request, String errorMessage) {
        request.setAttribute(ERROR_MESSAGE_ATTRIBUTE, errorMessage);
    }



}
