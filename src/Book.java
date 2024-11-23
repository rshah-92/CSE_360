import javafx.beans.property.*;

public class Book {
    private final SimpleIntegerProperty bookID;
    private final SimpleStringProperty title;
    private final SimpleStringProperty author;
    private final SimpleDoubleProperty originalPrice;
    private final SimpleStringProperty category;
    private final SimpleStringProperty condition;
    private final SimpleDoubleProperty sellPrice;

    public Book(int bookID, String title, String author, double originalPrice, String category, String condition) {
        this.bookID = new SimpleIntegerProperty(bookID);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.originalPrice = new SimpleDoubleProperty(originalPrice);
        this.category = new SimpleStringProperty(category);
        this.condition = new SimpleStringProperty(condition);
        this.sellPrice = new SimpleDoubleProperty(computeSellPrice(originalPrice, condition));
    }

    public double computeSellPrice(double originalPrice, String condition) {
        switch (condition.toLowerCase()) {
            case "used like new": return originalPrice * 0.8;
            case "moderately used": return originalPrice * 0.5;
            case "heavily used": return originalPrice * 0.3;
            default: return 0;
        }
    }

    public int getBookID() { return bookID.get(); }
    public String getTitle() { return title.get(); }
    public String getAuthor() { return author.get(); }
    public double getOriginalPrice() { return originalPrice.get(); }
    public String getCategory() { return category.get(); }
    public String getCondition() { return condition.get(); }
    public double getSellPrice() { return sellPrice.get(); }

    @Override
    public String toString() {
        return "Book [ID=" + bookID.get() + ", Title=" + title.get() + ", Author=" + author.get() +
               ", Category=" + category.get() + ", Condition=" + condition.get() + 
               ", Sell Price=$" + sellPrice.get() + "]";
    }
}
