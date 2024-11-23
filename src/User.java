public class User {
    private static int nextID = 0;
    private int userID;
    private String email;
    private String password;
    private int userRole;

    public User() {
        this.userID = 0;
        this.email = "";
        this.password = "";
        this.userRole = -1;
    }

    public User(int id, String e, String p, int r) {
        this.userID = id;
        this.email = e;
        this.password = p;
        this.userRole = r;
    }

    public User(String e, String p, int r) {
        this.userID = nextID++;
        this.email = e;
        this.password = p;
        this.userRole = r;
    }

    public int getUserID() { return this.userID; }

    public String getEmail() { return this.email; }

    public void setPassword(String p) { this.password = p; }

}
