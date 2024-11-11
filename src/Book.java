import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Book {

    private SimpleIntegerProperty bookID;
    private SimpleDoubleProperty originalPrice;
    private SimpleStringProperty category;
    private SimpleStringProperty author;
    private SimpleStringProperty title;
    private SimpleIntegerProperty status;
    private SimpleIntegerProperty sellerID;
    private SimpleStringProperty condition;
    private SimpleDoubleProperty sellPrice;

    public Book(int bookID, double originalPrice, String category, String author, String title, int status, int sellerID, String condition) {
        this.bookID = new SimpleIntegerProperty(bookID);
        this.originalPrice = new SimpleDoubleProperty(originalPrice);
        this.category = new SimpleStringProperty(category);
        this.author = new SimpleStringProperty(author);
        this.title = new SimpleStringProperty(title);
        this.status = new SimpleIntegerProperty(status);
        this.sellerID = new SimpleIntegerProperty(sellerID);
        this.condition = new SimpleStringProperty(condition);
        this.sellPrice = new SimpleDoubleProperty(0);
    }

    public double computeSellPrice() {
        switch (condition.get().toLowerCase()) {
            case "like new":
                sellPrice.set(originalPrice.get() * 0.8);
                break;
            case "moderate":
                sellPrice.set(originalPrice.get() * 0.5);
                break;
            case "heavily used":
                sellPrice.set(originalPrice.get() * 0.3);
                break;
            default:
                sellPrice.set(0);
        }
        return sellPrice.get();
    }

	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}
}
