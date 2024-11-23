import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class SellerDashboard {
    private final Stage window;
    private final List<Book> books; // List to hold book details
    private final List<Book> pendingApprovalBooks; // List for pending approval books

    public SellerDashboard(Stage window) {
        this.window = window;

        // Initialize sample data (replace with dynamic retrieval later)
        books = new ArrayList<>();
        books.add(new Book("The Great Gatsby", "Used Like New", "Natural Sciences", "$3"));
        books.add(new Book("To Kill a Mockingbird", "Moderately Used", "Computer Science", "$5"));
        books.add(new Book("1984", "Heavily Used", "English", "$4"));

        pendingApprovalBooks = new ArrayList<>();
    }

    public void showDashboard() {
        window.setTitle("Seller's Dashboard");

        // Top Navigation Bar
        HBox topBar = createTopBar();

        // My Listings Section
        VBox myListingsSection = createSection("My Listings", false);

        // Pending Approval Section
        VBox pendingApprovalSection = createPendingApprovalSection();

        // Main Layout
        VBox mainLayout = new VBox(20, topBar, myListingsSection, pendingApprovalSection);
        mainLayout.setPadding(new Insets(20));

        Scene scene = new Scene(mainLayout, 1000, 700);
        window.setScene(scene);
        window.show();
    }

    private HBox createTopBar() {
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(10));
        topBar.setAlignment(Pos.CENTER_LEFT);

        // Logo Image
        ImageView logoView = new ImageView(new Image(getClass().getResource("/logo.png").toExternalForm()));
        logoView.setFitHeight(40);
        logoView.setPreserveRatio(true);

        // List a Book Button
        Button listBookButton = new Button("List a Book");
        listBookButton.setOnAction(e -> listNewBook());

        // Profile Icon
        ImageView profileIcon = new ImageView(new Image(getClass().getResource("/ASU_mark_1_M.jpg").toExternalForm()));
        profileIcon.setFitHeight(40);
        profileIcon.setFitWidth(40);
        Button profileButton = new Button("", profileIcon);
        profileButton.setStyle("-fx-background-color: transparent; -fx-border-radius: 20;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topBar.getChildren().addAll(logoView, spacer, listBookButton, profileButton);
        return topBar;
    }

    private VBox createSection(String sectionTitle, boolean isPending) {
        VBox section = new VBox(10);
        section.setPadding(new Insets(10));

        Label sectionLabel = new Label(sectionTitle);
        sectionLabel.setFont(new Font("Arial", 18));

        GridPane bookGrid = isPending ? createPendingApprovalGrid() : createBookGrid();

        section.getChildren().addAll(sectionLabel, bookGrid);
        return section;
    }

    private GridPane createBookGrid() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));

        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            VBox bookCard = createBookCard(book, true);
            gridPane.add(bookCard, i % 3, i / 3);
        }

        return gridPane;
    }

    private GridPane createPendingApprovalGrid() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));

        for (int i = 0; i < pendingApprovalBooks.size(); i++) {
            Book book = pendingApprovalBooks.get(i);
            VBox bookCard = createBookCard(book, false);
            gridPane.add(bookCard, i % 3, i / 3);
        }

        return gridPane;
    }

    private VBox createBookCard(Book book, boolean showEditButton) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(10));
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-border-color: lightgray; -fx-border-width: 1px; -fx-background-color: #f9f9f9;");
        card.setPrefSize(150, 200);

        Label titleLabel = new Label("Title: " + book.getTitle());
        titleLabel.setFont(new Font("Arial", 14));
        titleLabel.setWrapText(true);
        titleLabel.setTextAlignment(TextAlignment.CENTER);

        Label conditionLabel = new Label("Condition: " + book.getCondition());
        Label categoryLabel = new Label("Category: " + book.getCategory());
        Label priceLabel = new Label("Price: " + book.getPrice());

        card.getChildren().addAll(titleLabel, conditionLabel, categoryLabel, priceLabel);

        if (showEditButton) {
            Button editButton = new Button("Edit");
            editButton.setOnAction(e -> openEditPage(book));
            card.getChildren().add(editButton);
        }

        return card;
    }

    private VBox createPendingApprovalSection() {
        return createSection("Pending Approval", true);
    }

    private void listNewBook() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label titleLabel = new Label("Title:");
        TextField titleField = new TextField();

        Label conditionLabel = new Label("Condition:");
        ComboBox<String> conditionDropdown = new ComboBox<>();
        conditionDropdown.getItems().addAll("Used Like New", "Moderately Used", "Heavily Used");

        Label categoryLabel = new Label("Category:");
        ComboBox<String> categoryDropdown = new ComboBox<>();
        categoryDropdown.getItems().addAll("Natural Sciences", "Computer Science", "English");

        Label priceLabel = new Label("Original Price:");
        TextField priceField = new TextField();

        Label estimatedPriceLabel = new Label("Estimated Resale Price: $0.00");

        Button estimateButton = new Button("Estimate Price");
        estimateButton.setOnAction(e -> {
            String condition = conditionDropdown.getValue();
            String category = categoryDropdown.getValue();
            String originalPrice = priceField.getText();

            if (condition != null && category != null && !originalPrice.isEmpty()) {
                double estimatedPrice = estimateResalePrice(Double.parseDouble(originalPrice), condition, category);
                estimatedPriceLabel.setText("Estimated Resale Price: $" + String.format("%.2f", estimatedPrice));
            }
        });

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            String title = titleField.getText();
            String condition = conditionDropdown.getValue();
            String category = categoryDropdown.getValue();
            String originalPrice = priceField.getText();

            if (!title.isEmpty() && condition != null && category != null && !originalPrice.isEmpty()) {
                double estimatedPrice = estimateResalePrice(Double.parseDouble(originalPrice), condition, category);
                pendingApprovalBooks.add(new Book(title, condition, category, "$" + String.format("%.2f", estimatedPrice)));
                showDashboard(); // Refresh the dashboard to show the new book in pending approvals
            }
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> showDashboard());

        layout.getChildren().addAll(
                titleLabel, titleField,
                conditionLabel, conditionDropdown,
                categoryLabel, categoryDropdown,
                priceLabel, priceField,
                estimateButton, estimatedPriceLabel,
                submitButton, backButton
        );

        Scene scene = new Scene(layout, 400, 400);
        window.setScene(scene);
    }

    private double estimateResalePrice(double originalPrice, String condition, String category) {
        double multiplier = 0.5; // Base multiplier
        if (condition.equals("Used Like New")) multiplier += 0.3;
        else if (condition.equals("Moderately Used")) multiplier += 0.2;

        if (category.equals("Computer Science")) multiplier += 0.2;
        else if (category.equals("Natural Sciences")) multiplier += 0.1;

        return originalPrice * multiplier;
    }

    private void openEditPage(Book book) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label editLabel = new Label("Edit Book Details");
        editLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        ComboBox<String> conditionDropdown = new ComboBox<>();
        conditionDropdown.getItems().addAll("Used Like New", "Moderately Used", "Heavily Used");
        conditionDropdown.setValue(book.getCondition());

        ComboBox<String> categoryDropdown = new ComboBox<>();
        categoryDropdown.getItems().addAll("Natural Sciences", "Computer Science", "English");
        categoryDropdown.setValue(book.getCategory());

        Button saveButton = new Button("Save Changes");
        saveButton.setOnAction(e -> {
            book.setCondition(conditionDropdown.getValue());
            book.setCategory(categoryDropdown.getValue());
            showDashboard(); // Refresh dashboard after saving
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> showDashboard());

        layout.getChildren().addAll(editLabel, conditionDropdown, categoryDropdown, saveButton, backButton);

        Scene editScene = new Scene(layout, 400, 300);
        window.setScene(editScene);
    }

    /**
     * Inner class to represent a book.
     */
    private static class Book {
        private final String title;
        private String condition;
        private String category;
        private final String price;

        public Book(String title, String condition, String category, String price) {
            this.title = title;
            this.condition = condition;
            this.category = category;
            this.price = price;
        }

        public String getTitle() {
            return title;
        }

        public String getCondition() {
            return condition;
        }

        public void setCondition(String condition) {
            this.condition = condition;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getPrice() {
            return price;
        }
    }
}
