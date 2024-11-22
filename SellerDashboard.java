import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

public class SellerDashboard extends Application {

    public static void main(String[] args) { launch(args); }

   @Override
   public void start(Stage primaryStage) {
       primaryStage.setTitle("Seller's Dashboard");

       // Top Bar
       HBox topBar = new HBox(20);
       topBar.setPadding(new Insets(10));
       topBar.setAlignment(Pos.CENTER_LEFT);
       Button logoButton = new Button("Sparky's Books");
       logoButton.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
       Button listBookButton = new Button("List a Book");
       Button listingsButton = new Button("Listings");
       Region spacer = new Region();
       HBox.setHgrow(spacer, Priority.ALWAYS);
       Button profileButton = new Button("My Profile");
       topBar.getChildren().addAll(logoButton, spacer, listBookButton, listingsButton, profileButton);

       // My Listings Section
       VBox myListingsSection = new VBox(10);
       myListingsSection.setPadding(new Insets(10));
       Label listingsLabel = new Label("My Listings");
       listingsLabel.setFont(new Font("Arial", 18));

       GridPane listingsGrid = createBookGrid(false); // For approved listings

       myListingsSection.getChildren().addAll(listingsLabel, listingsGrid);

       // Pending Approval Section
       VBox pendingApprovalSection = new VBox(10);
       pendingApprovalSection.setPadding(new Insets(10));
       Label pendingApprovalLabel = new Label("Pending Approval");
       pendingApprovalLabel.setFont(new Font("Arial", 18));

       GridPane pendingApprovalGrid = createBookGrid(true); // For pending books

       pendingApprovalSection.getChildren().addAll(pendingApprovalLabel, pendingApprovalGrid);

       // Main Layout
       VBox mainLayout = new VBox(20, topBar, myListingsSection, pendingApprovalSection);
       mainLayout.setPadding(new Insets(20));
       Scene scene = new Scene(mainLayout, 1000, 700);
       primaryStage.setScene(scene);
       primaryStage.show();
   }

   private GridPane createBookGrid(boolean isPending) {
       GridPane gridPane = new GridPane();
       gridPane.setHgap(10);
       gridPane.setVgap(10);
       gridPane.setPadding(new Insets(10));

       // Sample data for books (Replace with dynamic data from the database)
       String[] bookTitles = {"The Great Gatsby", "To Kill a Mockingbird", "1984"};
       String[] conditions = {"Used Like New", "Moderately Used", "Heavily Used"};
       String[] categories = {"Natural Sciences", "Computer Science", "English"};
       String[] prices = {"$3", "$5", "$4"};

       for (int i = 0; i < bookTitles.length; i++) {
           VBox bookCard = createBookCard(bookTitles[i], conditions[i], categories[i], prices[i], isPending);
           gridPane.add(bookCard, i % 3, i / 3);
       }
       return gridPane;
   }

   private VBox createBookCard(String title, String condition, String category, String price, boolean isPending) {
       VBox card = new VBox(5);
       card.setPadding(new Insets(10));
       card.setAlignment(Pos.CENTER);
       card.setStyle("-fx-border-color: lightgray; -fx-border-width: 1px; -fx-background-color: #f9f9f9;");
       card.setPrefSize(150, 200);

//        ImageView imageView = new ImageView(new Image("placeholder.png", 100, 100, true, true)); // Replace with book image
       Label titleLabel = new Label(title);
       titleLabel.setFont(new Font("Arial", 14));
       titleLabel.setWrapText(true);
       titleLabel.setTextAlignment(TextAlignment.CENTER);

       Label conditionLabel = new Label(condition);
       Label categoryLabel = new Label(category);
       Label priceLabel = new Label(price);

       if (isPending) {
           Label pendingLabel = new Label();
           card.getChildren().add(pendingLabel);
       }

       card.getChildren().addAll(titleLabel, conditionLabel, categoryLabel, priceLabel);
       return card;
   }

}
