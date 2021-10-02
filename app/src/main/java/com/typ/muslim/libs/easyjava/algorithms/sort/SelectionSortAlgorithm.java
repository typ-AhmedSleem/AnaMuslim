package com.typ.muslim.libs.easyjava.algorithms.sort;


import com.typ.muslim.libs.easyjava.enums.Direction;

import java.util.concurrent.TimeUnit;

public class SelectionSortAlgorithm extends SortAlgorithm {

    @Override
    public void performSort(int[] numbers, Direction direction) {
        long startTime = System.nanoTime();
        for (int counter = 0; counter < numbers.length - 1; counter++) {
            for (int innerCounter = counter; innerCounter < numbers.length; innerCounter++) {
                if (direction == Direction.ASCENDING ? numbers[counter] > numbers[innerCounter] : numbers[counter] < numbers[innerCounter]) {
                    final int temp = numbers[counter];
                    numbers[counter] = numbers[innerCounter];
                    numbers[innerCounter] = temp;
                }
            }
        }
        long endTime = System.nanoTime();
        // Print sorted array
        StringBuilder sortedNumbers = new StringBuilder("[ ");
        for (int number : numbers) sortedNumbers.append(number).append(" ");
        System.out.println(sortedNumbers.append("] Took: ").append(TimeUnit.NANOSECONDS.toSeconds(endTime-startTime)).append(" second").toString());
    }

}
