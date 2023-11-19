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
        Object[] list_key=mappingUrls.keySet().toArray();
        for(int i=0; i<this.mappingUrls.size(); i++) {
            pr.println("Classe numero : "+(i+1)+" avec annotation Url");
            pr.println("Lien : "+list_key[i]);
            pr.println("Nom de classe : "+this.mappingUrls.get(list_key[i]).getClassName());
            pr.println("Nom de la method : "+this.mappingUrls.get(list_key[i]).getMethod());
            pr.println("-----------------------------------------------------");
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