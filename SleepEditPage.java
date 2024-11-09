package com.fittogether.ui;

import com.fittogether.model.User;
import com.fittogether.model.Sleep;
import com.fittogether.database.SleepDAO;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.time.LocalDateTime;

public class SleepEditPage {
    private Stage primaryStage;
    private User user;
    private SleepDAO sleepDAO;
    private Sleep sleepToEdit;

    public SleepEditPage(Stage primaryStage, User user, Sleep sleepToEdit) {
        this.primaryStage = primaryStage;
        this.user = user;
        this.sleepDAO = new SleepDAO();
        this.sleepToEdit = sleepToEdit;
        setupUI();
    }

    private void setupUI() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getStyleClass().add("vbox");  // Add CSS class

        // Title label
        Label titleLabel = new Label("Edit Sleep Log");
        titleLabel.getStyleClass().add("title-label");  // Add CSS class

        // Create fields for start time, end time, and other information
        Label startTimeLabel = new Label("Sleep Time:");
        startTimeLabel.getStyleClass().add("form-label");  // Add CSS class

        TextField startHourField = new TextField(String.valueOf(sleepToEdit.getStartTime().getHour()));
        TextField startMinuteField = new TextField(String.valueOf(sleepToEdit.getStartTime().getMinute()));

        Label endTimeLabel = new Label("WakeUp Time:");
        endTimeLabel.getStyleClass().add("form-label");  // Add CSS class

        TextField endHourField = new TextField(String.valueOf(sleepToEdit.getEndTime().getHour()));
        TextField endMinuteField = new TextField(String.valueOf(sleepToEdit.getEndTime().getMinute()));

        Label moodLabel = new Label("Mood:");
        moodLabel.getStyleClass().add("form-label");  // Add CSS class

        ComboBox<String> moodComboBox = new ComboBox<>();
        moodComboBox.getItems().addAll("Happy", "Neutral", "Tired");
        moodComboBox.setValue(sleepToEdit.getMood());

        Label disturbancesLabel = new Label("Disturbances:");
        disturbancesLabel.getStyleClass().add("form-label");  // Add CSS class

        ComboBox<String> disturbancesComboBox = new ComboBox<>();
        disturbancesComboBox.getItems().addAll("None", "Few", "Many", "Interrupted", "Restless");
        disturbancesComboBox.setValue(sleepToEdit.getDisturbances());

        Label sleepQualityLabel = new Label("Sleep Quality:");
        sleepQualityLabel.getStyleClass().add("form-label");  // Add CSS class

        ComboBox<Integer> sleepQualityComboBox = new ComboBox<>();
        sleepQualityComboBox.getItems().addAll(1, 2, 3, 4, 5);
        sleepQualityComboBox.setValue(sleepToEdit.getQuality());

        Button saveButton = new Button("Save Changes");
        saveButton.getStyleClass().add("button");  // Add CSS class

        Button deleteButton = new Button("Delete Sleep Log");
        deleteButton.getStyleClass().add("button");  // Add CSS class

        Button cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().add("button");  // Add CSS class

        // Handle saving the edited sleep log
        saveButton.setOnAction(e -> {
            try {
                // Parse start and end time values
                int startHour = Integer.parseInt(startHourField.getText());
                int startMinute = Integer.parseInt(startMinuteField.getText());
                int endHour = Integer.parseInt(endHourField.getText());
                int endMinute = Integer.parseInt(endMinuteField.getText());

                // Update the start and end time using LocalDateTime
                LocalDateTime startDateTime = LocalDateTime.of(sleepToEdit.getStartTime().toLocalDate(),
                        LocalDateTime.of(0, 1, 1, startHour, startMinute).toLocalTime());
                LocalDateTime endDateTime = LocalDateTime.of(sleepToEdit.getEndTime().toLocalDate(),
                        LocalDateTime.of(0, 1, 1, endHour, endMinute).toLocalTime());

                // Collect the updated details
                String mood = moodComboBox.getValue();
                String disturbances = disturbancesComboBox.getValue();
                int sleepQuality = sleepQualityComboBox.getValue();

                // Update the Sleep object
                Sleep updatedSleep = new Sleep(user.getId(), startDateTime, endDateTime, sleepToEdit.getDuration(), mood, disturbances, sleepQuality);

                // Update the record in the database
                sleepDAO.updateSleep(updatedSleep);

                // Show confirmation message
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Sleep log updated successfully!", ButtonType.OK);
                alert.showAndWait();

                // Navigate back to the Sleep Tracker Page
                SleepTrackerPage sleepTrackerPage = new SleepTrackerPage(primaryStage, user);
                primaryStage.setScene(sleepTrackerPage.getScene());
            } catch (NumberFormatException ex) {
                // Show an error if time is invalid
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter valid times.", ButtonType.OK);
                alert.showAndWait();
            }
        });

        // Handle canceling the changes
        cancelButton.setOnAction(e -> {
            SleepTrackerPage sleepTrackerPage = new SleepTrackerPage(primaryStage, user);
            primaryStage.setScene(sleepTrackerPage.getScene());
        });

        // Handle deleting the sleep log
        deleteButton.setOnAction(e -> {
            // Confirm the deletion with a dialog
            Alert confirmDeleteAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this sleep log?", ButtonType.YES, ButtonType.NO);
            confirmDeleteAlert.setTitle("Delete Confirmation");
            confirmDeleteAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    // Delete the sleep log from the database
                    sleepDAO.deleteSleep(sleepToEdit.getUserId());

                    // Show success message
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Sleep log deleted successfully!", ButtonType.OK);
                    alert.showAndWait();

                    // Navigate back to the Sleep Tracker Page
                    SleepTrackerPage sleepTrackerPage = new SleepTrackerPage(primaryStage, user);
                    primaryStage.setScene(sleepTrackerPage.getScene());
                }
            });
        });

        // Add components to the layout
        layout.getChildren().addAll(
                titleLabel, startTimeLabel, startHourField, startMinuteField, endTimeLabel, endHourField, endMinuteField,
                moodLabel, moodComboBox, disturbancesLabel, disturbancesComboBox, sleepQualityLabel, sleepQualityComboBox,
                saveButton, deleteButton, cancelButton);

        // Wrap the layout in a ScrollPane to make it scrollable
        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);

        // Set the scene and add the CSS file
        Scene scene = new Scene(scrollPane, 400, 400);
        scene.getStylesheets().add(getClass().getResource("/sleepedit.css").toExternalForm());  // Link to CSS
        primaryStage.setScene(scene);
    }

    public Scene getScene() {
        return this.primaryStage.getScene();
    }
}
