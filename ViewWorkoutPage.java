package com.fittogether.ui;

import com.fittogether.database.WorkoutDAO;
import com.fittogether.model.User;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.ScrollPane;  // Import ScrollPane

import java.util.List;
import javafx.scene.control.Label;

public class ViewWorkoutPage {
    private User user;
    private Stage primaryStage;
    private WorkoutDAO workoutDAO;

    // Constructor
    public ViewWorkoutPage(User user, Stage primaryStage) {
        this.user = user;
        this.primaryStage = primaryStage;
        this.workoutDAO = new WorkoutDAO();  // Initialize WorkoutDAO for database operations
    }

    // Method to set up the scene
    public Scene getScene() {
        VBox layout = new VBox(10); // 10px spacing
        layout.setPadding(new Insets(20));
        
        // Title label
        Label titleLabel = new Label("View Workouts");

        // Create a ListView to display the list of workouts with details
        ListView<String> workoutListView = new ListView<>();

        // Fetch workouts from the database using WorkoutDAO
        List<String> workoutDetails = workoutDAO.getAllWorkoutsForUser(user.getId());
        workoutListView.getItems().addAll(workoutDetails);  // Populate the list with workout details

        // Button to go back to the previous page (WorkoutPage)
        Button backButton = new Button("Back to Workouts");

        // Back button functionality (to return to the WorkoutPage)
        backButton.setOnAction(e -> {
            WorkoutPage workoutPage = new WorkoutPage(primaryStage, user);  // Pass primaryStage and user to WorkoutPage
            primaryStage.setScene(workoutPage.getScene()); // Navigate back to WorkoutPage
        });

        // Add components to layout
        layout.getChildren().addAll(workoutListView, backButton);

        // Create a ScrollPane to make the layout scrollable
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(layout);
        scrollPane.setFitToWidth(true); // Ensure it fits the width
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS); // Always show the vertical scrollbar

        // Create the scene and apply styling from CSS
        Scene scene = new Scene(scrollPane, 400, 300);
        scene.getStylesheets().add(getClass().getResource("/resources/workout.css").toExternalForm()); // Link to workout.css

        return scene; // Return the created scene
    }
}
