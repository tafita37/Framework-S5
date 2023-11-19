package ETU1863.framework.servlet;

import java.util.*;
import ETU1863.framework.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import url.Utilitaire;

public class FrontServlet
extends HttpServlet {
    HashMap<String, Mapping> mappingUrls;

    public void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
        PrintWriter pr=response.getWriter();
        try {
            String queryString = request.getQueryString();
            StringBuffer requestURL = request.getRequestURL();
            Utilitaire util=new Utilitaire(queryString, requestURL);
            pr.println("URL : "+util.getQueryString());
        } catch (Exception e) {
            pr.println(e.getMessage());
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