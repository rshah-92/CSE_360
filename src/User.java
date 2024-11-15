public class User {
	private int userID = 00000;
	private String email;
	private String password;
	private int userRole;
	
	public User() {
		this.email = "";
		this.password = "";
		this.userRole = -1;
	}
	
	public User(String e, String p, int r) {
		this.email = e;
		this.password = p;
		this.userRole = r;
	}
	
	public void viewListings() {};
	
	public void viewProfile() {};
	
	public void addUserToDataBase() {};
	
}