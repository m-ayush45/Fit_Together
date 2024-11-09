package com.fittogether.ui;

import com.fittogether.database.WorkoutDAO; // Import WorkoutDAO
import com.fittogether.model.User; // Import User model
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AddWorkoutPage {
    private WorkoutDAO workoutDAO; // Declare WorkoutDAO
    private User user; // Add User field

    // Constructor to accept User object
    public AddWorkoutPage(User user) {
        this.user = user; // Initialize the user
        this.workoutDAO = new WorkoutDAO(); // Initialize WorkoutDAO independently
    }

    // Method to set up the UI components and return the scene
    public Scene getScene(Stage primaryStage) {
        VBox layout = new VBox(10); // 10px spacing
        layout.setPadding(new Insets(20));

        // Title label
        Label titleLabel = new Label("Add Workouts");
        
        // Label and TextField for exercise name
        Label exerciseLabel = new Label("Exercise Name:");
        TextField exerciseField = new TextField();

        // Label and TextField for duration
        Label durationLabel = new Label("Duration (in minutes):");
        TextField durationField = new TextField();

        // Label and TextField for calories
        Label calorieLabel = new Label("Calories Burned:");
        TextField calorieField = new TextField(); // TextField for calories input

        // Button to add workout
        Button addButton = new Button("Add Workout");
        addButton.setOnAction(e -> {
            String exerciseName = exerciseField.getText().trim(); // Get the exercise name
            int duration;
            int calories;

            // Validate and parse the duration
            try {
                duration = Integer.parseInt(durationField.getText().trim()); // Parse the duration
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a valid number for duration.");
                alert.showAndWait();
                return; // Exit the method if parsing fails
            }

            // Validate and parse the calories
            try {
                calories = Integer.parseInt(calorieField.getText().trim()); // Parse the calories
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a valid number for calories.");
                alert.showAndWait();
                return; // Exit the method if parsing fails
            }

            // Call the addWorkout method with the user ID, exercise name, duration, and calories
            if (workoutDAO.addWorkout(user.getId(), exerciseName, duration, calories)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Workout added successfully!");
                alert.showAndWait();
                primaryStage.setScene(new WorkoutPage(primaryStage, user).getScene()); // Navigate back to WorkoutPage
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to add workout!");
                alert.showAndWait();
            }
        });

        // Button to go back to WorkoutPage
        Button backButton = new Button("Back to Workout Page");
        backButton.setOnAction(e -> {
            primaryStage.setScene(new WorkoutPage(primaryStage, user).getScene()); // Navigate back to WorkoutPage
        });

        // Add all components to the layout
        layout.getChildren().addAll(exerciseLabel, exerciseField, durationLabel, durationField, calorieLabel, calorieField, addButton, backButton);

        // Create a ScrollPane to make the layout scrollable
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(layout);
        scrollPane.setFitToWidth(true); // Make the ScrollPane fit the width of the layout
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS); // Always show vertical scrollbar

        // Create and return the scene with the ScrollPane as root
        Scene scene = new Scene(scrollPane, 400, 350); // Adjust height for UI components
        return scene; // Return the created scene
    }
}
