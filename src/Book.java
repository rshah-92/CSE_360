import javafx.beans.property.*;

public class Book {
    private static int nextID = 0;
    private SimpleIntegerProperty bookID = new SimpleIntegerProperty(nextID);
    private SimpleStringProperty title;
    private SimpleStringProperty author;
    private SimpleDoubleProperty originalPrice;
    private SimpleStringProperty category;
    private SimpleStringProperty condition;
    private SimpleDoubleProperty sellPrice;
    private int status;

    public Book() {
        this.bookID = new SimpleIntegerProperty(0);
        this.title = new SimpleStringProperty("");
        this.author = new SimpleStringProperty("");
        this.originalPrice = new SimpleDoubleProperty(0.0);
        this.category = new SimpleStringProperty("");
        this.condition = new SimpleStringProperty("");
        this.sellPrice = new SimpleDoubleProperty(0.0);
        this.status = -1;
    }

    public Book(String title, String author, double originalPrice, String condition, String category, int status) {
        this.bookID = new SimpleIntegerProperty(nextID++);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.originalPrice = new SimpleDoubleProperty(originalPrice);
        this.category = new SimpleStringProperty(category);
        this.condition = new SimpleStringProperty(condition);
        this.sellPrice = new SimpleDoubleProperty(0);
        this.status = status;
    }

    public Book(int bookID, String title, String author, double originalPrice, String condition, String category, int status) {
        this.bookID = new SimpleIntegerProperty(bookID);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.originalPrice = new SimpleDoubleProperty(originalPrice);
        this.category = new SimpleStringProperty(category);
        this.condition = new SimpleStringProperty(condition);
        this.sellPrice = new SimpleDoubleProperty(0);
        this.status = status;
    }

    public int getBookID() { return bookID.get(); }
    public String getTitle() { return title.get(); }
    public String getAuthor() { return author.get(); }
    public double getPrice() { return originalPrice.get(); }
    public String getCategory() { return category.get(); }
    public String getCondition() { return condition.get(); }
    public double getSellPrice() { return sellPrice.get(); }
    public double getStatus() { return status; }
    public void setCategory(String category) { this.category = new SimpleStringProperty(category); }
    public void setCondition(String cond) { this.condition = new SimpleStringProperty(cond); }

    @Override
    public String toString() {
        return "Book [ID=" + bookID.get() + ", Title=" + title.get() + ", Author=" + author.get() +
                ", Category=" + category.get() + ", Condition=" + condition.get() +
                ", Sell Price=$" + sellPrice.get() + "]";
    }
}
