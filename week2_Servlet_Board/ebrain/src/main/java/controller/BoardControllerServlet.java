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

/**
 * All URL are forwarded to BoardControllerServlet
 */
@WebServlet(name = "controller", value = "/")
public class BoardControllerServlet extends HttpServlet {

    /**
     * List of Command
     */
    private Map<String, Command> commandMap = new HashMap<>();

    /**
     * At initialization, commands are mapped to the command map.
     */
    public void init() {

        commandMap.put("list", new GetBoardListCommand());
        commandMap.put("write", new GetCategoryToWriteCommand());
        commandMap.put("save", new SaveBoardCommand());
        commandMap.put("view", new ViewBoardCommand());
        commandMap.put("delete", new DeleteBoardCommand());
        commandMap.put("delete-file", new DeleteBoardCommand());
        commandMap.put("update", new UpdateBoardCommand());
        commandMap.put("modify", new ModifyBoardCommand());
        commandMap.put("addComment", new AddCommentCommand());
        commandMap.put("download", new DownloadFileCommand());
    }

    /**
     * When receiving a GET request, check the URL and execute the mapped command.
     *
     * @param request  an {@link HttpServletRequest} object that
     *                 contains the request the client has made
     *                 of the servlet
     * @param response an {@link HttpServletResponse} object that
     *                 contains the response the servlet sends
     *                 to the client
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();

        try {

            if (uri.startsWith(contextPath + "/list")) {
                commandMap.get("list").execute(request, response);
            } else if (uri.startsWith(contextPath + "/write")) {
                commandMap.get("write").execute(request, response);
            } else if (uri.startsWith(contextPath + "/view")) {
                commandMap.get("view").execute(request, response);
            } else if (uri.startsWith(contextPath + "/modify")) {
                commandMap.get("modify").execute(request, response);
            } else if (uri.startsWith(contextPath + "/download")) {
                commandMap.get("download").execute(request, response);
            } else if (uri.startsWith(contextPath + "/delete-file")) {
                commandMap.get("delete-file").execute(request, response);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * When receiving a POST request, check the URL and execute the mapped command.
     *
     * @param request  an {@link HttpServletRequest} object that
     *                 contains the request the client has made
     *                 of the servlet
     * @param response an {@link HttpServletResponse} object that
     *                 contains the response the servlet sends
     *                 to the client
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();

        try {
            if (uri.equals(contextPath + "/save")) {
                commandMap.get("save").execute(request, response);
            } else if (uri.equals(contextPath + "/delete")) {
                commandMap.get("delete").execute(request, response);
            } else if (uri.startsWith(contextPath + "/update")) {
                commandMap.get("update").execute(request, response);
            } else if (uri.startsWith(contextPath + "/addComment")) {
                commandMap.get("addComment").execute(request, response);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
