import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private final List<Book> cartBooks;

    public Cart() {
        this.cartBooks = new ArrayList<>();
    }

    public void addBookToCart(Book book) {
        cartBooks.add(book);
    }

    public void removeBookFromCart(Book book) {
        cartBooks.remove(book);
    }

    public void clearCart() {
        cartBooks.clear();
    }

    public void showCart(Stage parentWindow) {
        Stage cartWindow = new Stage();
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f8f9fa;");

        Label cartTitle = new Label("Your Cart");
        cartTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        VBox cartContent = new VBox(10); // Container for the cart's content
        refreshCartContent(cartContent);

        Button clearCartButton = new Button("Clear Cart");
        clearCartButton.setOnAction(e -> {
            clearCart();
            refreshCartContent(cartContent);
        });

        Button checkoutButton = new Button("Checkout");
        checkoutButton.setOnAction(e -> {
            Checkout checkoutFlow = new Checkout(cartBooks);
            checkoutFlow.showShippingPage(cartWindow, this);
        });

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> cartWindow.close());

        HBox actionButtons = new HBox(10, clearCartButton, checkoutButton, closeButton);
        actionButtons.setPadding(new Insets(10));
        actionButtons.setStyle("-fx-alignment: center;");

        layout.getChildren().addAll(cartTitle, cartContent, actionButtons);

        Scene scene = new Scene(layout, 600, 400);
        cartWindow.setScene(scene);
        cartWindow.initOwner(parentWindow);
        cartWindow.show();
    }

    private void refreshCartContent(VBox cartContent) {
        cartContent.getChildren().clear();

        if (cartBooks.isEmpty()) {
            Label emptyCartLabel = new Label("Your cart is currently empty.");
            cartContent.getChildren().add(emptyCartLabel);
        } else {
            GridPane cartGrid = new GridPane();
            cartGrid.setHgap(10);
            cartGrid.setVgap(10);
            cartGrid.setPadding(new Insets(10));

            // Headers
            cartGrid.add(new Label("Title"), 0, 0);
            cartGrid.add(new Label("Author"), 1, 0);
            cartGrid.add(new Label("Price"), 2, 0);
            cartGrid.add(new Label(""), 3, 0); // Empty header for "X" marks

            // Populate rows
            for (int i = 0; i < cartBooks.size(); i++) {
                Book book = cartBooks.get(i);
                cartGrid.add(new Label(book.getTitle()), 0, i + 1);
                cartGrid.add(new Label(book.getAuthor()), 1, i + 1);
                cartGrid.add(new Label("$" + book.getSellPrice()), 2, i + 1);

                Button removeButton = new Button("X");
                removeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: red; -fx-font-size: 14px;");
                removeButton.setOnAction(e -> {
                    removeBookFromCart(book);
                    refreshCartContent(cartContent); // Instantly refresh the cart content
                });

                cartGrid.add(removeButton, 3, i + 1);
            }

            cartContent.getChildren().add(cartGrid);
        }
    }

    public List<Book> getCartBooks() {
        return cartBooks;
    }
}
