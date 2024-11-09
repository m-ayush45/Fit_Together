package com.fittogether.ui;

import com.fittogether.database.DatabaseConnection;
import com.fittogether.database.WorkoutDAO;
import com.fittogether.model.User;
import com.fittogether.model.Workout;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DietRecommendationsPage {
    private Stage primaryStage;
    private User user;
    private Scene scene;
    private ChoiceBox<String> goalChoiceBox; // ChoiceBox for fitness goals

    public DietRecommendationsPage(Stage primaryStage, User user) {
        this.primaryStage = primaryStage;
        this.user = user;
        setupUI();
    }

    private void setupUI() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label titleLabel = new Label("Diet Recommendations");
        titleLabel.getStyleClass().add("title"); // Add title style

        // Create a ChoiceBox for selecting fitness goals
        goalChoiceBox = new ChoiceBox<>();
        goalChoiceBox.getItems().addAll("Lose Weight", "Gain Muscle", "Maintain Weight");
        goalChoiceBox.setValue("Lose Weight"); // Default selection

        // Button to generate recommendations based on the selected goal
        Button generateButton = new Button("Get Recommendations");
        generateButton.getStyleClass().add("button"); // Add button style
        generateButton.setOnAction(e -> {
            String selectedGoal = goalChoiceBox.getValue();
            String recommendations = generateDietRecommendations(selectedGoal);
            showRecommendations(recommendations);
        });

        Button backButton = new Button("Back to Diet Tracker");
        backButton.getStyleClass().add("button"); // Add button style
        backButton.setOnAction(e -> {
            DietTrackerPage dietTrackerPage = new DietTrackerPage(primaryStage, user); // Remove connection parameter
            primaryStage.setScene(dietTrackerPage.getScene());
        });

        layout.getChildren().addAll(titleLabel, goalChoiceBox, generateButton, backButton);
        
        // Create a ScrollPane for the main layout
        ScrollPane scrollPane = new ScrollPane(layout); 
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true); // Enable vertical scrolling

        this.scene = new Scene(scrollPane, 400, 400);
        scene.getStylesheets().add(getClass().getResource("/resources/diet.css").toExternalForm());
    }

    private void showRecommendations(String recommendations) {
        VBox recommendationsLayout = new VBox(10);
        recommendationsLayout.setPadding(new Insets(20));

        Label recommendationsLabel = new Label(recommendations);
        recommendationsLabel.setWrapText(true); // Allow text wrapping for readability

        Button backButton = new Button("Back to Goals");
        backButton.getStyleClass().add("button"); // Add button style
        backButton.setOnAction(e -> primaryStage.setScene(scene)); // Go back to the main scene

        recommendationsLayout.getChildren().addAll(recommendationsLabel, backButton);
        
        // Create a ScrollPane for recommendations
        ScrollPane recommendationsScrollPane = new ScrollPane(recommendationsLayout); 
        recommendationsScrollPane.setFitToWidth(true);
        recommendationsScrollPane.setFitToHeight(true); // Enable vertical scrolling

        primaryStage.setScene(new Scene(recommendationsScrollPane, 400, 400));
    }

    private String generateDietRecommendations(String fitnessGoal) {
        StringBuilder recommendations = new StringBuilder();
        recommendations.append("Based on your workouts and fitness goal:\n\n");

        // Get the database connection
        try (Connection connection = DatabaseConnection.getConnection()) {
            WorkoutDAO workoutDAO = new WorkoutDAO(); // Pass connection to WorkoutDAO
            List<Workout> workouts = workoutDAO.getWorkoutsByUserId(user.getId());

            // Calculate total calories burned
            int totalCaloriesBurned = workouts.stream().mapToInt(Workout::getCalories).sum(); 
            recommendations.append("Total calories burned in your workouts: ").append(totalCaloriesBurned).append(" calories.\n\n");

            // Generate recommendations based on the selected fitness goal
            switch (fitnessGoal) {
                case "Lose Weight":
                    recommendations.append("1. **If your goal is to lose weight**:\n")
                                   .append("   - Aim for a calorie deficit: consume fewer calories than you burn.\n")
                                   .append("   - Focus on high-fiber foods (e.g., vegetables, fruits, whole grains) to keep you full.\n")
                                   .append("   - Incorporate lean proteins (e.g., chicken, fish, legumes) to support muscle maintenance.\n\n");
                    break;
                case "Gain Muscle":
                    recommendations.append("2. **If your goal is to gain muscle**:\n")
                                   .append("   - Increase your calorie intake with a focus on protein-rich foods.\n")
                                   .append("   - Include healthy carbohydrates (e.g., brown rice, quinoa) to fuel workouts and recovery.\n")
                                   .append("   - Stay hydrated to support muscle function and recovery.\n\n");
                    break;
                case "Maintain Weight":
                    recommendations.append("3. **If your goal is to maintain weight**:\n")
                                   .append("   - Balance your calorie intake with your activity level.\n")
                                   .append("   - Include a variety of food groups to meet your nutritional needs.\n")
                                   .append("   - Monitor portion sizes and overall diet quality.\n\n");
                    break;
            }

            recommendations.append("4. **General recommendations based on your activity level**:\n")
                           .append("   - If you burned over ").append(totalCaloriesBurned).append(" calories during your workouts, consider:\n")
                           .append("      - Consuming an additional healthy snack (e.g., nuts, yogurt) post-workout.\n")
                           .append("      - Maintaining a balanced intake of carbohydrates and proteins to recover effectively.\n")
                           .append("   - Monitor your overall nutrition to ensure you are getting enough vitamins and minerals.\n")
                           .append("\nRemember to consult with a nutritionist for personalized advice!");

        } catch (SQLException e) {
            recommendations.append("Error retrieving workout data: ").append(e.getMessage());
        }

        return recommendations.toString();
    }

    public Scene getScene() {
        return scene;
    }
}
