public class User {
    private String username;
    private String password;
    private String role; // "Buyer" or "Seller"

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

	public String getEmail() {
		// TODO Auto-generated method stub
		return null;
	}
}
