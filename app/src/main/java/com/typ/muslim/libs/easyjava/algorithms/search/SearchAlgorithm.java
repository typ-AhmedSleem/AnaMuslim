package com.typ.muslim.libs.easyjava.algorithms.search;
import com.typ.muslim.libs.easyjava.interfaces.SearchCallback;

import java.util.List;

public abstract class SearchAlgorithm {

    public abstract void searchForString(List<String> list, String target, boolean matchCase, boolean matchWords, SearchCallback callback);

    public abstract void searchForNumber(List<? extends Number> list, Number number, SearchCallback callback);

    protected void notifyCallback(SearchCallback callback, boolean isFound, int where) {
        if (callback == null) return;
        callback.onSearchFinished(isFound, where);
    }

}
