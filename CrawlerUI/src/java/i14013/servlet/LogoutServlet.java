package i14013.servlet;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Gabriella
 */
@WebServlet(name = "LogoutServlet", urlPatterns = {"/pages/admin/Logout"})
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Enumeration<String> sessions = session.getAttributeNames();
        while (sessions.hasMoreElements()) {
            session.removeAttribute(sessions.nextElement());
        }
        response.sendRedirect("../../index.jsp");
    }

}
