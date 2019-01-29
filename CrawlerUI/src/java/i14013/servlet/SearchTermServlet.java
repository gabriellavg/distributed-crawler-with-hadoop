package i14013.servlet;

import i14013.engine.SearchingProcessor;
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
@WebServlet(name = "SearchTermServlet", urlPatterns = {"/pages/SearchTerm"})
public class SearchTermServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String term = request.getParameter("input-term");
        String temp = request.getParameter("search-term-submit");
        int page;
        if (temp.equals("custom")) {
            page = Integer.parseInt(request.getParameter("s-to-page"));
        } else {
            page = Integer.parseInt(temp);
        }

        Long start = System.currentTimeMillis();
        ArrayList<String> res = SearchingProcessor.getUrls(term, page);
        Long end = System.currentTimeMillis();
        double searchTime = (double) (end - start) / 1000.0;

        request.setAttribute("term", term);
        request.setAttribute("res", res);
        request.setAttribute("time", searchTime);
        RequestDispatcher rd = request.getRequestDispatcher("search-result.jsp");
        rd.include(request, response);

    }

}
