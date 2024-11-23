import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class SellerDashboard {
    private final Stage window;

    public SellerDashboard(Stage window) {
        this.window = window;
    }

    public void showDashboard() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label title = new Label("Seller's Dashboard");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button listBookButton = new Button("List a Book");
        listBookButton.setOnAction(e -> listBook());

        Button viewListingsButton = new Button("View Listings");
        viewListingsButton.setOnAction(e -> viewListings());

        layout.getChildren().addAll(title, listBookButton, viewListingsButton);

        Scene scene = new Scene(layout, 600, 400);
        window.setScene(scene);
    }

    private void listBook() {
        System.out.println("Redirecting to book listing page...");
    }

    private void viewListings() {
        System.out.println("Viewing all listings...");
    }
}
