package ebrain.board.service;

import ebrain.board.mapper.BoardMapper;
import ebrain.board.vo.BoardVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BoardService {

    @Autowired
    private BoardMapper boardMapper;

    public List<BoardVO> searchBoards(Map<String, Object> params) {
        return boardMapper.searchBoards(params);
    }


}
