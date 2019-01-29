<%-- 
    Document   : index
    Created on : Mar 20, 2018, 7:23:16 PM
    Author     : Gabriella
--%>

<%@page import="i14013.database.AdminDB"%>
<%@page import="i14013.database.WebRepository"%>
<%@page import="i14013.database.Frontier"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Distributed Web Crawler</title>
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
            }
            .search-button {
                border: none;
                background: none;
                width: 100%;
            }
            .search-button:hover {
                cursor: pointer;
                background-color: lightgray;
            }
            #index-div {
                margin-top: 35vh;
            }
        </style>
    </head>
    <body>
        <%

            AdminDB.initAdminDB();
            Frontier.initFrontier();
            WebRepository.initRepository();

        %>

        <div class ="w3-container w3-center">
            <header class="w3-container" style ="position: fixed; top: 0; left: 0; width: 100%;">
                <a class="w3-button w3-right" onclick="document.getElementById('modal-login').style.display = 'block'"><i class = "fa fa-sign-in"></i>&nbsp;Log in</a>
                <a class="w3-button w3-right" onclick="document.getElementById('modal-reg').style.display = 'block'"><i class = "fa fa-user-plus"></i>&nbsp;Register</a>
            </header>
            <div id="index-div">
                <div class="w3-panel">
                    <h1>Distributed Web Crawler Application</h1>
                </div> 
                <div class="w3-panel">
                    <form class = "w3-card w3-padding" method="get" action="pages/SearchTerm" style="margin: auto; width: 35%;">
                        <table style = "width: 100%;">
                            <tr>
                                <td>
                                    <input class="search-input" name="input-term" type="text" placeholder="Search term" required="required" />
                                </td>
                                <td>
                                    <button class="search-button" name="search-term-submit" type="submit"  value="1"><i class = "fa fa-search"></i></button>
                                </td>
                            </tr>
                        </table>
                    </form>
                </div>
            </div>
            <footer class="w3-container w3-black">
                <p class = "w3-small">Distributed Web Crawler Application by Gabriella</p>
            </footer>
        </div> 
        <!-- modal -->
        <div id="modal-login" class="w3-modal">
            <div class="w3-modal-content w3-card-4" style="width: 30%;">
                <header class="w3-container w3-black"> 
                    <span onclick="document.getElementById('modal-login').style.display = 'none'" 
                          class="w3-button w3-display-topright">&times;</span>
                    <h2>Log in</h2>
                </header>
                <div class="w3-container">
                    <form method="post" action="Login">
                        <p>
                            <input class="w3-input" name="login-username" type="text" placeholder="Username" required="required" />
                        </p>
                        <p>
                            <input class="w3-input" name="login-password" type="password" placeholder="Password" required="required" />
                        </p>
                        <p>
                            <button class="w3-button w3-black" name="login-submit" type="submit">Log in</button>
                        </p> 
                    </form>
                </div>
            </div>
        </div>
        <div id="modal-reg" class="w3-modal">
            <div class="w3-modal-content w3-card-4" style="width: 30%;">
                <header class="w3-container w3-black"> 
                    <span onclick="document.getElementById('modal-reg').style.display = 'none'" 
                          class="w3-button w3-display-topright">&times;</span>
                    <h2>Register</h2>
                </header>
                <div class="w3-container">
                    <form method="post" action="Register"> 
                        <p>
                            <input class="w3-input" type="text" name="input-username" placeholder="Username" required="required" />
                        </p>
                        <p>
                            <input class="w3-input" type="password" name="input-pwd" placeholder="Password" required="required" />
                        </p>
                        <p>
                            <input class="w3-button w3-black" type="submit" Value="Register" />
                        </p> 
                    </form>
                </div>
            </div>
        </div>
    </body>
    <script>
        var message = "<%=request.getAttribute("alertMessage")%>";
        if (message !== "null") {
            alert(message);
        <% request.setAttribute("alertMessage", null);%>
        }
    </script>
</html>
