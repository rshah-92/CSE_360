import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class login extends Application {
    private Stage window;
    private Scene loginScene, registerScene, forgotPasswordScene;
    private static final String CREDENTIALS_FILE = "src/login_information.txt"; // Path to the credentials file
    private static final String TEMPORARY_PASSWORD = "1234"; // Temporary password for reset

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;

        // Call method to create Login page
        loginScene = createLoginScene();

        // Call method to create Register page
        registerScene = createRegisterScene();

        // Call method to create Forgot Password page
        forgotPasswordScene = createForgotPasswordScene();

        window.setTitle("Sparky's Books");
        window.setScene(loginScene); // Show login scene at first
        window.show();
    }

    // Method to create the Login page
    private Scene createLoginScene() {
        Label titleLabel = new Label("Log in to Sparky's Books");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Text fields for ASU ID and password
        TextField asuIdField = new TextField();
        asuIdField.setPromptText("ASU ID");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        // Logo Placeholder (replace with actual image path)
        ImageView logoView = new ImageView(new Image(getClass().getResource("/logo.png").toExternalForm()));
        logoView.setFitHeight(100); // Adjust the size as needed
        logoView.setPreserveRatio(true);

        // Login button
        Button loginButton = new Button("Log in");
        loginButton.setOnAction(e -> handleLogin(asuIdField.getText(), passwordField.getText()));

        // Register button
        Button registerButton = new Button("Register Now");
        registerButton.setOnAction(e -> window.setScene(registerScene));

        // Forgot Password button
        Button forgotPasswordButton = new Button("Forgot Password");
        forgotPasswordButton.setOnAction(e -> window.setScene(forgotPasswordScene)); // Switch to forgot password page

        // Layout for login form
        VBox loginLayout = new VBox(10);
        loginLayout.getChildren().addAll(logoView, titleLabel, asuIdField, passwordField, loginButton, registerButton, forgotPasswordButton);
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.setPadding(new Insets(20));


        // Set background color to #F0E3C2 using inline CSS
        loginLayout.setStyle("-fx-background-color: #F0E3C2;");

        return new Scene(loginLayout, 600, 400);
    }

    // Method to create the Registration page
    private Scene createRegisterScene() {
        Label titleLabel = new Label("Create an account");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First name");
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last name");
        TextField asuIdField = new TextField();
        asuIdField.setPromptText("ASU ID");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm password");

        // Logo Placeholder (replace with actual image path)
        ImageView logoView = new ImageView(new Image(getClass().getResource("/logo.png").toExternalForm()));
        logoView.setFitHeight(100); // Adjust the size as needed
        logoView.setPreserveRatio(true);

        // Register button
        Button createAccountButton = new Button("Create Account");
        createAccountButton.setOnAction(e -> handleRegister(firstNameField.getText(), lastNameField.getText(),
                asuIdField.getText(), passwordField.getText(), confirmPasswordField.getText()));

        // Layout for register form
        VBox registerLayout = new VBox(10);
        registerLayout.getChildren().addAll(logoView, titleLabel, firstNameField, lastNameField, asuIdField, passwordField, confirmPasswordField, createAccountButton);
        registerLayout.setAlignment(Pos.CENTER);
        registerLayout.setPadding(new Insets(20));

        // Set background color to #F0E3C2 using inline CSS
        registerLayout.setStyle("-fx-background-color: #F0E3C2;");

        return new Scene(registerLayout, 600, 400);
    }

    // Method to create Forgot Password page
    private Scene createForgotPasswordScene() {
        Label titleLabel = new Label("Forgot Password");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        TextField tempPasswordField = new TextField();
        tempPasswordField.setPromptText("Temporary Password");
        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("New password");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm new password");

        // Update Password button
        Button updatePasswordButton = new Button("Update Password");
        updatePasswordButton.setOnAction(e -> handleForgotPassword(tempPasswordField.getText(), newPasswordField.getText(), confirmPasswordField.getText()));

        // Layout for forgot password form
        VBox forgotPasswordLayout = new VBox(10);
        forgotPasswordLayout.getChildren().addAll(titleLabel, tempPasswordField, newPasswordField, confirmPasswordField, updatePasswordButton);
        forgotPasswordLayout.setAlignment(Pos.CENTER);
        forgotPasswordLayout.setPadding(new Insets(20));

        // Set background color to #F0E3C2 using inline CSS
        forgotPasswordLayout.setStyle("-fx-background-color: #F0E3C2;");

        return new Scene(forgotPasswordLayout, 600, 400);
    }

    // Method to handle login logic by checking the credentials from the file
    private void handleLogin(String asuId, String password) {
        Map<String, String[]> credentials = loadCredentialsFromFile();
        if (asuId.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "ASU ID or password cannot be empty.");
        } else if (credentials.containsKey(asuId) && credentials.get(asuId)[2].equals(password)) {
            String firstName = credentials.get(asuId)[0];
            String lastName = credentials.get(asuId)[1];
            showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + firstName + " " + lastName + "!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid ASU ID or password.");
        }
    }

    // Method to handle registration logic and save the user information
    private void handleRegister(String firstName, String lastName, String asuId, String password, String confirmPassword) {
        // Basic validation
        if (asuId.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "All fields must be filled.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "Passwords do not match.");
            return;
        }

        // Save credentials to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CREDENTIALS_FILE, true))) {
            writer.write(asuId + "," + firstName + "," + lastName + "," + password);
            writer.newLine();
            showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "Account created for " + firstName + " " + lastName + ".");
            window.setScene(loginScene); // Redirect to login page after successful registration
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not save credentials.");
        }
    }

    // Method to handle password reset logic
    private void handleForgotPassword(String tempPassword, String newPassword, String confirmPassword) {
        if (!tempPassword.equals(TEMPORARY_PASSWORD)) {
            showAlert(Alert.AlertType.ERROR, "Reset Failed", "Incorrect temporary password.");
            return;
        }

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Reset Failed", "All fields must be filled.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Reset Failed", "Passwords do not match.");
            return;
        }

        showAlert(Alert.AlertType.INFORMATION, "Reset Successful", "Your password has been updated.");
        window.setScene(loginScene); // Redirect back to login page after reset
    }

    // Method to load credentials from the file into a HashMap
    private Map<String, String[]> loadCredentialsFromFile() {
        Map<String, String[]> credentials = new HashMap<>();
        File file = new File(CREDENTIALS_FILE);

        // Check if the credentials file exists, and if so, read it
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 4) {
                        credentials.put(parts[0], new String[]{parts[1], parts[2], parts[3]}); // Store ASU ID as key and {firstName, lastName, password} as value
                    }
                }
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error reading credentials file.");
            }
        }

        return credentials;
    }

    // Utility method to show alerts
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
