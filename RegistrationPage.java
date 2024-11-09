package com.fittogether.ui;

import com.fittogether.database.UserDAO;
import com.fittogether.model.User;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegistrationPage {
    private Stage primaryStage;
    private UserDAO userDAO; // Assume userDAO is already set up for database operations

    // Constructor to initialize primaryStage and UserDAO
    public RegistrationPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.userDAO = new UserDAO(); // Initialize UserDAO for database interaction
    }

    // Method to generate the registration page's scene
    public Scene getScene() {
        VBox layout = new VBox(10); // Create vertical layout with 10px spacing
        layout.getStylesheets().add(getClass().getResource("/resources/registration.css").toExternalForm()); // Link to CSS
        
        // Page Title
        Label pageTitle = new Label("Fit Together");
        pageTitle.getStyleClass().add("page-title");

        // Section Title
        Label headline = new Label("Create a new account");
        headline.getStyleClass().add("headline");
        
        // Input fields for user information
        TextField nameField = new TextField();
        nameField.setPromptText("Enter your name");

        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email");

        PasswordField passwordField = new PasswordField(); // Password field for security
        passwordField.setPromptText("Enter your password");
        
        TextField ageField = new TextField(); // Field for age
        ageField.setPromptText("Enter your age");

        Button registerButton = new Button("Create New User");
        Button backButton = new Button("Back to Login");

        // Registration button action to handle form submission
        registerButton.setOnAction(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            String ageText = ageField.getText();

            // Validate input fields - all fields are required
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || ageText.isEmpty()) {
                showAlert(AlertType.ERROR, "Registration Error", "All fields are required!");
                return;
            }

            // Validate email format
            if (!isValidEmail(email)) {
                showAlert(AlertType.ERROR, "Registration Error", "Invalid email format.");
                return;
            }

            // Validate age
            int age;
            try {
                age = Integer.parseInt(ageText);
                if (age < 0 || age > 120) {
                    showAlert(AlertType.ERROR, "Registration Error", "Please enter a valid age between 0 and 120.");
                    return;
                }
            } catch (NumberFormatException ex) {
                showAlert(AlertType.ERROR, "Registration Error", "Age must be a number.");
                return;
            }

            // Check if user already exists
            if (userDAO.userExists(email)) {
                showAlert(AlertType.ERROR, "Registration Error", "A user already exists with this email.");
            } else {
                // Create a new user and add to the database
                User newUser = new User(name, email, password, age); // Assuming User class has an age parameter
                userDAO.registerUser(newUser); // Save user to database

                // Show success alert and redirect to LoginPage
                showAlert(AlertType.INFORMATION, "Registration Successful", "User registered successfully.");
                register(); // Redirect to login page after successful registration
            }
        });

        // Back button to return to login page without registration
        backButton.setOnAction(e -> register());

        // Add all elements to the layout and create the scene
        layout.getChildren().addAll(nameField, emailField, passwordField, ageField, registerButton, backButton);
        Scene scene = new Scene(layout, 400, 300);
        return scene;
    }

    // Utility method to show alert dialogs
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Utility method to validate email format using a simple regex
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        return email.matches(emailRegex);
    }

    // Method to navigate back to the login page
    public void register() {
        LoginPage loginPage = new LoginPage(primaryStage);
        primaryStage.setScene(loginPage.getScene()); // Use getScene() to set the scene
    }
}
