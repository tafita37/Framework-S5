package ETU1863.framework.servlet;
import java.io.*;
import java.util.HashMap;

import javax.servlet.*;
import javax.servlet.http.*;

import ETU1863.framework.Mapping;
import url.Utilitaire;

public class FrontServlet
extends HttpServlet {
    HashMap<String, Mapping> mappingUrls;

    public HashMap<String, Mapping> getMappingUrls() {
        return mappingUrls;
    }

    public void setMappingUrls(HashMap<String, Mapping> mappingUrls) {
        this.mappingUrls = mappingUrls;
    }

    public void init() {
        try {
            ServletContext context = getServletContext();
            String fullPath = context.getRealPath("/WEB-INF/classes");
            Utilitaire util=new Utilitaire(fullPath);
            this.mappingUrls=util.getClassWithUrlAnnotation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
        PrintWriter pr=response.getWriter();
        try {
            String queryString = request.getQueryString();
            StringBuffer requestURL = request.getRequestURL();
            ServletContext context = getServletContext();
            String fullPath = context.getRealPath("/WEB-INF/classes");
            Utilitaire util=new Utilitaire(queryString, requestURL, fullPath);
            System.out.println(util.getView());
            RequestDispatcher dispat=request.getRequestDispatcher(util.getView());
            dispat.forward(request, response);
        } catch (Exception e) {
            pr.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
        processRequest(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
        processRequest(request, response);
    }
}