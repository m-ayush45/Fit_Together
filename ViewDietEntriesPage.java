package com.fittogether.ui;

import com.fittogether.database.DietDAO;
import com.fittogether.database.DatabaseConnection;
import com.fittogether.model.Diet;
import com.fittogether.model.User;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.util.List;

public class ViewDietEntriesPage {
    private Stage primaryStage;
    private User user;
    private Scene scene;

    public ViewDietEntriesPage(Stage primaryStage, User user) {
        this.primaryStage = primaryStage;
        this.user = user;
        setupUI();
    }

    private void setupUI() {
        // Create the main layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label titleLabel = createTitleLabel("Your Diet Entries");

        // Create TableView to display diet entries
        TableView<Diet> dietTableView = createDietTableView();

        // Fetch diet entries from the database
        fetchDietEntries(dietTableView);

        // Create back button
        Button backButton = createBackButton();

        // Add components to the layout
        layout.getChildren().addAll(titleLabel, dietTableView, backButton);

        // Create a ScrollPane to enable scrolling
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(layout); // Set the layout as content of the scroll pane
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true); // Enable vertical scrolling if needed

        // Create the scene with the ScrollPane
        this.scene = new Scene(scrollPane, 600, 400); // Adjusted width for table
        scene.getStylesheets().add(getClass().getResource("/resources/diet.css").toExternalForm()); // Load CSS
    }

    private Label createTitleLabel(String title) {
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("title"); // Add title style
        return titleLabel;
    }

    private TableView<Diet> createDietTableView() {
        TableView<Diet> dietTableView = new TableView<>();
        dietTableView.getStyleClass().add("table-view"); // Add table view style

        // Define columns
        TableColumn<Diet, String> foodItemColumn = new TableColumn<>("Food Item");
        foodItemColumn.setCellValueFactory(new PropertyValueFactory<>("foodItem"));
        foodItemColumn.setPrefWidth(200); // Set preferred width for the column

        TableColumn<Diet, Integer> caloriesColumn = new TableColumn<>("Calories");
        caloriesColumn.setCellValueFactory(new PropertyValueFactory<>("calories"));
        caloriesColumn.setPrefWidth(100); // Set preferred width for the column

        TableColumn<Diet, String> mealTypeColumn = new TableColumn<>("Meal Type");
        mealTypeColumn.setCellValueFactory(new PropertyValueFactory<>("mealType"));
        mealTypeColumn.setPrefWidth(150); // Set preferred width for the column

        TableColumn<Diet, String> dateConsumedColumn = new TableColumn<>("Date Consumed");
        dateConsumedColumn.setCellValueFactory(new PropertyValueFactory<>("dateConsumed"));
        dateConsumedColumn.setPrefWidth(150); // Set preferred width for the column

        // Disable column resizing
        foodItemColumn.setResizable(false);
        caloriesColumn.setResizable(false);
        mealTypeColumn.setResizable(false);
        dateConsumedColumn.setResizable(false);

        // Add columns to the table
        dietTableView.getColumns().addAll(foodItemColumn, caloriesColumn, mealTypeColumn, dateConsumedColumn);
        
        return dietTableView;
    }

    private void fetchDietEntries(TableView<Diet> dietTableView) {
        try (Connection connection = DatabaseConnection.getConnection()) { // Obtain connection
            DietDAO dietDAO = new DietDAO(connection);
            List<Diet> diets = dietDAO.getDietEntriesByUserId(user.getId());
            System.out.println("Number of diet entries fetched: " + diets.size()); // Debug statement
            if (diets.isEmpty()) {
                showAlert("No Entries", "You have no diet entries recorded.");
            } else {
                dietTableView.getItems().addAll(diets); // Add all diets to the table
            }
        } catch (Exception e) {
            showAlert("Error retrieving diet entries", e.getMessage()); // Display error message
        }
    }

    private Button createBackButton() {
        Button backButton = new Button("Back to Diet Tracker");
        backButton.getStyleClass().add("button"); // Add button style
        backButton.setOnAction(e -> goBackToDietTracker());
        return backButton;
    }

    private void goBackToDietTracker() {
        DietTrackerPage dietTrackerPage = new DietTrackerPage(primaryStage, user);
        primaryStage.setScene(dietTrackerPage.getScene());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Scene getScene() {
        return scene;
    }
}
