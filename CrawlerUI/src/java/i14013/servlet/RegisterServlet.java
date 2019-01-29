package i14013.servlet;

import i14013.database.AdminDB;
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
@WebServlet(name = "RegisterServlet", urlPatterns = {"/Register"})
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("input-username");
        String message;
        // cek username udah ada belum
        if (AdminDB.isUsernameExist(username)) {
            message = "Username already existed.";
        } else {
            String password = request.getParameter("input-pwd");
            AdminDB.addAdmin(username, password);
            message = "Admin added.";
        }
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        request.setAttribute("alertMessage", message);
        rd.include(request, response);

    }

}
