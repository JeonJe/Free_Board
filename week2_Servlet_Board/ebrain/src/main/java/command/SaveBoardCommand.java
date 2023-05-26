package command;

import attachment.Attachment;
import attachment.AttachmentDAO;
import board.Board;
import board.BoardDAO;
import exceptions.InvalidValidationException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import utils.BoardUtils;
import utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SaveBoardCommand implements Command {
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 업로드할 서버 디렉토리 위치
        String uploadPath = "/Users/premise/Desktop/github/Java/ebrain/upload";
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);

        if (isMultipart) {
            // 파일 아이템을 저장할 Factory 생성
            DiskFileItemFactory factory = new DiskFileItemFactory();

            // 업로드 파일이 임시로 저장될 디렉토리 설정
            File tempDir = new File(System.getProperty("java.io.tmpdir"));
            factory.setRepository(tempDir);

            // 파일 업로드를 처리할 ServletFileUpload 객체 생성
            ServletFileUpload upload = new ServletFileUpload(factory);
            try {
                // 요청 파라미터들을 파싱하여 파일 아이템 리스트를 얻어옴
                List<FileItem> items = upload.parseRequest(request);
                String category = null;
                String writer = null;
                String password = null;
                String passwordConfirm = null;
                String title = null;
                String content = null;

                AttachmentDAO attachmentDAO = new AttachmentDAO();
                // 업로드한 파일 정보를 저장할 리스트
                List<Attachment> attachments = new ArrayList<>();
                int boardId = -1;

                // 파일 아이템들을 처리
                for (FileItem item : items) {

                    if (item.isFormField()) {
                        String fieldName = item.getFieldName();
                        String fieldValue = item.getString("utf-8");

                        switch (fieldName) {
                            case "category_id":
                                category = fieldValue;
                                break;
                            case "writer":
                                writer = fieldValue;
                                break;
                            case "password":
                                password = fieldValue;
                                break;
                            case "password-confirm":
                                passwordConfirm = fieldValue;
                                break;
                            case "title":
                                title = fieldValue;
                                break;
                            case "content":
                                content = fieldValue;
                                break;
                        }
                    } else {

                        if (!StringUtils.isNullOrEmpty(item.getName())) {

                            // 파일 이름 가져오기
                            String fileName = item.getName();
                            System.out.println("아이템 이름 " + item.getName());

                            // 중복 파일명 처리
                            File uploadedFile = new File(uploadPath);
                            String baseName = FilenameUtils.getBaseName(fileName);
                            String extension = FilenameUtils.getExtension(fileName);

                            int count = 1;
                            String numberedFileName = null;
                            while (uploadedFile.exists()) {
                                // 중복 파일명에 번호를 붙임
                                numberedFileName = baseName + "_" + count + "." + extension;
                                uploadedFile = new File(uploadPath, numberedFileName);
                                count++;
                            }
                            Attachment attachment = new Attachment();
                            attachment.setFileName(numberedFileName);
                            attachment.setOriginName(fileName);
                            attachments.add(attachment);
                            // 파일 업로드
                            item.write(uploadedFile);
                        }
                    }
                }

                if (!BoardUtils.checkFormValidation(writer,password,passwordConfirm,title,content)){
                    throw new InvalidValidationException("폼 유효성 검증에 실패하였습니다.");
                }

                // 게시글 저장

                Board board = new Board();
                board.setCategoryId(Integer.parseInt(category));
                board.setWriter(writer);
                board.setPassword(BoardUtils.hashPassword(password));
                board.setTitle(title);
                board.setContent(content);
                board.setVisitCount(0);

                BoardDAO boardDAO = new BoardDAO();
                boardId = boardDAO.save(board);

                // 첨부 파일 저장
                for (Attachment attachment : attachments) {
                    if (attachment.getFileName() != null && attachment.getOriginName() != null) {
                        attachment.setBoardId(boardId);
                        attachmentDAO.save(attachment);
                    }
                }

                response.sendRedirect("list");

            } catch (Exception e) {
                e.printStackTrace();
                response.getWriter().println("File upload failed!");
            }
        } else {
            response.getWriter().println("Invalid request!");
        }
    }
}
