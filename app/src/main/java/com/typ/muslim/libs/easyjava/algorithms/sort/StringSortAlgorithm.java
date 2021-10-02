package com.typ.muslim.libs.easyjava.algorithms.sort;


import com.typ.muslim.libs.easyjava.enums.Direction;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringSortAlgorithm {

    public void performSort(String[] unsortedStrings, boolean ignoreCase, Direction direction) {
        this.performSort(Arrays.asList(unsortedStrings), ignoreCase, direction);
    }

    public void performSort(List<String> unsortedStrings, boolean ignoreCase, Direction direction) {
        // null Checks
        if (unsortedStrings == null) return;
        if (direction == null) direction = Direction.ASCENDING;
        // Sort string list
        for (int outerIndex = 0; outerIndex < unsortedStrings.size(); outerIndex++) {
            for (int innerIndex = 0; innerIndex < unsortedStrings.size() - outerIndex - 1; innerIndex++) {
                final int compareResult = ignoreCase ? unsortedStrings.get(innerIndex).compareToIgnoreCase(unsortedStrings.get(innerIndex + 1)) : unsortedStrings.get(innerIndex).compareTo(unsortedStrings.get(innerIndex + 1));
                if (direction == Direction.ASCENDING ? compareResult < 0 : compareResult > 0) Collections.swap(unsortedStrings, innerIndex, outerIndex);
            }
        }
    }

}
