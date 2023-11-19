package url;

import ETU1863.framework.*;
import annotation.*;
import upload.FileUpload;
import java.io.*;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.text.SimpleDateFormat;

public class Utilitaire {
    String completeUrl;
    String queryString;
    String urlGet;
    String generalPath;
    static String role;
    static String profil;

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
            this.setQueryString("./");
        } else {
            this.setQueryString(result);
        }
    }

    public static String getRole() {
        return role;
    }

    public static void setRole(String role) {
        Utilitaire.role = role;
    }

    public static String getProfil() {
        return profil;
    }

    public static void setProfil(String profil) {
        Utilitaire.profil = profil;
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

/// Récupérer toutes les classes avec annotations scope
    public HashMap<Class<?>, Object> getClassWithScopeAnnotation()
    throws Exception {
        HashMap<Class<?>, Object> result=new HashMap<Class<?>, Object>();
        List<Class<?>> list_classes=this.getAllClasses("");
        for(int i=0; i<list_classes.size(); i++) {
            if(list_classes.get(i).isAnnotationPresent(Scope.class)) {
                Scope scope = list_classes.get(i).getAnnotation(Scope.class);
                if(scope.singleton().compareToIgnoreCase("Singleton")==0) {
                    result.put(list_classes.get(i), null);
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

/// Setter les attributs d'un HttpServletRequest
    public static void setAttribute(HttpServletRequest request, ModelView md)
    throws Exception {
        Object[] keys=md.getData().keySet().toArray();
        if(!md.isJson()) {
            for(int i=0; i<keys.length; i++) {
                request.setAttribute((String) keys[i], md.getData().get(keys[i]));
            }
        } 
    }

/// Convertir la première lettre d'une chaine de caractère en majuscule
    public static String firstLetterMaj(String charact) {
        return charact.toUpperCase().substring(0, 1)+charact.substring(1, charact.length());
    }

/// Récupérer les méthodes setters d'un objet
    public static String[] getSettersMethods(Object ob) {
        String[] result=new String[ob.getClass().getDeclaredFields().length];
        String maj="";
        for(int i=0; i<result.length; i++) {
            maj=Utilitaire.firstLetterMaj(ob.getClass().getDeclaredFields()[i].getName());
            result[i]="set"+maj;
        }
        return result;
    }

/// Récupérer les méthodes getters d'un objet
    public static String[] getGettersMethods(Object ob) {
        String[] result=new String[ob.getClass().getDeclaredFields().length];
        String maj="";
        for(int i=0; i<result.length; i++) {
            maj=Utilitaire.firstLetterMaj(ob.getClass().getDeclaredFields()[i].getName());
            result[i]="get"+maj;
        }
        return result;
    }

/// Caster n'importe quel type de String dans un parametre de fonction
    public static Object castParameter(Parameter parameter, String param)
    throws Exception {
        if(param==null) {
            throw new Exception("Veuillez entrez un argument");
        } else if(parameter.getType()==int.class||parameter.getType()==Integer.class) {
            return Integer.valueOf(param);
        } else if(parameter.getType()==double.class||parameter.getType()==Double.class) {
            return Double.valueOf(param);
        } else if(parameter.getType()==float.class||parameter.getType()==Float.class) {
            return Float.valueOf(param);
        } else if(parameter.getType()==long.class||parameter.getType()==Long.class) {
            return Long.valueOf(param);
        } else if(parameter.getType()==boolean.class||parameter.getType()==Boolean.class) {
            return Boolean.valueOf(param);
        } else if(parameter.getType()==Date.class) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(param);
        } else if(parameter.getType()==String.class) {
            return String.valueOf(param);
        } else {
            throw new Exception("Le type d'argument est inconnu");
        }
    } 

/// Caster n'importe quel type de String dans un attribut de class
    public static void castField(Field field, String param, String setter, Object ob)
    throws Exception {
        if(param==null) {
            throw new Exception("Veuillez entrez un argument");
        } else if(field.getType()==int.class||field.getType()==Integer.class) {
            ob.getClass().getDeclaredMethod(setter, int.class).invoke(ob, Integer.valueOf(param));
        } else if(field.getType()==double.class||field.getType()==Double.class) {
            ob.getClass().getDeclaredMethod(setter, double.class).invoke(ob, Double.valueOf(param));
        } else if(field.getType()==float.class||field.getType()==Float.class) {
            ob.getClass().getDeclaredMethod(setter, float.class).invoke(ob, Float.valueOf(param));
        } else if(field.getType()==long.class||field.getType()==Long.class) {
            ob.getClass().getDeclaredMethod(setter, long.class).invoke(ob, Long.valueOf(param));
        } else if(field.getType()==boolean.class||field.getType()==Boolean.class) {
            ob.getClass().getDeclaredMethod(setter, boolean.class).invoke(ob, Boolean.valueOf(param));
        } else if(field.getType()==Date.class) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            ob.getClass().getDeclaredMethod(setter, Date.class).invoke(ob, sdf.parse(param));
        } else if(field.getType()==String.class) {
            ob.getClass().getDeclaredMethod(setter, String.class).invoke(ob, String.valueOf(param));
        } else {
            throw new Exception("Le type d'argument est inconnu");
        }
    } 

/// Traitement d'un HashMap sesion
    public static void treatSession(HttpServletRequest request, Object ob, Field field, String setter)
    throws Exception {
        if(field.getType()!=HashMap.class) {
            throw new Exception("L'attribut "+field.getName()+" doit retourner un HashMap");
        }
        HashMap<String, Object> vSession=new HashMap<String, Object>();
        Enumeration<String> attributes=request.getSession().getAttributeNames();
        while(attributes.hasMoreElements()) {
            String attribut = attributes.nextElement();
            vSession.put(attribut, request.getSession().getAttribute(attribut));
        }
        ob.getClass().getDeclaredMethod(setter, HashMap.class).invoke(ob, vSession);
    }

/// Setter chaque attribut de la class Model
    public static void parseString(Object ob, HttpServletRequest request)
    throws Exception {
        String contentType = request.getContentType();
        String[] setters=Utilitaire.getSettersMethods(ob);
        for(int j=0; j<setters.length; j++) {
            if(contentType!=null&&contentType.startsWith("multipart/form-data")&&request.getPart(ob.getClass().getDeclaredFields()[j].getName())!=null) {
                if(ob.getClass().getDeclaredFields()[j].getType()==FileUpload.class) {
                    Part part = request.getPart(ob.getClass().getDeclaredFields()[j].getName());
                    String name = part.getSubmittedFileName();
                    byte[] buffer = new byte[8192];
                    InputStream inputStream = part.getInputStream();
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    byte[] byteArray = outputStream.toByteArray();
                    inputStream.close();
                    outputStream.close();
                    if(ob.getClass().getDeclaredMethod(Utilitaire.getGettersMethods(ob)[j]).invoke(ob)==null) {
                        ob.getClass().getDeclaredMethod(setters[j], FileUpload.class).invoke(ob, new FileUpload(name, byteArray));
                    } else {
                        ob.getClass().getDeclaredMethod(Utilitaire.getGettersMethods(ob)[j]).invoke(ob).getClass().getDeclaredMethod("setName", String.class).invoke(ob.getClass().getDeclaredMethod(Utilitaire.getGettersMethods(ob)[j]).invoke(ob), name);
                        ob.getClass().getDeclaredMethod(Utilitaire.getGettersMethods(ob)[j]).invoke(ob).getClass().getDeclaredMethod("setBytes", byte[].class).invoke(ob.getClass().getDeclaredMethod(Utilitaire.getGettersMethods(ob)[j]).invoke(ob), buffer);
                    }
                    FileUpload fp = (FileUpload) ob.getClass().getDeclaredMethod(Utilitaire.getGettersMethods(ob)[j]).invoke(ob);
                    fp.writeFileUpload(request, part);
                } else {
                    Utilitaire.castField(ob.getClass().getDeclaredFields()[j], request.getParameter(ob.getClass().getDeclaredFields()[j].getName()), setters[j], ob);
                }
            } else if(request.getParameter(ob.getClass().getDeclaredFields()[j].getName())!=null) {
                Utilitaire.castField(ob.getClass().getDeclaredFields()[j], request.getParameter(ob.getClass().getDeclaredFields()[j].getName()), setters[j], ob);
            }  
            if(ob.getClass().getDeclaredFields()[j].isAnnotationPresent(SessionField.class)) {
                Utilitaire.treatSession(request, ob, ob.getClass().getDeclaredFields()[j], setters[j]);
            }
        }
    }

/// Récupérer un tableau d'objet contenant les variables de requete directement casté
    public static Object[] getArgs(HttpServletRequest request, Method method, Parameters param)
    throws Exception {
        Object[] args=new Object[method.getParameterCount()];
        Parameter[] parameters=method.getParameters();
        if(param!=null) {
            String[] argsName=param.args();
            if(argsName.length!=method.getParameterCount()) {
                throw new Exception("Nombre d'argument et de parametre incomptabile");
            }
            for(int i=0; i<args.length; i++) {
                args[i]=Utilitaire.castParameter(parameters[i], request.getParameter(argsName[i]));
            }
        }
        return args;
    }

/// Vérifier qu'un utilisateur a droit d'accéder à une méthod
    public static boolean hasAuthorisation(Method method, HttpServletRequest request) {
        if(method.isAnnotationPresent(Auth.class)) {
            if(request.getSession().getAttribute(Utilitaire.getRole())==null) {
                System.out.println(Utilitaire.getRole());
                return false;
            }
            if(method.getAnnotation(Auth.class).role().compareTo((String) request.getSession().getAttribute(Utilitaire.getRole()))==0) {
               return true; 
            }
            return false;
        }
        return true;
    }

/// Invoquer une méthode ModelView qui a l'url
    public static ModelReturn invokeMethod(HttpServletRequest request, Object ob, String url)
    throws Exception {
        Method[] methods=ob.getClass().getDeclaredMethods();
        Annotation annot=null;
        Parameters param=null;
        for(int i=0; i<methods.length; i++) {
            if(methods[i].isAnnotationPresent(Url.class)) {
                annot=methods[i].getAnnotation(Url.class);
                param=methods[i].getAnnotation(Parameters.class);
                String link=(String) annot.getClass().getDeclaredMethod("link").invoke(annot);
                if(link.compareTo(url)==0) {
                    if(!Utilitaire.hasAuthorisation(methods[i], request)) {
                        throw new Exception(methods[i].getAnnotation(Auth.class).exception());
                    }
                    Object[] args=new Object[methods[i].getParameterCount()];
                    args=Utilitaire.getArgs(request, methods[i], param);
                    Object result= methods[i].invoke(ob, args);
                    if(methods[i].isAnnotationPresent(Json.class)) {
                        return new ModelReturn(result, true);
                    }
                    return new ModelReturn(result);
                }
            }
        }
        throw new Exception("Vous n'avez annote aucune methode de la class "+ob.getClass().getSimpleName());
    }

/// Setter des attributs à leurs valeurs par défauts
    public static void setDefaultValue(Object ob)
    throws Exception {
        Field[] fields=ob.getClass().getDeclaredFields();
        for(int i=0; i<fields.length; i++) {
            fields[i].setAccessible(true);
            if(fields[i].getType().isPrimitive()) {
                if(fields[i].getType()==boolean.class) {
                    fields[i].set(ob, false);
                }
                if(fields[i].getType()==byte.class||fields[i].getType()==short.class||fields[i].getType()==int.class||fields[i].getType()==long.class||fields[i].getType()==float.class||fields[i].getType()==double.class) {
                    fields[i].set(ob, 0);
                }
                if(fields[i].getType()==char.class) {
                    fields[i].set(ob, ' ');
                }
            } else {
                fields[i].set(ob, null);
            }
        }
    }

/// Ajouter des sessions
    public static void addSession(HttpServletRequest request, ModelView md) {
        for(int i=0; i<md.getSession().size(); i++) {
            if(request.getSession().getAttribute((String) md.getSession().keySet().toArray()[i])==null) {
                request.getSession().setAttribute((String) md.getSession().keySet().toArray()[i], md.getSession().get((String) md.getSession().keySet().toArray()[i]));
            }
        }
    }
}
