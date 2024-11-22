package com.example.sparkysbookssystem;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AdminView extends Application{

    // Labels for metrics
    @FXML
    private Label totalUsersLabel;

    @FXML
    private Label totalActiveListingsLabel;

    @FXML
    private Label totalSoldListingsLabel;

    @FXML
    private Label totalRevenueLabel;

    @FXML
    private Label totalProfitLabel;

    // Initialize dashboard metrics
    @FXML
    public void initialize() {
        updateDashboardMetrics();
    }

    private void updateDashboardMetrics() {
        try {
            int totalUsers = countLines("src/main/resources/com/example/sparkysbookssystem/data/users.txt");
            int totalActiveListings = countBooksByStatus("Available");
            int totalSoldListings = countBooksByStatus("Sold");
            double totalRevenue = calculateTotalRevenue();
            double totalProfit = totalRevenue * 0.2; // 20% profit margin

            // Update labels
            totalUsersLabel.setText(String.valueOf(totalUsers));
            totalActiveListingsLabel.setText(String.valueOf(totalActiveListings));
            totalSoldListingsLabel.setText(String.valueOf(totalSoldListings));
            totalRevenueLabel.setText("$" + String.format("%.2f", totalRevenue));
            totalProfitLabel.setText("$" + String.format("%.2f", totalProfit));
        } catch (Exception e) {
            showAlert("Error", "Failed to load dashboard metrics.");
        }
    }

    // Utility: Count lines in a file
    private int countLines(String fileName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            int lines = 0;
            while (reader.readLine() != null) {
                lines++;
            }
            return lines;
        }
    }

    // Utility: Count books by status
    private int countBooksByStatus(String status) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/com/example/sparkysbookssystem/data/books.txt"))) {
            int count = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Status: " + status)) {
                    count++;
                }
            }
            return count;
        }
    }

    // Utility: Calculate total revenue
    private double calculateTotalRevenue() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/com/example/sparkysbookssystem/data/transactions.txt"))) {
            double totalRevenue = 0.0;
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Price: ")) {
                    String price = line.split(": ")[1].trim();
                    totalRevenue += Double.parseDouble(price);
                }
            }
            return totalRevenue;
        }
    }

    // Event Handlers
    @FXML
    private void handleManageUsers() {
        // Navigate to Manage Users view
        Navigation.navigateTo("admin-manage-users.fxml");
    }

    @FXML
    private void handleManageBooks() {
        // Navigate to Manage Books view
        Navigation.navigateTo("admin-manage-books.fxml");
    }

    @FXML
    private void handleEditConfig() {
        // Navigate to Edit Configurations view
        Navigation.navigateTo("admin-edit-config.fxml");
    }

    @FXML
    private void handleLogOut() {
        // Navigate back to login view
        Navigation.navigateTo("login-view.fxml");
    }

    // Helper: Show an alert dialog
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
