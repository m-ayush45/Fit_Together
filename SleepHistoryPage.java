package com.fittogether.ui;

import com.fittogether.model.User;
import com.fittogether.model.Sleep;
import com.fittogether.database.SleepDAO;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.List;

public class SleepHistoryPage {
    private Stage primaryStage;
    private User user;
    private SleepDAO sleepDAO;

    public SleepHistoryPage(Stage primaryStage, User user) {
        this.primaryStage = primaryStage;
        this.user = user;
        this.sleepDAO = new SleepDAO();
        setupUI();
    }

    private void setupUI() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getStyleClass().add("vbox"); // Add CSS class

        // Title label
        Label titleLabel = new Label("Sleep History");
        titleLabel.getStyleClass().add("label"); // Add CSS class

        // Fetch sleep history from the database
        List<Sleep> sleepHistory = sleepDAO.getUserSleepHistory(user.getId());

        // Create a table to display sleep history
        TableView<Sleep> tableView = new TableView<>();
        tableView.getStyleClass().add("table-view"); // Add CSS class

        // Create the columns with the appropriate data binding
        TableColumn<Sleep, String> startTimeColumn = new TableColumn<>("Sleep Time");
        startTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStartTime().toString()));

        TableColumn<Sleep, String> endTimeColumn = new TableColumn<>("WakeUp Time");
        endTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEndTime().toString()));

        TableColumn<Sleep, String> durationColumn = new TableColumn<>("Duration (hours)");
        durationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getDuration())));

        TableColumn<Sleep, String> moodColumn = new TableColumn<>("Mood");
        moodColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMood()));

        TableColumn<Sleep, String> disturbancesColumn = new TableColumn<>("Disturbances");
        disturbancesColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDisturbances()));

        TableColumn<Sleep, Integer> sleepQualityColumn = new TableColumn<>("Sleep Quality");
        sleepQualityColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuality()).asObject());

        TableColumn<Sleep, String> sleepStatusColumn = new TableColumn<>("Sleep Status");
        sleepStatusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(calculateSleepStatus(cellData.getValue())));

        // Add the columns to the table
        tableView.getColumns().addAll(startTimeColumn, endTimeColumn, durationColumn, moodColumn, disturbancesColumn, sleepQualityColumn, sleepStatusColumn);

        // Populate the table with sleep history data
        tableView.getItems().setAll((List<Sleep>) sleepHistory);

        // Back button logic to go back to the Sleep Tracker Page
        Button backButton = new Button("Back to Sleep Tracker");
        backButton.getStyleClass().add("button"); // Add CSS class

        backButton.setOnAction(e -> {
            SleepTrackerPage sleepTrackerPage = new SleepTrackerPage(primaryStage, user);
            primaryStage.setScene(sleepTrackerPage.getScene());
        });

        // Add components to the layout
        layout.getChildren().addAll(titleLabel, tableView, backButton);

        // Wrap the layout in a ScrollPane to make it scrollable
        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);

        // Set the scene
        Scene scene = new Scene(scrollPane, 600, 400);
        scene.getStylesheets().add(getClass().getResource("/sleep.css").toExternalForm()); // Add CSS file
        primaryStage.setScene(scene);
    }

    // Method to calculate sleep status based on sleep quality and disturbances
    private String calculateSleepStatus(Sleep sleep) {
        int quality = sleep.getQuality();
        String disturbances = sleep.getDisturbances();

        // Consider sleep unhealthy if the quality is less than 3 or disturbances are high
        if (quality < 3 || disturbances.equals("Many") || disturbances.equals("Interrupted") || disturbances.equals("Restless")) {
            return "Unhealthy";
        } else {
            return "Healthy";
        }
    }

    public Scene getScene() {
        return this.primaryStage.getScene();
    }
}
