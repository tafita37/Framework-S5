package ETU1863.framework.servlet;

import java.io.*;
import java.util.HashMap;
import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;
import ETU1863.framework.*;
import url.*;

@MultipartConfig
public class FrontServlet
extends HttpServlet {
    HashMap<String, Mapping> mappingUrls;
    HashMap<Class<?>, Object> singleton;
    String[] keys;
    Class<?>[] keysSingletons;
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

    public void setMappingUrls(HashMap<String, Mapping> mappingUrls) {
        this.mappingUrls = mappingUrls;
    }

    public HashMap<Class<?>, Object> getSingleton() {
        return singleton;
    }

    public void setSingleton(HashMap<Class<?>, Object> nouveau)
    throws Exception {
        if(nouveau==null) {
            throw new Exception("Veuillez entrer des singletons");
        }
        this.singleton = nouveau;
    }

    public void setKeys(String[] keys) {
        this.keys = keys;
    }

    public String getDossier() {
        return dossier;
    }

    public void setDossier(String dossier) {
        this.dossier = dossier;
    }

    public Class<?>[] getKeysSingletons() {
        return keysSingletons;
    }

    public void setKeysSingletons(Class<?>[] keysSingletons) {
        this.keysSingletons = keysSingletons;
    }

    public void setKeysSingletons(Object[] nouveau)
    throws Exception {
        if(nouveau==null) {
            throw new Exception("Il n'y a aucune clés");
        }
        if(nouveau.length>0) {
            this.keysSingletons=new Class<?>[nouveau.length];
        }
        for(int i=0; i<nouveau.length; i++) {
            this.getKeysSingletons()[i]=(Class<?>) nouveau[i];
        }
    }

    public void init() {
        try {
            ServletConfig config=this.getServletConfig();
            this.dossier=config.getInitParameter("paquet");
            ServletContext context = getServletContext();
            String fullPath = context.getRealPath("/WEB-INF/"+this.dossier);
            Utilitaire util=new Utilitaire(fullPath);
            this.setMappingUrls(util.getClassWithUrlAnnotation());
            this.setKeys(this.getMappingUrls().keySet().toArray());
            this.setSingleton(util.getClassWithScopeAnnotation());
            this.setKeysSingletons(this.getSingleton().keySet().toArray());
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
        if(this.getMappingUrls().containsKey(util.getQueryString())) {
            Class<?> classe=util.getClassByName(mappingUrls.get(util.getQueryString()).getClassName());
            Object ob=null;
            if(this.getSingleton().containsKey(classe)) {
                System.out.println("eto");
                if(this.getSingleton().get(classe)==null) {
                    ob=classe.newInstance();
                    this.getSingleton().replace(classe, null, ob);
                } else {
                    ob=this.getSingleton().get(classe);
                    Utilitaire.setDefaultValue(ob);
                    System.out.println(classe.getSimpleName()+" est deja instancier");
                }
            } else {
                System.out.println("tsia");
                ob=classe.newInstance();
            }
            Utilitaire.parseString(ob, request);
            return Utilitaire.invokeMethod(request, ob, util.getQueryString());
        }
        // for(int i=0; i<this.getKeys().length; i++) {
        //     if(keys[i].toString().compareTo(util.getQueryString())==0) {
        //         Class<?> classe=util.getClassByName(mappingUrls.get(keys[i]).getClassName());
        //         Object ob=classe.newInstance();
        //         Utilitaire.parseString(ob, request);
        //         return Utilitaire.invokeMethod(request, ob, keys[i].toString());
        //     }
        // }
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