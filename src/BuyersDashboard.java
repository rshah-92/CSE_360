import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BuyersDashboard {
    private final Stage window;
    private final Cart cart;
    private final List<Book> allBooks = new ArrayList<>();
    private final VBox booksDisplay = new VBox(10); // Vertical layout for books

    public BuyersDashboard(Stage window, Cart cart) {
        this.window = window;
        this.cart = cart;

        // Example books
        allBooks.add(new Book(1, "The Great Gatsby", "F. Scott Fitzgerald", 20.0, "Fiction", "Used Like New"));
        allBooks.add(new Book(2, "A Brief History of Time", "Stephen Hawking", 15.0, "Science", "Moderately Used"));
        allBooks.add(new Book(3, "To Kill a Mockingbird", "Harper Lee", 10.0, "Fiction", "Heavily Used"));
        allBooks.add(new Book(4, "Sapiens", "Yuval Noah Harari", 25.0, "History", "Used Like New"));
        allBooks.add(new Book(5, "1984", "George Orwell", 18.0, "Fiction", "Used Like New"));
        allBooks.add(new Book(6, "Cosmos", "Carl Sagan", 30.0, "Science", "Moderately Used"));
        allBooks.add(new Book(7, "A History of Time", "Stephen Hawking", 15.0, "Science", "Moderately Used"));
        allBooks.add(new Book(8, "To a Mockingbird", "Harper Lee", 10.0, "Fiction", "Heavily Used"));
        allBooks.add(new Book(9, "Sapns", "Yuval Noah Harari", 25.0, "History", "Used Like New"));
        allBooks.add(new Book(10, "14", "George Orwell", 18.0, "Fiction", "Used Like New"));
    }

    public void showDashboard() {
        BorderPane layout = new BorderPane();

        // Top Navigation Bar
        HBox topNavBar = createTopNavBar();

        // Search and Filters
        VBox searchAndFilters = createSearchAndFilters();

        // Books Display Area with Vertical Scroll
        ScrollPane booksScrollPane = createScrollableBooksDisplay();

        VBox content = new VBox(20, searchAndFilters, booksScrollPane);
        content.setPadding(new Insets(20));

        layout.setTop(topNavBar);
        layout.setCenter(content);

        Scene scene = new Scene(layout, 900, 600);
        window.setScene(scene);
    }

    private HBox createTopNavBar() {
        HBox topNavBar = new HBox(10);
        topNavBar.setPadding(new Insets(10));
        topNavBar.setStyle("-fx-background-color: #f0f0f0;");
        topNavBar.setAlignment(Pos.CENTER_LEFT);

        // Logo
        ImageView logo = new ImageView();
        try {
            logo.setImage(new Image(getClass().getResourceAsStream("/logo.png")));
            logo.setFitHeight(40);
            logo.setPreserveRatio(true);
        } catch (Exception e) {
            System.err.println("Error loading logo.png: " + e.getMessage());
        }

        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // User Icon
        ImageView userIcon = new ImageView();
        try {
            userIcon.setImage(new Image(getClass().getResourceAsStream("/user.png")));
            userIcon.setFitHeight(30);
            userIcon.setPreserveRatio(true);
        } catch (Exception e) {
            System.err.println("Error loading user.png: " + e.getMessage());
        }

        // Cart Button
        Button cartButton = new Button("Cart");
        cartButton.setOnAction(e -> cart.showCart(window));

        topNavBar.getChildren().addAll(logo, spacer, userIcon, cartButton);
        return topNavBar;
    }

    private VBox createSearchAndFilters() {
        VBox searchAndFilters = new VBox(10);
        searchAndFilters.setPadding(new Insets(10));
        searchAndFilters.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1px;");

        // Search Bar
        HBox searchBar = new HBox(10);
        searchBar.setPadding(new Insets(5));

        TextField searchField = new TextField();
        searchField.setPromptText("Search based on titles and authors");
        searchField.setPrefWidth(600);

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> {
            String query = searchField.getText().toLowerCase();
            List<Book> filteredBooks = allBooks.stream()
                    .filter(book -> book.getTitle().toLowerCase().contains(query) ||
                                    book.getAuthor().toLowerCase().contains(query))
                    .collect(Collectors.toList());

            if (filteredBooks.isEmpty()) {
                booksDisplay.getChildren().clear();
                booksDisplay.getChildren().add(new Label("No results found for your search."));
            } else {
                refreshBooksDisplay(filteredBooks);
            }
        });

        searchBar.getChildren().addAll(searchField, searchButton);

        // Filters
        VBox filters = new VBox(10);
        filters.setPadding(new Insets(10));

        ComboBox<String> categoryFilter = new ComboBox<>();
        categoryFilter.getItems().addAll("All Categories", "Fiction", "Science", "History");
        categoryFilter.setValue("All Categories");

        ComboBox<String> conditionFilter = new ComboBox<>();
        conditionFilter.getItems().addAll("All Conditions", "Used Like New", "Moderately Used", "Heavily Used");
        conditionFilter.setValue("All Conditions");

        Button applyFiltersButton = new Button("Apply Filters");
        applyFiltersButton.setOnAction(e -> {
            String selectedCategory = categoryFilter.getValue();
            String selectedCondition = conditionFilter.getValue();

            List<Book> filteredBooks = allBooks.stream()
                    .filter(book -> (selectedCategory.equals("All Categories") || book.getCategory().equals(selectedCategory)) &&
                                    (selectedCondition.equals("All Conditions") || book.getCondition().equals(selectedCondition)))
                    .collect(Collectors.toList());

            refreshBooksDisplay(filteredBooks);
        });

        filters.getChildren().addAll(
                new Label("Filters:"),
                new HBox(10, new Label("Category:"), categoryFilter),
                new HBox(10, new Label("Condition:"), conditionFilter),
                applyFiltersButton
        );

        searchAndFilters.getChildren().addAll(searchBar, filters);
        return searchAndFilters;
    }

    private ScrollPane createScrollableBooksDisplay() {
        booksDisplay.setPadding(new Insets(10));
        refreshBooksDisplay(allBooks); // Show all books by default

        ScrollPane scrollPane = new ScrollPane(booksDisplay);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS); // Ensure vertical scroll
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // No horizontal scroll
        return scrollPane;
    }

    private void refreshBooksDisplay(List<Book> books) {
        booksDisplay.getChildren().clear();
        GridPane booksGrid = new GridPane();
        booksGrid.setHgap(10);
        booksGrid.setVgap(10);

        for (int i = 0; i < books.size(); i++) {
            VBox bookCard = createBookCard(books.get(i));
            booksGrid.add(bookCard, i % 4, i / 4); // Three cards per row
        }

        booksDisplay.getChildren().add(booksGrid);
    }

    private VBox createBookCard(Book book) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1px; -fx-background-color: #f9f9f9;");
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(200);

        Label titleLabel = new Label(book.getTitle());
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label authorLabel = new Label("Author: " + book.getAuthor());
        Label categoryLabel = new Label("Category: " + book.getCategory());
        Label conditionLabel = new Label("Condition: " + book.getCondition());
        Label priceLabel = new Label("Price: $" + book.getSellPrice());

        Button addToCartButton = new Button("Add to Cart");
        addToCartButton.setOnAction(e -> {
            cart.addBookToCart(book);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Added " + book.getTitle() + " to your cart!");
            alert.showAndWait();
        });

        card.getChildren().addAll(titleLabel, authorLabel, categoryLabel, conditionLabel, priceLabel, addToCartButton);
        return card;
    }
}
