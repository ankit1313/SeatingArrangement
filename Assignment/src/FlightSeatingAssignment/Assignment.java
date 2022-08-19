package FlightSeatingAssignment;

import java.util.Scanner;

/**
 *
 * @author Ankit Kumar
 * @since 2022-08-18
 *
 */
public class Assignment {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // define the input 2D Array for input
        int[][] array = new int[10][2];
        int maxRowsIterator = 0;
        int totalColsIterator = 0;

        Scanner input = new Scanner(System.in);

        System.out.println("Enter the number of blocks for seating arrangement: ");
        int numberOfBlocks = input.nextInt();

        System.out.println("Enter the number of people waiting for seating arrangement: ");
        int numberOfPeople = input.nextInt();

        // loop for row  
        for (int i = 0; i < numberOfBlocks; i++) {
            // inner for loop for column  
            for (int j = 0; j < 2; j++) {
                if (j == 0) {
                    System.out.println("Enter the number of columns for block: " + (i + 1));
                    array[i][j] = input.nextInt();

                    // calculating total columns
                    totalColsIterator += array[i][j];
                }
                if (j == 1) {
                    System.out.println("Enter the number of rows for block: " + (i + 1));
                    array[i][j] = input.nextInt();

                    // calculating max number of rows
                    if (maxRowsIterator < array[i][1]) {
                        maxRowsIterator = array[i][j];
                    }
                }
            }
        }

        System.out.println("Max number of rows considering all blocks is: " + maxRowsIterator);
        System.out.println("Total number of columns considering all blocks is: " + totalColsIterator);

        createSeatingArrangement(array, numberOfBlocks, numberOfPeople, maxRowsIterator, totalColsIterator);

    }

    /**
     * Calculate the seating arrangement chart based on priority of aisle,
     * window and middle seats.
     *
     * @param array
     * @param blocks
     * @param people
     * @param maxRows
     * @param totalCols
     * @return
     */
    private static void createSeatingArrangement(int[][] array, int blocks, int people, int maxRows, int totalCols) {

        // Creating output 2D array as a consolidated result matrix.
        // We can also use 3D array but it will only increase the space complexity without any improvements in time complexity
        // To keep the blocks separated in output, we will write a logic separately.
        int outputSeatingChart[][] = new int[maxRows][totalCols];
        int counter = 1;

        counter = fillAisleSeats(outputSeatingChart, array, blocks, maxRows, counter);
        counter = fillWindowSeats(outputSeatingChart, array, blocks, counter, totalCols);
        counter = fillMiddleSeats(outputSeatingChart, maxRows, totalCols, counter);

        displayOutputResults(outputSeatingChart, maxRows, totalCols, people, counter);
    }

    /**
     * Calculate the seating arrangement chart for aisle seats.
     *
     *
     * @param outputSeatingChart
     * @param array
     * @param blocks
     * @param people
     * @param maxRows
     * @param counter
     * @return
     */
    private static int fillAisleSeats(int[][] outputSeatingChart, int[][] array, int blocks, int maxRows, int counter) {

        // first we will set the arrangement for aisle seats
        // rows iterator
        for (int i = 0; i < maxRows; i++) {
            int k = 0; // to store the last column position updated in the output array

            for (int j = 0; j < blocks; j++) {
                if (j == 0) {
                    // making the last column of the current block as the last updated position
                    // when both cases rows eligible/non-eligible are into existence
                    if (i >= array[j][1]) {
                        k += array[j][0] - 1;
                        continue;
                    } else {
                        k += array[j][0] - 1;
                        outputSeatingChart[i][k] = counter;
                        counter++;
                    }
                } else if (j == blocks - 1) {
                    // skip next execution if last block rows are not eligible
                    if (i >= array[j][1]) {
                        continue;
                    }
                    // Last block only has one aisle
                    k += 1;
                    outputSeatingChart[i][k] = counter;
                    counter++;
                } else if (j != 0 && j != blocks - 1) {
                    // Update value for k skipping the middle blocks if rows are not eligible.
                    if (i >= array[j][1]) {
                        k += array[j][0];
                        continue;
                    }
                    // For middle blocks, we can set two aisle at once
                    k += 1;
                    outputSeatingChart[i][k] = counter;
                    counter++;

                    k += array[j][0] - 1;
                    outputSeatingChart[i][k] = counter;
                    counter++;
                }
            }
        }
        
        return counter;
    }

    /**
     * Calculate the seating arrangement chart for window seats.
     *
     *
     * @param outputSeatingChart
     * @param array
     * @param blocks
     * @param people
     * @param counter
     * @param totalCols
     * @return
     */
    private static int fillWindowSeats(int[][] outputSeatingChart, int[][] array, int blocks, int counter, int totalCols) {

        int leftWindowSeats = array[0][1], rightWindowSeats = array[blocks - 1][1];
        int compareFlag = 0, min = 0;

        // finding the block with more rows based on flag
        if (leftWindowSeats >= rightWindowSeats) {
            compareFlag = 0;
            min = rightWindowSeats;
        } else if (leftWindowSeats < rightWindowSeats) {
            compareFlag = 1;
            min = leftWindowSeats;
        }

        for (int i = 0; i < min; i++) {
            // Setting the left window
            outputSeatingChart[i][0] = counter;
            counter++;
            // Setting right window
            outputSeatingChart[i][totalCols - 1] = counter;
            counter++;
        }

        // Based on more rows in either side of window, we do the increment based on the flag.
        if (compareFlag == 0) {
            for (int i = min; i < leftWindowSeats; i++) {
                outputSeatingChart[i][0] = counter;
                counter++;
            }
        } else if (compareFlag == 1) {
            for (int i = min; i < rightWindowSeats; i++) {
                outputSeatingChart[i][totalCols - 1] = counter;
                counter++;
            }
        }
        
        return counter;
    }

    /**
     * Calculate the seating arrangement chart for aisle seats.
     *
     *
     * @param outputSeatingChart
     * @param array
     * @param blocks
     * @param people
     * @param maxRows
     * @param totalCols
     * @return
     */
    private static int fillMiddleSeats(int[][] outputSeatingChart, int maxRows, int totalCols, int counter) {

        // Set the remaining middle seats with the counter in required sequence.
        for (int i = 0; i < maxRows; i++) {
            for (int j = 1; j < totalCols - 1; j++) { 
                // checking adjacent cell for invalid values to avoid filling non-required cells in output 2d array
                if (outputSeatingChart[i][j] == 0 && outputSeatingChart[i][j-1] != 0) {
                    outputSeatingChart[i][j] = counter;
                    counter++;
                }
            }
        }
        
        return counter - 1;
    }
    
    /**
     * Calculate the seating arrangement chart based on priority of aisle,
     * window and middle seats.
     *
     * @param outputSeatingChart
     * @param maxRows
     * @param totalCols
     * @return
     */
    private static void displayOutputResults(int[][] outputSeatingChart, int maxRows, int totalCols, int people, int counter) {
        
        if (counter >= people) {
            System.out.println("\nSeat assignment done successfully!!!");
        }
        else {
            System.out.println("\nPeople count is more than available seats!!!");
        }

        System.out.println("\nOutput arrangement plan: \n");
        for (int i = 0; i < maxRows; i++) {
            for (int j = 0; j < totalCols; j++) {
                // Don't display unrequired records like 0
                if (outputSeatingChart[i][j] == 0 || outputSeatingChart[i][j] > people) {
                    System.out.printf(" " + "\t");
                } else {
                    System.out.printf(outputSeatingChart[i][j] + "\t");
                }
            }
            System.out.println("\n");
        }
    }
}
