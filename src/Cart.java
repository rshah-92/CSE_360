import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private List<book_class> cartBooks;

    public Cart() {
        this.cartBooks = new ArrayList<>();
    }

    public void addBookToCart(book_class book) {
        cartBooks.add(book);
    }

    public void showCart(Stage parentWindow) {
        Stage cartWindow = new Stage();
        cartWindow.setTitle("Your Cart");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f8f9fa;");

        Label cartTitle = new Label("Cart");
        cartTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        if (cartBooks.isEmpty()) {
            Label emptyCartLabel = new Label("Your cart is currently empty.");
            layout.getChildren().addAll(cartTitle, emptyCartLabel);
        } else {
            for (book_class book : cartBooks) {
                Label bookDetails = new Label(book.getTitle() + " - $" + book.getPrice());
                layout.getChildren().add(bookDetails);
            }
        }

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> cartWindow.close());
        layout.getChildren().add(closeButton);

        Scene scene = new Scene(layout, 400, 300);
        cartWindow.setScene(scene);
        cartWindow.initOwner(parentWindow);
        cartWindow.show();
    }
}
