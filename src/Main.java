import java.io.File;
import java.io.IOException;
import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {

File f=new File("clinicDatabase.db");
        boolean newFile=false;
if (!f.exists())
         newFile = f.createNewFile();

        if (newFile)
            System.out.println("file crated");
        else
            System.out.println("file already exists");

        Class.forName("org.sqlite.JDBC");
        Connection conn=null;

        conn= DriverManager.getConnection("JDBC:sqlite:clinicDatabase.db");


        Login l = new Login(conn);





    }
}