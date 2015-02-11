<%-- 
    Document   : editState
    Created on : Jan 30, 2015, 9:26:39 PM
    Author     : gladyskhong.2012
--%>

<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="dao.*"%>
<%@page import="entity.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="protectPage/protectAdmin.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        
        <!--Web Title-->
        <title>EMR | Case Management | Manage Case | Edit State Information</title>
        
        <link rel="stylesheet" href="css/foundation.css" />
        <link rel="stylesheet" href="responsive-tables.css">
        <link rel="stylesheet" href="css/original.css" />
        <script src="responsive-tables.js"></script>
        <%@include file="/topbar/topbarAdmin.jsp" %>

        <link rel="stylesheet" href="//code.jquery.com/ui/1.11.1/themes/smoothness/jquery-ui.css">
        <script src="//code.jquery.com/jquery-1.10.2.js"></script>
        <script src="//code.jquery.com/ui/1.11.1/jquery-ui.js"></script>
        <script type="text/javascript" src="js/humane.js"></script>
        <script type="text/javascript" src="js/app.js"></script>
        <link rel="stylesheet" href="/resources/demos/style.css">
    </head>
    <body>
        <br>
        <ul class="breadcrumbs">
            <li><a href="editScenario.jsp">Edit Case Information</a></li>
            <li class="current">Edit State Information</li>
            <li><a href="editMedication.jsp">Edit Medication </a></li>
            <li><a href="editReportDocument.jsp">Edit Report and Document </a></li>
        </ul><br/>

        <%            String success = "";
            String error = "";

            String scenarioID = (String) session.getAttribute("scenarioID");
            String scenario = scenarioID.replace("SC", "Case ");
            String patientNRIC = (String) session.getAttribute("patientNRIC");
            String editingInProcess = "editingInProcess";

            List<State> stateList = StateDAO.retrieveAll(scenarioID);

        %>
        <div class="large-centered large-6 columns">
            <!--Display states that are in the database-->

            <% if (stateList == null || stateList.size() - 1 == 0 || stateList.size() - 1 == -1) {
                    out.println("<h3>" + "There are no states created yet." + "</h3>");

                } else {
            %>
            <center><h5><aa href="#" style data-reveal-id="createState">Click to Add New State for <%=scenario%></aa></h5></center>   
            <form action ="ProcessEditState" method="POST">
                <%
                    List<String> stList = new ArrayList<String>();
                    String stateDescription = "";
                    int counter = 0;
                    for (State state : stateList) {
                        String stateRetrieved = state.getStateID();
                        stateDescription = state.getStateDescription();
                        String stateNumber = "";
                        if (!stateRetrieved.equals("ST0")) {
                            int number = Integer.parseInt(stateRetrieved.replace("ST", ""));
                            //stateNumber.replace("ST", "State ");
                            String stateDescriptionNumber = "statedescription" + counter;
                            session.setAttribute("dNum", stateDescriptionNumber);
                            String stateDesc = stateRetrieved.replace("ST", "State ");

                            Prescription prescription = PrescriptionDAO.retrieve(scenarioID, stateRetrieved, "NA");
                            String doNum = "doctorOrder" + counter;
                            String pNum = "p" + counter;
                            String doctorOrder = "";
                            String p = "prescription is not null";
                            if (prescription != null) {
                                doctorOrder = prescription.getDoctorOrder();
                            } else {
                                p = "null";
                            }

                %>
                <div class="panelCase">
                    <div align="left">  <b><%=stateDesc%></b></div><br/>
                    <input type="hidden" name ="stateNumber" value="<%=stateNumber%>">
                    <input type="hidden" name ="stateListSize" value="<%=stateList.size()%>">
                    <label>Description
                        <input type ="text" name ="<%=stateDescriptionNumber%>" value ="<%=stateDescription%>">
                    </label>
                    <label>Healthcare Provider's Order
                        <!--<i>Please include "&lt;br&gt;" to separate doctor order to next line.</i>-->
                        <textarea style = "resize:vertical"  name="<%=doNum%>" rows="5" cols="10"><%=doctorOrder%></textarea>
                    </label>
                </div>

                <input type ="hidden" name ="scenarioID" value ="<%=scenarioID%>">
                <input type ="hidden" name="<%=pNum%>" value="<%=p%>">
                <% }
                            //stateName = state.getStateDescription();
                            counter++;

                        }

                        //session.setAttribute("stateListSize", counter);
                        // session.setAttribute("stList", stList);
                        //stList = request.getAttribute("stList");
                    }%>

                <!--End of display states in the database-->
                <center><input type = "submit" Value ="Save and Proceed  >>" class="button small"></center>

            </form>
            <!-- Reveal model for Create State -->
            <div id="createState" class="reveal-modal" data-reveal>
                <div class="large-centered large-8 columns">
                    <h2>Add State <%=stateList.size()%></h2>
                    <form action = "ProcessAddState" method = "POST">

                        <input type ="hidden" name ="scenarioID" value ="<%=scenarioID%>">
                        <input type ="hidden" name ="patientNRIC" value ="<%=patientNRIC%>">
                        <input type ="hidden" name ="editState" value ="<%=editingInProcess%>">

                        <label>State Description
                            <input type="text" name="stateDescription" value = "" required></label>

                        <label>Healthcare Provider's Order
                            <textarea style = "resize:vertical"  name="doctorOrderForState" rows="5" cols="10" placeholder ="" required></textarea>
                        </label>

                        <br/><br/>
                        <center><input type="submit" value="Create State" class="button small"></center>
                    </form>  
                </div> 
                <a class="close-reveal-modal">&#215;</a>
            </div>   


            <script src="js/vendor/jquery.js"></script>
            <script src="js/foundation.min.js"></script>

            <script>

                $(document).ready(function() {
                    $(document).foundation();
                    var humaneSuccess = humane.create({baseCls: 'humane-original', addnCls: 'humane-original-success', timeout: 8000, clickToClose: true})
                    var humaneError = humane.create({baseCls: 'humane-original', addnCls: 'humane-original-error', timeout: 8000, clickToClose: true})

                    var success1 = "<%=success%>";
                    var error1 = "<%=error%>";
                    if (success1 !== "") {
                        humaneSuccess.log(success1);
                    } else if (error1 !== "") {
                        humaneError.log(error1);
                    }

                });
            </script>
            <script type="text/javascript" src="js/humane.js"></script>


    </body>
</html>
