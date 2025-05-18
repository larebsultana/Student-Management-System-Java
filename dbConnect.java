package Project;

import java.sql.*;

public class dbConnect {
    private static Connection mycon = null;

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        String db = "studata";  // Replace with your database name
        String user = "root";   // MySQL username
        String pass = "";       // Empty password (if no password is set)
        String url = "jdbc:mysql://localhost:3306/" + db ;  // URL to the database

        Class.forName("com.mysql.jdbc.Driver");  // Load the MySQL driver
        Connection conn = DriverManager.getConnection(url, user, pass);  // Establish connection
        return conn;
    }
}