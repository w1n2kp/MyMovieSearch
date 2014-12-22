import javax.naming.Context;
import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by vzhang on 14/12/21.
 */
public class DatabaseService {
    private static DatabaseService ourInstance = new DatabaseService();

    public static DatabaseService getInstance() {
        return ourInstance;
    }

    private DatabaseService() {
    }

    public Connection connect(String connectionString) {

        Connection conn = null;

//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//        } catch (Exception e) {
//        }
//
//        try {
//            conn = DriverManager.getConnection(connectionString);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

        try {
            Context ctx = new javax.naming.InitialContext();
            javax.sql.DataSource dataSource =(javax.sql.DataSource) ctx.lookup("java:comp/env/jdbc/MovieSearchDB");
            conn = dataSource.getConnection();

        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return conn;
    }
}
