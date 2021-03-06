<%-- 
    Document   : topbarLecturer
    Created on : Oct 11, 2014, 10:45:54 PM
    Author     : weiyi.ngow.2012
--%>

<%@page import="entity.*"%>
<%@page import="dao.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="shortcut icon" href="img/DefaultLogo-favicon.ico">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <!--FONT-->
        <link href='http://fonts.googleapis.com/css?family=Roboto:400,100,100italic,300,300italic,400italic,500,500italic,700,700italic,900,900italic' rel='stylesheet' type='text/css'>
        <link href='http://fonts.googleapis.com/css?family=Playfair+Display+SC:400,700,900' rel='stylesheet' type='text/css'>

        <!--ICON-->
        <link rel="stylesheet" type="text/css" href="css/foundation-icons/foundation-icons.css">
        <link rel="stylesheet" type="text/css" href="css/foundation-icons/foundation-icons.svg">

    </head>
    <body>
        <div class="sticky">
            <nav class="top-bar" data-topbar data-options="sticky_on: large">
                <ul class="title-area">
                    <li class="name">
                        <h1><a href="viewScenarioLecturer.jsp"><img src="./img/DefaultLogo.png" width="30" height="30"/> EMR</a></h1>

                    </li>
                    <!-- Remove the class "menu-icon" to get rid of menu icon. Take out "Menu" to just have icon alone -->
                    <li class="toggle-topbar menu-icon"><a href="#"><span>Menu</span></a></li>
                </ul>

                <secion class="top-bar-section">
                    <!-- Right Nav Section -->

                    <ul class="left">
                        <li class="has-dropdown">
                            <a href="#">Case Management</a>
                            <ul class="dropdown">
                                <li class="has-dropdown"><a href="#">Manage Case</a>

                                    <ul class="dropdown">
                                        <li><label>Case</label></li>
                                        <li><a href="./viewScenarioLecturer.jsp">Activate</a></li>
                                        <li><a href="./resetCaseLecturer.jsp"><font color="#cf2a0e">Reset</font></a></li>
                                    </ul> 

                                </li>
                                <li><a href="./editStateLecturer.jsp">Activate State</a></li> 
                            </ul> 
                        <li><a href="./viewSubmissionLecturer.jsp">View Submissions</a></li>
                    </ul>
                    <%
                        String lecturerID = (String) session.getAttribute("lecturer");
                        Scenario activatedScenario = ScenarioDAO.retrieveScenarioActivatedByLecturer(lecturerID);
                        String caseName = "-";
                        if (activatedScenario != null) {
                            caseName = activatedScenario.getScenarioName();
                        }%>

                    <ul class="right"> 
                        <li><a href="#">Welcome, <%= lecturerID%>! </a></li>
                        <li><a href="#">Current Case: <%=caseName%></a></li>
                        <li><a href="ProcessLogoutLecturer">Log Out</a></li>
                    </ul>

                </secion>
            </nav>
        </div>
    </body>
</html>
