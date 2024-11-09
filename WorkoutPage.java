package com.fittogether.ui;

import com.fittogether.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos; // Import to center align elements
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class WorkoutPage {
    private Stage primaryStage;
    private User user;

    public WorkoutPage(Stage primaryStage, User user) {
        this.primaryStage = primaryStage;
        this.user = user;
    }

    public Scene getScene() {
        // Create a vertical layout with increased spacing between elements
        VBox layout = new VBox(20); // Increase spacing to make it look more appealing
        layout.setPadding(new Insets(30)); // Add padding to give space around the elements
        layout.setAlignment(Pos.TOP_CENTER); // Center align the title at the top

        // Add the workout page style class for consistent background and padding
        layout.getStyleClass().add("workout-page");

        // Title for the page
        Label pageTitle = new Label("Workout Tracker");
        pageTitle.getStyleClass().add("page-title"); // Apply page title CSS class

        // Create a VBox to hold the buttons aligned to the left
        VBox buttonLayout = new VBox(15); // Vertical box for buttons, with spacing
        buttonLayout.setAlignment(Pos.TOP_LEFT); // Align buttons to the left
        buttonLayout.setPadding(new Insets(10)); // Add some padding around buttons

        // Create buttons with consistent styling and minimum width
        Button addWorkoutButton = createButton("Add Workout");
        Button viewWorkoutButton = createButton("View Workouts");
        Button editWorkoutButton = createButton("Edit Workout");
        Button homeButton = createButton("Go to Home");

        // Set actions for buttons
        addWorkoutButton.setOnAction(e -> {
            AddWorkoutPage addWorkoutPage = new AddWorkoutPage(user);
            primaryStage.setScene(addWorkoutPage.getScene(primaryStage));
        });

        viewWorkoutButton.setOnAction(e -> {
            ViewWorkoutPage viewWorkoutPage = new ViewWorkoutPage(user, primaryStage);
            primaryStage.setScene(viewWorkoutPage.getScene());
        });

        editWorkoutButton.setOnAction(e -> {
            EditWorkoutPage editWorkoutPage = new EditWorkoutPage(user);
            primaryStage.setScene(editWorkoutPage.getScene(primaryStage));
        });

        homeButton.setOnAction(e -> {
            HomePage homePage = new HomePage(primaryStage, user);
            primaryStage.setScene(homePage.getScene());
        });

        // Add buttons to the left-aligned layout
        buttonLayout.getChildren().addAll(
                addWorkoutButton,
                viewWorkoutButton,
                editWorkoutButton,
                homeButton
        );

        // Add title and button layout to the main layout
        layout.getChildren().addAll(pageTitle, buttonLayout);

        // Create a ScrollPane for the layout
        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true); // Ensure layout fits to the width of the ScrollPane
        scrollPane.setFitToHeight(true); // Enable vertical scrolling if necessary
        scrollPane.getStyleClass().add("scroll-pane"); // Add the style class for dark background

        // Create and return the scene
        Scene scene = new Scene(scrollPane, 500, 500); // Set a larger size for a better layout
        scene.getStylesheets().add(getClass().getResource("/resources/workout.css").toExternalForm());
        return scene;
    }

    // Helper method to create buttons with consistent styles
    private Button createButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("button");
        button.setMinWidth(300); // Set minimum width for a more professional look
        return button;
    }
}
