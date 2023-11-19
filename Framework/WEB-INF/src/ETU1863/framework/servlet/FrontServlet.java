package ETU1863.framework.servlet;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import ETU1863.framework.*;
import annotation.Parameters;
import annotation.Url;
import url.*;

@MultipartConfig
public class FrontServlet
extends HttpServlet {
    HashMap<String, Mapping> mappingUrls;
    String[] keys;
    String dossier;
    
    public HashMap<String, Mapping> getMappingUrls() {
        return mappingUrls;
    }

    public String[] getKeys() {
        return this.keys;
    }

    public void setKeys(Object[] nouveau)
    throws Exception {
        if(nouveau==null) {
            throw new Exception("Il n'y a aucune clés");
        }
        if(nouveau.length>0) {
            this.keys=new String[nouveau.length];
        }
        for(int i=0; i<nouveau.length; i++) {
            this.keys[i]=(String) nouveau[i];
        }
    }

    public void init() {
        try {
            ServletConfig config=this.getServletConfig();
            this.dossier=config.getInitParameter("paquet");
            ServletContext context = getServletContext();
            String fullPath = context.getRealPath("/WEB-INF/"+this.dossier);
            Utilitaire util=new Utilitaire(fullPath);
            this.mappingUrls=util.getClassWithUrlAnnotation();
            this.setKeys(this.getMappingUrls().keySet().toArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ModelView getCorrespondingModelView(HttpServletRequest request)
    throws Exception {
        String queryString = request.getQueryString();
        StringBuffer requestURL = request.getRequestURL();
        ServletContext context = getServletContext();
        String fullPath = context.getRealPath("/WEB-INF/"+this.dossier);
        Utilitaire util=new Utilitaire(queryString, requestURL, fullPath);
        for(int i=0; i<this.getKeys().length; i++) {
            if(keys[i].toString().compareTo(util.getQueryString())==0) {
                Class<?> classe=util.getClassByName(mappingUrls.get(keys[i]).getClassName());
                Object ob=classe.newInstance();
                Utilitaire.parseString(ob, request);
                return Utilitaire.invokeMethod(request, ob, keys[i].toString());
            }
        }
        throw new Exception("Error 404 : Page not found");
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
        PrintWriter pr=response.getWriter();
        try {
            ModelView md=this.getCorrespondingModelView(request);
            Utilitaire.setAttribute(request, md);
            RequestDispatcher dispat=request.getRequestDispatcher(md.getView());
            dispat.forward(request, response);
        } catch (Exception e) {
            pr.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
        PrintWriter pr=response.getWriter();
        try {
            processRequest(request, response);
        } catch (Exception e) {
            pr.println(e.getMessage());
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
        PrintWriter pr=response.getWriter();
        try {
            processRequest(request, response);
        } catch (Exception e) {
            pr.println(e.getMessage());
        }
    }
}