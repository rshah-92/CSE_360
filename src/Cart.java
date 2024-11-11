import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Cart {

    private Stage cartStage;

    public Cart(Stage owner) {
        cartStage = new Stage();
        cartStage.initModality(Modality.WINDOW_MODAL);
        cartStage.initOwner(owner);
        cartStage.setTitle("Your Cart");

        // Layout for cart items
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f3f4f6;");

        // Placeholder for cart items
        Label cartTitle = new Label("Cart Items");
        cartTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label item1 = new Label("Item 1 - $10");
        Label item2 = new Label("Item 2 - $15");
        Label item3 = new Label("Item 3 - $20");

        // Close button
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> cartStage.close());

        // Adding components to layout
        layout.getChildren().addAll(cartTitle, item1, item2, item3, closeButton);

        Scene scene = new Scene(layout, 300, 400);
        cartStage.setScene(scene);
    }

    public void show() {
        cartStage.show();
    }
}
