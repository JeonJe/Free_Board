package controller;

import command.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "controller", value = "/")
public class BoardControllerServlet extends HttpServlet {

    private Map<String, Command> commandMap = new HashMap<>();

    public void init() {

        commandMap.put("list", new ListCommand());
        commandMap.put("write", new WriteCommand());
        commandMap.put("save", new SaveCommand());
        commandMap.put("view", new ViewCommand());
        commandMap.put("delete", new DeleteBoardCommand());
        commandMap.put("delete-file", new DeleteBoardCommand());
        commandMap.put("update", new UpdateCommand());
        commandMap.put("modify", new ModifyCommand());
        commandMap.put("addComment", new AddCommentCommand());
        commandMap.put("download", new DownloadCommand());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();

        try {
            if (uri.startsWith(contextPath + "/list")) {
                System.out.println("list 요청이 들어왔습니다.");
                commandMap.get("list").execute(request, response);
            } else if (uri.startsWith(contextPath + "/write")) {
                System.out.println("write 요청이 들어왔습니다.");
                commandMap.get("write").execute(request, response);
            } else if (uri.startsWith(contextPath + "/view")) {
                System.out.println("view 요청이 들어왔습니다.");
                commandMap.get("view").execute(request, response);
            } else if (uri.startsWith(contextPath + "/modify")) {
                System.out.println("modify 요청이 들어왔습니다.");
                commandMap.get("modify").execute(request, response);
            } else if (uri.startsWith(contextPath + "/download")) {
                System.out.println("download 요청이 들어왔습니다.");
                commandMap.get("download").execute(request, response);
            } else if (uri.startsWith(contextPath + "/delete-file")) {
                System.out.println("delete-file 요청이 들어왔습니다.");
                commandMap.get("delete-file").execute(request, response);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();

        try {
            if (uri.equals(contextPath + "/save")) {
                // GET 요청의 list 서비스 담당자 호출
                System.out.println("save 요청이 들어왔습니다.");
                commandMap.get("save").execute(request, response);
            } else if (uri.equals(contextPath + "/delete")) {
                System.out.println("delete 요청이 들어왔습니다.");
                commandMap.get("delete").execute(request, response);
            } else if (uri.startsWith(contextPath + "/update")) {
                System.out.println("update 요청이 들어왔습니다.");
                commandMap.get("update").execute(request, response);
            } else if (uri.startsWith(contextPath + "/addComment")) {
                System.out.println("addComment 요청이 들어왔습니다.");
                commandMap.get("addComment").execute(request, response);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
