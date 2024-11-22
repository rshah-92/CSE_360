import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;


public class ListBookView extends Application {

    private List<String> books = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        // Create the List a Book page
        Scene listBookScene = createListBookScene(primaryStage);
        primaryStage.setTitle("List a New Book");
        primaryStage.setScene(listBookScene);
        primaryStage.show();
    }

    private Scene createListBookScene(Stage stage) {
        // Header
        HBox topBar = createHeader();

        // Main Content
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20));

        Label titleLabel = new Label("List a book");
        titleLabel.setFont(new Font("Arial", 24));

        // Form Fields
        VBox form = new VBox(15);
        form.setMaxWidth(400);

        TextField titleField = new TextField();
        titleField.setPromptText("Title");

        TextField authorField = new TextField();
        authorField.setPromptText("Author");

        TextField publishedYearField = new TextField();
        publishedYearField.setPromptText("Published Year");

        TextField originalPriceField = new TextField();
        originalPriceField.setPromptText("Original Price");


        final ListView<String> selectedCategories = new ListView<>();
        MenuButton categoryDropdown = new MenuButton("Select Categories: [empty]");

        final List<CheckMenuItem> categories = Arrays.asList(new CheckMenuItem("Natural Sciences"), new CheckMenuItem("Mathematics"), new CheckMenuItem("Computer Science"), new CheckMenuItem("English Language"), new CheckMenuItem("Other"));
        categoryDropdown.getItems().addAll(categories);

        for (final CheckMenuItem item : categories) {
            item.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
                if (newValue) {
                    selectedCategories.getItems().add(item.getText());
                } else {
                    selectedCategories.getItems().remove(item.getText());
                }
                String sMenuText = "Select Categories: " + (selectedCategories.getItems().size()>0?"":"[none selected]");
                categoryDropdown.setText(sMenuText+String.join(", ", selectedCategories.getItems()));
            });
        }



        ComboBox<String> conditionDropdown = new ComboBox<>();
        conditionDropdown.getItems().addAll("Used Like New", "Moderately Used", "Heavily Used");
        conditionDropdown.setPromptText("Select Condition");

        Button uploadImageButton = new Button("Upload Image");
        uploadImageButton.setOnAction(e -> {
            System.out.println("Uploaded!");
        });

        Button submitButton = new Button("Get Potential Earnings");
        submitButton.setOnAction(e -> {
            if (!titleField.getText().isEmpty()) {
                books.add(titleField.getText());
                Scene bookListingScene = createBookListingScene(stage);
                stage.setScene(bookListingScene);
            } else {
                System.out.println("Error: Please enter a title for the book.");
            }
        });

        form.getChildren().addAll(
                titleField,
                authorField,
                publishedYearField,
                originalPriceField,
                categoryDropdown,
                conditionDropdown,
                submitButton
        );

        mainContent.getChildren().addAll(titleLabel, form);

        // Layout
        BorderPane layout = new BorderPane();
        layout.setTop(topBar);
        layout.setCenter(mainContent);

        Scene scene = new Scene(layout, 800, 600);
        return scene;
    }

    private Scene createBookListingScene(Stage stage) {
        // Header
        HBox topBar = createHeader();

        // Main Content
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20));
        mainContent.setAlignment(Pos.TOP_CENTER);

        Label titleLabel = new Label("Book Listing");
        titleLabel.setFont(new Font("Arial", 24));

        VBox form = new VBox(15);
        form.setMaxWidth(400);

        Label expectedPriceLabel = new Label("Expected Price");
        TextField expectedPriceField = new TextField("$10.00");
        expectedPriceField.setEditable(false);

        Label platformFeeLabel = new Label("Platform Fee (20%)");
        TextField platformFeeField = new TextField("$2.00");
        platformFeeField.setEditable(false);

        Label yourEarningsLabel = new Label("Your Earnings");
        TextField yourEarningsField = new TextField("$8.00");
        yourEarningsField.setEditable(false);

        Button listMyBookButton = new Button("List My Book");
        listMyBookButton.setOnAction(e -> {
            // Alert here
            
            // Direct back to seller dashboard, show this new listing in pending approvals
        });

        form.getChildren().addAll(
                createFieldRow(expectedPriceLabel, expectedPriceField),
                createFieldRow(platformFeeLabel, platformFeeField),
                createFieldRow(yourEarningsLabel, yourEarningsField),
                listMyBookButton
        );

        mainContent.getChildren().addAll(titleLabel, form);

        // Layout
        BorderPane layout = new BorderPane();
        layout.setTop(topBar);
        layout.setCenter(mainContent);

        Scene scene = new Scene(layout, 800, 600);
        return scene;
    }

    private HBox createHeader() {
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(10));
        topBar.setAlignment(Pos.CENTER_LEFT);

        Button logoButton = new Button("Sparky's Books");

        Button listingsButton = new Button("Listings");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button profileButton = new Button("My Profile");

        topBar.getChildren().addAll(logoButton, spacer, listingsButton, profileButton);
        return topBar;
    }

    private HBox createFieldRow(Label label, TextField textField) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getChildren().addAll(label, textField);
        return row;
    }
}