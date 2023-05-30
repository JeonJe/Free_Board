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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BoardUpdateCommand implements Command {
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String uploadPath = "/Users/premise/Desktop/github/Java/ebrain/upload";
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (isMultipart) {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            //TODO : get parametr로 꺼내쓸수 있게 구현체 확인 후 변경

            File tempDir = new File(System.getProperty("java.io.tmpdir"));
            factory.setRepository(tempDir);

            ServletFileUpload upload = new ServletFileUpload(factory);

            try {
                // 요청 파라미터들을 파싱하여 파일 아이템 리스트를 얻어옴
                List<FileItem> items = upload.parseRequest(request);

                List<FileItem> attachmentItems = new ArrayList<>();
                int boardId = 0;

                String writer = null;
                String password = null;
                String title = null;
                String content = null;

                //새로운 요청 데이터 Parsing
                for (FileItem item : items) {
                    System.out.println("아이템정보 " + item.getFieldName());

                    if (item.isFormField()) {
                        String fieldName = item.getFieldName();
                        String fieldValue = item.getString("utf-8");
                        //TODO : 이런식으로 하드코딩하며 안됌
                        switch (fieldName) {
                            case "id":
                                boardId = Integer.parseInt(fieldValue);
                                break;
                            case "writer":
                                writer = fieldValue;
                                break;
                            case "password":
                                password = fieldValue;
                                break;
                            case "title":
                                title = fieldValue;
                                break;
                            case "content":
                                content = fieldValue;
                                break;
                        }
                    } else {
//                        attachmentItems.add(item);
                    }
                }


                String hashedPassword = BoardUtils.hashPassword(password);
                AttachmentDAO attachmentDAO = new AttachmentDAO();


                //기존 Board 내용 Get
                BoardDAO boardDAO = new BoardDAO();
                Board board = boardDAO.getBoardInfoById(boardId);

                if (!BoardUtils.checkFormValidation(writer, password, password, title, content)) {
                    throw new FormValidationInvalidException("폼 유효성 검증에 실패하였습니다.");
                }

                List<Attachment> existingAttachments = attachmentDAO.getAttachmentsByBoardId(boardId);


                //내용 업데이트
                if (board != null && board.getPassword().equals(hashedPassword)) {
                    board.setBoardId(boardId);
                    board.setPassword(hashedPassword);
                    board.setWriter(writer);
                    board.setTitle(title);
                    board.setContent(content);
                    boardDAO.updateBoard(board);

                    //기존 attachment 삭제
//                    attachmentDAO.deleteAllAttachmentByBoardId(boardId);
//                    for (Attachment oldAttachment : existingAttachments){
//                        File file = new File(uploadPath, oldAttachment.getFileName());
//                        if (file.exists()) {
//                            file.delete();
//                        }
//                    }

//                    //새로운 attachment 업로드
//                    for (FileItem attachmentItem : attachmentItems) {
//                        if (!StringUtils.isNullOrEmpty(attachmentItem.getName())) {
//
//                            // 파일 이름 가져오기
//                            String fileName = attachmentItem.getName();
//
//                            System.out.println("아이템 이름 " + attachmentItem.getName());
//
//                            // 중복 파일명 처리
//                            File uploadedFile = new File(uploadPath);
//                            String baseName = FilenameUtils.getBaseName(fileName);
//                            String extension = FilenameUtils.getExtension(fileName);
//
//                            int count = 1;
//                            String numberedFileName = null;
//                            while (uploadedFile.exists()) {
//                                // 중복 파일명에 번호를 붙임
//                                numberedFileName = baseName + "_" + count + "." + extension;
//                                uploadedFile = new File(uploadPath, numberedFileName);
//                                count++;
//                            }
//
//                            Attachment attachment = new Attachment();
//                            attachment.setBoardId(boardId);
//                            attachment.setFileName(numberedFileName);
//                            attachment.setOriginName(fileName);
//
//                            attachmentDAO.save(attachment);
//                            // 파일 업로드
//                            attachmentItem.write(uploadedFile);
//                        }
//                    }

                    response.sendRedirect("list");

                } else {
                    String errorMessage = "비밀번호가 틀렸습니다.";
                    BoardUtils.setErrorAlert(request, errorMessage);
                    request.setAttribute("id", boardId);
                    request.setAttribute("board", board);
                    request.setAttribute("attachments", existingAttachments);

                    request.getRequestDispatcher("boardModifyConent.jsp").forward(request, response);
                }

            } catch (Exception e) {
                e.printStackTrace();
                String errorMessage = "에러가 발생하였습니다.";
                request.setAttribute("errorMessage", errorMessage);
                request.getRequestDispatcher("errorPage.jsp").forward(request, response);
            }
        } else {
            response.getWriter().println("Invalid request!");
        }
    }
}