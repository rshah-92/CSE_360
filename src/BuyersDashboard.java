import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class BuyersDashboard {

    private Stage window;
    private Cart cart = new Cart();

    public BuyerDashboard(Stage window, Cart cart) {
        this.window = window;
        this.cart = cart;
    }

    public void showDashboard() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f3f4f6;");

        HBox header = createHeader();

        ScrollPane scrollableContent = createScrollableContent();

        layout.getChildren().addAll(header, scrollableContent);

        Scene scene = new Scene(layout, 800, 600);
        window.setScene(scene);
        window.show();
    }

    private HBox createHeader() {
        HBox header = new HBox(10);
        header.setPadding(new Insets(10));

       ImageView profileView = new ImageView(new Image(getClass().getResource("/black.jpeg").toExternalForm()));
       profileView.setFitHeight(30);
       profileView.setPreserveRatio(true);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button viewCartButton = new Button("View Cart");
        viewCartButton.setOnAction(e -> cart.showCart(window));

       ImageView logoView = new ImageView(new Image(getClass().getResource("/logo.png").toExternalForm()));
       logoView.setFitHeight(40);
       logoView.setPreserveRatio(true);

        header.getChildren().addAll(spacer, viewCartButton);
        return header;
    }

    private ScrollPane createScrollableContent() {
        VBox bookList = new VBox(15);
        bookList.setPadding(new Insets(20));
        bookList.setStyle("-fx-background-color: #ffffff;");

        for (int i = 1; i <= 10; i++) {
            HBox bookItem = createBookItem("Book Title " + i, "Author " + i, "$" + (10 + i));
            bookList.getChildren().add(bookItem);
        }

        ScrollPane scrollPane = new ScrollPane(bookList);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #f3f4f6;");
        return scrollPane;
    }

    private HBox createBookItem(String title, String author, String price) {
        HBox bookItem = new HBox(10);
        bookItem.setPadding(new Insets(10));
        bookItem.setStyle("-fx-background-color: #e7e7e7; -fx-border-color: #cccccc; -fx-border-radius: 5px;");
        bookItem.setPrefHeight(80);

        Label bookDetails = new Label(title + "\n" + author + "\n" + price);
        bookDetails.setStyle("-fx-font-size: 14px; -fx-text-fill: #000000;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button addToCartButton = new Button("Add to Cart");
        addToCartButton.setOnAction(e -> {
            Book book = new Book(title, author, Double.parseDouble(price.substring(1)), "Used Like New", "Natural Sciences", 1); // Dummy book object
            cart.addBookToCart(book);
        });

        bookItem.getChildren().addAll(bookDetails, spacer, addToCartButton);
        return bookItem;
    }
}