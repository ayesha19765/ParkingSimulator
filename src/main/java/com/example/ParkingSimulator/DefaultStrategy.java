package com.example.ParkingSimulator;
import java.util.List;

public class DefaultStrategy implements IStrategy {
    @Override
    public int findSpot(List<Long> tempPlace, List<Boolean> spotOccupied, int nbrPlaces) {
        // Calculate the number of columns based on the number of spots
        int numColumns = (int) Math.ceil(Math.sqrt(nbrPlaces));
        // Calculate the number of rows based on the number of columns and spots
        int numRows = (int) Math.ceil((double) nbrPlaces / numColumns);

        // Iterate through each column
        for (int col = 0; col < numColumns; col++) {
            // Iterate through each row
            for (int row = 0; row < numRows; row++) {
                // Calculate the index of the current spot
                int spot = col + row * numColumns;
                // Check if the index is valid and if the spot is not occupied
                if (spot < nbrPlaces && !spotOccupied.get(spot)) {
                    return spot; // Return the index of the first available spot found
                }
            }
        }
        return -1; // Return -1 if no spot is found
    }
}
