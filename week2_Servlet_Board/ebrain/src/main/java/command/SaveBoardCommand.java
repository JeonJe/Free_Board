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

        String uploadPath = "/Users/premise/Desktop/github/Java/ebrain/upload";
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);

        if (isMultipart) {
            // Create Factory for storing file items.
            DiskFileItemFactory factory = new DiskFileItemFactory();

            //Set directory for temporarily storing uploaded files.
            File tempDir = new File(System.getProperty("java.io.tmpdir"));
            factory.setRepository(tempDir);

            //Create ServletFileUpload object to handle file uploads
            ServletFileUpload upload = new ServletFileUpload(factory);
            try {

                List<FileItem> items = upload.parseRequest(request);
                String category = null;
                String writer = null;
                String password = null;
                String passwordConfirm = null;
                String title = null;
                String content = null;

                AttachmentDAO attachmentDAO = new AttachmentDAO();

                List<Attachment> attachments = new ArrayList<>();
                int boardId = -1;


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

                            // Get File Name
                            String fileName = item.getName();

                            // Duplicate File Name Handling
                            File uploadedFile = new File(uploadPath);
                            String baseName = FilenameUtils.getBaseName(fileName);
                            String extension = FilenameUtils.getExtension(fileName);

                            int count = 1;
                            String numberedFileName = null;
                            while (uploadedFile.exists()) {
                                numberedFileName = baseName + "_" + count + "." + extension;
                                uploadedFile = new File(uploadPath, numberedFileName);
                                count++;
                            }
                            Attachment attachment = new Attachment();
                            attachment.setFileName(numberedFileName);
                            attachment.setOriginName(fileName);
                            attachments.add(attachment);
                            //File Upload to Server's upload folder
                            item.write(uploadedFile);
                        }
                    }
                }

                if (!BoardUtils.checkFormValidation(writer,password,passwordConfirm,title,content)){
                    throw new InvalidValidationException("폼 유효성 검증에 실패하였습니다.");
                }

                // Save Board content
                Board board = new Board();
                board.setCategoryId(Integer.parseInt(category));
                board.setWriter(writer);
                board.setPassword(BoardUtils.hashPassword(password));
                board.setTitle(title);
                board.setContent(content);
                board.setVisitCount(0);

                BoardDAO boardDAO = new BoardDAO();
                boardId = boardDAO.saveBoard(board);

                // Save Attachments
                for (Attachment attachment : attachments) {
                    if (attachment.getFileName() != null && attachment.getOriginName() != null) {
                        attachment.setBoardId(boardId);
//                        attachmentDAO.saveAttachment(attachment);
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
