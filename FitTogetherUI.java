package com.fittogether.ui;

import javafx.application.Application;
import javafx.stage.Stage;

public class FitTogetherUI extends Application {
    @Override
    public void start(Stage primaryStage) {
        LoginPage loginPage = new LoginPage(primaryStage);
        primaryStage.setScene(loginPage.getScene()); // Use getScene() to set the scene
        primaryStage.setTitle("Fit Together"); // Set the title of the window
        primaryStage.show(); // Show the primary stage
    }

    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}
