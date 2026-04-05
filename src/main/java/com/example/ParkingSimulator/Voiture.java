package com.example.ParkingSimulator;
import java.util.Random;

// Car class
public class Voiture extends Thread {
    private final String carName;
    private final Parking parking;
    private int spot;

    public Voiture(String carName, Parking parking) {
        this.carName = carName;
        this.parking = parking;
    }
    
    public String getCarName() {
        return this.carName;
    }

    public void setSpot(int spot) {
        this.spot = spot;
    }

    public int getSpot() {
        return this.spot;
    }

    Random random = new Random();

    @Override
    public void run() {
        try {
            // Try to park the car
            parking.park(this);

            // Sleep the thread to simulate the time the car remains parked
            Thread.sleep(random.nextInt(2000) + 10000);

            // Try to exit the car from the parking lot
            parking.exit(this);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
