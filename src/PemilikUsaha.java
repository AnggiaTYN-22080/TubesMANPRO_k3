import java.sql.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

class PemilikUsaha extends User {
    public PemilikUsaha(int userId, String nama) {
        super(userId, nama);
    }

    public void PUHome() {// Menu Pemilik Usaha
        System.out.println("Role: Pemilik Usaha");
        System.out.println("Selamat Datang, " + getNama());

        System.out.println("\n=== MENU PEMILIK USAHA ===");
        System.out.println("1. Kelola Asisten");
        System.out.println("2. Kelola Vendor");
        System.out.println("3. Kelola Jenis Vendor");
        System.out.println("4. Laporan Event");
        System.out.println("0. Logout");

        Scanner sc = new Scanner(System.in);
        System.out.print("Pilih menu: ");
        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                System.out.println("Mengelola Asisten...");
                KelolaAsisten();
                break;
            case 2:
                System.out.println("Mengelola Vendor...");
                KelolaVendor();
                break;
            case 3:
                System.out.println("Mengelola Jenis Vendor...");
                KelolaJenisVendor();
                break;
            case 4:
                System.out.println("Melihat Laporan Event...");
                KelolaLaporanEvent();
                break;
            case 0:
                System.out.println("Logout...");
                App.Login();
                break;
            default:
                System.out.println("Pilihan tidak valid.");
                PUHome();
        }
        sc.close();
    }

    private void KelolaAsisten() {//Print daftar asisten dan menu kelola asisten
        String query =
            "SELECT u.IdUser, u.Nama, u.Username, u.Password, u.Alamat, u.NoTelp " +
            "FROM Asisten a " +
            "JOIN [User] u ON a.IdAsisten = u.IdUser";

        try (Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {

            // Header tabel
            System.out.printf("%-5s %-20s %-15s %-15s %-30s %-15s\n",
                    "ID", "Nama", "Username", "Password", "Alamat", "No.Telp");
            System.out.println("-----------------------------------------------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-5d %-20s %-15s %-15s %-30s %-15s\n",
                        rs.getInt("IdUser"),
                        rs.getString("Nama"),
                        rs.getString("Username"),
                        rs.getString("Password"),
                        rs.getString("Alamat"),
                        rs.getString("NoTelp"));
            }

            System.out.println("-----------------------------------------------------------------------------------------------------");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        Scanner sc = new Scanner(System.in);
        System.out.println("Menu: ");
        System.out.println("1. Tambah Asisten");
        System.out.println("2. Edit Asisten");
        System.out.println("3. Hapus Asisten");
        System.out.println("0. Kembali");
        System.out.print("Pilih menu: ");
        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                System.out.println("Menambah Asisten...");
                TambahAsisten();
                break;
            case 2:
                System.out.println("Mengedit Asisten...");
                EditAsisten();
                break;
            case 3:
                System.out.println("Menghapus Asisten...");
                HapusAsisten();
                break;
            case 0:
                PUHome();
                break;
            default:
                System.out.println("Pilihan tidak valid.");
                KelolaAsisten();
        }
        sc.close();
    }

    private void TambahAsisten() {//Submenu tambah asisten
        Scanner sc = new Scanner(System.in);
        System.out.print("Nama: ");
        String nama = sc.nextLine();
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();
        System.out.print("Alamat: ");
        String alamat = sc.nextLine();
        System.out.print("No.Telp: ");
        String noTelp = sc.nextLine();

        String insertUserSQL = "INSERT INTO [User] (Nama, Alamat, NoTelp, Username, Password) VALUES (?, ?, ?, ?, ?)";
        String insertAsistenSQL = "INSERT INTO Asisten (IdAsisten) VALUES (?)";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement userStmt = conn.prepareStatement(insertUserSQL, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement asistenStmt = conn.prepareStatement(insertAsistenSQL)) {

            conn.setAutoCommit(false);

            // Insert ke tabel User
            userStmt.setString(1, nama);
            userStmt.setString(2, alamat);
            userStmt.setString(3, noTelp);
            userStmt.setString(4, username);
            userStmt.setString(5, password);
            userStmt.executeUpdate();

            // Dapatkan id user baru
            ResultSet generatedKeys = userStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int newUserId = generatedKeys.getInt(1);

                // Insert ke tabel Asisten
                asistenStmt.setInt(1, newUserId);
                asistenStmt.executeUpdate();

                conn.commit(); // sukses
                System.out.println("Asisten berhasil ditambahkan dengan ID: " + newUserId);
            } else {
                conn.rollback();
                System.out.println("Gagal mendapatkan ID untuk Asisten baru.");
            }
            KelolaAsisten();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void EditAsisten() {//Submenu edit asisten
        Scanner sc = new Scanner(System.in);
        System.out.print("Masukkan ID Asisten yang akan diedit: ");
        int idAsisten = sc.nextInt();
        sc.nextLine();

        System.out.print("Nama baru: ");
        String nama = sc.nextLine();
        System.out.print("Username baru: ");
        String username = sc.nextLine();
        System.out.print("Password baru: ");
        String password = sc.nextLine();
        System.out.print("Alamat baru: ");
        String alamat = sc.nextLine();
        System.out.print("No.Telp baru: ");
        String noTelp = sc.nextLine();

        String updateSQL = "UPDATE [User] SET Nama = ?, Username = ?, Password = ?, Alamat = ?, NoTelp = ? WHERE IdUser = ?";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(updateSQL)) {

            stmt.setString(1, nama);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.setString(4, alamat);
            stmt.setString(5, noTelp);
            stmt.setInt(6, idAsisten);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Asisten dengan ID " + idAsisten + " berhasil diupdate.");
            } else {
                System.out.println("Asisten dengan ID " + idAsisten + " tidak ditemukan.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        KelolaAsisten();
    }

    private void HapusAsisten() {//Submenu hapus asisten
        Scanner sc = new Scanner(System.in);
        System.out.print("Masukkan ID Asisten yang akan dihapus: ");
        int idAsisten = sc.nextInt();

        String deleteAsistenSQL = "DELETE FROM Asisten WHERE IdAsisten = ?";
        String deleteUserSQL = "DELETE FROM [User] WHERE IdUser = ?";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement asistenStmt = conn.prepareStatement(deleteAsistenSQL);
            PreparedStatement userStmt = conn.prepareStatement(deleteUserSQL)) {

            conn.setAutoCommit(false);

            // Hapus dari tabel Asisten
            asistenStmt.setInt(1, idAsisten);
            int asistenRows = asistenStmt.executeUpdate();

            // Hapus dari tabel User
            userStmt.setInt(1, idAsisten);
            int userRows = userStmt.executeUpdate();

            if (asistenRows > 0 && userRows > 0) {
                conn.commit();
                System.out.println("Asisten dengan ID " + idAsisten + " berhasil dihapus.");
            } else {
                conn.rollback();
                System.out.println("Asisten dengan ID " + idAsisten + " tidak ditemukan.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        KelolaAsisten();
    }
}