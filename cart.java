import javafx.scene.control.TextField;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.application.Application;

public class Cart extends Application {
    private int cartID;                    // Unique cart ID
    private double currentAmount;           // Total amount of items in the cart
    //private Buyer buyer;                    // Buyer associated with this cart
    private ArrayList<Book> listings;    // List of items in the cart

    private Label totalLabel;

    public Cart() {
        this.cartID = generateCartID();
        this.currentAmount = 0.0;
        this.listings = new ArrayList<Book>();
    }


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

        TextField categoryField = new TextField();
        categoryField.setPromptText("Category");

        TextField authorField = new TextField();
        authorField.setPromptText("Author");

        TextField conditionField = new TextField();
        conditionField.setPromptText("Condition");

        // Button to add items to the cart
        Button addItemButton = new Button("Add Item");
        addItemButton.setOnAction(e -> {
            try {
                // Get input values from the fields
            	int quantity = Integer.parseInt(quantityField.getText()); // User-provided quantity

                // Define default values for the Book
                int bookID = listings.size() + 1; // Automatically generate unique book ID
                double originalPrice = 20.00; // Default price for the book
                String category = "Default Category"; // Default category
                String author = "Default Author"; // Default author
                String title = "Default Title"; // Default title
                String condition = "New"; // Default condition
                int status = 1; // Default status
                int sellerID = 0; // Default seller ID

                // Create the Book object with default values
                Book book = new Book(bookID, originalPrice, category, author, title, status, sellerID, condition);

                listings.add(book);
                currentAmount += originalPrice * quantity;

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
    public static void main(String[] args) {
    	launch(args);
    }
    
    }
