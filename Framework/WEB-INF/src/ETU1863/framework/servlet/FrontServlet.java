package ETU1863.framework.servlet;

import java.io.*;
import java.util.HashMap;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import ETU1863.framework.*;
import url.*;

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

    public String site_url(String annot)
    throws Exception {
        ServletContext context=this.getServletContext();
        String fullPath=context.getRealPath("/WEB-INF");
        try {
            File fichierXML = new File(fullPath+"/config.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(fichierXML);
            document.getDocumentElement().normalize();
            NodeList nodeList = document.getElementsByTagName("site");
            for (int temp = 0; temp < nodeList.getLength(); temp++) {
                Node node = nodeList.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    return element.getElementsByTagName("url").item(0).getTextContent()+annot;
                }
            }
            throw new Exception("N'oubliez pas d'ecrire dans le fichier config.xml l'url general de votre projet");
        } catch (Exception e) {
            throw e;
        }
    }

    public void setAttribute(HttpServletRequest request, Utilitaire util)
    throws Exception {
        Object[] keys=util.getView(this.mappingUrls).getData().keySet().toArray();
        for(int i=0; i<keys.length; i++) {
            request.setAttribute((String) keys[i], util.getView(this.mappingUrls).getData().get(keys[i]));
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
            this.setAttribute(request, util);
            RequestDispatcher dispat=request.getRequestDispatcher(util.getView(this.mappingUrls).getView());
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