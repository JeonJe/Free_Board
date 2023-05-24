package attachment;

import utils.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttachmentDAO {

    /**
     * 첨부파일 저장
     *
     * @param attachment
     * @throws Exception
     */
    public void save(Attachment attachment) throws Exception {
        Connection conn = null;
        try {
            conn = DBUtils.getConnection();
            String sql = "INSERT INTO attachment (board_id, filename, origin_filename) VALUES (?, ?, ?)";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, attachment.getBoardId());
            pstmt.setString(2, attachment.getFileName());
            pstmt.setString(3, attachment.getOriginName());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.closeConnection(conn);
        }
    }

    /**
     * 게시글 ID로 첨부파일 가져오기
     *
     * @param boardId
     * @return
     * @throws Exception
     */
    public List<Attachment> getAttachmentsByBoardId(int boardId) throws Exception {
        List<Attachment> attachments = new ArrayList<>();
        Connection conn = null;

        try {
            conn = DBUtils.getConnection();
            String sql = "SELECT * FROM attachment WHERE board_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, boardId);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Attachment attachment = new Attachment();
                attachment.setAttachmentId(rs.getInt("attachment_id"));
                attachment.setAttachmentId(rs.getInt("board_id"));
                attachment.setFileName(rs.getString("filename"));
                attachment.setOriginName(rs.getString("origin_filename"));

                attachments.add(attachment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.closeConnection(conn);
        }
        return attachments;
    }
}
