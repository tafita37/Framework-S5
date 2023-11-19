package url;

public class ModelReturn {
    Object object;
    boolean json;

/*--------------------------------------------------Fonctions prérequise------------------------------------------- */
/// Getters and setters
    public Object getObject() {
        return object;
    }

    public void setObject(Object nouveau)
    throws Exception {
        if(nouveau==null) {
            throw new Exception("Votre fonction annote doit retourner un Objet");
        }
        this.object = nouveau;
    }

    public boolean isJson() {
        return json;
    }

    public void setJson(boolean nouveau)
    throws Exception {
        if(nouveau&&object instanceof ModelView) {
            throw new Exception("Vous ne pouvez pas convertir en Json si la method retourne un ModelView");
        }
        this.json = nouveau;
    }

/// Constructeurs
    public ModelReturn(Object object) 
    throws Exception {
        this.setObject(object);
    }

    public ModelReturn(Object object, boolean json) 
    throws Exception {
        this.setObject(object);
        this.setJson(json);
    }
}
