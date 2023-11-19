package exe;

import java.util.Date;

public class Main {
    public static void main(String[] args)
    throws Exception {
        Date d=new Date();
        String a="2023-04-25";
        System.out.println(d.getClass().cast(a));
    }
}