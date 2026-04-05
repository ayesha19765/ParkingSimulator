package com.example.ParkingSimulator;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class Main extends Application {
    private CustomRectangle[][] matrix;
    private int syncStrategy; // Variable to store the chosen synchronization strategy
    private int numCars; // Number of cars
    private int nbrPlaces; // Number of parking spots

    @Override
    public void start(Stage primaryStage) {
        // Get the synchronization strategy from the user
        syncStrategy = getSyncStrategyFromUser();
        // Get the number of cars from the user
        numCars = getNumFromUser("Number of cars", "Enter the number of cars:");
        // Get the number of parking spots from the user
        nbrPlaces = getNumFromUser("Number of parking spots", "Enter the number of parking spots:");

        // Calculate the number of columns and rows
        int numColumns = (int) Math.ceil(Math.sqrt(nbrPlaces));
        int numRows = (int) Math.ceil((double) nbrPlaces / numColumns);

        // Initialize the grid
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER); // Center the grid
        matrix = new CustomRectangle[numColumns][numRows];

        // Initialize and add custom rectangles to the grid
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numColumns; col++) {
                matrix[col][row] = new CustomRectangle();
                grid.add(matrix[col][row], col, row);
                if (row * numColumns + col < nbrPlaces) {
                    setRectangleColor(col, row, Color.GREEN); // Set green color for available spots
                } else {
                    setRectangleColor(col, row, Color.GREY); // Set grey color for unavailable spots
                }
            }
        }

        // Create a button to display waiting time statistics
        Button statsButton = new Button("Display average waiting time");
        statsButton.setStyle(
                "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10px 20px; -fx-font-size: 14px; -fx-font-family: 'Arial'; -fx-border-radius: 5px; -fx-background-radius: 5px;"
        );
        statsButton.setOnAction(e -> {
            String stats = analyze("../app.log"); // Analyze the log file to get statistics
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Waiting time statistics");
            alert.setHeaderText(null);
            alert.setContentText(stats);
            alert.showAndWait();
        });

        // Create a vertical container (VBox) for the grid and button
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(15));
        vbox.setAlignment(Pos.CENTER); // Center the content of the VBox
        vbox.getChildren().addAll(grid, statsButton);

        // Create and display the scene
        Scene scene = new Scene(vbox, 400, 400);
        primaryStage.setTitle("Parking system");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Initialize the default strategy and parking
        IStrategy defaultStrategy = new DefaultStrategy();
        Parking p = new Parking(nbrPlaces, defaultStrategy, this, syncStrategy);
        Random random = new Random();
        for (int t = 0; t < numCars; t++) {
            final int finalT = t;
            PauseTransition pause = new PauseTransition(Duration.millis(random.nextInt(3000) + 5000));
            pause.setOnFinished(event -> {
                Voiture v = new Voiture("Car " + finalT, p); // Create a new car
                v.start(); // Start the car
            });
            pause.play(); // Start the pause
        }
    }

    // Method to set the color of a specific rectangle
    public void setRectangleColor(int col, int row, Color color) {
        if (col >= 0 && col < matrix.length && row >= 0 && row < matrix[col].length) {
            matrix[col][row].setColor(color);
        }
    }

    // Method to set the text of a specific rectangle
    public void setRectangleText(int col, int row, String text) {
        if (col >= 0 && col < matrix.length && row >= 0 && row < matrix[col].length) {
            matrix[col][row].setText(text);
        }
    }

    // Method to get the synchronization strategy from the user
    private int getSyncStrategyFromUser() {
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Synchronization strategy selection");
        dialog.setHeaderText("Select the synchronization strategy");
        dialog.setContentText("Enter 1 for Semaphore or 2 for Mutex:");

        Optional<String> result = dialog.showAndWait();
        return result.map(Integer::parseInt).orElse(1); // Default to semaphore
    }

    // Method to get a number from the user
    private int getNumFromUser(String title, String content) {
        TextInputDialog dialog = new TextInputDialog("10");
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText(content);

        Optional<String> result = dialog.showAndWait();
        return result.map(Integer::parseInt).orElse(10); // Default to 10
    }

    // Method to analyze the log file and get waiting time information
    public static String analyze(String filePath) {
        List<Double> waitingTimeList = new ArrayList<>();
        double totalWaitTime = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("Waiting time for car")) {
                    int startIndex = line.lastIndexOf(":") + 2;
                    int endIndex = line.lastIndexOf(" milliseconds");
                    String waitTimeStr = line.substring(startIndex, endIndex);
                    // Remove all non-numeric characters
                    waitTimeStr = waitTimeStr.replaceAll("[^\\d.]", "");
                    double waitTime = Double.parseDouble(waitTimeStr);
                    waitingTimeList.add(waitTime);
                    totalWaitTime += waitTime;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder result = new StringBuilder();
        if (!waitingTimeList.isEmpty()) {
            result.append("Waiting time for each car:\n");
            for (int i = 0; i < waitingTimeList.size(); i++) {
                result.append("Car ").append(i + 1).append(": ").append(waitingTimeList.get(i)).append(" milliseconds\n");
            }

            double averageWaitTime = totalWaitTime / waitingTimeList.size();
            result.append("Average waiting time for cars: ").append(averageWaitTime / 1000).append(" Seconds");
        } else {
            result.append("No information about car waiting times found in the log file.");
        }
        return result.toString();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
