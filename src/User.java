class User {
    private static int userId;
    private String nama;
    
    public User(int userId, String username) {
        this.userId = userId;
        this.nama = username;
    }
    
    public static int getUserId() { return userId; }
    public String getNama() { return nama; }
}