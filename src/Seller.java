import java.util.ArrayList;

public class Seller extends User {
	private ArrayList<Book> listings;
	private boolean isBlocked;
	
	public Seller() {
		super();
		this.listings = new ArrayList<Book>();
		this.isBlocked = false;
	}
	
	public void listNewBook(Book b) {
		this.listings.add(b);
	}
	
	public void removeListing(Book b) {
		this.listings.remove(b);
	}
	
	public void uploadPicture(String img) {
		return;
	}
	
	public void viewListings() {
		
	}
}