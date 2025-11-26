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

    private void KelolaVendor() {
    String sql = 
        "SELECT v.IdVendor, v.Nama, jv.Nama AS JenisVendor " +
        "FROM Vendor v " +
        "JOIN JenisVendor jv ON v.IdJenisVendor = jv.IdJenisVendor";

    try (Connection conn = DBConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        System.out.printf("%-5s %-30s %-20s\n", 
                "ID", "Nama Vendor", "Jenis Vendor");
        System.out.println("------------------------------------------------------");

        while (rs.next()) {
            System.out.printf("%-5d %-30s %-20s\n",
                    rs.getInt("IdVendor"),
                    rs.getString("Nama"),
                    rs.getString("JenisVendor"));
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

        System.out.println("------------------------------------------------------");
        System.out.println("1. Tambah Vendor");
        System.out.println("2. Pilih Vendor");
        System.out.println("0. Kembali");
        System.out.print("Pilih menu: ");
        Scanner sc = new Scanner(System.in);
        int menu = sc.nextInt();

        switch (menu) {
            case 1:
                TambahVendor();
                break;
            case 2:
                PilihVendor();
                break;
            case 0:
                PUHome();
                return;
            default:
                System.out.println("Menu tidak valid!");
        }
    }

    private void PilihVendor() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Masukkan ID Vendor: ");
        int id = Integer.parseInt(sc.nextLine());

        String sql = 
            "SELECT v.Nama, v.NamaPemilik, v.Alamat, v.NoTelp, v.Harga, jv.Nama AS JenisVendor " +
            "FROM Vendor v " +
            "JOIN JenisVendor jv ON v.IdJenisVendor = jv.IdJenisVendor " +
            "WHERE v.IdVendor = ?";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("============================================");
                System.out.println("Nama Vendor      : " + rs.getString("Nama"));
                System.out.println("Nama Pemilik     : " + rs.getString("NamaPemilik"));
                System.out.println("Alamat           : " + rs.getString("Alamat"));
                System.out.println("No. Telepon      : " + rs.getString("NoTelp"));
                System.out.println("Harga            : " + rs.getDouble("Harga"));
                System.out.println("Jenis Vendor     : " + rs.getString("JenisVendor"));
                System.out.println("============================================");
            } else {
                System.out.println("Vendor dengan ID tersebut tidak ditemukan.");
                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("1. Edit Vendor");
        System.out.println("2. Hapus Vendor");
        System.out.println("0. Kembali");
        System.out.print("Pilih menu: ");
        int menu = sc.nextInt();

        switch (menu) {
            case 1:
                EditVendor(id);
                break;
            case 2:
                HapusVendor(id);
                break;
            case 0:
                KelolaVendor();
                break;
            default:
                System.out.println("Menu tidak valid!");
        }
    }

    private void TambahVendor() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nama Vendor : ");
        String nama = sc.nextLine();
        System.out.print("Nama Pemilik : ");
        String pemilik = sc.nextLine();
        System.out.print("Alamat : ");
        String alamat = sc.nextLine();
        System.out.print("No. Telp : ");
        String noTelp = sc.nextLine();
        System.out.print("Harga : ");
        double harga = sc.nextDouble();
        sc.nextLine(); 
        System.out.print("Jenis Vendor : ");
        int jenisVendor = sc.nextInt();

        String sql = "INSERT INTO Vendor (Nama, NamaPemilik, Alamat, NoTelp, Harga, IdJenisVendor) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nama);
            stmt.setString(2, pemilik);
            stmt.setString(3, alamat);
            stmt.setString(4, noTelp);
            stmt.setDouble(5, harga);
            stmt.setInt(6, jenisVendor);

            stmt.executeUpdate();
            System.out.println("\nVendor berhasil ditambahkan!\n");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        KelolaVendor();
    }

    private void EditVendor(int idVendor) {
        Scanner sc = new Scanner(System.in);

        // Ambil data vendor dulu
        String selectSQL = "SELECT * FROM Vendor WHERE IdVendor = ?";
        String updateSQL = "UPDATE Vendor SET Nama=?, NamaPemilik=?, Alamat=?, NoTelp=?, Harga=?, IdJenisVendor=? WHERE IdVendor=?";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement selectStmt = conn.prepareStatement(selectSQL)) {

            selectStmt.setInt(1, idVendor);
            ResultSet rs = selectStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Vendor tidak ditemukan.");
                return;
            }

            // Tampilkan data
            System.out.println("\n=== Edit Vendor ===");
            System.out.println("1. Nama          : " + rs.getString("Nama"));
            System.out.println("2. Pemilik       : " + rs.getString("NamaPemilik"));
            System.out.println("3. Alamat        : " + rs.getString("Alamat"));
            System.out.println("4. No Telp       : " + rs.getString("NoTelp"));
            System.out.println("5. Harga         : " + rs.getDouble("Harga"));
            System.out.println("6. Jenis Vendor  : " + rs.getInt("IdJenisVendor"));
            System.out.println("7. Batalkan");
            System.out.print("\nPilih bagian yang ingin diubah (1-7): ");
            int pilihan = sc.nextInt();
            sc.nextLine(); 

            if (pilihan == 7) KelolaVendor();

            String nama = rs.getString("Nama");
            String pemilik = rs.getString("NamaPemilik");
            String alamat = rs.getString("Alamat");
            String noTelp = rs.getString("NoTelp");
            double harga = rs.getDouble("Harga");
            int jenis = rs.getInt("IdJenisVendor");

            switch (pilihan) {
                case 1 -> { System.out.print("Nama baru: "); nama = sc.nextLine(); }
                case 2 -> { System.out.print("Pemilik baru: "); pemilik = sc.nextLine(); }
                case 3 -> { System.out.print("Alamat baru: "); alamat = sc.nextLine(); }
                case 4 -> { System.out.print("NoTelp baru: "); noTelp = sc.nextLine(); }
                case 5 -> { System.out.print("Harga baru: "); harga = sc.nextDouble(); sc.nextLine(); }
                case 6 -> { System.out.print("Jenis Vendor baru: "); jenis = sc.nextInt(); sc.nextLine(); }
                default -> { System.out.println("Pilihan tidak valid."); return; }
            }

            // Update
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSQL)) {
                updateStmt.setString(1, nama);
                updateStmt.setString(2, pemilik);
                updateStmt.setString(3, alamat);
                updateStmt.setString(4, noTelp);
                updateStmt.setDouble(5, harga);
                updateStmt.setInt(6, jenis);
                updateStmt.setInt(7, idVendor);

                updateStmt.executeUpdate();
                System.out.println("\nData vendor berhasil diupdate!\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        KelolaVendor();
    }

    private void HapusVendor(int idVendor) {
        Scanner sc = new Scanner(System.in);

        String checkSQL = "SELECT Nama FROM Vendor WHERE IdVendor = ?";
        String deleteSQL = "DELETE FROM Vendor WHERE IdVendor = ?";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement checkStmt = conn.prepareStatement(checkSQL)) {

            checkStmt.setInt(1, idVendor);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Vendor tidak ditemukan.");
                return;
            }

            System.out.println("Apakah Anda yakin ingin menghapus vendor: " + rs.getString("Nama") + "? (y/n)");
            String confirm = sc.nextLine();

            if (!confirm.equalsIgnoreCase("y")) {
                System.out.println("Penghapusan dibatalkan.");
                return;
            }

            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSQL)) {
                deleteStmt.setInt(1, idVendor);
                deleteStmt.executeUpdate();
                System.out.println("Vendor berhasil dihapus.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        KelolaVendor();
    }
}