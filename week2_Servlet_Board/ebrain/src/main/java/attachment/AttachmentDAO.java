package attachment;

import org.apache.ibatis.session.SqlSession;
import utils.DBUtils;

import java.util.List;

public class AttachmentDAO {

    /**
     * Save Attachment Information
     * @param attachment
     * @throws Exception
     */
    public void saveAttachment(Attachment attachment) throws Exception {
        SqlSession session = null;
        try {
            session = DBUtils.openSession();
            session.insert("AttachmentMapper.saveAttachment", attachment);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.sessionClose(session);
        }
    }

    /**
     * Get Attachment Information By Board ID
     * @param boardId
     * @return
     * @throws Exception
     */
    public List<Attachment> getAttachmentsByBoardId(int boardId) throws Exception {

        List<Attachment> attachments = null;
        SqlSession session = null;
        try {
            session = DBUtils.openSession();
            attachments = session.selectList("AttachmentMapper.getAttachmentsByBoardId", boardId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.sessionClose(session);
        }
        return attachments;
    }

    /**
     * Delete Attachment by Attachment ID
     * @param attachmentId
     * @throws Exception
     */
    public void deleteAttachmentByAttachmentId(int attachmentId) throws Exception {
        SqlSession session = null;
        try {
            session = DBUtils.openSession();
            session.delete("AttachmentMapper.deleteAttachmentByAttachmentId", attachmentId);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.sessionClose(session);
        }
    }

}
