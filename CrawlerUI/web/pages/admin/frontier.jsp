<%-- 
    Document   : frontier
    Created on : Mar 25, 2018, 9:53:07 PM
    Author     : Gabriella
--%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.SimpleDateFormat"%>
<%
    if (session.getAttribute("username") == null) {
        response.sendRedirect("../../index.jsp");
    }
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Frontier | Distributed Web Crawler</title>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/w3.css">
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/font-awesome.css">
        <style>
            footer {
                position: fixed;
                left: 0;
                bottom: 0;
                width: 100%;
            }
            .search-input {
                border: none;
                width: 100%;
                padding: 1%;
            }
            .search-button {
                color: lightgray;
                border: none;
                background: none;
                width: 100%;
            }
            .search-button:hover {
                color: black;
                cursor: pointer;
                background-color: lightgray;
            }
            .url-td {
                max-width: 30vw;
                word-wrap:break-word;
            }
        </style>
    </head>
    <body>
        <div class ="w3-container w3-center">
            <header class="w3-container w3-black" style ="position: fixed; top: 0; left: 0; width: 100%;">
                <table style="width: 100%; margin:0; padding: 0;">
                    <tr>
                        <td>
                            <form method="get" action="${pageContext.request.contextPath}/pages/SearchTerm" style = "width: 50%;">
                                <table style = "width: 100%;">
                                    <tr>
                                        <td>
                                            <input class="search-input" name="input-term" type="text" placeholder="Search term" required="required" />
                                        </td>
                                        <td>
                                            <button class="search-button" name="search-term-submit" type="submit" value="1"><i class = "fa fa-search"></i></button>
                                        </td>
                                    </tr>
                                </table>
                            </form>
                        </td>
                        <td>
                            <div class ="w3-right">
                                <a class="w3-button w3-black" href="admin-menu.jsp"><i class = "fa fa-home"></i>&nbsp;Admin Menu</a>
                            </div> 
                        </td>
                    </tr>
                </table>
            </header>


            <div class ="w3-container" style = "margin-top: 7.5vh; margin-bottom: 7.5vh;">
                <h2>Frontier's URL</h2>
                <div class = "w3-panel" style="margin-top: 5vh;">
                    <%

                        ArrayList<String> res = (ArrayList<String>) request.getAttribute("res");
                        int cPage = 0, maxPage = 0, count = 0;

                        if (!res.isEmpty()) {

                            out.write("<div class='w3-responsive'><table class='w3-table w3-bordered' width='100%'");
                            out.write("<tr'><th>URL</th><th>Mode</th><th>Status</th><th>Processed by</th><th align='right'>"
                                    + "Start Depth</th><th align='right'>Max Depth</th><th>Root</th><th align='right'>Added on</th></tr>");

                            Iterator i = res.iterator();
                            String[] tr = ((String) i.next()).split(" ");
                            cPage = Integer.parseInt(tr[0]);
                            count = Integer.parseInt(tr[1]);
                            maxPage = (count + 10 - 1) / 10;
                            while (i.hasNext()) {
                                tr = ((String) i.next()).split(" ");
                                String url = tr[0];
                                String mode = tr[1];
                                int status = Integer.parseInt(tr[2]);
                                int procby = Integer.parseInt(tr[3]);
                                int start = Integer.parseInt(tr[4]);
                                int max = Integer.parseInt(tr[5]);
                                String root = tr[6];
                                Long timestamp = Long.parseLong(tr[7]);
                                String date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(timestamp);

                                String statustext;
                                switch (status) {
                                    case 0:
                                        statustext = "On process";
                                        break;
                                    case 1:
                                        statustext = "Finished";
                                        break;
                                    default:    // status -1
                                        statustext = "Waiting process";
                                        break;
                                }

                                String procbytext;
                                switch (procby) {
                                    case 0:
                                        procbytext = "-";
                                        break;
                                    default:
                                        procbytext = "Agent #" + procby;
                                        break;
                                }

                                out.write("<tr><td class = 'url-td'>" + url + "</td><td>" + mode + "</td><td><b>" + statustext + "</b></td><td>"
                                        + procbytext + "</td><td align='right'>" + start + "</td><td align='right'>" + max + "</td><td class = 'url-td'>" + root
                                        + "</td><td align='right'>" + date + "</td></tr>");

                            }

                            out.write("</table></div>");

                        } else {
                            out.write("<p class = 'w3-large' style='margin-top: 25vh'>Frontier is empty.</p>");
                        }


                    %>
                </div>
                <% if (!res.isEmpty()) {%>
                <div class = "w3-panel">
                    <p>Page <%= cPage%>&sol;<%= maxPage%> (<%= count%> results)</p>
                    <% if (maxPage != 1) { %>
                    <form method="get" action="List">
                        <button type="submit" name="menu-submit" value="F 1" class="w3-button w3-black" />&laquo;</button>
                        <% if (cPage > 1) {%>
                        <button type="submit" name="menu-submit" value="F <%= cPage - 1%>" class="w3-button w3-black" />&lsaquo;</button>
                        <% } %>
                        <% if (cPage < maxPage) {%>
                        <button type="submit" name="menu-submit" value="F <%= cPage + 1%>" class="w3-button w3-black" />&rsaquo;</button>
                        <% }%>
                        <button type="submit" name="menu-submit" value="F <%= maxPage%>" class="w3-button w3-black" />&raquo;</button>
                    </form>
                    <button class="w3-margin-top w3-button w3-black" onclick="document.getElementById('modal-page').style.display = 'block'"/>To page &hellip;</button>
                    <% }  %>
                </div>
                <% }%>
            </div>
            <footer class="w3-container w3-black">
                <p class = "w3-small">Distributed Web Crawler Application by Gabriella</p>
            </footer>
        </div>
        <!-- modal -->
        <div id="modal-page" class="w3-modal">
            <div class="w3-modal-content w3-card-4" style="width: 20%;">
                <header class="w3-container w3-black"> 
                    <span onclick="document.getElementById('modal-page').style.display = 'none'" 
                          class="w3-button w3-display-topright">&times;</span>
                    <h2>To page &hellip;</h2>
                </header>
                <div class="w3-container">
                    <form method="get" action="List">
                        <p>
                            <input class="w3-input" name="f-to-page" type="number" min="1" max="<%= maxPage%>"placeholder="Page" required="required" />
                        </p>
                        <p>
                            <button class="w3-button w3-black" name="menu-submit" type="submit" Value="F custom">OK</button>
                        </p>
                    </form> 
                </div>
            </div>
        </div>
    </body>
</html>
