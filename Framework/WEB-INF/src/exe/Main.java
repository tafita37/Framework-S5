package exe;

import java.util.Date;
import java.util.HashMap;
import java.lang.reflect.*;

import ETU1863.framework.servlet.FrontServlet;
import model.Employes;

public class Main {
    public static void main(String[] args)
    throws Exception {
        Employes emp=new Employes();
        for(int i=0; i<emp.getClass().getDeclaredFields().length; i++) {
            System.out.println(emp.getClass().getDeclaredFields()[i].getName());
            System.out.println(emp.getClass().getDeclaredFields()[i].getName());
        }
    }
}