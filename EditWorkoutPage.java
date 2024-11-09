package com.fittogether.ui;

import com.fittogether.database.WorkoutDAO; // Import WorkoutDAO
import com.fittogether.model.User; // Import User model
import com.fittogether.model.Workout; // Import Workout model
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class EditWorkoutPage {
    private WorkoutDAO workoutDAO; // Declare WorkoutDAO
    private User user; // Add User field

    // Constructor to accept User object
    public EditWorkoutPage(User user) {
        this.user = user; // Initialize the user
        this.workoutDAO = new WorkoutDAO(); // Initialize WorkoutDAO
    }

    // Method to set up the UI components and return the scene
    public Scene getScene(Stage primaryStage) {
        VBox layout = new VBox(10); // 10px spacing
        layout.setPadding(new Insets(20));

        // Title label
        Label titleLabel = new Label("Edit Workouts");
        
        // Label and ComboBox for selecting workout
        Label selectWorkoutLabel = new Label("Select Workout:");
        ComboBox<String> workoutComboBox = new ComboBox<>();

        // Fetch all workout names for the user and add them to the ComboBox
        List<String> workoutNames = workoutDAO.getAllWorkoutNames(user.getId());
        workoutComboBox.getItems().addAll(workoutNames);

        // Label and TextField for exercise name
        Label exerciseLabel = new Label("Exercise Name:");
        TextField exerciseField = new TextField();
        exerciseField.setEditable(false); // Initially not editable

        // Label and TextField for duration
        Label durationLabel = new Label("Duration (in minutes):");
        TextField durationField = new TextField();

        // Label and TextField for calories
        Label calorieLabel = new Label("Calories Burned:");
        TextField calorieField = new TextField();

        // Button to save the edited workout
        Button saveButton = new Button("Save Changes");
        saveButton.setOnAction(e -> {
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

            // Call the editWorkout method with the updated details
            Workout selectedWorkout = workoutDAO.getWorkoutByName(user.getId(), workoutComboBox.getValue());
            if (selectedWorkout != null) {
                if (workoutDAO.editWorkout(selectedWorkout.getId(), exerciseName, duration, calories)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Workout updated successfully!");
                    alert.showAndWait();
                    primaryStage.setScene(new WorkoutPage(primaryStage, user).getScene()); // Navigate back to WorkoutPage
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to update workout!");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "No workout selected or workout not found.");
                alert.showAndWait();
            }
        });

        // Button to delete the selected workout
        Button deleteButton = new Button("Delete Workout");
        deleteButton.setOnAction(e -> {
            String selectedWorkoutName = workoutComboBox.getValue();
            if (selectedWorkoutName != null) {
                boolean deleted = workoutDAO.deleteWorkoutByExerciseName(selectedWorkoutName);
                if (deleted) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Workout deleted successfully!");
                    alert.showAndWait();
                    primaryStage.setScene(new WorkoutPage(primaryStage, user).getScene()); // Navigate back to WorkoutPage
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to delete workout.");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "No workout selected for deletion.");
                alert.showAndWait();
            }
        });

        // Button to go back to the previous page
        Button backButton = new Button("Back to Workout Page");
        backButton.setOnAction(e -> {
            primaryStage.setScene(new WorkoutPage(primaryStage, user).getScene()); // Navigate back to WorkoutPage
        });

        // Add listener to ComboBox to fetch workout details when a workout is selected
        workoutComboBox.setOnAction(e -> {
            String selectedWorkoutName = workoutComboBox.getValue();
            if (selectedWorkoutName != null) {
                Workout selectedWorkout = workoutDAO.getWorkoutByName(user.getId(), selectedWorkoutName);
                if (selectedWorkout != null) {
                    exerciseField.setText(selectedWorkout.getExerciseName());
                    durationField.setText(String.valueOf(selectedWorkout.getDuration()));
                    calorieField.setText(String.valueOf(selectedWorkout.getCalories()));

                    // Make the exerciseField editable when a workout is selected
                    exerciseField.setEditable(true);
                }
            }
        });

        // Add all components to the layout
        layout.getChildren().addAll(
                selectWorkoutLabel, workoutComboBox,
                exerciseLabel, exerciseField,
                durationLabel, durationField,
                calorieLabel, calorieField,
                saveButton, deleteButton, backButton
        );

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
