package ebrain.board.vo;

import lombok.Data;

@Data
public class AttachmentVO {
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
}
