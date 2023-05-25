package command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

public class DownloadCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String uploadPath = "/Users/premise/Desktop/github/Java/ebrain/upload";
            String fileName = request.getParameter("fileName"); // 파일 이름
            // 파일의 실제 경로
            String filePath = uploadPath + File.separator + fileName; // 파일 경로
            File file = new File(filePath);

            if (file.exists() && file.canRead()) {
                // 파일 다운로드 설정
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment; filename=\"" +
                        new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + "\"");

                // 파일을 읽어와서 클라이언트에 전송
                FileInputStream fis = new FileInputStream(file);
                OutputStream os = response.getOutputStream();
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                fis.close();
                os.close();
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 파일이 존재하지 않는 경우 404 에러 반환
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}