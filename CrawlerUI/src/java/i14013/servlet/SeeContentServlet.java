package i14013.servlet;

import i14013.engine.SearchingProcessor;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Gabriella
 */
@WebServlet(name = "SeeContentServlet", urlPatterns = {"/pages/SeeContent"})
public class SeeContentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String url = request.getParameter("res-url");
        String[] p = SearchingProcessor.getPage(url);
        request.setAttribute("page", p);
        RequestDispatcher rd = request.getRequestDispatcher("content.jsp");
        rd.include(request, response);

    }

}
