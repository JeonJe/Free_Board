//package command;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import java.io.IOException;
//
//public class ListCommand implements Command {
//    public void execute(HttpServletRequest request, HttpServletResponse response) {
//        // list 서비스 담당자의 실행 로직 작성
//
//        // 응답 생성
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//
//        // JSON 응답 데이터 생성
//        String responseData = "{\"message\": \"List service completed\"}";
//
//        try {
//            // 응답 전송
//            response.getWriter().write(responseData);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}