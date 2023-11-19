package exe;

import java.util.Date;
import java.lang.reflect.*;

import ETU1863.framework.servlet.FrontServlet;
import model.Employes;

public class Main {
    public static void main(String[] args)
    throws Exception {
        Employes emp=new Employes();
        Method[] methods=emp.getClass().getDeclaredMethods();
        for(int i=0; i<methods.length; i++) {
            System.out.println("Methods : "+methods[i].getName());
            Parameter[] param=methods[i].getParameters();
            for(int j=0; j<param.length; j++) {
                System.out.println("          "+param[j].getName());
            }
            System.out.println();
        }
    }
}