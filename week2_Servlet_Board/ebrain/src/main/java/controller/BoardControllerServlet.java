package controller;

import command.*;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import utils.StringUtils;

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

        commandMap.put("list", new BoardListGetCommand());
        commandMap.put("write", new CategoryGetListCommand());
        commandMap.put("save", new BoardSaveCommand());
        commandMap.put("view", new BoardGetInfoCommand());
        commandMap.put("delete", new BoardDeleteCommand());
        commandMap.put("deleteFile", new BoardDeleteCommand());
        commandMap.put("update", new BoardUpdateCommand());
        commandMap.put("modify", new BoardModifyCommand());
        commandMap.put("addComment", new CommentAddCommand());
        commandMap.put("download", new FileDownloadCommand());
    }

    /**
     * Handles the GET requests sent to the servlet. Forwards the request and response
     * to the appropriate command based on the requested action.
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            forwardToCommand(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Handles the Post requests sent to the servlet. Forwards the request and response
     * to the appropriate command based on the requested action.
     *
     * @param request  an {@link HttpServletRequest} object that
     *                 contains the request the client has made
     *                 of the servlet
     * @param response an {@link HttpServletResponse} object that
     *                 contains the response the servlet sends
     *                 to the client
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            forwardToCommand(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Forwards the request and response to the appropriate command based on the specified action.
     *
     * @param request  the HttpServletRequest object that contains the request from the client
     * @param response the HttpServletResponse object that contains the response to be sent to the client
     * @throws Exception if an error occurs during the execution of the command
     */
    private void forwardToCommand(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String action = null;
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        try {
            if (isMultipart) {
                String uri = request.getRequestURI();
                String contextPath = request.getContextPath();
                String actionPath = uri.substring(contextPath.length());
                action = actionPath.substring(1);  //  actionPath에서 '/' 제외

            } else {
                action = request.getParameter("action");
            }

            if (!StringUtils.isNullOrEmpty(action)) {
                commandMap.get(action).execute(request, response);
            } else {
                throw new RuntimeException("No action");
            }

        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
