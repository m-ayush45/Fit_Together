package com.fittogether.ui;

import com.fittogether.database.DietDAO;
import com.fittogether.database.DatabaseConnection;
import com.fittogether.model.Diet;
import com.fittogether.model.User;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox; // Import VBox for vertical stacking
import javafx.stage.Stage;

import java.sql.Connection;
import java.util.Date;

public class LogDietEntryPage {
    private Stage primaryStage;
    private User user;
    private Scene scene;

    public LogDietEntryPage(Stage primaryStage, User user) {
        this.primaryStage = primaryStage;
        this.user = user;
        setupUI();
    }

    private void setupUI() {
        GridPane layout = new GridPane();
        layout.setPadding(new Insets(20));
        layout.setVgap(10);
        layout.setHgap(10);

        Label titleLabel = new Label("Log Diet Entry");
        titleLabel.getStyleClass().add("title"); // Add title style

        // Form fields
        TextField foodItemField = createTextField("Food Item");
        TextField caloriesField = createTextField("Calories");

        DatePicker dateConsumedPicker = new DatePicker();
        
        ComboBox<String> mealTypeComboBox = new ComboBox<>();
        mealTypeComboBox.getStyleClass().add("combo-box"); // Add combo box style
        mealTypeComboBox.getItems().addAll("Breakfast", "Lunch", "Dinner", "Snack");
        mealTypeComboBox.setPromptText("Meal Type");

        Button logButton = createButton("Log Entry", e -> logDietEntry(foodItemField, caloriesField, dateConsumedPicker, mealTypeComboBox));
        Button backButton = createButton("Back to Diet Tracker", e -> goBackToDietTracker());

        // Layout arrangement for the form fields
        layout.add(titleLabel, 0, 0, 2, 1);
        layout.add(new Label("Food Item:"), 0, 1);
        layout.add(foodItemField, 1, 1);
        layout.add(new Label("Calories:"), 0, 2);
        layout.add(caloriesField, 1, 2);
        layout.add(new Label("Date Consumed:"), 0, 3);
        layout.add(dateConsumedPicker, 1, 3);
        layout.add(new Label("Meal Type:"), 0, 4);
        layout.add(mealTypeComboBox, 1, 4);

        // Use a VBox to stack buttons vertically
        VBox buttonLayout = new VBox(10); // 10 pixels of spacing between buttons
        buttonLayout.getChildren().addAll(logButton, backButton);

        layout.add(buttonLayout, 0, 5, 2, 1); // Add button layout to the grid

        // Wrap the layout in a ScrollPane
        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        
        this.scene = new Scene(scrollPane, 400, 300);
        scene.getStylesheets().add(getClass().getResource("/resources/diet.css").toExternalForm()); // Load CSS
    }

    private TextField createTextField(String promptText) {
        TextField textField = new TextField();
        textField.getStyleClass().add("text-field"); // Add text field style
        textField.setPromptText(promptText);
        return textField;
    }

    private Button createButton(String text, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        Button button = new Button(text);
        button.getStyleClass().add("button"); // Add button style
        button.setOnAction(action);
        return button;
    }

    private void logDietEntry(TextField foodItemField, TextField caloriesField, DatePicker dateConsumedPicker, ComboBox<String> mealTypeComboBox) {
        // Validate input
        if (isInputValid(foodItemField, caloriesField, dateConsumedPicker, mealTypeComboBox)) {
            try {
                int calories = Integer.parseInt(caloriesField.getText());
                Date dateConsumed = java.sql.Date.valueOf(dateConsumedPicker.getValue());
                String mealType = mealTypeComboBox.getValue();

                Diet diet = new Diet(0, user.getId(), foodItemField.getText(), calories, (java.sql.Date) dateConsumed, mealType);
                
                // Obtain connection and log diet entry
                try (Connection connection = DatabaseConnection.getConnection()) {
                    DietDAO dietDAO = new DietDAO(connection);
                    dietDAO.addDietEntry(diet);
                }

                showAlert(Alert.AlertType.INFORMATION, "Diet entry logged successfully!");
                
                // Clear form fields after successful entry
                clearFormFields(foodItemField, caloriesField, dateConsumedPicker, mealTypeComboBox);
                
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Please enter a valid number for calories.");
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error logging diet entry: " + ex.getMessage());
            }
        }
    }

    private boolean isInputValid(TextField foodItemField, TextField caloriesField, DatePicker dateConsumedPicker, ComboBox<String> mealTypeComboBox) {
        if (foodItemField.getText().isEmpty() || caloriesField.getText().isEmpty() || 
            dateConsumedPicker.getValue() == null || mealTypeComboBox.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Please fill in all fields.");
            return false;
        }
        return true;
    }

    private void clearFormFields(TextField foodItemField, TextField caloriesField, DatePicker dateConsumedPicker, ComboBox<String> mealTypeComboBox) {
        foodItemField.clear();
        caloriesField.clear();
        dateConsumedPicker.setValue(null);
        mealTypeComboBox.setValue(null);
    }

    private void goBackToDietTracker() {
        DietTrackerPage dietTrackerPage = new DietTrackerPage(primaryStage, user);
        primaryStage.setScene(dietTrackerPage.getScene());
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType, message);
        alert.showAndWait();
    }

    public Scene getScene() {
        return scene;
    }
}
