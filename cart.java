import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Cart extends Application {
    private int cartID;                    // Unique cart ID
    private double currentAmount;           // Total amount of items in the cart
    private Buyer buyer;                    // Buyer associated with this cart
    private ArrayList<Listing> listings;    // List of items in the cart

    private Label totalLabel;

    public Cart() {
        this.cartID = generateCartID();
        this.currentAmount = 0.0;
        this.listings = new ArrayList<>();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Cart");

        // Initialize the cart
        cart();

        // UI elements for adding items to the cart
        TextField itemNameField = new TextField();
        itemNameField.setPromptText("Item Name");

        TextField itemPriceField = new TextField();
        itemPriceField.setPromptText("Item Price");

        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantity");

        Button addItemButton = new Button("Add Item");
        addItemButton.setOnAction(e -> {
            try {
                String itemName = itemNameField.getText();
                double itemPrice = Double.parseDouble(itemPriceField.getText());
                int quantity = Integer.parseInt(quantityField.getText());

                Listing listing = new Listing(itemName, itemPrice);
                listings.add(listing);
                currentAmount += itemPrice * quantity;

                totalLabel.setText("Total Amount: $" + currentAmount);

                itemNameField.clear();
                itemPriceField.clear();
                quantityField.clear();
            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Please enter valid numbers for quantity.");
            }
        });

        totalLabel = new Label("Total Amount: $0.0");

        // Checkout button
        Button checkoutButton = new Button("Checkout");
        checkoutButton.setOnAction(e -> checkoutCart());

        // Add shipping details button
        Button addShippingButton = new Button("Add Shipping Details");
        addShippingButton.setOnAction(e -> addShippingDetails());

        // Add payment details button
        Button addPaymentButton = new Button("Add Payment Details");
        addPaymentButton.setOnAction(e -> addPaymentDetails());

        // Layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(itemNameField, itemPriceField, quantityField, addItemButton, totalLabel, checkoutButton, addShippingButton, addPaymentButton);

        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to initialize the cart
    public void cart() {
        System.out.println("Cart initialized with ID: " + cartID);
    }

    // Method to handle checkout process
    public void checkoutCart() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Checkout");
        alert.setHeaderText(null);
        alert.setContentText("Proceeding to checkout with total amount: $" + currentAmount);
        alert.showAndWait();
    }

    // Method to add shipping details
    public void addShippingDetails() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Shipping Details");
        dialog.setHeaderText("Enter Shipping Details");
        dialog.setContentText("Address:");

        dialog.showAndWait().ifPresent(shippingDetails -> {
            System.out.println("Shipping details added: " + shippingDetails);
            showAlert("Shipping Details", "Shipping details added: " + shippingDetails);
        });
    }

    // Method to add payment details
    public void addPaymentDetails() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Payment Details");
        dialog.setHeaderText("Enter Payment Details");
        dialog.setContentText("Card Number:");

        dialog.showAndWait().ifPresent(paymentDetails -> {
            System.out.println("Payment details added: " + paymentDetails);
            showAlert("Payment Details", "Payment details added: " + paymentDetails);
        });
    }

    // Helper method to generate a unique Cart ID
    private int generateCartID() {
        return (int) (Math.random() * 10000);
    }

    // Helper method to show alert messages
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
