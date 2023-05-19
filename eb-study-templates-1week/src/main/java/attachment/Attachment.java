package attachment;

import java.time.LocalDateTime;

public class Attachment {
    private int attachmentId;
    private int boardId;
    private String fileName;
    private String filePath;

    public Attachment() {

    }

    public Attachment(int boardId, String fileName, String filePath) {
        this.boardId = boardId;
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public int getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(int attachmentId) {
        this.attachmentId = attachmentId;
    }

    public int getBoardId() {
        return boardId;
    }

    public void setBoardId(int boardId) {
        this.boardId = boardId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
