package attachment;

public class Attachment {
    /**
     * Attachment ID
     */
    private int attachmentId;
    /**
     * ID of the board containing the Attachment
     */
    private int boardId;
    /**
     * Non-duplicate filenames stored on the server
     */
    private String fileName;
    /**
     * The name of the file uploaded by user
     */
    private String originName;

    public Attachment() {

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
