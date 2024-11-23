import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

// login class is the main class and always the first page, redirect to other pages from here
public class login extends Application {
    private Stage window;
    private Scene loginScene, registerScene, forgotPasswordScene;
    private double initialX;
    private static final String CREDENTIALS_FILE = "login_information.txt";
    private static final String TEMPORARY_PASSWORD = "1234";

    Map<String, User> users = loadUsers();

    // load users from the credentials file initially to a map of email -> User
    public Map<String, User> loadUsers() {
        Map<String,User> users = new HashMap<>();
        File file = new File(CREDENTIALS_FILE);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    int userID = Integer.parseInt(parts[0].trim());
                    String email = parts[1].trim();
                    String firstName = parts[2].trim();
                    String lastName = parts[3].trim();
                    String password = parts[4].trim();
                    int role = parts.length > 5 ? Integer.parseInt(parts[5].trim()) : -1;

                    users.put(email, new User(userID, email, password, role));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading credentials file: " + e.getMessage());
        }
        return users;
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;

        // create the login view
        loginScene = createLoginScene();

        // create the register view
        registerScene = createRegisterScene();

        // create the forgot password view
        forgotPasswordScene = createForgotPasswordScene();

        window.setTitle("Sparky's Books");

        setInitialFocus(loginScene);
        setInitialFocus(registerScene);
        setInitialFocus(forgotPasswordScene);

        window.setScene(loginScene);
        window.show();
    }

    private void setInitialFocus(Scene scene) {
        scene.setOnMouseEntered(e -> {
            Pane rootPane = (Pane) scene.getRoot();
            rootPane.requestFocus();
        });
    }

    // swipe to go back to the previous page
    private void addSwipeNavigation(Scene scene, Runnable swipeLeftAction, Runnable swipeRightAction) {
        scene.setOnMousePressed(e -> initialX = e.getSceneX());
        scene.setOnMouseReleased(e -> {
            double finalX = e.getSceneX();
            if (initialX - finalX > 100) { // go back
                swipeLeftAction.run();
            } else if (finalX - initialX > 100) { // go forward
                swipeRightAction.run();
            }
        });
    }

    // create login view
    private Scene createLoginScene() {
        Label titleLabel = new Label("Log in to Sparky's Books");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        TextField emailField = new TextField();
        emailField.setPromptText("Email ID");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

       ImageView logoView = new ImageView(new Image(getClass().getResource("/logo.png").toExternalForm()));
       logoView.setFitHeight(100);
       logoView.setPreserveRatio(true);

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            handleLogin(emailField.getText(), passwordField.getText());
            clearLoginFields(emailField, passwordField);
        });

        Button registerButton = new Button("Register Now");
        registerButton.setOnAction(e -> {
            window.setScene(registerScene);
            clearLoginFields(emailField, passwordField);
        });

        HBox buttonRow = new HBox(10);
        buttonRow.getChildren().addAll(loginButton, registerButton);
        buttonRow.setAlignment(Pos.CENTER);

        Hyperlink forgotPasswordLink = new Hyperlink("Forgot Password?");
        forgotPasswordLink.setOnAction(e -> {
            window.setScene(forgotPasswordScene);
            clearLoginFields(emailField, passwordField);
        });
        forgotPasswordLink.setAlignment(Pos.CENTER_LEFT);

        VBox loginLayout = new VBox(10);
        loginLayout.getChildren().addAll(titleLabel, emailField, passwordField, buttonRow, forgotPasswordLink);
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.setPadding(new Insets(20));
        loginLayout.setStyle("-fx-background-color: #F0E3C2;");

        Pane placeholderPane = new Pane();
        loginLayout.getChildren().add(placeholderPane);

        Scene loginScene = new Scene(loginLayout, 600, 400);

        addSwipeNavigation(loginScene, () -> window.setScene(registerScene), () -> {});

        return loginScene;
    }

    // create the register now view
    private Scene createRegisterScene() {
        Label titleLabel = new Label("Create an Account");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name");

        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");

        TextField emailField = new TextField();
        emailField.setPromptText("Email ID");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");

        // toggle radio buttons for the role selection
        Label roleLabel = new Label("Select Account Type:");
        RadioButton buyerButton = new RadioButton("Buyer");
        RadioButton sellerButton = new RadioButton("Seller");
        ToggleGroup roleGroup = new ToggleGroup();
        buyerButton.setToggleGroup(roleGroup);
        sellerButton.setToggleGroup(roleGroup);

        HBox roleLayout = new HBox(10);
        roleLayout.getChildren().addAll(roleLabel, buyerButton, sellerButton);
        roleLayout.setAlignment(Pos.CENTER);

       ImageView logoView = new ImageView(new Image(getClass().getResource("/logo.png").toExternalForm()));
       logoView.setFitHeight(100);
       logoView.setPreserveRatio(true);

        Button createAccountButton = new Button("Create Account");
        createAccountButton.setOnAction(e -> {
            RadioButton selectedRole = (RadioButton) roleGroup.getSelectedToggle();
            if (selectedRole == null) {
                showAlert(Alert.AlertType.ERROR, "Registration Failed", "Please select an account type.");
                return;
            }

            String role = selectedRole.getText();
            handleRegister(
                    firstNameField.getText(),
                    lastNameField.getText(),
                    emailField.getText(),
                    passwordField.getText(),
                    confirmPasswordField.getText(),
                    role
            );
//            clearRegisterFields(firstNameField, lastNameField, emailField, passwordField, confirmPasswordField, roleGroup);
        });

        Button backButton = new Button("Back to Login");
        backButton.setOnAction(e -> {
            clearRegisterFields(firstNameField, lastNameField, emailField, passwordField, confirmPasswordField, roleGroup);
            window.setScene(loginScene);
        });

        VBox registerLayout = new VBox(10);
        registerLayout.getChildren().addAll(
                titleLabel,
                firstNameField,
                lastNameField,
                emailField,
                passwordField,
                confirmPasswordField,
                roleLayout,
                createAccountButton,
                backButton
        );
        registerLayout.setAlignment(Pos.CENTER);
        registerLayout.setPadding(new Insets(20));
        registerLayout.setStyle("-fx-background-color: #F0E3C2;");

        Scene registerScene = new Scene(registerLayout, 600, 400);

        return registerScene;
    }

    // create the forgot password view
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

        Button updatePasswordButton = new Button("Update Password");
        updatePasswordButton.setOnAction(e -> {
            handleForgotPassword(emailField.getText(), tempPasswordField.getText(), newPasswordField.getText(), confirmPasswordField.getText());
            clearForgotPasswordFields(emailField, tempPasswordField, newPasswordField, confirmPasswordField);
        });

        VBox forgotPasswordLayout = new VBox(10);
        forgotPasswordLayout.getChildren().addAll(titleLabel, emailField, tempPasswordField, newPasswordField, confirmPasswordField, updatePasswordButton);
        forgotPasswordLayout.setAlignment(Pos.CENTER);
        forgotPasswordLayout.setPadding(new Insets(20));
        forgotPasswordLayout.setStyle("-fx-background-color: #F0E3C2;");

        Pane placeholderPane = new Pane();
        forgotPasswordLayout.getChildren().add(placeholderPane);

        Scene forgotPasswordScene = new Scene(forgotPasswordLayout, 600, 400);

        addSwipeNavigation(forgotPasswordScene, () -> {}, () -> window.setScene(registerScene));

        return forgotPasswordScene;
    }

    // validate the login info user inputs using the credentials file
    private void handleLogin(String email, String password) {
        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid Email format.");
            return;
        }

        Map<String, String[]> credentials = loadCredentialsFromFile();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Email or password cannot be empty.");
            return;
        }

        if (credentials.containsKey(email)) {
            String[] userDetails = credentials.get(email);

            if (userDetails.length < 5) {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid credentials format.");
                return;
            }

            // validate password
            if (userDetails[3].equals(password)) {
                String role = userDetails[4];
                if (role.equals("1")) {
                    Cart cart = new Cart();
                    BuyerDashboard buyersDashboard = new BuyerDashboard(window, cart);
                    buyersDashboard.showDashboard();
                } else if (role.equals("2")) {
                    SellerDashboard sellerDashboard = new SellerDashboard(window);
                    sellerDashboard.showDashboard();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Login Failed", "Unknown role. Please contact support.");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid Email or password.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "No account found with this email.");
        }
    }

    // add user to details to credentials file and create User object
    private void handleRegister(String firstName, String lastName, String email, String password, String confirmPassword, String role) {
        if (email.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || role == null) {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "All fields must be filled, and an account type must be selected.");
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
        int r = (role.equals("Buyer") ? 1 : 2);
        User u = new User(email, password, r);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CREDENTIALS_FILE, true))) {
            writer.write(u.getUserID() + "," + email + "," + firstName + "," + lastName + "," + password + "," + r);
            writer.newLine();
            showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "Account created for " + firstName + " " + lastName + " as a " + role + ".");
            window.setScene(loginScene);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not save credentials.");
        }
    }

    // handles forgot password implementation
    private void handleForgotPassword(String email, String tempPassword, String newPassword, String confirmPassword) {
        Map<String, String[]> credentials = loadCredentialsFromFile();

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

        if (credentials.containsKey(email)) {
            users.get(email).setPassword(newPassword);

            String firstName = credentials.get(email)[1];
            String lastName = credentials.get(email)[2];

            credentials.get(email)[3] = newPassword;
            saveCredentialsToFile(credentials);

            showAlert(Alert.AlertType.INFORMATION, "Reset Successful", "Password has been updated for " + firstName + " " + lastName + ".");
            window.setScene(loginScene);


        } else {
            showAlert(Alert.AlertType.ERROR, "Reset Failed", "Email not found.");
        }
    }

    // load credentials from the credentials file to a hashmap that holds user data
    private Map<String, String[]> loadCredentialsFromFile() {
        Map<String, String[]> credentials = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(CREDENTIALS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                String[] parts = line.split(",");
                if (parts.length == 6) {
                    credentials.put(parts[1].trim(), new String[]{
                            parts[0].trim(),
                            parts[2].trim(),
                            parts[3].trim(),
                            parts[4].trim(),
                            parts[5].trim()
                    });
                } else {
                    System.err.println("Malformed line in credentials file: " + line);
                }
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load credentials file.");
        }

        return credentials;
    }

    // saves any updated credentials back to the file
    private void saveCredentialsToFile(Map<String, String[]> credentials) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CREDENTIALS_FILE))) {
            for (Map.Entry<String, String[]> user : credentials.entrySet()) {
                String[] details = user.getValue();
                // id + email + fn + ln + pass + role
                writer.write(details[0] + "," + user.getKey() + "," + details[1] + "," + details[2] + "," + details[3] + "," + details[4]);
                writer.newLine();
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error saving credentials to file.");
        }
    }

    // validate email input
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        return pat.matcher(email).matches();
    }

    // displays an alert
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // clear login fields
    private void clearLoginFields(TextField emailField, PasswordField passwordField) {
        emailField.clear();
        passwordField.clear();
    }

    // clear register now fields
    private void clearRegisterFields(TextField firstNameField, TextField lastNameField, TextField emailField, PasswordField passwordField, PasswordField confirmPasswordField, ToggleGroup roleGroup) {
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        roleGroup.selectToggle(null);
    }

    // clear forgot password fields
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
