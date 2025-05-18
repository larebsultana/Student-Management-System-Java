package Project;

import java.sql.*;

public class JDBCConnectionTest {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/studata"; // your DB name
        String user = "root";
        String password = ""; // if no password

        try {
            Class.forName("com.mysql.jdbc.Driver"); // JDBC driver class
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connection successful!");
            conn.close();
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found.");
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }
}