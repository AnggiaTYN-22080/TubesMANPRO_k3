import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Login();
    }

    public static void Login() {

        Scanner sc = new Scanner(System.in);

        System.out.println("\n=== Login ===");
        System.out.print("Username: ");
        String username = sc.nextLine();

        System.out.print("Password: ");
        String password = sc.nextLine();

        login(username, password);

        sc.close();
    }

    public static void login(String username, String password) {
        String sql =
            "SELECT " +
            "u.IdUser, u.Nama, u.Username, u.Password, " +
            "CASE " +
            "   WHEN a.IdAsisten IS NOT NULL THEN 'asisten' " +
            "   WHEN p.IdPemilik IS NOT NULL THEN 'pemilik' " +
            "   ELSE 'unknown' " +
            "END AS Role " +
            "FROM [User] u " +
            "LEFT JOIN Asisten a ON u.IdUser = a.IdAsisten " +
            "LEFT JOIN PemilikUsaha p ON u.IdUser = p.IdPemilik " +
            "WHERE u.Username = ? AND u.[Password] = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String role = rs.getString("Role");
                if (role.equals("pemilik")) {
                    PemilikUsaha user = new PemilikUsaha(rs.getInt("IdUser"), rs.getString("Nama"));
                    user.PUHome();
                }
                else if (role.equals("asisten")) {
                    Asisten user = new Asisten(rs.getInt("IdUser"), rs.getString("Nama"));
                    user.AsistenHome();
                }
            }
            else {
                System.out.println("Login failed: Invalid username or password.");
                Login();
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
