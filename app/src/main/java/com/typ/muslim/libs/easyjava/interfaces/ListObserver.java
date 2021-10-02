package com.typ.muslim.libs.easyjava.interfaces;

public interface ListObserver<T> {

    void onItemAdded(int index, T addedItem);

    void onItemChanged(int index, T oldItem, T newItem);

    void onItemRemoved(int index, T removedItem);

}
