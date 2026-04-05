package com.example.ParkingSimulator;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.util.List;
import java.util.ArrayList;
import javafx.scene.paint.Color;

public class Parking {
    private static final Logger LOGGER = Logger.getLogger(Parking.class.getName());
    private final Semaphore placesSemaphore;
    private final Lock placesLock = new ReentrantLock();
    private final List<Long> tempPlace = new ArrayList<>();
    private final List<Boolean> placeOccupied = new ArrayList<>();
    private final IStrategy strategy;
    private final int nbrPlaces;
    private final Main gui;
    private final int syncStrategy; // 1 for Semaphore, 2 for Mutex

    public Parking(int nbrPlaces, IStrategy strategy, Main main, int syncStrategy) {
        // Configure logging file
        try {
            FileHandler fileHandler = new FileHandler("../app.log", false);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            LOGGER.info("Logging file configured successfully");
        } catch (Exception e) {
            LOGGER.severe("Failed to configure logging file: " + e.getMessage());
            System.exit(1);
        }
        // Initialize semaphores and mutexes
        this.placesSemaphore = new Semaphore(nbrPlaces, true);
        this.syncStrategy = syncStrategy;
        // Initialize the list containing the time needed to exit each spot and the list of occupied spots
        for (int i = 0; i < nbrPlaces; i++) {
            tempPlace.add((i % 5 + 1) * 1000L); // The list of possible values is [1000, 2000, 3000, 4000, 5000]
            placeOccupied.add(false);
        }
        // Initialize the strategy
        this.strategy = strategy;
        // Initialize the number of parking spots
        this.nbrPlaces = nbrPlaces;
        // Initialize the GUI
        this.gui = main;
    }

    public void park(Voiture car) throws Exception {
        long startAttemptTime = System.currentTimeMillis();
        LOGGER.info("Car " + car.getCarName() + " tries to park");

        // Acquire a parking spot according to the synchronization strategy
        if (syncStrategy == 1) {
            placesSemaphore.acquire();
        } else if (syncStrategy == 2) {
            placesLock.lock();
        }

        try {
            int spot = strategy.findSpot(tempPlace, placeOccupied, nbrPlaces);
            placeOccupied.set(spot, true); // Mark the spot as occupied
            int numColumns = (int) Math.ceil(Math.sqrt(nbrPlaces));
            int col = spot % numColumns;
            int row = spot / numColumns;
            gui.setRectangleColor(col, row, Color.RED); // Update the color of the spot in the GUI
            gui.setRectangleText(col, row, car.getName()); // Update the text of the spot in the GUI
            car.setSpot(spot); // Assign the spot to the car
            LOGGER.info("Car " + car.getCarName() + " parked!");
        } finally {
            // Release the lock if the synchronization strategy is mutex
            if (syncStrategy == 2) {
                placesLock.unlock();
            }
        }

        long endAttemptTime = System.currentTimeMillis(); // Record the end of parking (successful or not)
        long parkingWaitTime = endAttemptTime - startAttemptTime; // Calculate the waiting time
        LOGGER.info("Waiting time for car " + car.getCarName() + ": " + parkingWaitTime + " milliseconds");
    }

    public void exit(Voiture car) {
        LOGGER.info("Car " + car.getCarName() + " tries to exit");

        // Acquire the lock if the synchronization strategy is mutex
        if (syncStrategy == 2) {
            placesLock.lock();
        }

        try {
            Thread.sleep(tempPlace.get(car.getSpot())); // Simulate the time needed to exit the spot
            placeOccupied.set(car.getSpot(), false); // Mark the spot as free
            int numColumns = (int) Math.ceil(Math.sqrt(nbrPlaces));
            int col = car.getSpot() % numColumns;
            int row = car.getSpot() / numColumns;
            gui.setRectangleColor(col, row, Color.GREEN); // Update the color of the spot in the GUI
            gui.setRectangleText(col, row, ""); // Clear the text of the spot in the GUI
            LOGGER.info("Car " + car.getCarName() + " exited!");
        } catch (Exception e) {
            LOGGER.warning("Exception " + e.getMessage());
        } finally {
            // Release the parking spot according to the synchronization strategy
            if (syncStrategy == 1) {
                placesSemaphore.release();
            } else if (syncStrategy == 2) {
                placesLock.unlock();
            }
        }
    }
}
