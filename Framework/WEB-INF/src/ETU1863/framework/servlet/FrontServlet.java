package ETU1863.framework.servlet;

import java.io.*;
import java.util.HashMap;
import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;

import com.google.gson.Gson;

import ETU1863.framework.*;
import url.*;

@MultipartConfig
public class FrontServlet
extends HttpServlet {
    HashMap<String, Mapping> mappingUrls;
    HashMap<Class<?>, Object> singleton;
    String dossier;
    String role;
    String profil;
    Gson gson;
    
    public HashMap<String, Mapping> getMappingUrls() {
        return mappingUrls;
    }

    public void setMappingUrls(HashMap<String, Mapping> nouveau)
    throws Exception {
        if(nouveau==null) {
            throw new Exception("Veuillez inserer des mappingUrls");
        }
        this.mappingUrls = nouveau;
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

    public String getDossier() {
        return dossier;
    }

    public void setDossier(String nouveau) {
        if(nouveau==null) {
            nouveau="";
        }
        this.dossier = nouveau;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String nouveau)
    throws Exception {
        if(nouveau==null||nouveau.length()==0) {
            throw new Exception("Veuillez definir un role dans web.xml avec init-param");
        }
        this.role = nouveau;
    }

    public String getProfil() {
        return profil;
    }

    public void setProfil(String nouveau)
    throws Exception {
        if(nouveau==null||nouveau.length()==0) {
            throw new Exception("Veuillez definir un profil dans web.xml avec init-param");
        }
        this.profil = nouveau;
    }

    public void init() {
        try {
            this.gson=new Gson();
            ServletConfig config=this.getServletConfig();
            this.dossier=config.getInitParameter("paquet");
            ServletContext context = getServletContext();
            String fullPath = context.getRealPath("/WEB-INF/"+this.dossier);
            Utilitaire util=new Utilitaire(fullPath);
            this.setMappingUrls(util.getClassWithUrlAnnotation());
            this.setSingleton(util.getClassWithScopeAnnotation());
            this.setRole(this.getInitParameter("role"));
            this.setProfil(this.getInitParameter("profil"));
            Utilitaire.setRole(this.getRole());
            Utilitaire.setProfil(this.getProfil());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ModelReturn getCorrespondingModelReturn(HttpServletRequest request)
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
                if(this.getSingleton().get(classe)==null) {
                    ob=classe.newInstance();
                    this.getSingleton().replace(classe, null, ob);
                    System.out.println("Nouvelle instance de "+classe.getSimpleName());
                } else {
                    ob=this.getSingleton().get(classe);
                    Utilitaire.setDefaultValue(ob);
                    System.out.println(classe.getSimpleName()+" est deja instancier");
                }
            } else {
                ob=classe.newInstance();
            }
            Utilitaire.parseString(ob, request);
            return Utilitaire.invokeMethod(request, ob, util.getQueryString());
        }
        throw new Exception("Error 404 : Page not found");
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
        PrintWriter pr=null;
        try {
            ModelReturn md=this.getCorrespondingModelReturn(request);
            if(md.isJson()) {
                response.setContentType("application/json");
                pr=response.getWriter();
                pr.println(this.gson.toJson(md.getObject()));
            } else {
                ModelView mdv= (ModelView) md.getObject();
                Utilitaire.addSession(request, mdv);
                if(md.isJson()) {
                    response.setContentType("application/json");
                    pr=response.getWriter();
                    pr.println(this.gson.toJson(mdv.getData()));
                } else {
                    Utilitaire.setAttribute(request, mdv);
                    RequestDispatcher dispat=null;
                    for(int i=0; i<mdv.getView().length; i++) {
                        dispat=request.getRequestDispatcher(mdv.getView()[i]);
                        dispat.include(request, response);
                    }        
                }
            }
            // if(!(ob instanceof ModelView)) {
            //     response.setContentType("application/json");
            //     pr=response.getWriter();
            //     pr.println(this.gson.toJson(ob));
            // } else {
            //     ModelView md= (ModelView) ob;
            //     Utilitaire.addSession(request, md);
            //     if(md.isJson()) {
            //         response.setContentType("application/json");
            //         pr=response.getWriter();
            //         pr.println(this.gson.toJson(md.getData()));
            //     } else {
            //         Utilitaire.setAttribute(request, md);
            //         RequestDispatcher dispat=null;
            //         for(int i=0; i<md.getView().length; i++) {
            //             dispat=request.getRequestDispatcher(md.getView()[i]);
            //             dispat.include(request, response);
            //         }        
            //     }
            // }
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