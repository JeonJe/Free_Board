package ebrain.board.reponse;

import ebrain.board.vo.AttachmentVO;
import ebrain.board.vo.BoardVO;
import ebrain.board.vo.CommentVO;
import lombok.Data;

import java.util.List;

@Data
public class BoardInfoResponse {
    private BoardVO board;
    private List<CommentVO> comments;
    private List<AttachmentVO> attachments;
}
