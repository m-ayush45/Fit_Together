package com.fittogether.ui;

import com.fittogether.model.User; // Assuming you have a User model
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane; // Import ScrollPane
import javafx.stage.Stage;

public class HomePage {
    private Stage primaryStage;
    private User user; // Store user object
    private Scene scene; // Declare the scene

    // Constructor to accept Stage and User object
    public HomePage(Stage primaryStage, User user) {
        this.primaryStage = primaryStage;
        this.user = user; // Initialize user object
        setupUI(); // Call method to set up UI components
    }

    // Method to set up the UI components
    private void setupUI() {
        VBox layout = new VBox(10); // 10px spacing
        layout.setPadding(new Insets(20));

        Label welcomeLabel = new Label("Welcome, " + user.getName());

        // Create buttons for navigation
        Button workoutPageButton = new Button("Go to Workout Tracker Page");
        Button sleepTrackerButton = new Button("Go to Sleep Tracker Page");
        Button dietTrackerButton = new Button("Go to Diet Tracker Page"); // New Diet Tracker Button
        Button logoutButton = new Button("Log Out");

        // Set button actions
        workoutPageButton.setOnAction(e -> {
            // Navigate to Workout Page
            WorkoutPage workoutPage = new WorkoutPage(primaryStage, user); // Pass all required arguments
            primaryStage.setScene(workoutPage.getScene()); // Switch to the WorkoutPage scene
        });

        sleepTrackerButton.setOnAction(e -> {
            // Navigate to Sleep Tracker Page
            SleepTrackerPage sleepTrackerPage = new SleepTrackerPage(primaryStage, user);
            primaryStage.setScene(sleepTrackerPage.getScene()); // Switch to Sleep Tracker page
        });

        // Set action for Diet Tracker button
        dietTrackerButton.setOnAction(e -> {
            // Navigate to Diet Tracker Page
            DietTrackerPage dietTrackerPage = new DietTrackerPage(primaryStage, user); // Remove connection parameter
            primaryStage.setScene(dietTrackerPage.getScene()); // Switch to Diet Tracker page
        });

        logoutButton.setOnAction(e -> {
            // Logic to log out the user and return to LoginPage
            LoginPage loginPage = new LoginPage(primaryStage);
            primaryStage.setScene(loginPage.getScene()); // Make sure getScene() is implemented in LoginPage
        });

        // Add components to layout
        layout.getChildren().addAll(welcomeLabel, workoutPageButton, sleepTrackerButton, dietTrackerButton, logoutButton); // Include Diet Tracker button

        // Create a ScrollPane for the layout
        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true); // Fit content to the width of the ScrollPane
        scrollPane.setFitToHeight(true); // Enable vertical scrolling

        // Set the CSS file for styling
        scrollPane.getStylesheets().add(getClass().getResource("/resources/home.css").toExternalForm());

        // Create the scene and store it in the instance variable
        this.scene = new Scene(scrollPane, 400, 300); // Set the size of the window
    }

    // Method to return the scene
    public Scene getScene() {
        return scene;
    }
}
