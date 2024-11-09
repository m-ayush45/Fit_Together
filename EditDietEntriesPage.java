package com.fittogether.ui;

import com.fittogether.database.DietDAO;
import com.fittogether.database.DatabaseConnection;
import com.fittogether.model.Diet;
import com.fittogether.model.User;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

public class EditDietEntriesPage {
    private Stage primaryStage;
    private User user;
    private Scene scene;
    private ComboBox<Diet> dietComboBox;
    private TextField foodItemField;
    private TextField caloriesField;
    private TextField mealTypeField;
    private DatePicker dateConsumedField;

    public EditDietEntriesPage(Stage primaryStage, User user) {
        this.primaryStage = primaryStage;
        this.user = user;
        setupUI();
    }

    private void setupUI() {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));

        Label titleLabel = new Label("Edit Diet Entry");
        titleLabel.getStyleClass().add("title");

        dietComboBox = new ComboBox<>();
        fetchDietEntries(); // Populate the ComboBox with existing diet entries

        // Create input fields
        foodItemField = new TextField();
        caloriesField = new TextField();
        mealTypeField = new TextField();
        dateConsumedField = new DatePicker();

        // Create button to save changes
        Button saveButton = new Button("Save Changes");
        saveButton.setOnAction(e -> saveChanges());

        // Create button to delete entry
        Button deleteButton = new Button("Delete Entry");
        deleteButton.setOnAction(e -> deleteEntry());

        // Create button to navigate to Diet Tracker Page
        Button dietTrackerButton = new Button("Back to Diet Tracker");
        dietTrackerButton.setOnAction(e -> goToDietTrackerPage());

        layout.getChildren().addAll(titleLabel, dietComboBox, foodItemField, caloriesField, mealTypeField, dateConsumedField, saveButton, deleteButton, dietTrackerButton);

        // Wrap layout in a ScrollPane for scrolling capability
        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true); // Fit content width
        scrollPane.setFitToHeight(true); // Fit content height

        this.scene = new Scene(scrollPane, 400, 300);
        scene.getStylesheets().add(getClass().getResource("/resources/diet.css").toExternalForm()); // Load CSS

        // Add a listener to update fields when a diet entry is selected
        dietComboBox.setOnAction(e -> populateFields());
    }

    private void fetchDietEntries() {
        try (Connection connection = DatabaseConnection.getConnection()) { // Obtain connection
            DietDAO dietDAO = new DietDAO(connection);
            List<Diet> diets = dietDAO.getDietEntriesByUserId(user.getId());
            dietComboBox.getItems().addAll(diets);
        } catch (Exception e) {
            showAlert("Error", "Unable to retrieve diet entries: " + e.getMessage());
        }
    }

    private void populateFields() {
        Diet selectedDiet = dietComboBox.getValue();
        if (selectedDiet != null) {
            foodItemField.setText(selectedDiet.getFoodItem());
            caloriesField.setText(String.valueOf(selectedDiet.getCalories()));
            mealTypeField.setText(selectedDiet.getMealType());
            dateConsumedField.setValue(selectedDiet.getDateConsumed().toLocalDate()); // Convert sql.Date to LocalDate
        }
    }

    private void saveChanges() {
        Diet selectedDiet = dietComboBox.getValue();
        if (selectedDiet != null) {
            try (Connection connection = DatabaseConnection.getConnection()) { // Obtain connection
                DietDAO dietDAO = new DietDAO(connection);
                selectedDiet.setFoodItem(foodItemField.getText());
                selectedDiet.setCalories(Integer.parseInt(caloriesField.getText()));
                selectedDiet.setMealType(mealTypeField.getText());
                selectedDiet.setDateConsumed(Date.valueOf(dateConsumedField.getValue())); // Convert LocalDate to sql.Date

                dietDAO.updateDietEntry(selectedDiet); // Update the diet entry in the database
                showAlert("Success", "Diet entry updated successfully.");
            } catch (Exception e) {
                showAlert("Error", "Failed to update diet entry: " + e.getMessage());
            }
        }
    }

    private void deleteEntry() {
        Diet selectedDiet = dietComboBox.getValue();
        if (selectedDiet != null) {
            try (Connection connection = DatabaseConnection.getConnection()) { // Obtain connection
                DietDAO dietDAO = new DietDAO(connection);
                dietDAO.deleteDietEntry(selectedDiet.getId()); // Delete the entry from the database
                dietComboBox.getItems().remove(selectedDiet); // Remove the entry from the ComboBox
                showAlert("Success", "Diet entry deleted successfully.");
                clearFields(); // Clear fields after deletion
            } catch (Exception e) {
                showAlert("Error", "Failed to delete diet entry: " + e.getMessage());
            }
        } else {
            showAlert("Warning", "No diet entry selected for deletion.");
        }
    }

    private void clearFields() {
        foodItemField.clear();
        caloriesField.clear();
        mealTypeField.clear();
        dateConsumedField.setValue(null);
    }

    private void goToDietTrackerPage() {
        DietTrackerPage dietTrackerPage = new DietTrackerPage(primaryStage, user); // Create instance of DietTrackerPage
        primaryStage.setScene(dietTrackerPage.getScene()); // Set the scene to DietTrackerPage
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Scene getScene() {
        return scene;
    }
}
