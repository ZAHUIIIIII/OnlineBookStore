package com.onlinebookstore.structures;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class CustomArrayList<T> implements Iterable<T> {
    private Object[] elements;
    private int size;

    public CustomArrayList() {
        elements = new Object[10];
        size = 0;
    }

    // Add method with complexity comments
    public void add(T element) {
        // O(1) average case, O(n) when resizing is needed
        if (size == elements.length) {
            resize(); // O(n)
        }
        elements[size++] = element;
    }

    // Remove method (retained for functionality)
    public void remove(int index) {
        // O(n) time complexity
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[--size] = null; // Clear the last element
    }

    // Enhanced sorting algorithm: mergeSort
    public void mergeSort(CustomComparator<T> comparator) {
        mergeSortHelper(0, size - 1, comparator);
    }

    private void mergeSortHelper(int left, int right, CustomComparator<T> comparator) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSortHelper(left, mid, comparator);
            mergeSortHelper(mid + 1, right, comparator);
            merge(left, mid, right, comparator);
        }
    }

    private void merge(int left, int mid, int right, CustomComparator<T> comparator) {
        Object[] tempArray = new Object[right - left + 1];
        int i = left, j = mid + 1, k = 0;

        while (i <= mid && j <= right) {
            if (comparator.compare((T) elements[i], (T) elements[j]) <= 0) {
                tempArray[k++] = elements[i++];
            } else {
                tempArray[k++] = elements[j++];
            }
        }
        while (i <= mid) {
            tempArray[k++] = elements[i++];
        }
        while (j <= right) {
            tempArray[k++] = elements[j++];
        }
        for (i = left, k = 0; i <= right; i++, k++) {
            elements[i] = tempArray[k];
        }
    }

    // Binary search method (for sorted arrays)
    public int binarySearch(T target, CustomComparator<T> comparator) {
        // O(log n) time complexity
        int left = 0;
        int right = size - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            int compareResult = comparator.compare((T) elements[mid], target);
            if (compareResult == 0) {
                return mid;
            } else if (compareResult < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1; // Not found
    }

    // Method to get the size of the list
    public int size() {
        return size;
    }

    // Method to get an element at a specific index
    public T get(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return (T) elements[index];
    }

    // Method to set an element at a specific index
    public void set(int index, T element) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        elements[index] = element;
    }

    // Method to check if the list is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // Method to return an iterator for the list
    @Override
    public Iterator<T> iterator() {
        return new CustomArrayListIterator();
    }

    private class CustomArrayListIterator implements Iterator<T> {
        private int currentIndex = 0;

        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return (T) elements[currentIndex++];
        }
    }

    private void resize() {
        Object[] newElements = new Object[elements.length * 2];
        System.arraycopy(elements, 0, newElements, 0, elements.length);
        elements = newElements;
    }

    public interface CustomComparator<T> {
        int compare(T o1, T o2);
    }
}