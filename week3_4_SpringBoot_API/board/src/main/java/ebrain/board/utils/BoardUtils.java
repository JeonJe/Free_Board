package ebrain.board.utils;

import ebrain.board.reponse.APIResponse;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
/**
 * 게시판 관련 유틸리티 클래스
 */
public class BoardUtils {

    /**
     * 폼 유효성을 검사하는 메서드
     *
     * @param writer         작성자
     * @param password       비밀번호
     * @param passwordConfirm 비밀번호 확인
     * @param title          제목
     * @param content        내용
     * @return 폼 유효성 검사 결과
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
     * 비밀번호를 해싱하는 메서드
     *
     * @param password 비밀번호
     * @return 해시화된 비밀번호
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
     * 파일을 업로드하는 메서드
     *
     * @param file        업로드할 파일
     * @param uploadPath  파일 업로드 경로
     * @return 업로드된 파일의 이름
     * @throws Exception 파일 업로드 중 발생한 예외
     */
    public static String uploadFile(MultipartFile file, String uploadPath) throws Exception {
        String fileName = file.getOriginalFilename();
        String baseName = FilenameUtils.getBaseName(fileName);
        String extension = FilenameUtils.getExtension(fileName);

        // 중복 파일명 처리
        File uploadedFile = new File(uploadPath + File.separator + fileName);

        int count = 1;
        while (uploadedFile.exists()) {
            // 중복 파일명에 번호 추가
            String numberedFileName = baseName + "_" + count + "." + extension;
            uploadedFile = new File(uploadPath + File.separator + numberedFileName);
            count++;
        }

        // 파일을 서버의 업로드 폴더로 업로드
        file.transferTo(uploadedFile);

        return uploadedFile.getName();
    }

    public static ResponseEntity<APIResponse> createBadRequestResponse(String message) {
        APIResponse apiResponse = new APIResponse();
        apiResponse.setSuccess(false);
        apiResponse.setMessage(message);
        return ResponseEntity.badRequest().body(apiResponse);
    }


    public static ResponseEntity<APIResponse> createOkResponse(Object data) {
        APIResponse apiResponse = new APIResponse();
        apiResponse.setSuccess(true);
        apiResponse.setData(data);
        return ResponseEntity.ok(apiResponse);
    }


    public static ResponseEntity<APIResponse> createInternalServerErrorResponse(String errorMessage) {
        APIResponse apiResponse = new APIResponse();
        apiResponse.setSuccess(false);
        apiResponse.setMessage("Internal server error");
        apiResponse.setData(errorMessage);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }
}
