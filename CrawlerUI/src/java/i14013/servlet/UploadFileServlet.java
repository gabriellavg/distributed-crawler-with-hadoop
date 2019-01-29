package i14013.servlet;

import i14013.database.Frontier;
import i14013.engine.UrlChecker;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author Gabriella
 */
@WebServlet(name = "UploadFileServlet", urlPatterns = {"/pages/admin/UploadFile"})
@MultipartConfig
public class UploadFileServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String mode = request.getParameter("mode-radio");

        Part filePart = request.getPart("url-file");
        InputStream fileContent = filePart.getInputStream();

        Scanner scanner = new Scanner(fileContent).useDelimiter(System.getProperty("line.separator"));
        int ct = 0;
        int total = 0;
        if (mode.equals("crawl")) {
            while (scanner.hasNext()) {
                String[] str = scanner.next().split(" "); // [0]url [1]depth
                if (UrlChecker.isValid(str[0]) && !Frontier.urlExist(str[0])) {
                    Frontier.addUrl(str[0], 0, Integer.parseInt(str[1]), str[0]);
                } else {
                    ct++;
                }
                total++;
            }
        } else {
            while (scanner.hasNext()) {
                String url = scanner.next();
                if (UrlChecker.isValid(url) && Frontier.urlExist(url)) {
                    Frontier.markForRecrawl(url);
                } else {
                    ct++;
                }
                total++;
            }
        }

        String message;
        if (ct > 0) {
            message = ct + " of " + total + " URL(s) from file not saved to frontier.";
        } else {
            message = "All URLs from file saved to frontier.";
        }
        request.setAttribute("alertMessage", message);
        RequestDispatcher rd = request.getRequestDispatcher("admin-menu.jsp");
        rd.include(request, response);

    }

}
