package attachment;

public class Attachment {
    /**
     *첨부파일 식별 ID
     */
    private int attachmentId;
    /**
     *첨부파일과 연관된 게시글 ID
     */
    private int boardId;
    /**
     *첨부파일 서버 저장 이름
     */
    private String fileName;
    /**
     *첨부파일 원래 이름
     */
    private String originName;

    public Attachment() {

    }

    public Attachment(int boardId, String fileName, String filePath) {
        this.boardId = boardId;
        this.fileName = fileName;
        this.originName = filePath;
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

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }
}
