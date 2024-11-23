import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class Checkout {
    private final List<Book> booksToCheckout;
    private String shippingAddress = ""; // Store shipping address

    public Checkout(List<Book> booksToCheckout) {
        this.booksToCheckout = booksToCheckout;
    }

    public void showShippingPage(Stage parentWindow, Cart cart) {
        Stage shippingWindow = new Stage();
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f8f9fa;");

        Label title = new Label("Checkout - Shipping Address");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Address Form
        GridPane addressForm = new GridPane();
        addressForm.setHgap(10);
        addressForm.setVgap(10);
        addressForm.setPadding(new Insets(10));

        Label nameLabel = new Label("Full Name:");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter your full name");

        Label addressLabel = new Label("Address:");
        TextArea addressField = new TextArea();
        addressField.setPromptText("Enter your shipping address...");
        addressField.setPrefRowCount(3);

        Label cityLabel = new Label("City:");
        TextField cityField = new TextField();
        cityField.setPromptText("Enter your city");

        Label zipLabel = new Label("Zip Code:");
        TextField zipField = new TextField();
        zipField.setPromptText("Enter your zip code");

        Label phoneLabel = new Label("Phone Number:");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Enter your phone number");

        addressForm.add(nameLabel, 0, 0);
        addressForm.add(nameField, 1, 0);
        addressForm.add(addressLabel, 0, 1);
        addressForm.add(addressField, 1, 1);
        addressForm.add(cityLabel, 0, 2);
        addressForm.add(cityField, 1, 2);
        addressForm.add(zipLabel, 0, 3);
        addressForm.add(zipField, 1, 3);
        addressForm.add(phoneLabel, 0, 4);
        addressForm.add(phoneField, 1, 4);

        Button continueButton = new Button("Continue");
        continueButton.setOnAction(e -> {
            // Validate inputs
            if (nameField.getText().trim().isEmpty() || addressField.getText().trim().isEmpty() ||
                cityField.getText().trim().isEmpty() || zipField.getText().trim().isEmpty() ||
                phoneField.getText().trim().isEmpty()) {
                showAlert("Error", "Please fill in all the fields.");
            } else {
                shippingAddress = String.format("%s\n%s, %s\nZip: %s\nPhone: %s",
                        nameField.getText().trim(), addressField.getText().trim(),
                        cityField.getText().trim(), zipField.getText().trim(),
                        phoneField.getText().trim());
                showTotalPage(shippingWindow);
            }
        });

        Button backToCartButton = new Button("Back to Cart");
        backToCartButton.setOnAction(e -> {
            shippingWindow.close();
            if (cart != null) {
                cart.showCart(parentWindow); // Navigate back to the cart
            }
        });

        HBox buttons = new HBox(10, backToCartButton, continueButton);
        buttons.setStyle("-fx-alignment: center;");

        layout.getChildren().addAll(title, addressForm, buttons);

        Scene scene = new Scene(layout, 500, 400);
        shippingWindow.setScene(scene);
        shippingWindow.initOwner(parentWindow);
        shippingWindow.show();
    }


    public void showTotalPage(Stage parentWindow) {
        Stage totalWindow = new Stage();
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f8f9fa;");

        Label title = new Label("Order Summary");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        double totalBookPrice = booksToCheckout.stream().mapToDouble(Book::getSellPrice).sum();
        double tax = totalBookPrice * 0.1;
        double shippingFee = 4.99;
        double totalCost = totalBookPrice + tax + shippingFee;

        Label bookPriceLabel = new Label("Books: $" + String.format("%.2f", totalBookPrice));
        Label taxLabel = new Label("Tax (10%): $" + String.format("%.2f", tax));
        Label shippingLabel = new Label("Shipping Fee: $" + String.format("%.2f", shippingFee));
        Label totalLabel = new Label("Total: $" + String.format("%.2f", totalCost));
        totalLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Button confirmButton = new Button("Confirm Purchase");
        confirmButton.setOnAction(e -> showConfirmationPage(totalWindow));

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> showShippingPage(totalWindow, null)); // Back to Shipping Page

        HBox buttons = new HBox(10, backButton, confirmButton);
        buttons.setStyle("-fx-alignment: center;");

        layout.getChildren().addAll(title, bookPriceLabel, taxLabel, shippingLabel, totalLabel, buttons);

        Scene scene = new Scene(layout, 400, 300);
        totalWindow.setScene(scene);
        totalWindow.initOwner(parentWindow);
        totalWindow.show();
    }

    public void showConfirmationPage(Stage parentWindow) {
        Stage confirmationWindow = new Stage();
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f8f9fa;");

        Label message = new Label("Purchase Confirmed!");
        message.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: green;");

        Label shippingDetails = new Label("Your order will be shipped to:");
        Label addressLabel = new Label(shippingAddress);
        addressLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> confirmationWindow.close());

        layout.getChildren().addAll(message, shippingDetails, addressLabel, closeButton);

        Scene scene = new Scene(layout, 400, 300);
        confirmationWindow.setScene(scene);
        confirmationWindow.initOwner(parentWindow);
        confirmationWindow.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
