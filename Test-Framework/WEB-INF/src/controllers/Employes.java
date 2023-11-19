package controllers;

import java.util.HashMap;

import annotation.*;
import upload.FileUpload;
import url.ModelView;

@Scope(singleton = "Singleton")
public class Employes {
    int id_employes;
    String nom;
    String prenom;
    FileUpload photo;
    @SessionField
    HashMap<String, Object> session;

/*----------------------------------------Fonctions prérequis---------------------------------------- */
/// Getters and setters
    public int getId_employes() {
        return id_employes;
    }

    public void setId_employes(int nouveau)
    throws Exception {
        if(nouveau<=0) {
            throw new Exception("Id de employes trop petit");
        }
        this.id_employes = nouveau;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nouveau)
    throws Exception {
        if(nouveau==null) {
            throw new Exception("Nom null");
        } else if(nouveau.length()==0) {
            throw new Exception("Veuillez entrer un nom");
        }
        this.nom = nouveau;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public void setPrenom(String nouveau)
    throws Exception {
        if(nouveau==null) {
            throw new Exception("Prenom null");
        } else if(nouveau.length()==0) {
            throw new Exception("Veuillez entrer un Prenom");
        }
        this.prenom=nouveau;
    }

    public FileUpload getPhoto() {
        return photo;
    }

    public void setPhoto(FileUpload photo) {
        this.photo = photo;
    }

    public HashMap<String, Object> getSession() {
        return session;
    }

    public void setSession(HashMap<String, Object> nouveau)
    throws Exception {
        if(nouveau==null) {
            throw new Exception("Veuillez entrer une liste de session");
        }
        this.session = nouveau;
    }

/// Constructeurs
    public Employes()
    throws Exception {
        this.photo=new FileUpload("assets/img");
    }

    public Employes(int id_employes, String nom, String prenom)
    throws Exception {
        this.setId_employes(id_employes);
        this.setNom(nom);
        this.setPrenom(prenom);
        this.photo=new FileUpload("assets/img");
    }

/*------------------------------------------Fonctions principales------------------------------------------ */
/// Find all
    @Url(link = "./")
    public ModelView completeInformation()
    throws Exception {
        ModelView result=new ModelView("formulaire.jsp");
        result.setJson(true);
        result.addItem("testJson", new Employes());
        result.addItem("testJson2", "new Employes()");
        return result;
    }

/// Deuxième test
    @Url(link = "test")
    public ModelView allEmp()
    throws Exception {
        ModelView result=new ModelView("test.jsp");
        result.addItem("employe", this);
        result.addSession("role", "admin");
        result.addSession("isConnected", true);
        return result;
    }

/// Autorisation
    @Auth(role = "admin", exception = "Seule l'admin peut acceder a la fonction admin")
    @Url(link = "admin")
    public ModelView admin()
    throws Exception {
        ModelView md=new ModelView("admin.jsp");
        System.out.println(this.getSession().get("role"));
        return md;
    }
}
