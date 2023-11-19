package url;

import ETU1863.framework.*;
import annotation.*;

import java.io.*;
import java.util.*;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.lang.reflect.*;

public class Utilitaire {
    String queryString;
    String urlGet;
    String generalPath;

/*---------------------------------------Fonctions prérequis------------------------------------ */
/// Getters and Setters
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

/// Constructeurs
    public Utilitaire() {}

    public Utilitaire(String generalPath)
    throws Exception {
        this.setGeneralPath(generalPath);
    }

    public Utilitaire(String urlGet, StringBuffer requestURL, String generalPath)
    throws Exception {
        String url=requestURL.toString();
        this.setUrlGet(urlGet);
        url=url.split("//")[1];
        String result="";
        for(int i=2; i<url.split("/").length-1; i++) {
            result+=url.split("/")[i]+"/";
        }
        result+=url.split("/")[url.split("/").length-1];
        this.setQueryString(result);
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
}
