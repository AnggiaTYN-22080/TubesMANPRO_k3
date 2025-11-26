import java.sql.*;

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
    
}