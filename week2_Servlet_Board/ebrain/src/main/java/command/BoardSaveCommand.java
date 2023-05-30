package command;

import attachment.Attachment;
import attachment.AttachmentDAO;
import board.Board;
import board.BoardDAO;
import exceptions.FormValidationInvalidException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import utils.BoardUtils;
import utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;

public class BoardSaveCommand implements Command {
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String uploadPath = ResourceBundle.getBundle("db").getString("UPLOAD_PATH");

        try {

            // Create Factory for storing file items.
            DiskFileItemFactory factory = new DiskFileItemFactory();

            //Set directory for temporarily storing uploaded files.
            File tempDir = new File(System.getProperty("java.io.tmpdir"));
            factory.setRepository(tempDir);

            //Create ServletFileUpload object to handle file uploads
            ServletFileUpload upload = new ServletFileUpload(factory);

            List<FileItem> items = upload.parseRequest(request);
            int boardId = -1;
            int categoryId = 0;
            String writer = null;
            String password = null;
            String confirmPassword = null;
            String title = null;
            String content = null;
            String hashedPassword = null;


            List<FileItem> attachmentItems = new ArrayList<>();
            Map<String, String> fieldMap = new HashMap<>();

            for (FileItem item : items) {

                if (item.isFormField()) {
                    String fieldName = item.getFieldName();
                    String fieldValue = item.getString("utf-8");
                    fieldMap.put(fieldName, fieldValue);

                } else {
                    //Attachment Items
                    attachmentItems.add(item);
                }
            }
            String categoryValue = fieldMap.get("category_id");
            if (categoryValue != null) {
                categoryId = Integer.parseInt(categoryValue);
            }

            writer = fieldMap.get("writer");
            password = fieldMap.get("password");
            confirmPassword = fieldMap.get("confirmPassword");
            hashedPassword = BoardUtils.hashPassword(password);
            title = fieldMap.get("title");
            content = fieldMap.get("content");


            if (!BoardUtils.checkFormValidation(writer, password, confirmPassword, title, content)) {
                throw new FormValidationInvalidException("폼 유효성 검증에 실패하였습니다.");
            }

            // Save Board content
            Board board = new Board();
            board.setCategoryId(categoryId);
            board.setWriter(writer);
            board.setPassword(hashedPassword);
            board.setTitle(title);
            board.setContent(content);
            board.setVisitCount(0);

            BoardDAO boardDAO = new BoardDAO();
            boardId = boardDAO.saveBoard(board);

            AttachmentDAO attachmentDAO = new AttachmentDAO();
            // Save Attachments
            for (FileItem attachmentItem : attachmentItems) {
                if (!StringUtils.isNullOrEmpty(attachmentItem.getName())) {
                    String numberedFileName = BoardUtils.uploadFile(attachmentItem, uploadPath);
                    Attachment attachment = new Attachment();
                    attachment.setBoardId(boardId);
                    attachment.setFileName(numberedFileName);
                    attachment.setOriginName(attachmentItem.getName());
                    attachmentDAO.saveAttachment(attachment);
                }
            }

            response.sendRedirect("list?action=list");

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("File upload failed!");
        }

    }
}
