package com.fittogether.ui;

import com.fittogether.model.User;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BalancedDietPage {
    private Stage primaryStage;
    private User user;
    private Scene scene;

    public BalancedDietPage(Stage primaryStage, User user) {
        this.primaryStage = primaryStage;
        this.user = user;
        setupUI();
    }

    private void setupUI() {
        // Create layout for the page
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label titleLabel = new Label("Balanced Diet Information");
        titleLabel.getStyleClass().add("title"); // Add title style

        // Detailed content about a balanced diet
        String dietInfo = "A balanced diet includes a variety of foods:\n\n"
                + "1. **Fruits**: Provide essential vitamins, minerals, and fiber.\n"
                + "   - Aim for a variety of colors to ensure a wide range of nutrients.\n"
                + "   - Examples: apples (fiber, vitamin C), bananas (potassium, vitamin B6), "
                + "berries (antioxidants), oranges (vitamin C).\n"
                + "   - Consume whole fruits instead of fruit juices to maximize fiber intake.\n\n"
                + "2. **Vegetables**: Key for vitamins, minerals, and antioxidants.\n"
                + "   - Include a variety of types and colors, focusing on leafy greens and "
                + "cruciferous vegetables.\n"
                + "   - Examples: spinach (iron, vitamins A and C), carrots (beta-carotene, fiber), "
                + "broccoli (vitamin K, vitamin C, fiber).\n"
                + "   - Raw or steamed vegetables retain more nutrients compared to overcooked ones.\n\n"
                + "3. **Whole Grains**: Good source of energy, fiber, and various nutrients.\n"
                + "   - Choose whole grains over refined grains for more fiber and nutrients.\n"
                + "   - Examples: brown rice (fiber, magnesium), quinoa (complete protein), "
                + "whole wheat bread (fiber, B vitamins).\n"
                + "   - Incorporating whole grains can help with digestion and keep you fuller longer.\n\n"
                + "4. **Proteins**: Important for muscle repair, growth, and overall body function.\n"
                + "   - Incorporate both animal and plant-based protein sources for variety.\n"
                + "   - Examples: chicken (lean protein, B vitamins), fish (omega-3 fatty acids), "
                + "beans (fiber, protein), lentils (protein, iron), tofu (plant protein).\n"
                + "   - Aim for lean proteins to reduce saturated fat intake.\n\n"
                + "5. **Healthy Fats**: Essential for brain health, hormone production, and energy.\n"
                + "   - Opt for unsaturated fats over saturated fats and trans fats.\n"
                + "   - Examples: avocados (healthy monounsaturated fats), nuts (vitamin E, magnesium), "
                + "olive oil (anti-inflammatory properties), fatty fish (omega-3 fatty acids).\n"
                + "   - Moderation is key; fats are calorie-dense, so keep portion sizes in check.\n\n"
                + "### Tips for a Balanced Diet:\n"
                + "- Stay hydrated by drinking plenty of water; aim for at least 8 cups a day.\n"
                + "- Limit added sugars and processed foods, which can lead to weight gain and health issues.\n"
                + "- Practice portion control to avoid overeating; use smaller plates to help manage serving sizes.\n"
                + "- Enjoy your meals mindfully, focusing on flavors and textures; this can enhance satisfaction.\n"
                + "- Consider meal prepping to ensure balanced meals are easily accessible during the week.\n"
                + "- Include snacks like fruits, vegetables, or nuts to maintain energy levels throughout the day.\n"
                + "- Pay attention to food labels; look for high fiber content and low added sugars.\n"
                + "- Consult a healthcare provider or nutritionist for personalized dietary advice, especially if you have specific health conditions.\n";

        Label infoLabel = new Label(dietInfo);
        infoLabel.setWrapText(true); // Allows text wrapping

        // Wrap content in a ScrollPane
        ScrollPane scrollPane = new ScrollPane(infoLabel);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400); // Set a preferred height for the scroll pane
        scrollPane.getStyleClass().add("scroll-pane"); // Add scroll-pane style

        Button backButton = new Button("Back to Diet Tracker");
        backButton.getStyleClass().add("button"); // Add button style
        backButton.setOnAction(e -> openDietTrackerPage());

        layout.getChildren().addAll(titleLabel, scrollPane, backButton);
        this.scene = new Scene(layout, 400, 450); // Adjusted height for more content
        scene.getStylesheets().add(getClass().getResource("/resources/diet.css").toExternalForm()); // Load CSS
    }

    // Method to handle navigation back to Diet Tracker
    private void openDietTrackerPage() {
        DietTrackerPage dietTrackerPage = new DietTrackerPage(primaryStage, user); // Remove connection parameter
        primaryStage.setScene(dietTrackerPage.getScene());
    }

    public Scene getScene() {
        return scene;
    }
}
