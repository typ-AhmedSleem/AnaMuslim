package com.typ.muslim.libs.easyjava.algorithms.sort;

import com.typ.muslim.libs.easyjava.enums.Direction;

import java.util.concurrent.TimeUnit;

public class InsertionSortAlgorithm extends SortAlgorithm {

    @Override
    public void performSort(int[] numbers, Direction direction) {
        long startTime = System.nanoTime();
        for (int i = 1; i < numbers.length; i++) {
            int currValue = numbers[i];
            int backLooper = i - 1;
            while (backLooper >= 0 && (direction == Direction.ASCENDING ? numbers[backLooper] > currValue : numbers[backLooper] < currValue)) {
                final int temp = numbers[backLooper];
                numbers[backLooper] = currValue;
                numbers[backLooper + 1] = temp;
                backLooper--;
            }
        }
        long endTime = System.nanoTime();
        // Print sorted array
        StringBuilder sortedNumbers = new StringBuilder("[ ");
        for (int number : numbers) sortedNumbers.append(number).append(" ");
        System.out.println(sortedNumbers.append("] Took: ").append(TimeUnit.NANOSECONDS.toSeconds(endTime - startTime)).append(" second").toString());
    }

}
