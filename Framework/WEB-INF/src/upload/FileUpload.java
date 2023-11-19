package upload;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

public class FileUpload {
    String name;
    String path="assets";
    byte[] bytes;

/*-----------------------------------------Fonctions prérequise---------------------------------------- */
/// Getters and setters
    public String getName() {
        return name;
    }
    
    public void setName(String nouveau)
    throws Exception {
        if(nouveau==null) {
            throw new Exception("Veuillez uploader un fichier");
        }
        this.name = nouveau;
    }
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String nouveau)
    throws Exception {
        if(nouveau==null) {
            throw new Exception("Veuillez definir un dossier ou mettre votre fichier");
        }
        this.path = nouveau;
    }
    
    public byte[] getBytes() {
        return bytes;
    }
    
    public void setBytes(byte[] nouveau)
    throws Exception {
        if(nouveau==null||nouveau.length==0) {
            throw new Exception("Tableau de byte null ou inexistant");
        }
        this.bytes = nouveau;
    }

/// Constructeurs
    public FileUpload(String path)
    throws Exception {
        this.setPath(path);
    }

    public FileUpload(String name, byte[] bytes)
    throws Exception {
        this.setName(name);
        this.setBytes(bytes);
    }

/*--------------------------------------------Fonctions principales------------------------------------------- */
/// Ecrire le fichier dans sa destination
    public void writeFileUpload(HttpServletRequest request, Part part)
    throws Exception {
        String fullPath = request.getServletContext().getRealPath("/"+this.path+"/"+this.name);
        File repertoire=new File(fullPath);
        if(!repertoire.exists()) {
            repertoire.mkdirs();
        }
        part.write(fullPath);
    }
}
