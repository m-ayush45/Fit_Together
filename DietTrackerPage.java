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

public class DietTrackerPage {
    private Stage primaryStage;
    private User user;

    public DietTrackerPage(Stage primaryStage, User user) {
        this.primaryStage = primaryStage;
        this.user = user;
    }

    public Scene getScene() {
        // Create a vertical layout with increased spacing between elements
        VBox layout = new VBox(20); // Increase spacing to make it look more appealing
        layout.setPadding(new Insets(30)); // Add padding to give space around the elements
        layout.setAlignment(Pos.TOP_CENTER); // Center align the title at the top

        // Add the diet tracker page style class for consistent background and padding
        layout.getStyleClass().add("diet-page");

        // Title for the page
        Label pageTitle = new Label("Diet Tracker");
        pageTitle.getStyleClass().add("diet-title");  // Apply diet title CSS class

        // Create a VBox to hold the buttons aligned to the left
        VBox buttonLayout = new VBox(15); // Vertical box for buttons, with spacing
        buttonLayout.setAlignment(Pos.TOP_LEFT); // Align buttons to the left
        buttonLayout.setPadding(new Insets(10)); // Add some padding around buttons

        // Create buttons with consistent styling and minimum width
        Button logDietButton = createButton("Log Diet Entry");
        Button viewDietEntriesButton = createButton("View Diet Entries");
        Button editDietEntriesButton = createButton("Edit Diet Entries");
        Button balancedDietButton = createButton("Balanced Diet");
        Button dietRecommendationsButton = createButton("Diet Recommendations");
        Button backButton = createButton("Back to Home");

        // Set actions for buttons
        logDietButton.setOnAction(e -> {
            LogDietEntryPage logDietEntryPage = new LogDietEntryPage(primaryStage, user);
            primaryStage.setScene(logDietEntryPage.getScene());
        });

        viewDietEntriesButton.setOnAction(e -> {
            ViewDietEntriesPage viewDietEntriesPage = new ViewDietEntriesPage(primaryStage, user);
            primaryStage.setScene(viewDietEntriesPage.getScene());
        });

        editDietEntriesButton.setOnAction(e -> {
            EditDietEntriesPage editDietEntriesPage = new EditDietEntriesPage(primaryStage, user);
            primaryStage.setScene(editDietEntriesPage.getScene());
        });

        balancedDietButton.setOnAction(e -> {
            BalancedDietPage balancedDietPage = new BalancedDietPage(primaryStage, user);
            primaryStage.setScene(balancedDietPage.getScene());
        });

        dietRecommendationsButton.setOnAction(e -> {
            DietRecommendationsPage dietRecommendationsPage = new DietRecommendationsPage(primaryStage, user);
            primaryStage.setScene(dietRecommendationsPage.getScene());
        });

        backButton.setOnAction(e -> {
            HomePage homePage = new HomePage(primaryStage, user);
            primaryStage.setScene(homePage.getScene());
        });

        // Add buttons to the left-aligned layout
        buttonLayout.getChildren().addAll(
                logDietButton,
                viewDietEntriesButton,
                editDietEntriesButton,
                balancedDietButton,
                dietRecommendationsButton,
                backButton
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
        scene.getStylesheets().add(getClass().getResource("/resources/diet.css").toExternalForm());
        return scene;
    }

    // Helper method to create buttons with consistent styles
    private Button createButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("button"); // Apply button CSS class
        button.setMinWidth(300); // Set minimum width for a more professional look
        return button;
    }
}
