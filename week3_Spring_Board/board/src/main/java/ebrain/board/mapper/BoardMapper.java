package ebrain.board.mapper;

import ebrain.board.vo.BoardVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BoardMapper {
    List<BoardVO> searchBoards(Map<String, Object> params);
}
