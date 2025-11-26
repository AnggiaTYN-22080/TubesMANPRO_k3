import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class DBConnection {
   private static final String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=Manpro;integratedSecurity=true;encrypt=false;trustServerCertificate=true";

   DBConnection() {
   }

   public static Connection getConnection() throws SQLException {
      try {
         Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
      } catch (ClassNotFoundException var1) {
         System.err.println("SQL Server JDBC Driver not found.");
         var1.printStackTrace();
      }

      return DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=Manpro;integratedSecurity=true;encrypt=false;trustServerCertificate=true");
   }

   public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getConnection();
            System.out.println("Connected!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
