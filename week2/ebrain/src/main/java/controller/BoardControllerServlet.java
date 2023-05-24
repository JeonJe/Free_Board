package controller;

import command.Command;
import command.ListCommand;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "controller", value = "/")
public class BoardControllerServlet extends HttpServlet {

    private Map<String, Command> commandMap = new HashMap<>();

    public void init(){
        commandMap.put("ww", new ListCommand());
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();

        if (uri.equals(contextPath + "/ww")) {
            // GET 요청의 list 서비스 담당자 호출
            System.out.println("List 요청이 들어왔습니다.");  // 로그 출력
            try {
                commandMap.get("ww").execute(request, response);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
