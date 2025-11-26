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
}