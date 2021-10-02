package com.typ.muslim.libs.easyjava.algorithms.search;


import com.typ.muslim.libs.easyjava.interfaces.SearchCallback;

import java.util.List;

public class LinearSearchAlgorithm extends SearchAlgorithm {

    @Override
    public void searchForString(List<String> list, String target, boolean matchCase, boolean matchWords, SearchCallback callback) {
        // Null checks first
        if (list == null || target == null) {
            System.out.println("sfs list is null");
            notifyCallback(callback, false, -1);
            return;
        }
        // Loop searching for target
        boolean isFound;
        for (int currIndex = 0; currIndex < list.size(); currIndex++) {
            if (list.get(currIndex) == null) continue;
            if (matchCase) isFound = matchWords ? list.get(currIndex).equals(target) : list.get(currIndex).contains(target);
            else isFound = matchWords ? list.get(currIndex).equalsIgnoreCase(target) : list.get(currIndex).contains(target);
            if (isFound) {
                notifyCallback(callback, true, currIndex);
                return;
            }
        }
    }

    @Override
    public void searchForNumber(List<? extends Number> list, Number number, SearchCallback callback) {
        // Null checks first
        if (list == null) {
            System.out.println("sfn list is null");
            notifyCallback(callback, false, -1);
            return;
        }
        // Loop searching for target
        boolean isFound = false;
        for (int currIndex = 0; currIndex < list.size(); currIndex++) {
            if (list.get(currIndex) == number) {
                notifyCallback(callback, true, currIndex);
                return;
            }
        }
    }

}
