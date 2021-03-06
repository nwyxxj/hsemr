/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.pdfcleanup.PdfCleanUpLocation;
import com.itextpdf.text.pdf.pdfcleanup.PdfCleanUpProcessor;
import dao.*;
import entity.Keyword;
import entity.Patient;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author weiyi.ngow.2012
 */
@WebServlet(name = "ProcessExtractPDF", urlPatterns = {"/ProcessExtractPDF"})
public class ProcessExtractPDF extends HttpServlet {

    /**
     * The original PDF that will be parsed.
     */
    // location to store file uploaded
    private static final String DATA_DIRECTORY = "scenarioPDF";

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

        String[] wordsLine = new String[10000];
        ArrayList<String> contentsInLineArrayList = new ArrayList<String>();

        HttpSession session = request.getSession(false);
        String pathToRoot = System.getenv("OPENSHIFT_DATA_DIR");
        String uploadFolder = "";
        if (pathToRoot == null) {
            uploadFolder = getServletContext().getRealPath("") + File.separator + "upload";
        } else {
            uploadFolder = pathToRoot + File.separator + DATA_DIRECTORY;
        }

        String retrievePDF = (String) session.getAttribute("pdf_path");
        File pdfFile = (File) session.getAttribute("pdf_file");
        String name = pdfFile.getName();
        name = name.replaceAll(".pdf", "");
        String outputName = name + "output.pdf";
        
        //for cleaning the pdf file
        String SRC = retrievePDF;
        String DEST = uploadFolder + File.separator + outputName;
        
        File outputFile = new File(DEST);
        try {
            //clean up the pdf and block out information first
            manipulatePdf(SRC, DEST, request, response);
        } catch (DocumentException ex) {
            Logger.getLogger(ProcessExtractPDF.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            PdfReader reader = new PdfReader(DEST);
            int totalPages = reader.getNumberOfPages();
            String contentsOfCase = "";

            for (int i = 1; i <= totalPages; i++) {
                String page = PdfTextExtractor.getTextFromPage(reader, i);
                contentsOfCase += page;

                wordsLine = page.split("\n"); //separate lines of words

                for (String words : wordsLine) {
                    contentsInLineArrayList.add(words); // combine all line of words in one arraylist
                }
            }

            /////////////////////////////////
            //// SUBSTRING METHOD        ////
            ///////////////////////////////// 
            
            //NRIC generation
            Random rand = new Random();
            int randomNum = rand.nextInt((99999 - 10000) + 10000);
            String patientNRIC = "S38" + randomNum + "Q";

            Patient retrievedPatient = PatientDAO.retrieve(patientNRIC);
            while (retrievedPatient != null) {
                randomNum = rand.nextInt((99999 - 10000) + 10000);
                patientNRIC = "S38" + randomNum + "Q";
                retrievedPatient = PatientDAO.retrieve(patientNRIC);
            }
             
            /////////////////////////////////
            //// ONLY PAGE 1             ////
            /////////////////////////////////  
            String pageOne = PdfTextExtractor.getTextFromPage(reader, 1);

            //1. Initialise the keywords
            String keywordScenarioName = "";
            String keywordScenarioDesc = "";
            String keywordAdmissionInformation = "";
            String keywordDoctorOrder = "";

            //2. Get keywords from database
            List<Keyword> keywordsForScenarioName = (List<Keyword>) KeywordDAO.retrieveKeywordsByFields("scenarioName");
            List<Keyword> keywordsForScenarioDescription = (List<Keyword>) KeywordDAO.retrieveKeywordsByFields("scenarioDescription");
            List<Keyword> keywordsForAdmissionInformation = (List<Keyword>) KeywordDAO.retrieveKeywordsByFields("admissionNote");
            List<Keyword> keywordsForDoctorOrder = (List<Keyword>) KeywordDAO.retrieveKeywordsByFields("doctorOrder");

            //3. Finding the appropriate keywords. 
            // Loop all the keywords to find the respective keywords that exists in the chunk of test
            //Scenario Name Keywords
            for (Keyword keywordsScenarioName : keywordsForScenarioName) {
                keywordScenarioName = keywordsScenarioName.getKeywordDesc();
                if (pageOne.contains(keywordScenarioName)) {
                    //if found, this is the START of the substring for scenarioName
                    break;
                }
            }

            //Scenario Description Keywords
            for (Keyword keywordsScenarioDesc : keywordsForScenarioDescription) {
                keywordScenarioDesc = keywordsScenarioDesc.getKeywordDesc();

                if (pageOne.contains(keywordScenarioDesc)) {
                    //if found, this is the END to substring for scenarioName
                    break;
                }
            }

            // Admission Notes Keywords
            for (Keyword keywordsAdmissionInformation : keywordsForAdmissionInformation) {
                keywordAdmissionInformation = keywordsAdmissionInformation.getKeywordDesc();

                if (pageOne.contains(keywordAdmissionInformation)) {
                    //if found, this is the END to substring for scenarioName
                    break;
                }
            }

            // Healthcare Provider Orders Keywords
            for (Keyword keywordsDoctorOrder : keywordsForDoctorOrder) {
                keywordDoctorOrder = keywordsDoctorOrder.getKeywordDesc();

                if (pageOne.contains(keywordDoctorOrder)) {
                    //if found, this is the END to substring for scenarioName
                    break;
                }
            }

            //4. Extracting Information based on keywords found earlier in step 3
            //Scenario Name
            int startPositionScenarioName = pageOne.indexOf(keywordScenarioName) + keywordScenarioName.length();
            int endPositionScenarioName = pageOne.indexOf(keywordScenarioDesc, startPositionScenarioName);
            String scenarioNameExtracted = pageOne.substring(startPositionScenarioName, endPositionScenarioName);

            //Scenario Description
            int startPositionScenarioDesc = pageOne.indexOf("Synopsis:") + ("Synopsis:").length();
            int endPositionScenarioDesc = pageOne.indexOf(keywordAdmissionInformation, startPositionScenarioDesc);
            String scenarioDescExtracted = pageOne.substring(startPositionScenarioDesc, endPositionScenarioDesc);

            //AdmissionNotes
            int startPositionAdmissionNotes = pageOne.indexOf(keywordAdmissionInformation) + keywordAdmissionInformation.length();
            int endPositionAdmissionNotes = pageOne.indexOf(keywordDoctorOrder, startPositionAdmissionNotes);
            String scenarioAdmissionNotesExtracted = pageOne.substring(startPositionAdmissionNotes, endPositionAdmissionNotes);

            //State 0 Healthcare Provider Order
            int startPositionInitialStateOrders = pageOne.indexOf(keywordDoctorOrder) + keywordDoctorOrder.length();
            int endPositionInitialStateOrders = pageOne.indexOf("®", startPositionInitialStateOrders);
            String initialStateOrdersExtracted = pageOne.substring(startPositionInitialStateOrders, endPositionInitialStateOrders);

            //Insertion into database:
            //Scenario Table
            Integer scNumber = ScenarioDAO.retrieveMaxBedNumber() + 1;
            String scenarioID = "SC" + scNumber;
            ScenarioDAO.add(scenarioID, scenarioNameExtracted.trim(), scenarioDescExtracted.trim(), scenarioAdmissionNotesExtracted.trim(), scNumber);

            //State Table
            StateDAO.add("ST0", scenarioID, "default state", patientNRIC);
            
            //Prescription Table
            PrescriptionDAO.add(scenarioID, "ST0", "Dr.Tan/01234Z", initialStateOrdersExtracted, "NA", "NA", "-", "-", "N.A");

            AllergyPatientDAO.add(patientNRIC, "");
            PatientDAO.add(patientNRIC, "", "", "", "");
                    
            /////////////////////////////////
            //// END OF PAGE 1             ////
            ///////////////////////////////// 
            
            /////////////////////////////////
            //// PAGE 2        STATE INFO////
            ///////////////////////////////// 
            //1. Get keywords for state information
            List<Keyword> keywordsForState = (List<Keyword>) KeywordDAO.retrieveKeywordsByFields("stateID");

            //2. Loop through ALL the keywords in state information

            //State 1 Healthcare Provider Order
            out.println("<h1>State Information</h1>");

            // i = 0 refers to from page 1 til state information
            String pageTwo = PdfTextExtractor.getTextFromPage(reader, 2);
            String[] linesOfPageTwo = pageTwo.split("\n");

            int lineNumberOfPageTwo = 0;
            boolean first = false;
            for (int i = 0; i < contentsInLineArrayList.size(); i++) {
                String contentInLine = contentsInLineArrayList.get(i);
                String stateID = "";
                String stateInformation = "";
                String healthcareProviderOrder = "";
                

                if (contentInLine.contains(linesOfPageTwo[0]) && first == false) {
                    lineNumberOfPageTwo = i; // To only start finding HPO after page two
                    first = true; // prevent other page from having the same line
                }

                
                for (int j = 0; j < keywordsForState.size(); j++) {

                    String keywordState = keywordsForState.get(j).getKeywordDesc();
                    // State ID

                    if (contentInLine.contains(keywordState)) {
                        int lineNumberOfKeywordOfState = i;
                        stateID = keywordState.replaceAll(":", "");
                        stateID = "ST" + stateID.replaceAll("\\D+", "");
                        if(!stateID.equals("")) {
                            session.setAttribute("pdfState", stateID);
                        }

                        //get line 1 of state information 
                        contentsInLineArrayList.get(lineNumberOfKeywordOfState + 1);
                        String contentsInLine1 = contentsInLineArrayList.get(lineNumberOfKeywordOfState + 1);

                        int startPositionForStateLine1 = 0;
                        int endPositionForStateLine1 = contentsInLine1.indexOf("HR", startPositionForStateLine1);
                        String stateLine1Extracted = contentsInLine1.substring(startPositionForStateLine1, endPositionForStateLine1);

                        String contentsInLine2 = contentsInLineArrayList.get(lineNumberOfKeywordOfState + 2);

                        int startPositionForStateLine2 = 0;
                        int endPositionForStateLine2 = contentsInLine2.indexOf("BP", startPositionForStateLine2);
                        String stateLine2Extracted = "";
                        if (endPositionForStateLine2 > 0) {
                            stateLine2Extracted = contentsInLine2.substring(startPositionForStateLine2, endPositionForStateLine2);

                        }

                        // State Description
                        stateInformation = stateLine1Extracted + stateLine2Extracted;

                        out.println("<h2>" + stateInformation + "</h2>");

                    }

                    //Healthcare Provider's Order AKA doctor's order
                    if ((first == true && contentInLine.contains("Healthcare Provider’s Orders:")) || (first == true && contentInLine.contains("Surgeon’s Orders:"))) { // continue to extract next few lines and stop before ® 
                        int lineToStopLoop = contentsInLineArrayList.size();

                        for (int lineOfHPO = i + 1; lineOfHPO < lineToStopLoop; lineOfHPO++) {
                            if (!contentsInLineArrayList.get(lineOfHPO).contains("®") && j == 0) {
                                String line = contentsInLineArrayList.get(lineOfHPO);
                                line = line.replaceAll("•", "");
                                if (line.length() > 0) {
                                    
                                }
                                healthcareProviderOrder += line;

                            } else {
                                lineToStopLoop = lineOfHPO; // to stop extracting
                            }
                        }

                    }

                }
                
                //Adding in state information
               if (!stateID.isEmpty() && !stateInformation.isEmpty()) {
                    StateDAO.add(stateID, scenarioID, stateInformation, patientNRIC);
               }

                stateID = (String) session.getAttribute("pdfState");

                if (!healthcareProviderOrder.isEmpty()) {
                    PrescriptionDAO.add(scenarioID, stateID, "Dr.Tan/01234Z", healthcareProviderOrder, "NA", "NA", "-", "-", "N.A");
                }
            }
            session.setAttribute("scenarioID", scenarioID);
            
            pdfFile.delete();
            outputFile.delete();
            
            response.sendRedirect("./editScenario.jsp");

        } catch(FileNotFoundException e){
            session.setAttribute("error", "Please wait a few seconds and upload again.");
            response.sendRedirect("./createPDFUpload.jsp");
        }catch (IOException e) {
            out.println(e);
        }catch(StringIndexOutOfBoundsException ex){
            session.setAttribute("error", "Please upload a file with the correct format.");
            response.sendRedirect("./createPDFUpload.jsp");
        
        }
    }

    //creating a gray block to block out information
    public void manipulatePdf(String src, String dest, HttpServletRequest request, HttpServletResponse response) throws IOException, DocumentException {
        

        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        List<PdfCleanUpLocation> cleanUpLocations = new ArrayList<PdfCleanUpLocation>();
        //block in first page
        //cleanUpLocations.add(new PdfCleanUpLocation(1, new Rectangle(97f, 750f, 430f, 450f), BaseColor.BLACK));
        cleanUpLocations.add(new PdfCleanUpLocation(1, new Rectangle(97f, 470f, 430f, 3000f), BaseColor.BLACK));
        PdfCleanUpProcessor cleaner = new PdfCleanUpProcessor(cleanUpLocations, stamper);
        cleaner.cleanUp();

        int totalPages = reader.getNumberOfPages();

        //loop from 3rd page onwards, 2 confirm not used
        for (int i = 2; i <= totalPages; i++) {
            //cleanUpLocations.add(new PdfCleanUpLocation(i, new Rectangle(95f, 330f, 550f, 3000f), BaseColor.BLACK));
            cleanUpLocations.add(new PdfCleanUpLocation(i, new Rectangle(95f, 330f, 550f, 3000f), BaseColor.BLACK));
            cleaner = new PdfCleanUpProcessor(cleanUpLocations, stamper);
            cleaner.cleanUp();
        }

        stamper.close();
        reader.close();


 
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
