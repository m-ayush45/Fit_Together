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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class SleepLogPage {
    private Stage primaryStage;
    private User user;
    private SleepDAO sleepDAO;

    public SleepLogPage(Stage primaryStage, User user) {
        this.primaryStage = primaryStage;
        this.user = user;
        this.sleepDAO = new SleepDAO();  // Initialize the SleepDAO to interact with the database
        setupUI();
    }

    private void setupUI() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        
        // Title label
        Label titleLabel = new Label("Add Sleep Log");
        titleLabel.getStyleClass().add("headline"); // Apply CSS class for title

        // Add Labels (Titles) for Start Time, End Time, Mood, Disturbances, and Sleep Quality
        Label startTimeLabel = new Label("Sleep Time:");
        startTimeLabel.getStyleClass().add("label"); // Apply CSS class

        // Creating a DatePicker for selecting date and ComboBox for hour and minute selection
        DatePicker startDatePicker = new DatePicker();
        startDatePicker.getStyleClass().add("combo-box"); // Apply CSS class

        ComboBox<Integer> startHourComboBox = new ComboBox<>();
        for (int i = 0; i < 24; i++) {
            startHourComboBox.getItems().add(i);
        }
        startHourComboBox.setPromptText("Hour");
        startHourComboBox.getStyleClass().add("combo-box"); // Apply CSS class

        ComboBox<Integer> startMinuteComboBox = new ComboBox<>();
        for (int i = 0; i < 60; i++) {
            startMinuteComboBox.getItems().add(i);
        }
        startMinuteComboBox.setPromptText("Minute");
        startMinuteComboBox.getStyleClass().add("combo-box"); // Apply CSS class

        Label endTimeLabel = new Label("Wake Up Time:");
        endTimeLabel.getStyleClass().add("label"); // Apply CSS class

        // Creating ComboBoxes for end time hour and minute selection
        ComboBox<Integer> endHourComboBox = new ComboBox<>();
        for (int i = 0; i < 24; i++) {
            endHourComboBox.getItems().add(i);
        }
        endHourComboBox.setPromptText("Hour");
        endHourComboBox.getStyleClass().add("combo-box"); // Apply CSS class

        ComboBox<Integer> endMinuteComboBox = new ComboBox<>();
        for (int i = 0; i < 60; i++) {
            endMinuteComboBox.getItems().add(i);
        }
        endMinuteComboBox.setPromptText("Minute");
        endMinuteComboBox.getStyleClass().add("combo-box"); // Apply CSS class

        Label moodLabel = new Label("Mood:");
        moodLabel.getStyleClass().add("label"); // Apply CSS class
        ComboBox<String> moodComboBox = new ComboBox<>();
        moodComboBox.getItems().addAll("Happy", "Neutral", "Tired");
        moodComboBox.getStyleClass().add("combo-box"); // Apply CSS class

        Label disturbancesLabel = new Label("Disturbances:");
        disturbancesLabel.getStyleClass().add("label"); // Apply CSS class
        ComboBox<String> disturbancesComboBox = new ComboBox<>();
        disturbancesComboBox.getItems().addAll("None", "Few", "Many", "Interrupted", "Restless");
        disturbancesComboBox.getStyleClass().add("combo-box"); // Apply CSS class

        Label sleepQualityLabel = new Label("Sleep Quality:");
        sleepQualityLabel.getStyleClass().add("label"); // Apply CSS class
        ComboBox<Integer> sleepQualityComboBox = new ComboBox<>();
        sleepQualityComboBox.getItems().addAll(1, 2, 3, 4, 5);
        sleepQualityComboBox.getStyleClass().add("combo-box"); // Apply CSS class

        Button logSleepButton = new Button("Log Sleep");
        logSleepButton.getStyleClass().add("sleep-button"); // Apply CSS class for button
        Button backButton = new Button("Back to Sleep Tracker");
        backButton.getStyleClass().add("sleep-button"); // Apply CSS class for button

        // Logic for the "Log Sleep" button
        logSleepButton.setOnAction(e -> {
            // Validate input fields
            LocalDate startDate = startDatePicker.getValue();
            Integer startHour = startHourComboBox.getValue();
            Integer startMinute = startMinuteComboBox.getValue();
            Integer endHour = endHourComboBox.getValue();
            Integer endMinute = endMinuteComboBox.getValue();
            String mood = moodComboBox.getValue();
            String disturbances = disturbancesComboBox.getValue();
            Integer sleepQuality = sleepQualityComboBox.getValue();

            // Validate all fields are filled
            if (startDate != null && startHour != null && startMinute != null && endHour != null && endMinute != null
                    && mood != null && disturbances != null && sleepQuality != null) {

                // Create the start and end LocalDateTime objects
                LocalDateTime startDateTime = startDate.atTime(startHour, startMinute);
                LocalDateTime endDateTime = startDate.atTime(endHour, endMinute);

                // Ensure the end time is after the start time
                if (startDateTime.isBefore(endDateTime)) {
                    long durationInMinutes = ChronoUnit.MINUTES.between(startDateTime, endDateTime);
                    double durationInHours = durationInMinutes / 60.0;

                    // Create a Sleep object
                    Sleep sleep = new Sleep(user.getId(), startDateTime, endDateTime, durationInHours, mood, disturbances, sleepQuality);

                    // Log the sleep record to the database
                    sleepDAO.logSleep(sleep);

                    // Show confirmation alert
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Sleep logged successfully!", ButtonType.OK);
                    alert.showAndWait();
                } else {
                    // Show error if end time is before start time
                    Alert alert = new Alert(Alert.AlertType.ERROR, "End time cannot be before start time.", ButtonType.OK);
                    alert.showAndWait();
                }
            } else {
                // Show error if any field is empty
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill all fields before logging sleep.", ButtonType.OK);
                alert.showAndWait();
            }
        });

        // Back button logic to return to Sleep Tracker Page
        backButton.setOnAction(e -> {
            SleepTrackerPage sleepTrackerPage = new SleepTrackerPage(primaryStage, user);
            primaryStage.setScene(sleepTrackerPage.getScene());
        });

        // Add input fields to layout
        layout.getChildren().addAll(
                titleLabel,
                startTimeLabel, startDatePicker, startHourComboBox, startMinuteComboBox,
                endTimeLabel, endHourComboBox, endMinuteComboBox,
                moodLabel, moodComboBox,
                disturbancesLabel, disturbancesComboBox,
                sleepQualityLabel, sleepQualityComboBox,
                logSleepButton, backButton
        );

        // Wrap the layout in a ScrollPane to make it scrollable
        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);

        // Set the scene with the scrollable layout
        Scene scene = new Scene(scrollPane, 400, 400);

        // Load CSS file
        scene.getStylesheets().add(getClass().getResource("/resources/sleep.css").toExternalForm());

        primaryStage.setScene(scene);
    }

    public Scene getScene() {
        return this.primaryStage.getScene();
    }
}
