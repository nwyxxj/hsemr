<%-- 
    Document   : editPracticalGroupAccount
    Created on : Oct 15, 2014, 2:50:03 PM
    Author     : weiyi.ngow.2012
--%>

<%@page import="entity.PracticalGroup"%>
<%@page import="entity.Lecturer"%>
<%@page import="java.util.List"%>
<%@page import="java.util.List"%>
<%@page import="entity.Admin"%>
<%@page import="dao.PracticalGroupDAO"%>
<%@page import="dao.LecturerDAO"%>
<%@page import="dao.AdminDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="protectPage/protectAdmin.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="css/foundation.css" />
        <link rel="stylesheet" href="responsive-tables.css">
        <script src="responsive-tables.js"></script>
        <%@include file="/topbar/topbarAdmin.jsp" %>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Edit Accounts</title>
    </head>
    <body>
       
        <div class="row" style="padding-top: 30px;">
            <div class="large-centered large-12 columns">
                <center>
                    <h1>Edit <%=request.getParameter("userID")%>'s details</h1>

                    <form action = "ProcessEditAccount" method = "post">
                        <div class="row">
                            <div class="large-8 columns">
                                <div class="row">
                                    <div class="small-7 columns">
                                        <label for="right-label" class="right inline">User ID</label>
                                        <label for="right-label" class="right inline">Password</label>
                                        <label for="right-label" class="right inline">Lecturer ID</label>
                                    </div>
                                    
                                    <div class="small-5 columns">
                                        <input type="text" id="right-label" name="userID" value="<%=request.getParameter("userID")%>" readonly>
                                        <input type="text" id="right-label" name="password" value="<%=request.getParameter("password")%>" required autofocus>
                                        <input type="text" id="right-label" name="lecturerID" value="<%=request.getParameter("lecturerID")%>" required>
                                        <input type="hidden" id="right-label" name="type" value="<%=request.getParameter("type")%>">
                                    </div>
                                    <input type="submit" class="button tiny" value="Save"> 


                                    <%
                                        String location = "";
                                        if (request.getParameter("type") != null && !request.getParameter("type").equals("")) {
                                            if (request.getParameter("type").equals("admin")) {
                                                location = "viewAdminAccounts.jsp";
                                            } else if (request.getParameter("type").equals("lecturer")) {
                                                location = "viewLecturerAccounts.jsp";
                                            } else {
                                                location = "viewPracticalGroupAccounts.jsp";
                                            }
                                        }
                                    %>
                                    <input type="button" value="Cancel" class="button tiny" onClick="window.location='<%=location%>'"/>
                                </div>
                            </div>
                        </div>
                    </form>
                </center>
            </div>
        </div>
    </body>
</html>