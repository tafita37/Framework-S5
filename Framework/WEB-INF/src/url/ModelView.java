package url;

import java.util.HashMap;

public class ModelView {
    String view;                                                      /// La vue que l'on va charger
    HashMap<String, Object> data=new HashMap<String, Object>();       /// Les données à envoyer à la vues

/*-----------------------------------Fonctions prérequis--------------------------------------- */
/// Getters and setters
    public String getView() {
        return this.view;
    }

    public void setView(String nouveau)
    throws Exception {
        if(nouveau==null) {
            throw new Exception("Nom de vue null lors de l'instanciation de ModelView");
        } else if(nouveau.length()==0) {
            throw new Exception("Veuillez entrer un nom de vue");
        }
        this.view=nouveau;
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }

/// Constructeurs
    public ModelView(String view)
    throws Exception {
        this.setView(view);
    }

/*---------------------------------------------Fonctions principales------------------------------------------- */
/// Add item
    public void addItem(String key, Object value) {
        this.getData().put(key, value);
    }
}
