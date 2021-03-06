<%-- 
    Document   : viewAllChart
    Created on : Jan 27, 2015, 12:15:14 AM
    Author     : hpkhoo.2012
--%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="entity.Vital"%>
<%@page import="java.util.List"%>
<%@page import="dao.VitalDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!-- Load c3.css -->
        <link href="css/c3.css" rel="stylesheet" type="text/css">

        <!-- Load d3.js and c3.js -->
        <script src="js/d3.min.js" charset="utf-8"></script>
        <script src="js/c3/c3.min.js"></script>
        <link rel="stylesheet" href="css/foundation.css" />
        <link rel="stylesheet" href="css/original.css" />
        <script src="js/vendor/modernizr.js"></script>
        
        <!--FONT-->
        <link href='http://fonts.googleapis.com/css?family=Roboto:400,100,100italic,300,300italic,400italic,500,500italic,700,700italic,900,900italic' rel='stylesheet' type='text/css'>
    </head>
    <body>
        <% 
           //retrieve list of temperature based on scenario
           String scenarioID= (String) session.getAttribute("scenarioID");
           List<Double> tempList= VitalDAO.retrieveTemp(scenarioID); 
           //retrieve vitals related to current case
           List<Vital> vitals = VitalDAO.retrieveTempByScenarioID(scenarioID);
           
           //get dates of all vitals
           List<Date> vitalsDateTime = VitalDAO.retrieveVitalTime(vitals);           
           
           //format date to be printed in string format
           DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           //a string to store all dates in format to be used in javascript 
           //e.g. new Date ('2012-01-02T22:25:15'), new Date ('2012-02-02T22:25:17'), new Date ('2012-02-02T22:25:20'),new Date ('2012-02-02T22:25:23') 
           String vitalsDate = ""; 
           if (vitalsDateTime.size() > 0) { 
                for (int i = 0; i < vitalsDateTime.size(); i++ ) { 
                    String dateTimeVital = df.format(vitalsDateTime.get(i));
                    if (i != vitalsDateTime.size()-1) {
                        vitalsDate += "'" + dateTimeVital + "', ";
                    } else { 
                        vitalsDate += "'" +  dateTimeVital + "'";
                    }
                }
           }    
           //converting templist to string for mainpulation
           String tempStringArr= tempList.toString();
           String withoutbracket = tempStringArr.replace("[", ""); 
           String dataOfTemp= withoutbracket.replace("]", "") ;
        %>
        <h3>Temperature Chart</h3>           
        <div id="chart"></div>
        <%
            if (tempList == null || tempList.size() == 0) {
                out.println("<h5>There is no historial data at the moment.</h5>");
            } else { 
        %>
        <script type="text/javascript">

            var chart = c3.generate({
                padding: {
                    left: 60, //at least 60 for y axis to be seen
                    right: 100 // add 100px for some spacing
                },
                data: {
                    columns: [
                        ['Temperature', <% out.println(dataOfTemp); %>]
                    ],
                    labels: true,
                    type: 'line',
                },
                axis: {
                    x: {
                        type: 'category',
                        categories: [<% out.println(vitalsDate);%>],
                    },
                    y: {
                        label: {
                            text: 'Temperature (ºC)',
                            position: 'outer-middle'
                        },
                        tick: {
                            format: function(x) {
                                return (x === Math.floor(x)) ? x : "";
                            }
                        }
                    }

                },
                grid: {
                    x: {
                        show: true
                    },
                    y: {
                        show: true
                    }
                }

            });
            chart.resize({height: 300, width: 700});
        </script>
        <% 
        }
       //retrieve list of temperature based on scenario
           
       List<Integer> hrList= VitalDAO.retrieveHR(scenarioID); 
       List<Integer> spoList= VitalDAO.retrieveSPO(scenarioID); 
       List<Integer> rrList= VitalDAO.retrieveRR(scenarioID); 
       List<Integer> bpSystolicList= VitalDAO.retrieveBPSystolic(scenarioID); 
       List<Integer> bpDiastolicList= VitalDAO.retrieveBPDiastolic(scenarioID); 
            
           
       //get dates of all vitals
       List<Date> vitalsDateTimeAll = VitalDAO.retrieveLatestDateTime(scenarioID);  
            
       //format date to be printed in string format
       //DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       //a string to store all dates in format to be used in javascript 
       //e.g. new Date ('2012-01-02T22:25:15'), new Date ('2012-02-02T22:25:17'), new Date ('2012-02-02T22:25:20'),new Date ('2012-02-02T22:25:23') 
       String vitalsDateAll = ""; 
       if (vitalsDateTimeAll.size() > 0) { 
            for (int i = 0; i < vitalsDateTimeAll.size(); i++ ) { 
                String dateTimeVital = df.format(vitalsDateTimeAll.get(i));
                
                if (i != vitalsDateTimeAll.size()-1) {
                    vitalsDateAll += "'" + dateTimeVital + "', ";
                } else { 
                    vitalsDateAll += "'" +  dateTimeVital + "'";
                }
            }
       }
           
         
                     
       //converting templist to string for mainpulation
       String hrStringArr= hrList.toString();
       String withoutbracketHR = hrStringArr.replace("[", ""); 
       String dataOfHR= withoutbracketHR.replace("]", "") ;
           
       String spoStringArr= spoList.toString();
       String withoutbracketSPO = spoStringArr.replace("[", ""); 
       String dataOfSPO= withoutbracketSPO.replace("]", "") ;
           
       String rrStringArr= rrList.toString();
       String withoutbracketRR = rrStringArr.replace("[", ""); 
       String dataOfRR= withoutbracketRR.replace("]", "") ;
         
       //converting bpSystolicList to string for mainpulation
       String bpSystolicStringArr= bpSystolicList.toString();
       String withoutbracketBPsystolic = bpSystolicStringArr.replace("[", ""); 
       String dataOfBPsystolic= withoutbracketBPsystolic.replace("]", "") ;
           
       //converting bpDiastolicList to string for mainpulation
       String bpDiastolicStringArr= bpDiastolicList.toString();
       String withoutbracketBPdiastolic = bpDiastolicStringArr.replace("[", ""); 
       String dataOfBPdiastolic= withoutbracketBPdiastolic.replace("]", "") ;
        %>
        <hr>
        <h3>Heart Rate - Respiratory Rate - Blood Pressure - SPO<sub>2</sub> Chart</h3>        
        <div id="chart2"></div>
        <%
            if (vitalsDateTimeAll == null || vitalsDateTimeAll.size() == 0) {
                out.println("<h5>There is no historial data at the moment.</h5>");
            } else { 
        %>
        <script type="text/javascript">

            var chart2 = c3.generate({
                bindto: '#chart2',
                padding: {
                    left: 60, //at least 60 for y axis to be seen
                    right: 100 // add 10px for some spacing
                },
                data: {
                    columns: [
                        ['Respiratory', <% out.println(dataOfRR); %>],
                        ['HeartRate', <% out.println(dataOfHR); %>],
                        ['SPO', <% out.println(dataOfSPO); %>],
                        ['BPsystolic', <% out.println(dataOfBPsystolic); %>],
                        ['BPdiastolic', <% out.println(dataOfBPdiastolic); %>]
                    ],
                    labels: true,
                    type: 'line',
                    colors: {
                        BPdiastolic: '#d00',
                        BPsystolic: '#d00',
                        Respiratory: '#216608',
                        SPO: '#F2CE02',
                        HeartRate: '#5D98FC'
                    }
                },
                legend: {
                    show: false
                },
                axis: {
                    x: {
                        type: 'category',
                        categories: [<% out.println(vitalsDateAll);%>],
                    },
                    y: {
                        label: {// ADD
                            text: 'Respiration-Pulse-SPO-Blood Pressure',
                            position: 'outer-middle',
                        },
                        tick: {
                            format: function(x) {
                                return (x === Math.floor(x)) ? x : "";
                            }
                        }

                    }

                },
                grid: {
                    x: {
                        show: true
                    },
                    y: {
                        show: true
                    }
                }
            });
            chart2.resize({height: 300, width: 700});
        </script>  
        <% } %>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="img/Legend.PNG" width = "400" height = "30"/>
    </body>
</html>
