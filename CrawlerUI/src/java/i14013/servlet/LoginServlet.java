package i14013.servlet;

import i14013.database.AdminDB;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author 2014730013
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/Login"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("login-username");
        String password = request.getParameter("login-password");

        boolean usernameExist = AdminDB.isUsernameExist(username);
        if (!usernameExist) {
            request.setAttribute("alertMessage", "Username does not exist.");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.include(request, response);
        } else {
            boolean correctPassword = AdminDB.isCorrectPassword(username, password);
            if (!correctPassword) {
                request.setAttribute("alertMessage", "Password does not match.");
                RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
                rd.include(request, response);
            } else {
                HttpSession session = request.getSession();
                session.setAttribute("username", username);
                response.sendRedirect("pages/admin/admin-menu.jsp");
            }
        }

    }

}
