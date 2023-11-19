package ETU1863.framework;
public class Mapping {
    String className;
    String method;

/*-------------------------------------------Fonctions prérequis------------------------------------- */
/// Getters and Setters
    public String getClassName() {
        return className;
    }

    public void setClassName(String nouveau)
    throws Exception {
        if(nouveau==null) {
            throw new Exception("Class name ne peut etre null");
        } else if(nouveau.length()==0) {
            throw new Exception("Veuillez entrer une class name");
        }
        this.className = nouveau;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String nouveau)
    throws Exception {
        if(nouveau==null) {
            throw new Exception("La method ne peut etre null");
        } else if(nouveau.length()==0) {
            throw new Exception("Veuillez entrer une method");
        }
        this.method = nouveau;
    }

/// Constructeurs
    public Mapping() {}

    public Mapping(String className, String method)
    throws Exception {
        this.setClassName(className);
        this.setMethod(method);
    }
}
