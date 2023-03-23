/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.structure;

import androidx.annotation.Nullable;

import com.typ.muslim.libs.easyjava.data.EasyList;
import com.typ.muslim.libs.easyjava.interfaces.EasyConsumer;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Queue<T> implements Iterator<T> {

    // Statics
    private static final String TAG = Queue.class.getSimpleName();
    // Runtime
    private final EasyList<T> items;
    private int currPosition = 0; // For iteration.

    public Queue() {
        this.items = new EasyList<>();
    }

    public Queue<T> push(T item) {
        items.add(item);
        return this;
    }

    @SafeVarargs
    public final Queue<T> pushAll(T... items) {
        this.items.addAll(Arrays.asList(items));
        return this;
    }

    public Queue<T> pushAll(List<T> items) {
        this.items.addAll(items);
        return this;
    }

    @Nullable
    public T pop() {
        return items.size() > 0 ? items.remove(0) : null;
    }

    @Nullable
    public T grab() {
        return items.size() > 0 ? items.get(0) : null;
    }

    public T get(int pos) {
        return pos >= items.size() ? null : items.get(pos);
    }

    public void clear() {
        items.clear();
    }

    public int size() {
        return items.size();
    }

    public void iterate(EasyConsumer<T> action) {
        for (T item : items) action.accept(item);
    }

    @Override
    public boolean hasNext() {
        return currPosition < items.size();
    }

    @Override
    public T next() {
        return items.get(currPosition++);
    }

    public void resetIterator() {
        this.currPosition = 0;
    }
}
