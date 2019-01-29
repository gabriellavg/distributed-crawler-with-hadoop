<%-- 
    Document   : content
    Created on : Apr 8, 2018, 5:52:07 PM
    Author     : Gabriella
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Content | Distributed Web Crawler</title>
        <link rel="stylesheet" href="../css/w3.css">
        <link rel="stylesheet" href="../css/font-awesome.css">
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

                    String title = "", content = "";
                    if (request.getAttribute("page") != null) {
                        String[] p = (String[]) request.getAttribute("page");

                        title = p[0];
                        if (title.length() == 0) {
                            title = "Untitled";
                        }

                        content = p[1];
                        if (content.length() == 0) {
                            content = "This page does not have a content.";
                        }

                    } else {
                        out.write("<p class = 'w3large' style='margin-top: 35vh'>No page found.</p>");
                    }

                %>
                <h3><b><%= title%></b></h3>
                <div class = "w3-panel">
                    <p align="justify">
                        <%= content%>
                    </p>

                </div>
            </div>
            <footer class="w3-container w3-black">
                <p class = "w3-small">Distributed Web Crawler Application by Gabriella</p>
            </footer>
        </div>
    </body>
</html>
