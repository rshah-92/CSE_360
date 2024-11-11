import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class BuyersDashboard {

    private Stage window;

    public BuyersDashboard(Stage window) {
        this.window = window;
    }

    public void showDashboard() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f3f4f6;");

        // Create the header with logo, profile icon, and cart button
        HBox header = createHeader();

        // Placeholder for categories or main content
        VBox mainContent = createMainContent();

        // Adding header and main content to the layout
        layout.getChildren().addAll(header, mainContent);

        Scene scene = new Scene(layout, 800, 600);
        window.setScene(scene);
        window.show();
    }

    private HBox createHeader() {
        HBox header = new HBox(10);
        header.setPadding(new Insets(10));
        header.setStyle("-fx-background-color: #3b5998;");

        // Placeholder for Logo
        ImageView logoView = new ImageView(new Image(getClass().getResource("/logo.png").toExternalForm()));
        logoView.setFitHeight(40);
        logoView.setPreserveRatio(true);

        // Cart button with event handler to open CartWindow
        Button cartButton = new Button("Cart");
        cartButton.setStyle("-fx-background-color: #f0e3c2; -fx-font-size: 14px; -fx-padding: 5px;");
        cartButton.setOnAction(e -> openCartWindow());  // Open cart window on click
        
        // Placeholder for Profile Icon
        ImageView profileView = new ImageView(new Image(getClass().getResource("/black.jpeg").toExternalForm()));
        profileView.setFitHeight(30);
        profileView.setPreserveRatio(true);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(logoView, spacer, cartButton, profileView);
        return header;
    }

    private VBox createMainContent() {
        VBox mainContent = new VBox(15);
        mainContent.setPadding(new Insets(20, 0, 0, 0));
        
        Label title = new Label("Buyer's Dashboard");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Placeholder buttons for categories
        Button mathButton = new Button("Mathematics");
        Button csButton = new Button("Computer Science");
        Button englishButton = new Button("English");
        Button scienceButton = new Button("Science");

        // Set style for buttons to make them look minimalistic and consistent
        mathButton.setStyle("-fx-pref-width: 200px; -fx-padding: 10px;");
        csButton.setStyle("-fx-pref-width: 200px; -fx-padding: 10px;");
        englishButton.setStyle("-fx-pref-width: 200px; -fx-padding: 10px;");
        scienceButton.setStyle("-fx-pref-width: 200px; -fx-padding: 10px;");

        // Adding placeholders to main content
        mainContent.getChildren().addAll(title, mathButton, csButton, englishButton, scienceButton);
        return mainContent;
    }

    // Method to open the cart window
    private void openCartWindow() {
        Cart cart = new Cart(window);
        cart.show();
    }
}
