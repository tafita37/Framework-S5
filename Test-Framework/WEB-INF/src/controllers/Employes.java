package controllers;

import java.util.HashMap;

import annotation.*;
import upload.FileUpload;
import url.ModelView;

@Scope(singleton = true)
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
/// Sprint 4
    @Url(link = "sprint-4.do")
    public ModelView sprint4()
    throws Exception {
        ModelView resut=new ModelView("sprint-4.jsp");
        return resut;
    }

/// Sprint 5
    @Url(link = "sprint-5.do")
    public ModelView sprint5() 
    throws Exception {
        ModelView resut=new ModelView("sprint-5.jsp");
        return resut;
    }

/// Sprint 6
    @Url(link = "sprint-6.do")
    public ModelView sprint6()
    throws Exception {
        ModelView resut=new ModelView("sprint-6.jsp");
        resut.addItem("emp", this);
        resut.addItem("emp2", new Employes(1, "nom", "prenom"));
        return resut;
    }

/// Sprint 7
    @Url(link = "sprint-7.do")
    public ModelView sprint7()
    throws Exception {
        ModelView resut=new ModelView("sprint-7.jsp");
        resut.addItem("emp", this);
        return resut;
    }

/// Sprint 8
    @Url(link = "sprint-8.do")
    @Parameters(args = {"id_emp"})
    public ModelView sprint8(int id_emp)
    throws Exception {
        ModelView resut=new ModelView("sprint-8.jsp");
        resut.addItem("id_emp", id_emp);
        return resut;
    }
    
/// Sprint 9
    @Url(link = "sprint-9.do")
    public ModelView sprint9()
    throws Exception {
        ModelView resut=new ModelView("index.html");
        return resut;
    }

/// Sprint 10 
    @Url(link = "sprint-10.do")
    public ModelView sprint10()
    throws Exception {
        ModelView resut=new ModelView("index.html");
        return resut;
    }

/// login
    @Url(link = "login.do")
    public ModelView login()
    throws Exception {
        ModelView md=new ModelView("sprint-11.jsp");
        md.addSession("role", "admin");
        md.addSession("isConnected", true);
        return md;
    }

/// Sprint 11
    @Url(link = "sprint-11.do")
    @Auth(role = "admin")
    public ModelView sprint11()
    throws Exception {
        ModelView resut=new ModelView("sprint-11.jsp");
        return resut;
    }
    
/// Sprint 12
    @Url(link = "sprint-12.do")
    public ModelView sprint12()
    throws Exception {
        ModelView resut=new ModelView("sprint-12.jsp");
        return resut;
    }

/// Sprint 13
    @Url(link = "sprint-13.do")
    public ModelView sprint13()
    throws Exception {
        ModelView resut=new ModelView("index.html");
        resut.addItem("nom", this);
        resut.setJson(true);
        return resut;
    }

/// Sprint 14
    @Url(link = "sprint-12.do")
    public Employes[] sprint14()
    throws Exception {
        Employes[] resut=new Employes[3];
        resut[0]=new Employes(1, "nom", "prenom");
        resut[1]=new Employes(2, "nom2", "prenom2");
        resut[2]=new Employes(3, "nom3", "prenom3");
        return resut;
    }

/// Sprint 15-1
    @Url(link = "sprint-15-1.do")
    public ModelView sprint15_1()
    throws Exception {
        ModelView resut=new ModelView("sprint-15-1.jsp");
        resut.removeSession("role");
        return resut;
    }

/// Sprint 15-2
    @Url(link = "sprint-15-2.do")
    public ModelView sprint15_2()
    throws Exception {
        ModelView resut=new ModelView("sprint-15-2.jsp");
        resut.setInvalidateSession(true);
        return resut;
    }
}
