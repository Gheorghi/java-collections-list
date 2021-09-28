package com.endava.internship.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class StudentList implements List<Student> {

    private int sizeOfArray = 0;
    private static final int CAPACITY = 10;
    private Student[] elements;
    private int modificationCounter = 0;

    public StudentList() {
        elements = new Student[CAPACITY];
    }

    public StudentList(int initialCapacity) {
        elements = new Student[initialCapacity];
    }

    public StudentList(Collection<? extends Student> studentsCollection) {
        elements = new Student[sizeOfArray + studentsCollection.size()];
        addAll(studentsCollection);
    }

    @Override
    public int size() {
        return sizeOfArray;
    }

    @Override
    public boolean isEmpty() {
        return sizeOfArray == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public Iterator<Student> iterator() {
        return new StudentItr();
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elements, sizeOfArray);
    }

    @Override
    public <T> T[] toArray(final T[] input) {
        if (input.length < size()) {
            return (T[]) Arrays.copyOf(elements, size(), input.getClass());
        }
        System.arraycopy(elements, 0, input, 0, input.length);
        return input;
    }

    @Override
    public boolean add(final Student student) {
        checkCapacity();
        elements[sizeOfArray++] = student;
        modificationCounter++;
        return true;
    }

    private void checkCapacity() {
        if (elements.length == size()) {
            int capacity = elements.length * 2;
            updateCapacity(capacity);
        }
    }

    private void updateCapacity(int newSize) {
        elements = Arrays.copyOf(elements, newSize);
    }

    @Override
    public boolean remove(final Object o) {
        try {
            remove(indexOf(o));
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    @Override
    public void clear() {
        modificationCounter++;
        elements = new Student[CAPACITY];
        sizeOfArray = 0;
    }

    @Override
    public Student get(int i) {
        checkTheExistenceOfIndex(i);
        return elements[i];
    }

    private void checkTheExistenceOfIndex(int index) {
        if (size() <= index) {
            throw new IndexOutOfBoundsException("Array size: " + size() + " is less or equal to index: " + index);
        }
    }

    private void checkIndex(int index) {
        if (size() < index || index < 0) {
            throw new IndexOutOfBoundsException("Array size: " + size() + " less than index: " + index + " or index less than: 0");
        }
    }

    @Override
    public Student set(int i, final Student student) {
        checkTheExistenceOfIndex(i);
        Student previous = elements[i];
        elements[i] = student;
        modificationCounter++;
        return previous;
    }

    @Override
    public void add(int i, final Student student) {
        checkIndex(i);
        checkCapacity();
        System.arraycopy(elements, i, elements, i + 1, size() - i);
        elements[i] = student;
        sizeOfArray++;
        modificationCounter++;
    }

    @Override
    public Student remove(int i) {
        checkTheExistenceOfIndex(i);
        Student targetToRemove = get(i);

        int numToShift = sizeOfArray - i - 1;
        if (numToShift > 0) {
            System.arraycopy(elements, i + 1, elements, i, numToShift);
        }
        elements[--sizeOfArray] = null;
        elements = removeNullsFromArray(elements);
        modificationCounter++;
        return targetToRemove;
    }

    @Override
    public int indexOf(Object o) {
        if (o != null) {
            for (int index = 0; index < size(); index++) {
                if (elements[index].equals(o)) {
                    return index;
                }
            }
        } else {
            for (int index = 0; index < size(); index++) {
                if (elements[index] == null) {
                    return index;
                }
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o != null) {
            for (int index = size() - 1; index >= 0; index--) {
                if (elements[index].equals(o)) {
                    return index;
                }
            }
        } else {
            for (int index = size() - 1; index >= 0; index--) {
                if (elements[index] == null) {
                    return index;
                }
            }
        }
        return -1;
    }

    @Override
    public ListIterator<Student> listIterator() {
        return new StudentItr();
    }

    @Override
    public ListIterator<Student> listIterator(int i) {
        checkIndex(i);
        return new StudentItr(i);
    }

    @Override
    public List<Student> subList(int i, int i1) {
        subListRangeCheck(i, i1, size());
        return Arrays.asList(Arrays.copyOfRange(elements, i, i1));
    }

    private void subListRangeCheck(int from, int to, int arraySize) {
        if (to > arraySize)
            throw new IndexOutOfBoundsException("to is: " + to);
        if (from > to)
            throw new IllegalArgumentException("from: " + from + " - to: " + to);
        if (from < 0)
            throw new IndexOutOfBoundsException("from: " + from);
    }

    @Override
    public boolean addAll(Collection<? extends Student> collection) {
        int sizeOfCollection = collection.size();
        System.arraycopy(collection.toArray(), 0, elements, size(), sizeOfCollection);
        sizeOfArray += sizeOfCollection;
        modificationCounter++;
        return sizeOfCollection > 0;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        for (Object e : collection)
            if (!contains(e)) {
                return false;
            }
        return true;
    }

    @Override
    public boolean addAll(int i, final Collection<? extends Student> collection) {
        final Student[] students = collection.toArray(new Student[collection.size()]);
        final Student[] afterIndex = removeNullsFromArray(Arrays.copyOfRange(elements, i + 1, elements.length - 1));

        updateCapacity(collection.size() + size());
        System.arraycopy(students, 0, elements, i + 1, students.length);
        System.arraycopy(afterIndex, 0, elements, elements.length - 1, afterIndex.length);

        sizeOfArray += collection.size();
        modificationCounter++;
        return containsAll(collection);
    }

    private <T> int getActualSizeOfArray(T[] array) {
        return (int) Arrays.stream(array).filter(e -> e != null).count();
    }

    private Student[] removeNullsFromArray(Student[] array) {
        return Arrays.stream(array).filter(e -> e != null).collect(Collectors.toList()).toArray(new Student[0]);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        Objects.requireNonNull(collection);
        Iterator<?> it = collection.iterator();
        while (it.hasNext()) {
            Object student = it.next();
            remove(student);
        }
        return !containsAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        Objects.requireNonNull(collection);
        int index = 0;

        while (index != elements.length & (elements.length != 0 || elements.length != collection.size())) {
            if (!collection.contains(elements[index])) {
                remove(elements[index]);
            } else {
                index++;
            }
        }

        sizeOfArray = getActualSizeOfArray(elements);
        return collection.size() == size();
    }

    private class StudentItr implements ListIterator<Student> {
        int cursor = 0;
        int expectedModifCounter = modificationCounter;

        public StudentItr(int index) {
            cursor = index;
        }

        public StudentItr() {
        }

        @Override
        public boolean hasNext() {
            try {
                checkIndex(cursor + 1);
                return true;
            } catch (IndexOutOfBoundsException e) {
                return false;
            }
        }

        @Override
        public Student next() {
            checkIfNoActionsMade();
            try {
                hasNext();
                ++cursor;
                return elements[cursor];
            } catch (IndexOutOfBoundsException e) {
                throw new NoSuchElementException();
            }
        }

        @Override
        public boolean hasPrevious() {
            try {
                checkIndex(cursor - 1);
                return true;
            } catch (IndexOutOfBoundsException e) {
                return false;
            }
        }

        @Override
        public Student previous() {
            checkIfNoActionsMade();
            try {
                hasPrevious();
                --cursor;
                return elements[cursor];
            } catch (IndexOutOfBoundsException e) {
                throw new NoSuchElementException();
            }
        }

        @Override
        public int nextIndex() {
            hasNext();
            return cursor + 1;
        }

        @Override
        public int previousIndex() {
            hasPrevious();
            return cursor - 1;
        }

        @Override
        public void remove() {
            checkIfNoActionsMade();
            try {
                StudentList.this.remove(cursor);
                expectedModifCounter = modificationCounter;
            } catch (IndexOutOfBoundsException e) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public void forEachRemaining(Consumer<? super Student> action) {
            ListIterator.super.forEachRemaining(action);
        }

        @Override
        public void set(Student student) {
            checkIfNoActionsMade();
            StudentList.this.set(cursor, student);
            expectedModifCounter = modificationCounter;
            checkIfNoActionsMade();
        }

        @Override
        public void add(Student student) {
            checkIfNoActionsMade();
            StudentList.this.add(cursor, student);
            expectedModifCounter = modificationCounter;
            checkIfNoActionsMade();
        }

        final void checkIfNoActionsMade() {
            if (modificationCounter != expectedModifCounter)
                throw new ConcurrentModificationException();
        }
    }
}
