import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class Asisten extends User {
    public Asisten(int userId, String username) {
        super(userId, username);
    }

    private String getNamaAsisten(Connection conn) {
        String nama = "Unknown Asisten";
        String sql = "SELECT Nama FROM [User] WHERE IdUser = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, this.getUserId());
            rs = ps.executeQuery();

            if (rs.next()) {
                nama = rs.getString("Nama");
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil nama asisten: " + e.getMessage());
        } finally {
            closeResources(ps, rs);
        }
        return nama;
    }
    

    private void closeResources(PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        } catch (SQLException e) {
            System.err.println("Error saat menutup resources: " + e.getMessage());
        }
    }
    
    public void AsistenHome() {
        Scanner sc = new Scanner(System.in); 
        Connection conn = null;

        try {
            conn = DBConnection.getConnection(); 
            String namaAsisten = getNamaAsisten(conn); 

            System.out.println("\n===================================");
            System.out.println("   Selamat Datang, Asisten " + namaAsisten);
            System.out.println("===================================");
            
            int choice;
            do {
                System.out.println("\nMENU ASISTEN:");
                System.out.println("1. Lihat Daftar Klien");
                System.out.println("2. Tambah Event Baru");
                System.out.println("3. Alokasikan Vendor ke Event");
                System.out.println("4. Update Status Event");
                System.out.println("0. Logout");
                System.out.print("Pilih menu: ");
                
                if (sc.hasNextInt()) {
                    choice = sc.nextInt();
                    sc.nextLine();

                    switch (choice) {
                        case 1:
                            handleLihatKlien(conn);
                            break;
                        case 2:
                            handleTambahEvent(conn, sc);
                            break;
                        case 3:
                            handleAlokasikanVendor(conn, sc);
                            break;
                        case 4:
                            handleUpdateStatusEvent(conn, sc);
                            break;
                        case 0:
                            System.out.println("Logging out... Kembali ke menu utama.");
                            break;
                        default:
                            System.out.println("Pilihan tidak valid. Silakan coba lagi.");
                    }
                } else {
                    System.out.println("Input tidak valid. Silakan masukkan angka.");
                    sc.nextLine(); 
                    choice = -1;
                }

            } while (choice != 0);

        } catch (SQLException e) {
            System.err.println("Database Error: Gagal terhubung/beroperasi: " + e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
            }
        }
    }

    private void handleLihatKlien(Connection conn) {
        List<String> daftar = lihatDaftarKlien(conn);
        if (daftar.isEmpty()) {
            System.out.println("Belum ada klien yang terdaftar.");
        } else {
            System.out.println("\n--- DAFTAR KLIEN ---");
            for (String klien : daftar) {
                System.out.println(klien);
            }
            System.out.println("--------------------");
        }
        try (Scanner tempSc = new Scanner(System.in)) { 
             System.out.print("Tekan ENTER untuk kembali ke menu utama...");
             tempSc.nextLine(); 
        } catch (java.util.NoSuchElementException | IllegalStateException e) {
        }
    }

    private void handleTambahEvent(Connection conn, Scanner sc) {
        int choice;
        do {
            System.out.println("\n--- TAMBAH EVENT BARU ---");
            System.out.println("1. Lanjutkan Tambah Event");
            System.out.println("0. Kembali ke Menu Utama");
            System.out.print("Pilih menu: ");
            
            if (sc.hasNextInt()) {
                choice = sc.nextInt();
                sc.nextLine(); 
                
                if (choice == 1) {
                    try {
                        System.out.print("Nama Event: ");
                        String namaEvent = sc.nextLine();
                        System.out.print("Tanggal (YYYY-MM-DD): ");
                        String tanggal = sc.nextLine();
                        
                        System.out.print("Jumlah Undangan: ");
                        int jumlahUndangan = sc.nextInt();
                        
                        System.out.print("Budget (DECIMAL): ");
                        double budget = sc.nextDouble();
                        
                        System.out.print("ID Klien: ");
                        int idKlien = sc.nextInt();
                        
                        System.out.print("ID Jenis Event: ");
                        int idJenisEvent = sc.nextInt();
                        sc.nextLine(); 
                        
                        int idAsisten = this.getUserId(); 

                        // Memanggil method dengan signature yang disesuaikan
                        tambahEvent(conn, namaEvent, tanggal, jumlahUndangan, budget, idJenisEvent, idKlien, idAsisten);
                        choice = 0; 
                    } catch (java.util.InputMismatchException e) {
                        System.err.println("Input tidak valid. Pastikan format angka dan budget sudah benar.");
                        sc.nextLine();
                        choice = -1;
                    }

                } else if (choice == 0) {
                    System.out.println("Membatalkan penambahan event.");
                } else {
                    System.out.println("Pilihan tidak valid.");
                }
            } else {
                System.out.println("Input tidak valid. Silakan masukkan angka.");
                sc.nextLine(); 
                choice = -1;
            }
        } while (choice != 0);
    }

    private void handleAlokasikanVendor(Connection conn, Scanner sc) {
        int choice;
        do {
            System.out.println("\n--- ALOKASI VENDOR KE EVENT ---");
            System.out.println("1. Lanjutkan Alokasi");
            System.out.println("0. Kembali ke Menu Utama");
            System.out.print("Pilih menu: ");

            if (sc.hasNextInt()) {
                choice = sc.nextInt();
                sc.nextLine();

                if (choice == 1) {
                    try {
                        System.out.print("ID Event: ");
                        int idEvent = sc.nextInt();
                        
                        System.out.print("ID Vendor: ");
                        int idVendor = sc.nextInt();
                        
                        System.out.print("Harga Dealing (DECIMAL/Double): ");
                        double hargaDealing = sc.nextDouble();
                        sc.nextLine();

                        alokasikanVendorKeEvent(conn, idEvent, idVendor, hargaDealing); 
                        
                        choice = 0; 
                    } catch (java.util.InputMismatchException e) {
                        System.err.println("Input tidak valid. Pastikan ID Event, ID Vendor, dan Harga Dealing adalah angka.");
                        sc.nextLine(); 
                        choice = -1;
                    }

                } else if (choice == 0) {
                    System.out.println("Membatalkan alokasi vendor.");
                } else {
                    System.out.println("Pilihan tidak valid.");
                }
            } else {
                System.out.println("Input tidak valid. Silakan masukkan angka.");
                sc.nextLine(); 
                choice = -1;
            }
        } while (choice != 0);
    }

    private void handleUpdateStatusEvent(Connection conn, Scanner sc) {
        int choice;
        do {
            System.out.println("\n--- UPDATE STATUS EVENT ---");
            System.out.println("1. Lanjutkan Update Status");
            System.out.println("0. Kembali ke Menu Utama");
            System.out.print("Pilih menu: ");

            if (sc.hasNextInt()) {
                choice = sc.nextInt();
                sc.nextLine(); 

                if (choice == 1) {
                    try {
                        System.out.print("ID Event yang akan diupdate: ");
                        int idEvent = sc.nextInt();
                        sc.nextLine(); 
                        System.out.print("Status Baru (cth: Selesai, Dibatalkan, On Progress): ");
                        String statusBaru = sc.nextLine();

                        updateStatusEvent(conn, idEvent, statusBaru);
                        choice = 0; 
                    } catch (java.util.InputMismatchException e) {
                        System.err.println("Input ID Event tidak valid. Silakan coba lagi.");
                        sc.nextLine(); 
                        choice = -1;
                    }
                } else if (choice == 0) {
                    System.out.println("Membatalkan update status event.");
                } else {
                    System.out.println("Pilihan tidak valid.");
                }
            } else {
                System.out.println("Input tidak valid. Silakan masukkan angka.");
                sc.nextLine(); 
                choice = -1;
            }
        } while (choice != 0);
    }
}