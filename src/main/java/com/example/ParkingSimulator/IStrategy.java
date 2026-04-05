package com.example.ParkingSimulator;
import java.util.List;

public interface IStrategy {
    public int findSpot(List<Long> tempPlace, List<Boolean> spotOccupied, int nbrPlaces);
}
