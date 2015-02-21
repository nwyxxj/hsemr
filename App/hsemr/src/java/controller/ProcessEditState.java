/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.PrescriptionDAO;
import dao.StateDAO;
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
 * @author gladyskhong.2012
 */
@WebServlet(name = "ProcessEditState", urlPatterns = {"/ProcessEditState"})
public class ProcessEditState extends HttpServlet {

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
            /* TODO output your page here. You may use following sample code. */
            HttpSession session = request.getSession(false);
            String scenarioID = request.getParameter("scenarioID");
            int stateListSize = Integer.parseInt(request.getParameter("stateListSize"));
            for (int i = 0; i < stateListSize; i++) {
                int num = i+1; 
                String descNum = "statedescription" + num;
                String doNum = "doctorOrder" + num;
                String pNum = "p" + num;
                String desc = request.getParameter(descNum);
                String doctorOrder = request.getParameter(doNum);
//                String prescription = request.getParameter(pNum);
                String prescription = request.getParameter(pNum);
                int statenum = i + 1;
                String stateID = "ST" + statenum;
                
                
                if (prescription == null || prescription.equals("null")) {
                    if (doctorOrder != null && !doctorOrder.equals("") ) {
                       // MedicinePrescriptionDAO.add("NA", scenarioID, stateID, "NA", "Nil");
                        PrescriptionDAO.add(scenarioID, stateID, "Dr.Tan/01234Z", doctorOrder, "NA", "NA", "-","-","N.A") ;
                    }
                } else {
                    if(doctorOrder == null || doctorOrder.equals("")) {
                        //MedicinePrescriptionDAO.deleteNA(scenarioID, stateID);
                        PrescriptionDAO.deletePrescriptionNA(scenarioID, stateID) ;
                    } else {
                        //PrescriptionDAO.updatePres("Dr.Tan/01234Z", doctorOrder, "NA", scenarioID, stateID, "NA");
                        PrescriptionDAO.updatePresOrderDesc(scenarioID, stateID , "Dr.Tan/01234Z", doctorOrder, "NA");
                    }
                }
                
                StateDAO.updateStateDesc(stateID, desc, scenarioID);
            }

            response.sendRedirect("editMedication.jsp");
        } catch (Exception e) {
            e.printStackTrace();
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
