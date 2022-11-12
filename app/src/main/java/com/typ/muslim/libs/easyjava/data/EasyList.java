/*
 * Created by: TheYoungProgrammers
 * Created in: 24/4/2021
 */

package com.typ.muslim.libs.easyjava.data;

import androidx.annotation.Nullable;

import com.typ.muslim.structure.Queue;
import com.typ.muslim.libs.easyjava.algorithms.sort.InsertionSortAlgorithm;
import com.typ.muslim.libs.easyjava.algorithms.sort.SortAlgorithm;
import com.typ.muslim.libs.easyjava.enums.Direction;
import com.typ.muslim.libs.easyjava.interfaces.EasyConsumer;
import com.typ.muslim.libs.easyjava.interfaces.EasyExtendedConsumer;
import com.typ.muslim.libs.easyjava.interfaces.EasyListConsumer;
import com.typ.muslim.libs.easyjava.interfaces.EasyListCounter;
import com.typ.muslim.libs.easyjava.interfaces.EasyListLooper;
import com.typ.muslim.libs.easyjava.interfaces.ListObserver;
import com.typ.muslim.managers.AManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class EasyList<T> extends ArrayList<T> {

	// TODO: Write documentation for this class.
	// TODO: Add sort algorithms as method which takes (sort algorithm enum, sort direction enum) and sort list according to its operations.
	// TODO: Add search algorithms as method which takes (search algorithm enum, search for what object) and search list according to its operations.

	protected ListObserver<T> observer;

	public EasyList() {
	}

	public EasyList(int initialCapacity) {
		super(initialCapacity);
	}

	public EasyList(Collection<? extends T> collection) {
		super(collection);
	}

	@SafeVarargs
	public static <T> EasyList<T> createList(T... elements) {
		return new EasyList<>(Arrays.asList(elements));
	}

	public static <T> EasyList<T> createList(List<T> list) {
		return new EasyList<>(list);
	}

	public void observeChanges(ListObserver<T> observer) {
		this.observer = observer;
	}

	@Override
	public void add(int index, T element) {
		if (this.observer != null) this.observer.onItemAdded(index, element);
		super.add(index, element);
	}

	@Override
	public boolean add(T item) {
		if (this.observer != null) this.observer.onItemAdded(size() - 1, item);
		return super.add(item);
	}

	@Override
	public T remove(int index) {
		if (this.observer != null) this.observer.onItemRemoved(index, get(index));
		return super.remove(index);
	}

	@Override
	public T set(int index, T element) {
		if (this.observer != null) this.observer.onItemChanged(index, get(index), element);
		return super.set(index, element);
	}

	public void iterate(EasyConsumer<? super T> action) {
		if (action == null) return;
		for (T t : this) action.accept(t);
	}

	public void iterate(EasyListConsumer<? super T> action) {
		if (action == null) return;
		for (int index = 0; index < size(); index++) action.execute(index, get(index));
	}

	public void extendedIterate(EasyExtendedConsumer<T, ?> action, EasyList<?> list) {
		// TODO: 2021-04-29 to be coded
	}

	public void loop(EasyListLooper<? super T> action) {
		if (action == null) return;
		// Break on this item at this index.
		for (int index = 0; index < size(); index++) if (action.validate(index, get(index))) break;
	}

	public int countValues(EasyListCounter<T> counter) {
		if (counter == null) return 0;
		int count = 0;
		for (T item : this) count += counter.count(item);
		return count;
	}

	public void removeIf(EasyListLooper<? super T> action) {
		if (action == null) return;
		for (int i = 0; i < size(); i++)
			if (action.validate(i, get(i))) {
				AManager.log("EasyList", "Checking: %s", get(i));
			}
	}

	public Queue<T> toQueue() {
		return new Queue<T>().pushAll(this);
	}

	public void sort(SortAlgorithm algorithm, Direction direction) {
		if (algorithm == null) algorithm = new InsertionSortAlgorithm();
		if (direction == null) direction = Direction.ASCENDING;
		int[] numbers = new int[50000];
		Random random = new Random(System.currentTimeMillis());
		for (int i = 0; i < 50000; i++) numbers[i] = random.nextInt();
		algorithm.performSort(numbers, direction);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("EasyList{\n");
		for (T item : this) sb.append(item).append(",\n");
		return sb.append("\n}").toString();
	}

	@Nullable
	public T getIf(EasyListLooper<T> condition) {
		T foundItem = null;
		for (T item : this) {
			if (condition.validate(0,item)) {
				foundItem = item;
				break;
			}
		}
		return foundItem;
	}

	public void doIf(EasyListLooper<T> condition, EasyListConsumer<T> action) {
		if (condition == null || action == null) return;
		for (T item : this) if (condition.validate(-1,item)) action.execute(-1,item);
	}


}
