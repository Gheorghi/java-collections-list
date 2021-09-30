package com.endava.internship.collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

class StudentListTest {
    private StudentList studentsList;
    private StudentList emptyList;
    private static final Student student1 = new Student("Bob1", LocalDate.parse("2020-01-01"), "Good student");
    private static final Student student2 = new Student("Bob2", LocalDate.parse("2020-01-02"), "Good student");
    private static final Student student3 = new Student("Bob3", LocalDate.parse("2020-01-03"), "Good student");
    private static final Student student4 = new Student("Bob4", LocalDate.parse("2020-01-04"), "Good student");
    private static final Student student5 = new Student("Bob5", LocalDate.parse("2020-01-05"), "Good student");
    private static final Student student6 = new Student("Bob6", LocalDate.parse("2020-01-06"), "Good student");
    private static final Student student7 = new Student("Bob7", LocalDate.parse("2020-01-07"), "Good student");
    private static final Student student8 = new Student("Bob8", LocalDate.parse("2020-01-08"), "Good student");
    private static final Student student9 = new Student("Bob9", LocalDate.parse("2020-01-09"), "Good student");
    private static final Student student10 = new Student("Bob10", LocalDate.parse("2020-01-10"), "Good student");
    private static final Student student11 = new Student("Bob11", LocalDate.parse("2020-01-11"), "Good student");
    private static final Student student12 = new Student("Bob12", LocalDate.parse("2020-01-12"), "Good student");
    private static final Student student0 = new Student("Jery", LocalDate.parse("2020-01-19"), "Best Student");

    private static Stream<Arguments> studentsStream() {
        return Stream.of(
                arguments(student1),
                arguments(student2),
                arguments(student3),
                arguments(student4),
                arguments(student5),
                arguments(student6),
                arguments(student7),
                arguments(student8),
                arguments(student9),
                arguments(student10),
                arguments(student11),
                arguments(student12));
    }

    private enum ThreadActions {
        REMOVE_BY_INDEX, REMOVE, ADD_TO_INDEX, ADD, CLEAR, SET_TO_INDEX
    }

    private void createThreads(int numberOfThreads, StudentList targetList, ThreadActions action) {
        for (int i = 0; i < numberOfThreads; i++) {
            new Thread(threadProperties(targetList, action)).start();
        }
    }

    private static Runnable threadProperties(StudentList targetList, ThreadActions action) {
        return () -> {
            switch (action) {
                case REMOVE_BY_INDEX:
                    targetList.remove(targetList.get(0));
                case REMOVE:
                    targetList.remove(0);
                case ADD_TO_INDEX:
                    targetList.add(0, student0);
                case ADD:
                    targetList.add(student0);
                case CLEAR:
                    targetList.clear();
                case SET_TO_INDEX:
                    targetList.set(0, student0);
            }
        };
    }

    private static <T> List<Student> listOfStudents() {
        return Arrays.asList(student1, student2, student3, student4, student5, student6, student7, student8,
                student9, student10, student11, student12);
    }

    private Object[] getArrayOfStudents() {
        return new Object[]{student1, student2, student3, student4, student5, student6, student7, student8,
                student9, student10, student11, student12};
    }

    @BeforeEach
    void setUp() {
        studentsList = new StudentList(listOfStudents());
        emptyList = new StudentList();
    }

    @Test
    @DisplayName("Constructor initialization with custom capacity")
    public void constructorInitializationWithCustomCapacity() {
        assertTrue(new StudentList(studentsList.size()).addAll(listOfStudents()));
    }

    @Test
    @DisplayName("Check throwing an ArrayIndexOutOfBoundsException while adding more elements than it's capacity to collection ")
    public void checkThrowingAnExceptionWhileAddingMoreElementsThanItSCapacityToCollection() {
        assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> new StudentList(studentsList.size() - 1).addAll(listOfStudents()));
    }

    @ParameterizedTest()
    @MethodSource("studentsStream")
    @DisplayName("Check if list contains objects")
    public void checkIfListContainsObjects(Student student) {
        assertTrue(studentsList.contains(student));
    }

    @Test
    @DisplayName("Should return the actual size of list")
    public void shouldReturnTheActualSizeOfList() {
        assertEquals(12, studentsList.size());
    }

    @Test
    @DisplayName("Check the list if it is empty after removing the content")
    public void checkTheListIfItIsEmptyAfterTheRemovingTheContent() {
        studentsList.clear();
        assertTrue(studentsList.isEmpty());
    }

    @Test
    @DisplayName("Verify getting the Objects from the beginning, middle and last indexes")
    public void verifyGettingTheObjectByIndex() {
        assertAll("Getting",
                () -> assertEquals(studentsList.get(0), student1),
                () -> assertEquals(studentsList.get(5), student6),
                () -> assertEquals(studentsList.get(11), student12));
    }

    @Test()
    @DisplayName("Check removing the Object from the beginning, middle and last indexes")
    public void checkRemovingTheObjectFromTheList() {
        assertAll("Removing",
                () -> assertTrue(studentsList.remove(student1)),
                () -> assertFalse(studentsList.contains(student1)),
                () -> assertTrue(studentsList.remove(student6)),
                () -> assertFalse(studentsList.contains(student6)),
                () -> assertTrue(studentsList.remove(student10)),
                () -> assertFalse(studentsList.contains(student10)));
    }

    @ParameterizedTest()
    @MethodSource("studentsStream")
    @DisplayName("Check clearing the list by removing one by one")
    public void checkClearingTheList(Student student) {
        assertAll("Clearing",
                () -> assertTrue(studentsList.remove(student)),
                () -> assertFalse(studentsList.contains(student)));
    }

    @Test
    @DisplayName("Check throwing an IndexOutOfBoundsException while deleting from unfilled Index")
    public void checkThrowingAn_IndexOutOfBoundsException_WhileDeletingFromUnfilledIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> studentsList.remove(studentsList.size() + 1));
    }

    @Test
    @DisplayName("Check throwing IndexOutOfBoundsException while adding to unallocated Index ")
    public void checkThrowingIndexOutOfBoundsExceptionWhileAddingToUnallocatedIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> studentsList.add(studentsList.size() + 1, student0));
    }

    @Test
    @DisplayName("Check the returning of sub list elements")
    public void checkTheReturningOfSubListElements() {
        List<Student> studentsExternal = Arrays.asList(student4, student5, student6);
        List<Student> studentsActual = studentsList.subList(3, 6);

        assertEquals(studentsActual.size(), studentsExternal.size());
        assertEquals(studentsActual, studentsExternal);
    }

    @Test
    @DisplayName("Check throwing of IndexOutOfBoundsException if 'to > arraySize' in subListRangeCheck()")
    public void checkThrowingOf_IndexOutOfBoundsException_IfToArraySizeInSubListRangeCheck() {
        assertThrows(IndexOutOfBoundsException.class,
                () -> studentsList.subList(studentsList.size() - 1, studentsList.size() + 1));
    }

    @Test
    @DisplayName("Check throwing of IllegalArgumentException if 'from > to' in subListRangeCheck()")
    public void checkThrowingOf_IllegalArgumentException_IfFromToInSubListRangeCheck() {
        assertThrows(IllegalArgumentException.class,
                () -> studentsList.subList(studentsList.size() + 1, studentsList.size()));
    }

    @Test
    @DisplayName("Check throwing of IndexOutOfBoundsException if 'from < 0' in subListRangeCheck()")
    public void checkThrowingOf_IndexOutOfBoundsException_IfFromToInSubListRangeCheck() {
        assertThrows(IndexOutOfBoundsException.class, () -> studentsList.subList(-1, 2));
    }

    @Test
    @DisplayName("Verify T[] toArray(final T[] input)")
    public void verifyGenericToArray() {
        Student[] students = new Student[studentsList.size()];
        students = studentsList.toArray(students);

        assertEquals(students.length, studentsList.size());
        for (int i = 0; i<students.length; i++){
        assertEquals(students[i], studentsList.get(i));
        }
    }

    @Test
    @DisplayName("Verify T[] toArray(final T[] input) when input.length < size()")
    public void verifyTToArrayFinalTInputWhenInputLengthSize() {
        Student[] students = new Student[studentsList.size() / 2];
        int sizeBefore = students.length;
        students = studentsList.toArray(students);

        assertNotEquals(sizeBefore, studentsList.size()); // Check that size are not same
        assertEquals(students.length, studentsList.size()); // Check that size are same
        assertEquals(students[0], studentsList.get(0)); // Check first objects
        assertEquals(students[students.length - 1], studentsList.get(studentsList.size() - 1)); // Check last objects
    }

    @Test
    @DisplayName("Add object to the index")
    public void addObjectToTheIndex() {
        int index = studentsList.size() - 3;
        studentsList.add(index, student0);

        assertEquals(student0, studentsList.get(index));
    }

    @Test
    @DisplayName("Set object to the specified index")
    public void setObjectToTheSpecifiedIndex() {
        int index = studentsList.indexOf(studentsList.get(studentsList.size() / 2));
        Student studentAtIndex = studentsList.get(index);

        studentsList.set(index, student0);

        assertNotEquals(studentAtIndex, studentsList.get(index));
        assertEquals(student0, studentsList.get(index));
    }

    @Test
    @DisplayName("Check returning the false if remove an unexisting  Object")
    public void checkReturningFalseIfRemoveUnexistingObject() {
        assertFalse(studentsList.remove(student0));
    }

    @ParameterizedTest()
    @MethodSource("studentsStream")
    @DisplayName("Check adding an objects")
    public void checkAddingAnObjects(Student student) {
        emptyList.add(student);
        assertTrue(emptyList.contains(student));
        assertFalse(emptyList.contains(student0));
    }

    @Test
    @DisplayName("Test toArray()")
    public void testToArray() {
        assertArrayEquals(studentsList.toArray(), getArrayOfStudents());
    }

    @Test
    @DisplayName("Test indexOf() null element")
    public void testIndexOfNullElement() {
        studentsList.add(1, null);
        assertEquals(studentsList.indexOf(null), 1);
    }

    @Test
    @DisplayName("Test lastIndexOf()")
    public void testLastIndexOf() {
        assertEquals(studentsList.lastIndexOf(student2), 1);
    }

    @Test
    @DisplayName("Test lastIndexOf null element")
    public void testLastIndexOfNull() {
        emptyList.add(null);
        assertEquals(0, emptyList.lastIndexOf(null));
    }

    @Test
    @DisplayName("test returning -1 if provided non existing object to lastIndexOf()")
    public void testReturning1IfProvidedNonExistingObjectToLastIndexOf() {
        assertEquals(-1, studentsList.lastIndexOf(student0));
    }

    @Test
    @DisplayName("Contains All test")
    public void containsAllTest() {
        assertTrue(studentsList.containsAll(listOfStudents()));
    }

    @Test
    @DisplayName("Contains All when not all present")
    public void containsNotAllTest() {
        assertFalse(studentsList.containsAll(Arrays.asList(student1, student2, student0)));
    }

    @Test
    @DisplayName("Check addAll to index")
    public void checkAddAllToIndex() {
        emptyList = new StudentList();
        emptyList.add(student0);
        emptyList.add(new Student("Karl", LocalDate.parse("2020-01-05"), "Motivated"));
        emptyList.add(new Student("Colombo", LocalDate.parse("1940-01-05"), "Investigator"));
        emptyList.addAll(1, listOfStudents());

        assertTrue(emptyList.containsAll(listOfStudents()));
    }

    @Test
    @DisplayName("Check deletion the collection of objects")
    public void checkDeletionTheCollectionOfObjects() {
        studentsList.add(new Student("Karl", LocalDate.parse("2020-01-05"), "Motivated"));
        studentsList.add(new Student("Colombo", LocalDate.parse("1940-01-05"), "Investigator"));

        assertTrue(studentsList.removeAll(listOfStudents()));
    }

    @Test
    @DisplayName("Test retaining the objects")
    public void testRetainingTheObjects() {
        studentsList.add(new Student("Karl", LocalDate.parse("2020-01-05"), "Motivated"));
        studentsList.add(new Student("Colombo", LocalDate.parse("1940-01-05"), "Investigator"));
        List<Student> sublistOfStudents = studentsList.subList(2, 5);

        assertTrue(studentsList.retainAll(sublistOfStudents));
        assertEquals(studentsList.size(), 3);
        assertEquals(sublistOfStudents.get(0), studentsList.get(0));
        assertEquals(sublistOfStudents.get(1), studentsList.get(1));
        assertEquals(sublistOfStudents.get(2), studentsList.get(2));
    }

    @Test
    @DisplayName("Check hasNext() of listIterator() when collection is not empty")
    public void checkHasNextListIterator() {
        assertTrue(studentsList.listIterator().hasNext());
        assertNotEquals(0, studentsList.size());
    }

    @Test
    @DisplayName("Check hasNext() of listIterator() when collection is empty")
    public void checkHasNextListIteratorWhenEmptyCollection() {
        assertFalse(emptyList.listIterator().hasNext());
        assertEquals(emptyList.size(), 0);
    }

    @Test
    @DisplayName("Check throwing NoSuchElementException() of next() when cursor at the end of collection")
    public void checkExceptionOfNext() {
        ListIterator<Student> iterator = studentsList.listIterator(studentsList.size() - 1);

        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    @DisplayName("Check next() of listIterator() when collection is not empty")
    public void checkNextOfListIterator() {
        assertNotNull(studentsList.listIterator().next());
    }

    @Test
    @DisplayName("Check next() of listIterator() when collection is empty")
    public void checkNextOfListIteratorWhenEmptyCollection() {
        assertNull(emptyList.listIterator().next());
    }

    @Test
    @DisplayName("Check previous() of listIterator when collection is not empty")
    public void checkPreviousOfListIteratorWhenCollectionIsNotEmpty() {
        ListIterator<Student> iterator = studentsList.listIterator(2);
        assertTrue(iterator.hasPrevious());
        assertNotNull(iterator.previous());
        assertNotNull(iterator.previous());
        assertFalse(iterator.hasPrevious());
    }

    @Test
    @DisplayName("Test throwing IndexOutOfBoundsException when no cursors index is wrong for previous()")
    public void testThrowingIndexOutOfBoundsExceptionWhenNoCursorsIndexIsWrongForPrevious() {
        ListIterator<Student> iterator = studentsList.listIterator(0);

        assertThrows(NoSuchElementException.class, iterator::previous);
    }

    @Test
    @DisplayName("Check the nextIndex() when it exist")
    public void checkTheNextIndexWhenItExist() {
        ListIterator<Student> iterator = studentsList.listIterator(0);
        assertEquals(1, iterator.nextIndex());
    }

    @Test
    @DisplayName("Check the previousIndex() when it exist")
    public void checkThePreviousIndexWhenItExist() {
        ListIterator<Student> iterator = studentsList.listIterator(1);
        assertEquals(0, iterator.previousIndex());
    }

    @Test
    @DisplayName("Check removing by iterator")
    public void checkRemovingByIterator() {
        emptyList.add(student0);
        ListIterator<Student> iterator = emptyList.listIterator();
        iterator.remove();

        assertEquals(0, emptyList.size());
    }

    @Test
    @DisplayName("Check adding by iterator")
    public void checkAddingByIterator() {
        ListIterator<Student> iterator = emptyList.listIterator();
        iterator.add(student0);

        assertEquals(1, emptyList.size());
        assertTrue(emptyList.contains(student0));
    }

    @Test
    @DisplayName("Check setting by iterator")
    public void checkSettingByIterator() {
        ListIterator<Student> iterator = studentsList.listIterator();
        iterator.set(student0);

        assertTrue(studentsList.contains(student0));
    }

    @Test
    @DisplayName("Check throwing ConcurrentModificationException by iterator while removing the last element")
    public void checkThrowingConcurrentModificationExceptionWhileRemovingTheLastElement() {
        ListIterator<Student> iterator = emptyList.listIterator();

        assertThrows(ConcurrentModificationException.class, iterator::remove);
    }

    @Test
    @DisplayName("Check iterator of iterator()")
    public void checkIteratorOfIterator() {
        Iterator<Student> it = studentsList.iterator();

        assertTrue(it.hasNext());
        assertNotNull(it.next());
    }

    @Test
    @DisplayName("Test forEachRemaining() method of iterator")
    public void testForEachRemainingMethodOfIterator() {
        Iterator<Student> it = studentsList.iterator();
        int sizeBeforeAction = studentsList.size();
        it.forEachRemaining(n -> it.remove());

        assertNotEquals(sizeBeforeAction, studentsList.size());
    }

    @Test
    @DisplayName("Check throwing ConcurrentModificationException by iterator while removing")
    public void checkRemovingConcurrently() {
        ListIterator<Student> iterator = studentsList.listIterator();
        createThreads(2000, studentsList, ThreadActions.ADD);
        createThreads(200, studentsList, ThreadActions.REMOVE);
        createThreads(2000, studentsList, ThreadActions.ADD_TO_INDEX);
        createThreads(100, studentsList, ThreadActions.REMOVE_BY_INDEX);

        assertThrows(ConcurrentModificationException.class, iterator::remove);
    }

    @Test
    @DisplayName("Check throwing ConcurrentModificationException by iterator while adding")
    public void checkAddingConcurrently() {
        ListIterator<Student> iterator = studentsList.listIterator();
        createThreads(2000, studentsList, ThreadActions.ADD);
        createThreads(400, studentsList, ThreadActions.REMOVE);
        createThreads(2000, studentsList, ThreadActions.ADD_TO_INDEX);
        createThreads(300, studentsList, ThreadActions.SET_TO_INDEX);

        assertThrows(ConcurrentModificationException.class, () -> iterator.add(student0));
    }

    @Test
    @DisplayName("Check throwing ConcurrentModificationException by iterator while set")
    public void checkSetConcurrently() {
        ListIterator<Student> iterator = studentsList.listIterator();
        createThreads(2000, studentsList, ThreadActions.ADD);
        createThreads(200, studentsList, ThreadActions.REMOVE);
        createThreads(2000, studentsList, ThreadActions.ADD_TO_INDEX);
        createThreads(400, studentsList, ThreadActions.SET_TO_INDEX);

        assertThrows(ConcurrentModificationException.class, () -> iterator.set(student0));
    }
}