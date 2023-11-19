package exe;

import java.util.Date;
import java.util.HashMap;

import com.google.gson.Gson;

import java.lang.reflect.*;

import ETU1863.framework.servlet.FrontServlet;
import model.Employes;

public class Main {
    public static void main(String[] args)
    throws Exception {
        Employes[] list_emp=new Employes[2];
        list_emp[0]=new Employes(1, "aaa");
        list_emp[1]=new Employes(2, "aaa");
        Gson gson=new Gson();
        System.out.println(gson.toJson(list_emp));
    }
}