import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import javafx.scene.text.*;
import java.io.*;
import java.time.LocalDate;
import java.util.Random;

public class HeartHealthSystem extends Application {

    @Override
    public void start(Stage primaryStage) {
        Button receptionistButton = new Button("Patient Intake");
        Button technicianButton = new Button("CT Scan Tech View");
        Button patientButton = new Button("Patient View");

        receptionistButton.setPrefWidth(200);
        technicianButton.setPrefWidth(200);
        patientButton.setPrefWidth(200);

        VBox mainLayout = new VBox(20, new Label("Welcome to Heart Health Imaging and Recording System"), receptionistButton, technicianButton, patientButton);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(20));

        receptionistButton.setOnAction(e -> showReceptionistView(primaryStage));
        technicianButton.setOnAction(e -> showTechnicianView(primaryStage));
        patientButton.setOnAction(e -> showPatientView(primaryStage));

        Scene mainScene = new Scene(mainLayout, 400, 300);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Heart Health Imaging System");
        primaryStage.show();
    }

    private void showReceptionistView(Stage stage) {
        GridPane grid = createGridPane();

        Label titleLabel = new Label("Patient Intake Form");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        TextField firstNameField = createTextField("First Name:");
        TextField lastNameField = createTextField("Last Name:");
        TextField emailField = createTextField("Email:");
        TextField phoneField = createTextField("Phone Number:");
        TextField historyField = createTextField("Health History:");
        TextField insuranceField = createTextField("Insurance ID:");
        DatePicker examDatePicker = new DatePicker();
        examDatePicker.setPromptText("Select Exam Date");

        Button saveButton = createSaveButton();
        Button backButton = createBackButton(stage);

        saveButton.setOnAction(e -> {
            // Validate fields
            if (isAnyFieldEmpty(firstNameField, lastNameField, emailField, phoneField, historyField, insuranceField)) {
                showAlert(Alert.AlertType.ERROR, "Error", "All fields must be filled!");
                return;
            }

            // Validate email
            if (!isValidEmail(emailField.getText())) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid email address!");
                return;
            }

            // Validate phone number
            if (!isValidPhoneNumber(phoneField.getText())) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid 10-digit phone number!");
                return;
            }

            // Validate exam date
            if (examDatePicker.getValue() == null || examDatePicker.getValue().isBefore(LocalDate.now())) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please select a valid future exam date!");
                return;
            }

            // Save patient information
            try {
                String id = generatePatientID();
                FileWriter writer = new FileWriter(id + "_PatientInfo.txt");
                writer.write("First Name: " + firstNameField.getText() + "\n");
                writer.write("Last Name: " + lastNameField.getText() + "\n");
                writer.write("Email: " + emailField.getText() + "\n");
                writer.write("Phone: " + phoneField.getText() + "\n");
                writer.write("History: " + historyField.getText() + "\n");
                writer.write("Insurance ID: " + insuranceField.getText() + "\n");
                writer.write("Exam Date: " + examDatePicker.getValue() + "\n");
                writer.close();
                showAlert(Alert.AlertType.INFORMATION, "Saved Successfully", "Patient ID: " + id);
            } catch (IOException ex) {
                ex.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Could not save the data.");
            }
        });

        grid.add(titleLabel, 0, 0, 2, 1);
        grid.addRow(1, new Label("First Name:"), firstNameField);
        grid.addRow(2, new Label("Last Name:"), lastNameField);
        grid.addRow(3, new Label("Email:"), emailField);
        grid.addRow(4, new Label("Phone Number:"), phoneField);
        grid.addRow(5, new Label("Health History:"), historyField);
        grid.addRow(6, new Label("Insurance ID:"), insuranceField);
        grid.addRow(7, new Label("Exam Date:"), examDatePicker);
        grid.addRow(8, saveButton, backButton);

        Scene scene = new Scene(grid, 400, 450);
        stage.setScene(scene);
    }


    private void showTechnicianView(Stage stage) {
        GridPane grid = createGridPane();

        Label titleLabel = new Label("CT Scan Technician View");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        TextField patientIDField = createTextField("Patient ID:");
        TextField totalScoreField = createTextField("The total Agatston CAC score:");
        TextField lmField = createTextField("LM:");
        TextField ladField = createTextField("LAD:");
        TextField lcxField = createTextField("LCX:");
        TextField rcaField = createTextField("RCA:");
        TextField pdaField = createTextField("PDA:");

        Button saveButton = createSaveButton();
        Button backButton = createBackButton(stage);

        saveButton.setOnAction(e -> {
            String patientID = patientIDField.getText();
            if (patientID.isEmpty() || totalScoreField.getText().isEmpty() || lmField.getText().isEmpty() ||
                ladField.getText().isEmpty() || lcxField.getText().isEmpty() || rcaField.getText().isEmpty() || pdaField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "All fields must be filled!");
                return;
            }
            File file = new File(patientID + "_PatientInfo.txt");
            if (!file.exists()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Patient ID not found!");
                return;
            }
            try {
                FileWriter writer = new FileWriter(patientID + "CTResults.txt");
                writer.write("Total Score: " + totalScoreField.getText() + "\n");
                writer.write("LM: " + lmField.getText() + "\n");
                writer.write("LAD: " + ladField.getText() + "\n");
                writer.write("LCX: " + lcxField.getText() + "\n");
                writer.write("RCA: " + rcaField.getText() + "\n");
                writer.write("PDA: " + pdaField.getText() + "\n");
                writer.close();
                showAlert(Alert.AlertType.INFORMATION, "Saved Successfully", "CT Results saved!");
            } catch (IOException ex) {
                ex.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Could not save the data.");
            }
        });

        grid.add(titleLabel, 0, 0, 2, 1);
        grid.addRow(1, new Label("Patient ID:"), patientIDField);
        grid.addRow(2, new Label("The total Agatston CAC score:"), totalScoreField);
        grid.addRow(3, new Label("LM:"), lmField);
        grid.addRow(4, new Label("LAD:"), ladField);
        grid.addRow(5, new Label("LCX:"), lcxField);
        grid.addRow(6, new Label("RCA:"), rcaField);
        grid.addRow(7, new Label("PDA:"), pdaField);
        grid.addRow(8, saveButton, backButton);

        Scene scene = new Scene(grid, 400, 450);
        stage.setScene(scene);
    }

    private void showPatientView(Stage stage) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        grid.setAlignment(Pos.CENTER);

        // Title Label
        Label titleLabel = new Label("Patient View");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        grid.add(titleLabel, 0, 0, 2, 1); // Spanning across two columns

        // Patient ID Input
        Label patientIDLabel = new Label("Enter Patient ID:");
        TextField patientIDField = new TextField();
        Button viewButton = new Button("View Results");
        grid.addRow(1, patientIDLabel, patientIDField);
        grid.add(viewButton, 1, 2); // Place View button below the Patient ID field

        // Labels for Patient Name and Total Score (Initially Hidden)
        Label helloLabel = new Label();
        helloLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        helloLabel.setVisible(false);

        Label totalScoreLabel = new Label("The total Agatston CAC score:");
        TextField totalScoreField = createResultField();
        totalScoreLabel.setVisible(false);
        totalScoreField.setVisible(false);

        grid.add(helloLabel, 0, 3, 2, 1); // Spanning across two columns
        grid.add(totalScoreLabel, 0, 4);
        grid.add(totalScoreField, 1, 4);

        // Vessel-Level CAC Scores (Initially Hidden)
        Label lmLabel = new Label("LM:");
        Label ladLabel = new Label("LAD:");
        Label lcxLabel = new Label("LCX:");
        Label rcaLabel = new Label("RCA:");
        Label pdaLabel = new Label("PDA:");

        TextField lmField = createResultField();
        TextField ladField = createResultField();
        TextField lcxField = createResultField();
        TextField rcaField = createResultField();
        TextField pdaField = createResultField();

        lmLabel.setVisible(false);
        ladLabel.setVisible(false);
        lcxLabel.setVisible(false);
        rcaLabel.setVisible(false);
        pdaLabel.setVisible(false);

        lmField.setVisible(false);
        ladField.setVisible(false);
        lcxField.setVisible(false);
        rcaField.setVisible(false);
        pdaField.setVisible(false);

        grid.addRow(5, lmLabel, lmField);
        grid.addRow(6, ladLabel, ladField);
        grid.addRow(7, lcxLabel, lcxField);
        grid.addRow(8, rcaLabel, rcaField);
        grid.addRow(9, pdaLabel, pdaField);

        // Back Button
        Button backButton = new Button("Back");
        grid.add(backButton, 1, 10); // Place Back button at the bottom
        backButton.setOnAction(e -> start(stage));

        // View Results Button Logic
        viewButton.setOnAction(e -> {
            String patientID = patientIDField.getText().trim();
            File infoFile = new File(patientID + "_PatientInfo.txt");
            File resultFile = new File(patientID + "CTResults.txt");

            if (!infoFile.exists()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid Patient ID!");
                return;
            }

            if (!resultFile.exists()) {
                showAlert(Alert.AlertType.INFORMATION, "No Results", "CT Results are not available yet.");
                return;
            }

            try {
                // Read Patient Info
                BufferedReader infoReader = new BufferedReader(new FileReader(infoFile));
                String patientName = "Patient"; // Default if name is not found
                String line;
                while ((line = infoReader.readLine()) != null) {
                    if (line.startsWith("First Name:")) {
                        patientName = line.split(":")[1].trim();
                    }
                }
                infoReader.close();

                // Update UI with patient name
                helloLabel.setText("Hello " + patientName);
                helloLabel.setVisible(true);

                // Read CT Results
                BufferedReader resultReader = new BufferedReader(new FileReader(resultFile));
                while ((line = resultReader.readLine()) != null) {
                    if (line.startsWith("Total Score:")) {
                        totalScoreField.setText(line.split(":")[1].trim());
                        totalScoreLabel.setVisible(true);
                        totalScoreField.setVisible(true);
                    } else if (line.startsWith("LM:")) {
                        lmField.setText(line.split(":")[1].trim());
                        lmLabel.setVisible(true);
                        lmField.setVisible(true);
                    } else if (line.startsWith("LAD:")) {
                        ladField.setText(line.split(":")[1].trim());
                        ladLabel.setVisible(true);
                        ladField.setVisible(true);
                    } else if (line.startsWith("LCX:")) {
                        lcxField.setText(line.split(":")[1].trim());
                        lcxLabel.setVisible(true);
                        lcxField.setVisible(true);
                    } else if (line.startsWith("RCA:")) {
                        rcaField.setText(line.split(":")[1].trim());
                        rcaLabel.setVisible(true);
                        rcaField.setVisible(true);
                    } else if (line.startsWith("PDA:")) {
                        pdaField.setText(line.split(":")[1].trim());
                        pdaLabel.setVisible(true);
                        pdaField.setVisible(true);
                    }
                }
                resultReader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Could not read patient results.");
            }
        });

        // Set the Scene
        Scene scene = new Scene(grid, 500, 600);
        stage.setScene(scene);
    }



    private GridPane createGridPane() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        return grid;
    }

    private TextField createTextField(String prompt) {
        TextField textField = new TextField();
        textField.setPromptText(prompt);
        return textField;
    }

    private Button createSaveButton() {
        Button button = new Button("Save");
        button.setPrefWidth(100);
        return button;
    }

    private Button createBackButton(Stage stage) {
        Button backButton = new Button("Back");
        backButton.setPrefWidth(100);
        backButton.setOnAction(e -> start(stage)); // Go back to Main Menu
        return backButton;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String generatePatientID() {
        Random random = new Random();
        return String.format("%05d", random.nextInt(10000));
    }
    private boolean isAnyFieldEmpty(TextField... fields) {
        for (TextField field : fields) {
            if (field.getText().trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }
    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("\\d{10}");
    }

    
    private boolean isValidEmail(String email) {
        // Regular expression for valid email
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        return email.matches(emailRegex);
    }
    private TextField createResultField() {
        TextField field = new TextField();
        field.setEditable(false);
        field.setPrefWidth(200);
        return field;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
