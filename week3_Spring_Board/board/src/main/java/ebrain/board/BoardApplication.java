package ebrain.board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

/**
 * 게시판 애플리케이션의 entry point
 */
@MapperScan("ebrain.board.mapper")
@SpringBootApplication
public class BoardApplication {
    /**
     * 애플리케이션을 실행
     * @param args 명령줄 파라미터
     */
    public static void main(String[] args) {
        SpringApplication.run(BoardApplication.class, args);
    }

}
