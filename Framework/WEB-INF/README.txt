packages et class nécessaire à importer :
    annotation.Url
    url.ModelView

autre packages non nécessaire à importer :
    ETU1863.framework.servlet.FrontServlet
    ETU1863.framework.Mapping
    url.Utilitaire

Annotation : 
    @Url(link="annotation")
Remplacer annotation par votre annotation
Les méthodes annotés ne doivent recevoir aucun paramètre satria mbola tsy vita ny sprint 8
Toutes vos méthodes portant cette annotation devra retourner un ModelView
Exemple : "emp-all" 
Mettre un "/" à la place de annotation pour indiquer quelle page ouvrir à l'ouverture de l'application

Configurer le fichier web.xml :
    <?xml version="1.0" encoding="UTF-8"?>
    <web-app xmlns="http://java.sun.com/xml/ns/j2ee"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee
        http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd"
        version="2.4">
        <servlet> 
            <servlet-name>Front</servlet-name> 
            <servlet-class>ETU1863.framework.servlet.FrontServlet</servlet-class> 
        </servlet>
        <servlet-mapping> 
            <servlet-name>Front</servlet-name>
            <url-pattern>/</url-pattern>
        </servlet-mapping>
    </web-app>
