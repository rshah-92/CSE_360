import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Transactions {
    private int transID;
    private double totalAmount;
    private String date;
    private User buyer;
    private User seller;
    private Book book;

    public Transactions(int transID, double totalAmount, String date, User buyer, User seller, Book book) {
        this.transID = transID;
        this.totalAmount = totalAmount;
        this.date = date;
        this.buyer = buyer;
        this.seller = seller;
        this.book = book;
    }

    public int getTransID() {
        return transID;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getDate() {
        return date;
    }

    public User getBuyer() {
        return buyer;
    }

    public User getSeller() {
        return seller;
    }

    public Book getBook() {
        return book;
    }

    public void saveTransaction(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write("Transaction ID: " + transID);
            writer.newLine();
            writer.write("Total Amount: $" + totalAmount);
            writer.newLine();
            writer.write("Date: " + date);
            writer.newLine();
            writer.write("Buyer: " + (buyer != null ? buyer.getEmail() : "N/A"));
            writer.newLine();
            writer.write("Seller: " + (seller != null ? seller.getEmail() : "N/A"));
            writer.newLine();
            writer.write("Book: " + (book != null ? book.toString() : "N/A"));
            writer.newLine();
            writer.write("------------------------------");
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving transaction: " + e.getMessage());
        }
    }
}
