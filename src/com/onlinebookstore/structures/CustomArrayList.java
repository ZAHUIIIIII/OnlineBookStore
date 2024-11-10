package com.onlinebookstore.structures;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class CustomArrayList<T> implements Iterable<T> {
    private static final int DEFAULT_CAPACITY = 4;
    private Object[] elements;
    private int size;

    // Constructor
    public CustomArrayList() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    // Add method to append elements
    public void add(T element) {
        // O(1) average case, O(n) when resizing is needed
        if (size == elements.length) {
            resize(); // O(n)
        }
        elements[size++] = element;
    }


    // Remove method by index
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

    // Method to return the size of the list
    public int size() {
        return size;
    }

    // Method to check if the list is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // Method to resize the internal array
    private void resize() {
        Object[] newElements = new Object[elements.length * 2];
        System.arraycopy(elements, 0, newElements, 0, elements.length);
        elements = newElements;
    }

    // Method to clear the list
    public void clear() {
        elements = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new CustomIterator();
    }

    private class CustomIterator implements Iterator<T> {
        private int currentIndex = 0;
        private boolean canRemove = false;

        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            canRemove = true;
            return (T) elements[currentIndex++];
        }

        @Override
        public void remove() {
            if (!canRemove) {
                throw new IllegalStateException();
            }
            CustomArrayList.this.remove(--currentIndex);
            canRemove = false;
        }
    }

        // Method to sort the list using merge sort
        public void mergeSort(CustomComparator<T> comparator) {
            if (size > 1) {
                Object[] tempArray = new Object[size];
                mergeSortHelper(elements, tempArray, 0, size - 1, comparator);
            }
        }
    
        private void mergeSortHelper(Object[] array, Object[] tempArray, int leftStart, int rightEnd, CustomComparator<T> comparator) {
            if (leftStart >= rightEnd) {
                return;
            }
            int middle = (leftStart + rightEnd) / 2;
            mergeSortHelper(array, tempArray, leftStart, middle, comparator);
            mergeSortHelper(array, tempArray, middle + 1, rightEnd, comparator);
            mergeHalves(array, tempArray, leftStart, rightEnd, comparator);
        }
    
        private void mergeHalves(Object[] array, Object[] tempArray, int leftStart, int rightEnd, CustomComparator<T> comparator) {
            int leftEnd = (rightEnd + leftStart) / 2;
            int rightStart = leftEnd + 1;
            int size = rightEnd - leftStart + 1;
    
            int left = leftStart;
            int right = rightStart;
            int index = leftStart;
    
            while (left <= leftEnd && right <= rightEnd) {
                if (comparator.compare((T) array[left], (T) array[right]) <= 0) {
                    tempArray[index] = array[left];
                    left++;
                } else {
                    tempArray[index] = array[right];
                    right++;
                }
                index++;
            }
    
            System.arraycopy(array, left, tempArray, index, leftEnd - left + 1);
            System.arraycopy(array, right, tempArray, index, rightEnd - right + 1);
            System.arraycopy(tempArray, leftStart, array, leftStart, size);
        }
    
        public interface CustomComparator<T> {
            int compare(T o1, T o2);
        }
    }