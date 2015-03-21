/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.MedicineDAO;
import dao.PrescriptionDAO;
import entity.Medicine;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author hpkhoo.2012
 */
@WebServlet(name = "ProcessAddMedication", urlPatterns = {"/ProcessAddMedication"})
public class ProcessAddMedication extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            HttpSession session = request.getSession(false);
            
            String editMedicine = request.getParameter("editingMedicine");
            String scenarioID = (String) session.getAttribute("scenarioID");
            String stateID = (String) request.getParameter("stateID");
            String discontinueStateID = (String) request.getParameter("discontinueStateID");
            
            String fullTextStateID = stateID.replace("ST", "State ");

            //retrieve values for Medicine table
            String medicineName = request.getParameter("medicineName");
            Medicine medicineRetrieved = MedicineDAO.retrieveByMedicineName(medicineName);
            String medicineBarcode = medicineRetrieved.getMedicineBarcode();
       

           // String routeRetrieved = medicineRetrieved.getRouteAbbr(); //commented out after db refactor

            
            //retrieve values for Prescription table
            String doctorName = request.getParameter("doctorName");
            String doctorOrder = request.getParameter("doctorOrder");
            String freq = request.getParameter("frequency");
            String route = request.getParameter("route");

            //retrieve values for Frequency table
            String dosage = request.getParameter("dosage");


            if (medicineName != null && (!stateID.equals(discontinueStateID)) ) {
                PrescriptionDAO.add(scenarioID, stateID, doctorName, doctorOrder, freq, medicineBarcode, discontinueStateID, dosage, route);
            }else{
                  session.setAttribute("error", "You cannot select the same state for state and discontinue state.");
            }
            
            if (editMedicine == null || editMedicine.equals("")) {
                response.sendRedirect("createMedicationBC.jsp");
            } else {
                response.sendRedirect("editMedication.jsp");
            }

            
            
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
