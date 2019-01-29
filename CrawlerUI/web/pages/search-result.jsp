<%-- 
    Document   : search-result
    Created on : Mar 23, 2018, 5:34:24 PM
    Author     : Gabriella
--%>

<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Search Result | Distributed Web Crawler</title>
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
            .h-url {
                word-wrap:break-word;
                font-size: 90%;
            }
            .page-title {
                color: blue;
                background: none;
                border: none;
                font-size: 120%;
                margin: 0;
                padding: 0;
            }
            .page-title:hover {
                cursor: pointer;
                color: black; 
                text-decoration: underline;
            }
        </style>
    </head>
    <body>
        <div class ="w3-container w3-center">
            <header class="w3-container w3-black" style ="position: fixed; top: 0; left: 0; width: 100%;">
                <table style="width: 100%; margin:0; padding: 0;">
                    <tr>
                        <td>
                            <form method="get" action="SearchTerm" style = "width: 50%;">
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
                                <%

                                    String location, name;
                                    if (session.getAttribute("username") != null) {
                                        location = "admin/admin-menu.jsp";
                                        name = "Admin Menu";
                                    } else {
                                        location = "../index.jsp";
                                        name = "Homepage";
                                    }

                                %>
                                <div class ="w3-right">
                                    <a class="w3-button w3-black" href="<%= location%>"><i class = "fa fa-home"></i>&nbsp;<%= name%></a>
                                </div>
                            </div> 
                        </td>
                    </tr>
                </table>
            </header>

            <div class ="w3-container" style = "margin-top: 7.5vh; margin-bottom: 7.5vh;">
                <%
                    ArrayList<String> result = (ArrayList<String>) request.getAttribute("res");
                    int cPage = 0, maxPage = 0, count = 0;
                    if (!result.isEmpty()) {
                        String[] str = result.get(0).split("\\^");
                        cPage = Integer.parseInt(str[0]);
                        count = Integer.parseInt(str[1]);
                        maxPage = (count + 10 - 1) / 10;
                    }
                %>
                <div style = "text-align: left">
                    <h2 class="w3-large">Search result for <b><%= request.getAttribute("term")%></b></h2>
                    <p>Search time <b><%= request.getAttribute("time")%> seconds</b>
                        with total <b><%= count%> results.</b></p>
                </div>
                <div class = "w3-panel">
                    <%

                        if (!result.isEmpty()) {

                            Iterator i = result.iterator();
                            i.next();
                            while (i.hasNext()) {

                                String[] tr = ((String) i.next()).split("\\^");
                                String url = tr[0];
                                String title;
                                if (tr.length > 1) {
                                    title = tr[1];
                                } else {
                                    title = "Untitled";
                                }
                                String tmp = url.replace("'", "^");

                                out.write("<table class = 'w3-table'><tr><td><form method='get' "
                                        + "action='SeeContent'><input type='hidden' name='res-url' value='"
                                        + tmp + "' /><button type='submit' class='page-title'>"
                                        + title + "</button></form><p class='h-url'>" + url + "</p></td></tr></table>");

                            }

                        } else {
                            out.write("<p class = 'w3large' style='margin-top: 35vh'>No result found.</p>");
                        }


                    %>
                </div>
                <% if (!result.isEmpty()) {%>
                <div class = "w3-panel">
                    <p>Page <%= cPage%>&sol;<%= maxPage%> (<%= count%> results)</p>
                    <% if (maxPage != 1) {%>
                    <form method="get" action="SearchTerm">
                        <input type="hidden" name="input-term" value="<%= request.getAttribute("term")%>" />
                        <button type="submit" name="search-term-submit" value="1" class="w3-button w3-black" />&laquo;</button>
                        <% if (cPage > 1) {%>
                        <button type="submit" name="search-term-submit" value="<%= cPage - 1%>" class="w3-button w3-black" />&lsaquo;</button>
                        <% } %>
                        <% if (cPage < maxPage) {%>
                        <button type="submit" name="search-term-submit" value="<%= cPage + 1%>" class="w3-button w3-black" />&rsaquo;</button>
                        <% }%>
                        <button type="submit" name="search-term-submit" value="<%= maxPage%>" class="w3-button w3-black" />&raquo;</button>
                    </form>
                    <button class="w3-margin-top w3-button w3-black" onclick="document.getElementById('modal-page-s').style.display = 'block'"/>To page &hellip;</button>
                    <% }  %>
                </div>
                <% }%>
            </div>
            <footer class="w3-container w3-black">
                <p class = "w3-small">Distributed Web Crawler Application by Gabriella</p>
            </footer>
        </div>
        <!-- modal -->
        <div id="modal-page-s" class="w3-modal">
            <div class="w3-modal-content w3-card-4" style="width: 20%;">
                <header class="w3-container w3-black"> 
                    <span onclick="document.getElementById('modal-page-s').style.display = 'none'" 
                          class="w3-button w3-display-topright">&times;</span>
                    <h2>To page &hellip;</h2>
                </header>
                <div class="w3-container">
                    <form method="get" action="SearchTerm">
                        <p>
                            <input type="hidden" name="input-term" value="<%= request.getAttribute("term")%>" /> 
                            <input class="w3-input" name="s-to-page" type="number" min="1" max="<%= maxPage%>"placeholder="Page" required="required" />
                        </p>
                        <p>
                            <button class="w3-button w3-black" name="search-term-submit" type="submit" Value="custom">OK</button>
                        </p>
                    </form> 
                </div>
            </div>
        </div>
    </body>
</html>
