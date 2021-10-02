package com.typ.muslim.libs.easyjava.algorithms.sort;


import com.typ.muslim.libs.easyjava.enums.Direction;

import java.util.concurrent.TimeUnit;

public class LinearSortAlgorithm extends SortAlgorithm {

    @Override
    public void performSort(int[] numbers, Direction direction) {
        if (numbers == null) return;
        long startTime = System.nanoTime();
        for (int baseIndex = 0; baseIndex < numbers.length; baseIndex++) {
            if (baseIndex == numbers.length - 1) break;
            for (int childIndex = 0; childIndex < numbers.length - baseIndex - 1; childIndex++) {
                if (direction == Direction.ASCENDING ? numbers[childIndex] > numbers[childIndex + 1] : numbers[childIndex] < numbers[childIndex + 1]) {
                    final int temp = numbers[childIndex];
                    numbers[childIndex] = numbers[childIndex + 1];
                    numbers[childIndex + 1] = temp;
                }
            }
        }
        long endTime = System.nanoTime();
        // Print sorted array
        StringBuilder sortedNumbers = new StringBuilder("[ ");
        for (int number : numbers) sortedNumbers.append(number).append(" ");
        System.out.println(sortedNumbers.append("] Took: ").append(TimeUnit.NANOSECONDS.toSeconds(endTime - startTime)).append(" second").toString());
    }

}
