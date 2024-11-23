import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BookStoreManager {
    private final List<Book> approvedBooks = new ArrayList<>();
    private final List<Book> pendingApprovalBooks = new ArrayList<>();
    private static final String FILE_PATH = "books.txt";

    public BookStoreManager() {
        loadBooksFromFile();
    }

    private void loadBooksFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                // Skip the header
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length < 4) continue;

                String title = parts[0].trim();
                String condition = parts[1].trim();
                String category = parts[2].trim();
                String price = parts[3].trim();

                // Add books to approved list initially
                approvedBooks.add(new Book(0, title, condition, 0, category, price));
            }
        } catch (IOException e) {
            System.err.println("Error reading books file: " + e.getMessage());
        }
    }

    public List<Book> getApprovedBooks() {
        return approvedBooks;
    }

    public List<Book> getPendingApprovalBooks() {
        return pendingApprovalBooks;
    }

    public void addBookToPending(Book book) {
        pendingApprovalBooks.add(book);
    }

    public void moveBookToApproved(Book book) {
        pendingApprovalBooks.remove(book);
        approvedBooks.add(book);
    }
}