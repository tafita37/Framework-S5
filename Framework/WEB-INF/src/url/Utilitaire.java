package url;

import ETU1863.framework.*;
import ETU1863.framework.servlet.FrontServlet;
import annotation.*;

import java.io.*;
import java.util.*;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.lang.reflect.*;

public class Utilitaire {
    String completeUrl;
    String queryString;
    String urlGet;
    String generalPath;

/*---------------------------------------Fonctions prérequis------------------------------------ */
/// Getters and Setters
    public String getCompleteUrl() {
        return this.completeUrl;
    }

    public void setCompleteUrl(String nouveau) {
        this.completeUrl=nouveau;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String nouveau) {
        if(nouveau==null) {
            this.queryString="";
        } else {
            this.queryString = nouveau;
        }
    }

    public String getUrlGet() {
        return urlGet;
    }

    public void setUrlGet(String nouveau) {
        if(nouveau==null) {
            this.urlGet="";
        } else {
            this.urlGet = nouveau;
        }
    }

    public String getGeneralPath() {
        return this.generalPath;
    }

    public void setGeneralPath(String nouveau)
    throws Exception {
        if(nouveau==null) {
            throw new Exception("General path null");
        } else if(nouveau.length()==0) {
            throw new Exception("Veuillez entrer un chemin absolu");
        }
        this.generalPath=nouveau;
    }

/// Obtenir l'url pattern
    public void setQueryString(StringBuffer requestURL) {
        String url=requestURL.toString();
        url=url.split("//")[1];
        String result="";
        for(int i=2; i<url.split("/").length-1; i++) {
            result+=url.split("/")[i]+"/";
        }
        if(url.split("/").length-1>=2) {
            result+=url.split("/")[url.split("/").length-1];
        }
        if(result.length()==0) {
            this.setQueryString("/");
        } else {
            this.setQueryString(result);
        }
    }

/// Constructeurs
    public Utilitaire() {}

    public Utilitaire(String generalPath)
    throws Exception {
        this.setGeneralPath(generalPath);
    }

    public Utilitaire(String urlGet, StringBuffer requestURL, String generalPath)
    throws Exception {
        String url=requestURL.toString();
        this.setCompleteUrl(url.substring(0, url.length()-1));
        this.setUrlGet(urlGet);
        this.setQueryString(requestURL);
        this.setGeneralPath(generalPath);
    }

/*--------------------------------Fonctions principales--------------------------------------- */
/// Récupérer tout les .class dans le package classes
    public List<Class<?>> getAllClasses(String pathName)
    throws Exception { 
        if(pathName.length()==0||pathName==null) {
            pathName=this.getGeneralPath();
        }
        List<Class<?>> result=new ArrayList<>();
        File f=new File(pathName);
        for(int i=0; i<f.listFiles().length; i++) {
            if(f.listFiles()[i].isFile()&&f.listFiles()[i].getName().endsWith(".class")) {
                String almostCompletClass=pathName.replace("\\", "-").split(this.getGeneralPath().replace("\\", "-"))[1];
                String packageName="";
                for(int j=1; j<almostCompletClass.split("-").length; j++) {
                    packageName+=almostCompletClass.split("-")[j]+".";
                }
                String className=f.listFiles()[i].getName().replace(".", "-").split("-")[0];
                result.add(Class.forName(packageName+className));
            } else if(f.listFiles()[i].isDirectory()) {
                result.addAll(getAllClasses(pathName+"\\"+f.listFiles()[i].getName()));
            }
        }
        return result;
    }

/// Récupérer toutes les classes avec annotations urls
    public HashMap<String, Mapping> getClassWithUrlAnnotation()
    throws Exception {
        HashMap<String, Mapping> result=new HashMap<String, Mapping>();
        List<Class<?>> list_classes=this.getAllClasses("");
        for(int i=0; i<list_classes.size(); i++) {
            Method[] list_method=list_classes.get(i).getDeclaredMethods();
            for(int j=0; j<list_method.length; j++) {
                if(list_method[j].isAnnotationPresent(Url.class)) {
                    Annotation url=list_method[j].getAnnotation(Url.class);
                    String link=(String) url.getClass().getMethod("link").invoke(url);
                    Mapping map=new Mapping(list_classes.get(i).getSimpleName(), list_method[j].getName());
                    result.put(link, map);
                }
            }
        }
        return result; 
    }

/// Récupérer une classe par son nom
    public Class<?> getClassByName(String name)
    throws Exception {
        List<Class<?>> classes=this.getAllClasses("");
        Class<?> result=null;
        for(int i=0; i<classes.size(); i++) {
            if(classes.get(i).getSimpleName().compareTo(name)==0) {
                result=classes.get(i);
            }
        }
        return result;
    }

/// Récupérer la vue correspondante
    public ModelView getView()
    throws Exception {
        HashMap<String, Mapping> urls=this.getClassWithUrlAnnotation();
        Object[] keys=urls.keySet().toArray();
        for(int i=0; i<keys.length; i++) {
            if(keys[i].toString().compareTo(this.getQueryString())==0) {
                Class<?> classe=this.getClassByName(urls.get(keys[i]).getClassName());
                Object ob=classe.newInstance();
                if(!(ob.getClass().getDeclaredMethod(urls.get(keys[i]).getMethod()).invoke(ob) instanceof ModelView)) {
                    throw new Exception("La method "+ob.getClass().getDeclaredMethod(urls.get(keys[i]).getMethod()).getName()+" doit retourner un objet de type ModelView");
                }
                return (ModelView) ob.getClass().getDeclaredMethod(urls.get(keys[i]).getMethod()).invoke(ob);
            }
        }
        throw new Exception("Error 404 : Page not found");
    }

/// Site url
    public String site_url(String chemin) {
        return this.completeUrl+chemin;
    }
}
