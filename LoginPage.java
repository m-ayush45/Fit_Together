package com.fittogether.ui;

import com.fittogether.model.User;
import com.fittogether.database.UserDAO;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane; // Import ScrollPane
import javafx.stage.Stage;

public class LoginPage {
    private Stage primaryStage;
    private Scene scene;

    public LoginPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setupUI();
    }

    private void setupUI() {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.getStyleClass().add("root");

        // Page Title
        Label pageTitle = new Label("Fit Together");
        pageTitle.getStyleClass().add("page-title");

        // Section Title
        Label headline = new Label("Login to your account");
        headline.getStyleClass().add("headline");

        // Create input fields
        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email");
        emailField.getStyleClass().add("text-field");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.getStyleClass().add("password-field");

        // Create buttons
        Button loginButton = new Button("Login");
        loginButton.getStyleClass().add("button");

        Button registerButton = new Button("Create Account");
        registerButton.getStyleClass().add("button");

        // Error message label
        Label errorMessage = new Label();
        errorMessage.getStyleClass().add("error-message");

        // Action for login button
        loginButton.setOnAction(e -> {
            String email = emailField.getText();
            String password = passwordField.getText();

            // Check if any field is empty
            if (email.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Login Error", "All fields are required.");
                return;
            }

            // Validate email format
            if (!isValidEmail(email)) {
                showAlert(Alert.AlertType.ERROR, "Login Error", "Please enter a valid email.");
                return;
            }

            UserDAO userDAO = new UserDAO();
            User user = userDAO.findUserByEmailAndPassword(email, password);

            if (user != null) {
                // Navigate to HomePage on successful login
                HomePage homePage = new HomePage(primaryStage, user);
                primaryStage.setScene(homePage.getScene());
            } else {
                // Show an error message if login fails
                errorMessage.setText("Invalid email or password.");
                errorMessage.setStyle("-fx-text-fill: red;");
            }
        });

        // Action for register button
        registerButton.setOnAction(e -> {
            RegistrationPage registrationPage = new RegistrationPage(primaryStage);
            primaryStage.setScene(registrationPage.getScene());
        });

        // Add all components to the layout
        layout.getChildren().addAll(pageTitle, headline, emailField, passwordField, loginButton, registerButton, errorMessage);

        // Create a ScrollPane for the layout
        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true); // Fit content to the width of the ScrollPane
        scrollPane.setFitToHeight(true); // Enable vertical scrolling

        // Create the scene and set it on the primary stage
        this.scene = new Scene(scrollPane, 500, 600);
        
        // Load CSS stylesheets correctly
        scene.getStylesheets().add(getClass().getResource("/resources/login.css").toExternalForm());

        // Set scene to the primary stage
        primaryStage.setScene(scene);
    }

    // Method to return the scene
    public Scene getScene() {
        return scene;
    }

    // Method to display alert dialogs
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to validate email format
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        return email.matches(emailRegex);
    }
}
