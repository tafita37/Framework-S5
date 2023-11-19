package controllers;
import annotation.*;
import url.ModelView;
public class Employes {
    int id_employes;
    String nom;
    String prenom;

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

/// Constructeurs
    public Employes() {}

    public Employes(int id_employes, String nom, String prenom)
    throws Exception {
        this.setId_employes(id_employes);
        this.setNom(nom);
        this.setPrenom(prenom);
    }

/*------------------------------------------Fonctions principales------------------------------------------ */
/// Find all
    @Url(link = "/")
    public ModelView completeInformation()
    throws Exception {
        ModelView result=new ModelView("formulaire.jsp");
        return result;
    }

/// Deuxième test
    @Url(link = "test")
    public ModelView allEmp()
    throws Exception {
        ModelView result=new ModelView("test.jsp");
        result.addItem("employe", new Employes(id_employes, nom, prenom));
        return result;
    }
}
