package attachment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttachmentDAO {
    static final String DB_URL = "jdbc:mysql://localhost:3306/ebrainsoft_study";
    static final String USER = "ebsoft";
    static final String PASS = "ebsoft";

    /**
     *
     * @return
     * @throws Exception
     */
    private Connection getConnection() throws Exception {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    /**
     *
     * @param attachment
     * @throws Exception
     */
    public void save(Attachment attachment) throws Exception {

        try (Connection conn = getConnection()) {

            String sql = "INSERT INTO attachment (board_id, filename, origin_filename) VALUES (?, ?, ?)";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, attachment.getBoardId());
            pstmt.setString(2, attachment.getFileName());
            pstmt.setString(3, attachment.getOriginName());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param boardId
     * @return
     * @throws Exception
     */
    public List<Attachment> getAttachmentsByBoardId(int boardId) throws Exception{
        List<Attachment> attachments = new ArrayList<>();
        try (Connection conn = getConnection()){
            String sql = "SELECT * FROM attachment WHERE board_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,boardId);

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                Attachment attachment = new Attachment();
                attachment.setAttachmentId(rs.getInt("attachment_id"));
                attachment.setAttachmentId(rs.getInt("board_id"));
                attachment.setFileName(rs.getString("filename"));
                attachment.setOriginName(rs.getString("filepath"));

                attachments.add(attachment);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return attachments;
    }
}
