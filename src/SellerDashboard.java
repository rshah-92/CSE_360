import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

import javax.swing.plaf.synth.Region;
import javax.swing.text.html.ListView;

public class SellerDashboard {
    private final Stage window;
    private final List<Book> books;
    private final List<Book> pendingApprovalBooks;
    private static final String BOOKS = "books.txt";

    // sample data for all the listed books and pending books
    public SellerDashboard(Stage window) {
        this.window = window;

        books = new ArrayList<>();
        books.add(new Book("The Great Gatsby", "F Scott Fitzgerald", 3.00, "Used Like New", "Natural Sciences", 1));
        books.add(new Book("To Kill a Mockingbird", "", 5.00, "Moderately Used", "Computer Science", 1));
        books.add(new Book("1984", "", 4.00, "Heavily Used", "English Language", 1));

        for (Book book : books) {
            addBookToDatabase(book);
        }

        pendingApprovalBooks = new ArrayList<>();
        pendingApprovalBooks.add(new Book("The Great Gatsby", "", 3.00, "Used Like New", "Natural Sciences", 3));
        pendingApprovalBooks.add(new Book("To Kill a Mockingbird", "", 5.00, "Moderately Used", "Computer Science", 3));
        pendingApprovalBooks.add(new Book("1984", "", 4.00, "Heavily Used", "English Language", 3));

        for (Book book : pendingApprovalBooks) {
            addBookToDatabase(book);
        }
    }

    public void showDashboard() {
        window.setTitle("Seller's Dashboard");

        HBox topBar = createTopBar();
        VBox myListingsSection = createSection("My Listings", false);

        VBox pendingApprovalSection = createPendingApprovalSection();

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

        Button listBookButton = new Button("List a Book");
        listBookButton.setOnAction(e -> listNewBook());

        Button profileButton = new Button("Profile");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topBar.getChildren().addAll(spacer, listBookButton, profileButton);
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

        Label titleLabel = new Label(book.getTitle());
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
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20));

        Label titleLabel = new Label("List a book");
        titleLabel.setFont(new Font("Arial", 24));

        VBox form = new VBox(15);
        form.setMaxWidth(400);

        TextField titleField = new TextField();
        titleField.setPromptText("Title");

        TextField authorField = new TextField();
        authorField.setPromptText("Author");

        TextField publishedYearField = new TextField();
        publishedYearField.setPromptText("Published Year");

        TextField originalPriceField = new TextField();
        originalPriceField.setPromptText("Original Price");


        ListView<String> selectedCategories = new ListView<>();
        MenuButton categoryDropdown = new MenuButton("Select Categories: [empty]");

        List<CheckMenuItem> categories = Arrays.asList(new CheckMenuItem("Natural Sciences"), new CheckMenuItem("Mathematics"), new CheckMenuItem("Computer Science"), new CheckMenuItem("English Language"), new CheckMenuItem("Other"));
        categoryDropdown.getItems().addAll(categories);

        for (final CheckMenuItem item : categories) {
            item.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
                if (newValue) {
                    selectedCategories.getItems().add(item.getText());
                } else {
                    selectedCategories.getItems().remove(item.getText());
                }
                String sMenuText = "Select Categories: " + (selectedCategories.getItems().size() > 0 ? "" : "[empty]");
                categoryDropdown.setText(sMenuText + String.join(", ", selectedCategories.getItems()));
            });
        }

        ComboBox<String> conditionDropdown = new ComboBox<>();
        conditionDropdown.getItems().addAll("Used Like New", "Moderately Used", "Heavily Used");
        conditionDropdown.setPromptText("Select Condition");

        Button uploadImageButton = new Button("Upload Image");
        uploadImageButton.setOnAction(e -> {
            showAlert(Alert.AlertType.CONFIRMATION, "Image Upload", "Image uploaded successfully!");
            // System.out.println("Uploaded!");
        });

        BorderPane layout = new BorderPane();
        layout.setCenter(mainContent);

        Label estimatedPriceLabel = new Label("Estimated Resale Price: $0.00");

        Button estimateButton = new Button("Estimate Price");
        estimateButton.setOnAction(e -> {
            String condition = conditionDropdown.getValue();
            String category = selectedCategories.getItems().get(0);
            String originalPrice = originalPriceField.getText();

            if (condition != null && category != null && originalPrice != null) {
                double estimatedPrice = estimateResalePrice(Double.parseDouble(originalPrice), condition, category);
                estimatedPriceLabel.setText("Estimated Resale Price: $" + String.format("%.2f", estimatedPrice));
            }
        });

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            String title = titleField.getText();
            String author = authorField.getText();
            String condition = conditionDropdown.getValue();
            String category = selectedCategories.getItems().get(0);
            double originalPrice = Double.parseDouble(originalPriceField.getText());


            Book b = new Book(title, author, originalPrice, category, condition, 3);

            if (!title.isEmpty() && condition != null && category != null) {
                double estimatedPrice = estimateResalePrice(originalPrice, condition, category);
                pendingApprovalBooks.add(b);
                showDashboard();
            }

            addBookToDatabase(b);
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> showDashboard());

        form.getChildren().addAll(
                titleField,
                authorField,
                publishedYearField,
                originalPriceField,
                categoryDropdown,
                conditionDropdown,
                estimateButton, estimatedPriceLabel,
                submitButton,
                backButton
        );

        mainContent.getChildren().addAll(titleLabel, form);

        Scene scene = new Scene(layout, 1000, 700);
        window.setScene(scene);
    }

    public void addBookToDatabase(Book b) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKS))) {
            for (Book book : books) {
                String bookData = String.format("%d,%s,%s,%.2f,%s,%s,%.2f,%d",
                        book.getBookID(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getPrice(),
                        book.getCondition(),
                        book.getCategory(),
                        book.getSellPrice(),
                        book.getStatus());
                writer.write(bookData);
                writer.newLine();
            }
            System.out.println("Books saved to file successfully.");
        } catch (IOException e) {
            System.err.println("Error writing to books.txt: " + e.getMessage());
        }
    }

    private double estimateResalePrice(double originalPrice, String condition, String category) {
        double multiplier = 0.5;
        if (condition.equals("Used Like New")) multiplier += 0.3;
        else if (condition.equals("Moderately Used")) multiplier += 0.2;

        if (category.equals("Computer Science")) multiplier += 0.4;
        else if (category.equals("Natural Sciences")) multiplier += 0.1;
        else if (category.equals("English Language")) multiplier += 0.2;
        else if (category.equals("Mathematics")) multiplier += 0.4;
        else if (category.equals("Other")) multiplier += 0.3;

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

        ListView<String> selectedCategories = new ListView<>();
        ObservableList<String> cats = FXCollections.observableArrayList(book.getCategory());
        selectedCategories.setItems(cats);
        MenuButton categoryDropdown = new MenuButton("Select Categories: " + selectedCategories.getItems().get(0));

        List<CheckMenuItem> categories = Arrays.asList(new CheckMenuItem("Natural Sciences"), new CheckMenuItem("Mathematics"), new CheckMenuItem("Computer Science"), new CheckMenuItem("English Language"), new CheckMenuItem("Other"));
        categoryDropdown.getItems().addAll(categories);

        for (final CheckMenuItem item : categories) {
            item.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
                if (newValue) {
                    selectedCategories.getItems().add(item.getText());
                } else {
                    selectedCategories.getItems().remove(item.getText());
                }
                String sMenuText = "Select Categories: " + (selectedCategories.getItems().size() > 0 ? "" : "[empty]");
                categoryDropdown.setText(sMenuText + String.join(", ", selectedCategories.getItems()));
            });
        }

        Button saveButton = new Button("Save Changes");
        saveButton.setOnAction(e -> {
            book.setCondition(conditionDropdown.getValue());
            book.setCategory(selectedCategories.getItems().get(0));
            showDashboard();
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> showDashboard());

        layout.getChildren().addAll(editLabel, conditionDropdown, categoryDropdown, saveButton, backButton);

        Scene editScene = new Scene(layout, 1000, 700);
        window.setScene(editScene);
    }

    // displays an alert
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}