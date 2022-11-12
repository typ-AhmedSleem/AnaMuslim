/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.utils;

/**
 * Contains helper code that works with Mathematics
 */
public class MathUtils {

    /**
     * Finds and returns the largest number of the given array of numbers
     */
    public static int getLargestNumber(int... numbers) {
        // Some necessary checks
        if (numbers == null) return 0;
        if (numbers.length == 0) return 0;
        if (numbers.length == 1) return numbers[0];
        // Iteration
        int largestNumber = 0;
        for (int counter = 0; counter < numbers.length - 1; counter++) if (Math.max(numbers[counter], numbers[counter + 1]) >= largestNumber) largestNumber = numbers[counter];
        return largestNumber;
    }

    /**
     * Finds and returns the smallest number of the given array of numbers
     */
    public static int getSmallestNumber(int... numbers) {
        // Some necessary checks
        if (numbers == null) return 0;
        if (numbers.length == 0) return 0;
        if (numbers.length == 1) return numbers[0];
        // Iteration
        int smallestNumber = 0;
        for (int counter = 0; counter < numbers.length - 1; counter++) if (Math.min(numbers[counter], numbers[counter + 1]) <= smallestNumber) smallestNumber = Math.max(numbers[counter], numbers[counter + 1]);
        return smallestNumber;
    }

    public static float fixAngle(float angle) {
        return angle > 360 ? angle - 360 : angle < 0 ? angle + 360 : angle == 0 ? 360 : angle;
    }

}
