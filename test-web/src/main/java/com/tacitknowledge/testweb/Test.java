/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tacitknowledge.testweb;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tacitknowledge.flip.FeatureService;
import com.tacitknowledge.flip.FeatureServiceReflectionFactory;
import com.tacitknowledge.flip.FlipContext;
import com.tacitknowledge.flip.aspectj.Flippable;

/**
 *
 * @author ssoloviov
 */
@WebServlet(name = "Test", loadOnStartup=1)
public class Test extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        FeatureServiceReflectionFactory factory = new FeatureServiceReflectionFactory();
        FeatureService featureService = factory.createFeatureService("com.tacitknowledge.flip");
        FlipContext.setFeatureService(featureService);
        Logger.getAnonymousLogger().warning("HHHHHHHHHHHHHEEEEEEEEEEEELLLLOOOOOOOOOOOO");
        
//        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
//        
//        FlipContext.setFeatureService(context.getBean("featureService", FeatureService.class));
    }

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
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
        AClass aClass = new AClass();
        try {
            /*
             * TODO output your page here. You may use following sample code.
             */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Test</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Test at " + request.getContextPath() + " = " + getSamplleText() + "(" + aClass.getTestCode() + ") from "+ aClass.getSourceCode("Human's") +"</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {            
            out.close();
        }
    }
    
    @Flippable(feature="test", disabledValue="BBB")
    public String getSamplleText() {
        return "AAA";
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
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
     * Handles the HTTP
     * <code>POST</code> method.
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
