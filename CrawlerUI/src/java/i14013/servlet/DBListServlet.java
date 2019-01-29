package i14013.servlet;

import i14013.engine.DBListingProcessor;
import java.io.IOException;
import java.util.ArrayList;
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
@WebServlet(name = "DBListServlet", urlPatterns = {"/pages/admin/List"})
public class DBListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String[] val = (request.getParameter("menu-submit")).split(" ");
        ArrayList<String> res;
        String location;
        int page;
        if (val[0].equals("F")) {
            if (val[1].equals("custom")) {
                page = Integer.parseInt(request.getParameter("f-to-page"));
            } else {
                page = Integer.parseInt(val[1]);
            }
            res = DBListingProcessor.getFrontierData(page);
            location = "frontier.jsp";

        } else {
            if (val[1].equals("custom")) {
                page = Integer.parseInt(request.getParameter("r-to-page"));
            } else {
                page = Integer.parseInt(val[1]);
            }
            res = DBListingProcessor.getRepositoryData(page);
            location = "repository.jsp";
        }

        request.setAttribute("res", res);
        RequestDispatcher rd = request.getRequestDispatcher(location);
        rd.include(request, response);

    }

}
