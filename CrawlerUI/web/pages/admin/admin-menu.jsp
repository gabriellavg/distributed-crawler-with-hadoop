<%-- 
    Document   : admin-menu
    Created on : Mar 19, 2018, 8:49:52 PM
    Author     : Gabriella
--%>

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
        <title>Admin Menu | Distributed Web Crawler</title>
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
            .menu-form-submit {
                font-size: 100%;
                background: none;
                display: block;
                border: 2px solid black;
                border-radius: 10px;
                margin: auto;
                padding: 5%;
                width: 100%;
                margin-top: 5%;
            }
            .menu-form-submit:hover {
                cursor: pointer;
                color: beige;
                background-color: black;
                opacity: 0.7;
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
                                <form method="post" action="Logout">
                                    <button class="w3-button w3-black" type="submit"><i class = "fa fa-sign-out"></i>&nbsp;Log out</a>
                                </form>
                            </div> 
                        </td>
                    </tr>
                </table>
            </header>

            <div class ="w3-container" style = "margin-top: 15vh;">
                <h2>Admin Menu</h2>
                <div class = "w3-panel" style="margin-top: 5vh;">
                    <button class="w3-button w3-margin-bottom w3-black w3-large" 
                            onclick="document.getElementById('modal-addurl').style.display = 'block'" 
                            style="display: block; padding: 1%; width: 25%; margin: auto;">Add URL File</button>
                    <form method="get" action="List">
                        <button type="submit" name="menu-submit" value="F 1" 
                               class="w3-button w3-margin-bottom w3-black w3-large"
                               style="display: block; padding: 1%; width: 25%; margin: auto;">Frontier</button>
                        <button type="submit" name="menu-submit" value="R 1" 
                               class="w3-button w3-margin-bottom w3-black w3-large"
                               style="display: block; padding: 1%; width: 25%; margin: auto;">Repository</button>
                    </form>
                </div>
            </div>
            <footer class="w3-container w3-black">
                <p class = "w3-small">Distributed Web Crawler Application by Gabriella</p>
            </footer>
        </div>
        <!-- modal -->
        <div id="modal-addurl" class="w3-modal">
            <div class="w3-modal-content w3-card-4" style="width: 30%;">
                <header class="w3-container w3-black"> 
                    <span onclick="document.getElementById('modal-addurl').style.display = 'none'" 
                          class="w3-button w3-display-topright">&times;</span>
                    <h2>Add URL File</h2>
                </header>
                <div class="w3-container">
                    <form method="post" action="UploadFile" enctype = "multipart/form-data"> 
                        <p><label for="file">Choose URL file to upload</label</p>
                        <p><input name="url-file" type="file" required/></p>
                        <p>
                            Mode:&nbsp;
                            <input type="radio" name="mode-radio" value="crawl" required>&nbsp;Crawl</input>
                            &nbsp;
                            <input type="radio" name="mode-radio" value="recrawl">&nbsp;Recrawl</input>
                        </p>
                        <p><button class="w3-button w3-black" name="upload-submit" type="submit">Add File</button></p>
                    </form>
                </div>
            </div>
        </div>
        <script>
            var message = "<%=request.getAttribute("alertMessage")%>";
            if (message !== "null") {
                alert(message);
            <% request.setAttribute("alertMessage", null);%>
            }
        </script> 
    </body>
</html>
