package command;


import attachment.Attachment;
import attachment.AttachmentDAO;
import board.Board;
import board.BoardDAO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import exceptions.FormValidationInvalidException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import utils.BoardUtils;
import utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.lang.reflect.Type;
import java.util.*;

public class BoardUpdateCommand implements Command {
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
            List<FileItem> attachmentItems = new ArrayList<>();
            Map<String, String> fieldMap = new HashMap<>();

            int boardId = 0;
            String writer = null;
            String password = null;
            String title = null;
            String content = null;
            String hashedPassword = null;

            //Parsing the items
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

            String boardIdValue = fieldMap.get("id");
            if (boardIdValue != null) {
                boardId = Integer.parseInt(boardIdValue);
            }
            writer = fieldMap.get("writer");
            password = fieldMap.get("password");
            hashedPassword = BoardUtils.hashPassword(password);
            title = fieldMap.get("title");
            content = fieldMap.get("content");
            String deletedAttachmentIdsJson = fieldMap.get("deletedAttachmentIds");

            //Get Deleted Item List
            Type type = new TypeToken<List<Integer>>() {
            }.getType();
            List<Integer> deletedAttachmentIds = new Gson().fromJson(deletedAttachmentIdsJson, type);

            BoardDAO boardDAO = new BoardDAO();
            Board board = boardDAO.getBoardInfoById(boardId);

            if (!BoardUtils.checkFormValidation(writer, password, password, title, content)) {
                throw new FormValidationInvalidException("폼 유효성 검증에 실패하였습니다.");
            }

            //Update Board Information
            if (board != null && board.getPassword().equals(hashedPassword)) {
                board.setBoardId(boardId);
                board.setPassword(hashedPassword);
                board.setWriter(writer);
                board.setTitle(title);
                board.setContent(content);
                boardDAO.updateBoard(board);

                AttachmentDAO attachmentDAO = new AttachmentDAO();
                //Delete the attachments requested for removal by the user
                for (Integer deletedId : deletedAttachmentIds) {
                    String deletedFileName = attachmentDAO.
                            getAttachmentInfoByAttachmentId(deletedId).getFileName();
                    // Delete the file from the server
                    if (deletedFileName != null) {
                        File file = new File(uploadPath + '/'+ deletedFileName);
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                    attachmentDAO.deleteAttachmentByAttachmentId(deletedId);
                }

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
            } else {
                String errorMessage = "비밀번호가 틀렸습니다.";
                BoardUtils.setErrorAlert(request, errorMessage);
                request.setAttribute("id", boardId);
                request.setAttribute("board", board);

                request.getRequestDispatcher("boardModifyContent.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = "에러가 발생하였습니다.";
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("errorPage.jsp").forward(request, response);
        }
    }
}