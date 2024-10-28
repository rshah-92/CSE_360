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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class login extends Application {
    private Stage window;
    private Scene loginScene, registerScene, forgotPasswordScene;
    private static final String CREDENTIALS_FILE = "login_information.txt"; // External file path for updates
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

        // Text fields for Email ID and password
        TextField emailField = new TextField();
        emailField.setPromptText("Email ID");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        // Logo Placeholder (replace with actual image path)
        ImageView logoView = new ImageView(new Image(getClass().getResource("/logo.png").toExternalForm()));
        logoView.setFitHeight(100); // Adjust the size as needed
        logoView.setPreserveRatio(true);

        // Log in button
        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            handleLogin(emailField.getText(), passwordField.getText());
            clearLoginFields(emailField, passwordField);
        });

        // Register Now button
        Button registerButton = new Button("Register Now");
        registerButton.setOnAction(e -> {
            window.setScene(registerScene);
            clearLoginFields(emailField, passwordField);
        });

        // Align Login and Register buttons horizontally
        HBox buttonRow = new HBox(10); // Spacing between buttons
        buttonRow.getChildren().addAll(loginButton, registerButton);
        buttonRow.setAlignment(Pos.CENTER);

        // Forgot Password hyperlink (aligned left below the buttons)
        Hyperlink forgotPasswordLink = new Hyperlink("Forgot Password?");
        forgotPasswordLink.setOnAction(e -> {
            window.setScene(forgotPasswordScene);
            clearLoginFields(emailField, passwordField);
        });
        forgotPasswordLink.setAlignment(Pos.CENTER_LEFT);

        // Layout for login form
        VBox loginLayout = new VBox(10); // Spacing between form elements
        loginLayout.getChildren().addAll(logoView, titleLabel, emailField, passwordField, buttonRow, forgotPasswordLink);
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
        TextField emailField = new TextField();
        emailField.setPromptText("Email ID");
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
        createAccountButton.setOnAction(e -> {
            handleRegister(firstNameField.getText(), lastNameField.getText(),
                    emailField.getText(), passwordField.getText(), confirmPasswordField.getText());
            clearRegisterFields(firstNameField, lastNameField, emailField, passwordField, confirmPasswordField);
        });

        // Layout for register form
        VBox registerLayout = new VBox(10);
        registerLayout.getChildren().addAll(logoView, titleLabel, firstNameField, lastNameField, emailField, passwordField, confirmPasswordField, createAccountButton);
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

        TextField emailField = new TextField();
        emailField.setPromptText("Email ID");
        TextField tempPasswordField = new TextField();
        tempPasswordField.setPromptText("Temporary Password (1234)");
        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("New password (at least 8 characters)");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm new password");

        // Update Password button
        Button updatePasswordButton = new Button("Update Password");
        updatePasswordButton.setOnAction(e -> {
            handleForgotPassword(emailField.getText(), tempPasswordField.getText(), newPasswordField.getText(), confirmPasswordField.getText());
            clearForgotPasswordFields(emailField, tempPasswordField, newPasswordField, confirmPasswordField);
        });

        // Layout for forgot password form
        VBox forgotPasswordLayout = new VBox(10);
        forgotPasswordLayout.getChildren().addAll(titleLabel, emailField, tempPasswordField, newPasswordField, confirmPasswordField, updatePasswordButton);
        forgotPasswordLayout.setAlignment(Pos.CENTER);
        forgotPasswordLayout.setPadding(new Insets(20));

        // Set background color to #F0E3C2 using inline CSS
        forgotPasswordLayout.setStyle("-fx-background-color: #F0E3C2;");

        return new Scene(forgotPasswordLayout, 600, 400);
    }

    // Method to handle login logic by checking the credentials from the file
    private void handleLogin(String email, String password) {
        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid Email format.");
            return;
        }
        Map<String, String[]> credentials = loadCredentialsFromFile();
        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Email or password cannot be empty.");
        } else if (credentials.containsKey(email) && credentials.get(email)[2].equals(password)) {
            String firstName = credentials.get(email)[0];
            String lastName = credentials.get(email)[1];
            showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + firstName + " " + lastName + "!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid Email or password.");
        }
    }

    // Method to handle registration logic and save the user information
    private void handleRegister(String firstName, String lastName, String email, String password, String confirmPassword) {
        // Basic validation
        if (email.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "All fields must be filled.");
            return;
        }

        Map<String, String[]> credentials = loadCredentialsFromFile();

        if (credentials.containsKey(email)) {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "An account already exists with this email.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "Passwords do not match.");
            return;
        }

        if (password.length() < 8) {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "Password must be at least 8 characters long.");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "Invalid Email format.");
            return;
        }

        // Save credentials to an external file (not in src)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CREDENTIALS_FILE, true))) {
            writer.write(email + "," + firstName + "," + lastName + "," + password);
            writer.newLine();
            showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "Account created for " + firstName + " " + lastName + ".");
            window.setScene(loginScene); // Redirect to login page after successful registration
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not save credentials.");
        }
    }

    // Method to handle password reset logic
    private void handleForgotPassword(String email, String tempPassword, String newPassword, String confirmPassword) {
        Map<String, String[]> credentials = loadCredentialsFromFile();

        // Validate inputs
        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Reset Failed", "Invalid Email format.");
            return;
        }

        if (!tempPassword.equals(TEMPORARY_PASSWORD)) {
            showAlert(Alert.AlertType.ERROR, "Reset Failed", "Incorrect temporary password.");
            return;
        }

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Reset Failed", "All fields must be filled.");
            return;
        }

        if (newPassword.length() < 8) {
            showAlert(Alert.AlertType.ERROR, "Reset Failed", "New password must be at least 8 characters long.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Reset Failed", "Passwords do not match.");
            return;
        }

        // Check if Email ID exists and update the password
        if (credentials.containsKey(email)) {
            String firstName = credentials.get(email)[0];
            String lastName = credentials.get(email)[1];

            // Update the password in the credentials map
            credentials.get(email)[2] = newPassword;
            saveCredentialsToFile(credentials);

            showAlert(Alert.AlertType.INFORMATION, "Reset Successful", "Password has been updated for " + firstName + " " + lastName + ".");
            window.setScene(loginScene); // Redirect back to login page after reset
        } else {
            showAlert(Alert.AlertType.ERROR, "Reset Failed", "Email not found.");
        }
    }

    // Method to load credentials from the file into a HashMap
    private Map<String, String[]> loadCredentialsFromFile() {
        Map<String, String[]> credentials = new HashMap<>();
        File file = new File(CREDENTIALS_FILE); // External file path

        // Check if the external credentials file exists, if not, initialize it by reading from src
        if (!file.exists()) {
            initializeExternalCredentialsFile();
        }

        // Read the credentials from the external file
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    credentials.put(parts[0], new String[]{parts[1], parts[2], parts[3]}); // Store Email as key and {firstName, lastName, password} as value
                }
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error reading credentials file.");
        }

        return credentials;
    }

    // Initialize the external file by copying data from the internal file in the src folder
    private void initializeExternalCredentialsFile() {
        try (InputStream is = getClass().getResourceAsStream("/login_information.txt");
             BufferedWriter writer = new BufferedWriter(new FileWriter(CREDENTIALS_FILE))) {

            if (is != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = reader.readLine()) != null) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error initializing external credentials file.");
        }
    }

    // Method to save updated credentials back to the file
    private void saveCredentialsToFile(Map<String, String[]> credentials) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CREDENTIALS_FILE))) {
            for (Map.Entry<String, String[]> entry : credentials.entrySet()) {
                String[] details = entry.getValue();
                writer.write(entry.getKey() + "," + details[0] + "," + details[1] + "," + details[2]);
                writer.newLine();
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error saving credentials to file.");
        }
    }

    // Method to validate email format using regex
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        return pat.matcher(email).matches();
    }

    // Utility method to show alerts
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Clear all login fields
    private void clearLoginFields(TextField emailField, PasswordField passwordField) {
        emailField.clear();
        passwordField.clear();
    }

    // Clear all register fields
    private void clearRegisterFields(TextField firstNameField, TextField lastNameField, TextField emailField, PasswordField passwordField, PasswordField confirmPasswordField) {
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
    }

    // Clear all forgot password fields
    private void clearForgotPasswordFields(TextField emailField, TextField tempPasswordField, PasswordField newPasswordField, PasswordField confirmPasswordField) {
        emailField.clear();
        tempPasswordField.clear();
        newPasswordField.clear();
        confirmPasswordField.clear();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
