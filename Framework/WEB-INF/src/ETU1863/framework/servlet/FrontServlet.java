package ETU1863.framework.servlet;

import java.io.*;
import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
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
    String[] keys;
    
    public HashMap<String, Mapping> getMappingUrls() {
        return mappingUrls;
    }

    public void setMappingUrls(HashMap<String, Mapping> mappingUrls) {
        this.mappingUrls = mappingUrls;
    }

    public String[] getKeys() {
        return this.keys;
    }

    public void setKeys(String[] nouveau)
    throws Exception {
        if(nouveau==null) {
            throw new Exception("Les cles ne peuvent etre null");
        }
        this.keys=nouveau;
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
            ServletContext context = getServletContext();
            String fullPath = context.getRealPath("/WEB-INF/classes");
            Utilitaire util=new Utilitaire(fullPath);
            this.mappingUrls=util.getClassWithUrlAnnotation();
            this.setKeys(this.getMappingUrls().keySet().toArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 

    // public String site_url(String annot)
    // throws Exception {
    //     ServletContext context=this.getServletContext();
    //     String fullPath=context.getRealPath("/WEB-INF");
    //     try {
    //         File fichierXML = new File(fullPath+"/config.xml");
    //         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    //         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    //         Document document = dBuilder.parse(fichierXML);
    //         document.getDocumentElement().normalize();
    //         NodeList nodeList = document.getElementsByTagName("site");
    //         for (int temp = 0; temp < nodeList.getLength(); temp++) {
    //             Node node = nodeList.item(temp);
    //             if (node.getNodeType() == Node.ELEMENT_NODE) {
    //                 Element element = (Element) node;
    //                 return element.getElementsByTagName("url").item(0).getTextContent()+annot;
    //             }
    //         }
    //         throw new Exception("N'oubliez pas d'ecrire dans le fichier config.xml la base_url de votre projet");
    //     } catch (Exception e) {
    //         throw e;
    //     }
    // }

    public void setAttribute(HttpServletRequest request, ModelView md)
    throws Exception {
        Object[] keys=md.getData().keySet().toArray();
        for(int i=0; i<keys.length; i++) {
            request.setAttribute((String) keys[i], md.getData().get(keys[i]));
        }
        // request.setAttribute("Reference", this);
    }

    public String firstLetterMaj(String charact) {
        return charact.toUpperCase().substring(0, 1)+charact.substring(1, charact.length());
    }

    public String[] getSettersMethods(Object ob) {
        String[] result=new String[ob.getClass().getDeclaredFields().length];
        String maj="";
        for(int i=0; i<result.length; i++) {
            maj=this.firstLetterMaj(ob.getClass().getDeclaredFields()[i].getName());
            result[i]="set"+maj;
        }
        return result;
    } 

    public void parseString(Object ob, HttpServletRequest request)
    throws Exception {
        String[] setters=this.getSettersMethods(ob);
        for(int j=0; j<setters.length; j++) {
            if(request.getParameter(ob.getClass().getDeclaredFields()[j].getName())!=null) {
                try {
                    ob.getClass().getDeclaredMethod(setters[j], int.class).invoke(ob, Integer.parseInt(request.getParameter(ob.getClass().getDeclaredFields()[j].getName())));
                } catch (Exception e1) {
                    try {
                        ob.getClass().getDeclaredMethod(setters[j], double.class).invoke(ob, Double.parseDouble(request.getParameter(ob.getClass().getDeclaredFields()[j].getName())));
                    } catch (Exception e2) {
                        try {
                            ob.getClass().getDeclaredMethod(setters[j], float.class).invoke(ob, Float.parseFloat(request.getParameter(ob.getClass().getDeclaredFields()[j].getName())));
                        } catch (Exception e3) {
                            try {
                                ob.getClass().getDeclaredMethod(setters[j], long.class).invoke(ob, Long.parseLong(request.getParameter(ob.getClass().getDeclaredFields()[j].getName())));
                            } catch (Exception e4) {
                                try {
                                    ob.getClass().getDeclaredMethod(setters[j], boolean.class).invoke(ob, Boolean.parseBoolean(request.getParameter(ob.getClass().getDeclaredFields()[j].getName())));
                                } catch (Exception e5) {
                                    try {
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                        ob.getClass().getDeclaredMethod(setters[j], Date.class).invoke(ob, sdf.parse(request.getParameter(ob.getClass().getDeclaredFields()[j].getName())));
                                    } catch (Exception e6) {
                                        try {
                                            ob.getClass().getDeclaredMethod(setters[j], String.class).invoke(ob, request.getParameter(ob.getClass().getDeclaredFields()[j].getName()));
                                        } catch (Exception e7) {
                                            throw new Exception("Le type ne correspond pas a la variable");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public ModelView getCorrespondingModelView(HttpServletRequest request)
    throws Exception {
        String queryString = request.getQueryString();
        StringBuffer requestURL = request.getRequestURL();
        ServletContext context = getServletContext();
        String fullPath = context.getRealPath("/WEB-INF/classes");
        Utilitaire util=new Utilitaire(queryString, requestURL, fullPath);
        for(int i=0; i<this.getKeys().length; i++) {
            if(keys[i].toString().compareTo(util.getQueryString())==0) {
                Class<?> classe=util.getClassByName(mappingUrls.get(keys[i]).getClassName());
                Object ob=classe.newInstance();
                // String[] setters=this.getSettersMethods(ob);
                // for(int j=0; j<setters.length; j++) {
                //     if(request.getParameter(ob.getClass().getDeclaredFields()[j].getName())!=null) {
                //         ob.getClass().getDeclaredMethod(setters[j], String.class).invoke(ob, request.getParameter(ob.getClass().getDeclaredFields()[j].getName()));
                //     }
                // }
                this.parseString(ob, request);
                if(!(ob.getClass().getDeclaredMethod(mappingUrls.get(keys[i]).getMethod()).invoke(ob) instanceof ModelView)) {
                    throw new Exception("La method "+ob.getClass().getDeclaredMethod(mappingUrls.get(keys[i]).getMethod()).getName()+" doit retourner un objet de type ModelView");
                }
                return (ModelView) ob.getClass().getDeclaredMethod(mappingUrls.get(keys[i]).getMethod()).invoke(ob);
            }
        }
        throw new Exception("Error 404 : Page not found");
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
        PrintWriter pr=response.getWriter();
        try {
            ModelView md=this.getCorrespondingModelView(request);
            this.setAttribute(request, md);
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