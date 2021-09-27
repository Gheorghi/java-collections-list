package com.endava.internship.collections;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

class TestStudentList {
    static StudentList students_List;
    static StudentList emptyList;
    static Student student1 = new Student("Bob1", LocalDate.parse("2020-01-01"), "Good student");
    static Student student2 = new Student("Bob2", LocalDate.parse("2020-01-02"), "Good student");
    static Student student3 = new Student("Bob3", LocalDate.parse("2020-01-03"), "Good student");
    static Student student4 = new Student("Bob4", LocalDate.parse("2020-01-04"), "Good student");
    static Student student5 = new Student("Bob5", LocalDate.parse("2020-01-05"), "Good student");
    static Student student6 = new Student("Bob6", LocalDate.parse("2020-01-06"), "Good student");
    static Student student7 = new Student("Bob7", LocalDate.parse("2020-01-07"), "Good student");
    static Student student8 = new Student("Bob8", LocalDate.parse("2020-01-08"), "Good student");
    static Student student9 = new Student("Bob9", LocalDate.parse("2020-01-09"), "Good student");
    static Student student10 = new Student("Bob10", LocalDate.parse("2020-01-10"), "Good student");
    static Student student11 = new Student("Bob11", LocalDate.parse("2020-01-11"), "Good student");
    static Student student12 = new Student("Bob12", LocalDate.parse("2020-01-12"), "Good student");
    static Student student0 = new Student("Jery", LocalDate.parse("2020-01-19"), "Best Student"); // Note this objects will not be added in setUp()

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

    private void createThreads(int numberOfThreads, StudentList targetList, String action) {
        for (int i = 0; i < numberOfThreads; i++) {
            new Thread(threadProperties(targetList, action)).start();
        }
    }

    private static Runnable threadProperties(StudentList targetList, String action) {
        return () -> {
            if (action.equalsIgnoreCase("remove by index")) {
                targetList.remove(targetList.get(0));
            }
            if (action.equalsIgnoreCase("remove")) {
                targetList.remove(0);
            }
            if (action.equalsIgnoreCase("add to index")) {
                targetList.add(0, student0);
            }
            if (action.equalsIgnoreCase("add")) {
                targetList.add(student0);
            }
            if (action.equalsIgnoreCase("clear")) {
                targetList.clear();
            }
            if (action.equalsIgnoreCase("set to index")) {
                targetList.set(0, student0);
            }
            System.out.println(targetList.size());
        };
    }


    private static int[] getIndexes() {
        int[] indexes = new int[listOfStudents().size()];
        for (int i = 0; i < students_List.size(); i++) {
            indexes[i] = i;
        }
        return indexes;
    }

    private static <T> List<Student> listOfStudents() {
        return Arrays.asList(student1, student2, student3, student4, student5, student6, student7, student8, student9, student10, student11, student12);
    }

    private Object[] getArrayOfStudents() {
        Object[] objects = {student1, student2, student3, student4, student5, student6, student7, student8, student9, student10, student11, student12};
        return objects;
    }

    @BeforeEach
    void setUp() {
        students_List = new StudentList(listOfStudents());
        emptyList = new StudentList();
    }

    @Test
    @DisplayName("Constructor initialization with custom capacity")
    public void constructorInitializationWithCustomCapacity() {
        assertTrue(new StudentList(students_List.size()).addAll(listOfStudents()));
    }

    @Test
    @DisplayName("Check throwing an ArrayIndexOutOfBoundsException while adding more elements than it's capacity to collection ")
    public void checkThrowingAnExceptionWhileAddingMoreElementsThanItSCapacityToCollection() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> new StudentList(students_List.size() - 1).addAll(listOfStudents()));
    }

    @ParameterizedTest()
    @MethodSource("studentsStream")
    @DisplayName("Check if list contains objects")
    public void checkIfListContainsObjects(Student student) {
        assertTrue(students_List.contains(student));
    }

    @Test
    @DisplayName("Should return the actual size of list")
    public void shouldReturnTheActualSizeOfList() {
        assertEquals(12, students_List.size());
    }

    @Test
    @DisplayName("Check the list if it is empty after removing the content")
    public void checkTheListIfItIsEmptyAfterTheRemovingTheContent() {
        students_List.clear();
        assertTrue(students_List.isEmpty());
    }

    @Test
    @DisplayName("Verify getting the Objects from the beginning, middle and last indexes")
    public void verifyGettingTheObjectByIndex() {
        assertAll("Getting",
                () -> assertEquals(students_List.get(0), student1),
                () -> assertEquals(students_List.get(5), student6),
                () -> assertEquals(students_List.get(11), student12));
    }

    @ParameterizedTest()
    @MethodSource("studentsStream")
    @DisplayName("Check removing the Object from the beginning, middle and last indexes")
    public void checkRemovingTheObjectFromTheList(Student student) {
        assertAll("Removing",
                () -> assertTrue(students_List.remove(student)),
                () -> assertFalse(students_List.contains(student)));
    }

    @ParameterizedTest()
    @MethodSource("studentsStream")
    @DisplayName("Check clearing the list by removing one by one")
    public void checkClearingTheList(Student student) {
        assertAll("Clearing",
                () -> assertTrue(students_List.remove(student1)),
                () -> assertFalse(students_List.contains(student1)));
    }

    @Test
    @DisplayName("Check throwing an IndexOutOfBoundsException while deleting from unfilled Index")
    public void checkThrowingAn_IndexOutOfBoundsException_WhileDeletingFromUnfilledIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> students_List.remove(students_List.size() + 1));
    }

    @Test
    @DisplayName("Check throwing IndexOutOfBoundsException while adding to unallocated Index ")
    public void checkThrowingIndexOutOfBoundsExceptionWhileAddingToUnallocatedIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> students_List.add(students_List.size() + 1, student0));
    }

    @Test
    @DisplayName("Check the returning of sub list elements")
    public void checkTheReturningOfSubListElements() {
        List<Student> studentsExternal = Arrays.asList(student4, student5, student6);
        List<Student> studentsActual = students_List.subList(3, 6);
        assertEquals(studentsActual.size(), studentsExternal.size());
        assertEquals(studentsActual, studentsExternal);
    }

    @Test
    @DisplayName("Check throwing of IndexOutOfBoundsException if 'to > arraySize' in subListRangeCheck()")
    public void checkThrowingOf_IndexOutOfBoundsException_IfToArraySizeInSubListRangeCheck() {
        assertThrows(IndexOutOfBoundsException.class, () -> students_List.subList(students_List.size() - 1, students_List.size() + 1));
    }

    @Test
    @DisplayName("Check throwing of IllegalArgumentException if 'from > to' in subListRangeCheck()")
    public void checkThrowingOf_IllegalArgumentException_IfFromToInSubListRangeCheck() {
        assertThrows(IllegalArgumentException.class, () -> students_List.subList(students_List.size() + 1, students_List.size()));
    }

    @Test
    @DisplayName("Check throwing of IndexOutOfBoundsException if 'from < 0' in subListRangeCheck()")
    public void checkThrowingOf_IndexOutOfBoundsException_IfFromToInSubListRangeCheck() {
        assertThrows(IndexOutOfBoundsException.class, () -> students_List.subList(-1, 2));
    }

    @Test
    @DisplayName("Verify T[] toArray(final T[] input)")
    public void verifyGenericToArray() {
        Student[] students = new Student[students_List.size()];
        students = students_List.toArray(students);

        assertEquals(students.length, students_List.size());
        assertEquals(students[0], students_List.get(0)); // Check first objects
        assertEquals(students[students.length - 1], students_List.get(students_List.size() - 1)); // Check last objects
    }

    @Test
    @DisplayName("Verify T[] toArray(final T[] input) when input.length < size()")
    public void verifyTToArrayFinalTInputWhenInputLengthSize() {
        Student[] students = new Student[students_List.size() / 2];
        int sizeBefore = students.length;
        students = students_List.toArray(students);

        assertNotEquals(sizeBefore, students_List.size()); // Check that size are not same
        assertEquals(students.length, students_List.size()); // Check that size are same
        assertEquals(students[0], students_List.get(0)); // Check first objects
        assertEquals(students[students.length - 1], students_List.get(students_List.size() - 1)); // Check last objects
    }

    @Test
    @DisplayName("Add object to the index")
    public void addObjectToTheIndex() {
        int index = students_List.size() - 3;
        students_List.add(index, student0);

        assertEquals(student0, students_List.get(index));
    }

    @Test
    @DisplayName("Set object to the specified index")
    public void setObjectToTheSpecifiedIndex() {
        int index = students_List.indexOf(students_List.get(students_List.size() / 2));
        Student studentAtIndex = students_List.get(index);

        students_List.set(index, student0);

        assertFalse(studentAtIndex.equals(students_List.get(index)));
        assertTrue(student0.equals(students_List.get(index)));
    }

    @Test
    @DisplayName("Check returning the false if remove an unexisting  Object")
    public void checkReturningFalseIfRemoveUnexistingObject() {
        assertFalse(students_List.remove(student0));
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
        assertArrayEquals(students_List.toArray(), getArrayOfStudents());
    }

    @Test
    @DisplayName("Test indexOf() null element")
    public void testIndexOfNullElement() {
        students_List.add(1, null);
        assertTrue(students_List.indexOf(null) == students_List.size() - students_List.size() + 1);
    }

    @Test
    @DisplayName("Test lastIndexOf()")
    public void testLastIndexOf() {
        assertTrue(students_List.lastIndexOf(student2) == students_List.size() - students_List.size() + 1);
    }

    @Test
    @DisplayName("Test lastIndexOf null element")
    public void testLastIndexOfNull() {
        emptyList.add(null);
        assertTrue(emptyList.lastIndexOf(null) == 0);
    }

    @Test
    @DisplayName("test returning -1 if provided non existing object to lastIndexOf()")
    public void testReturning1IfProvidedNonExistingObjectToLastIndexOf() {
        assertTrue(students_List.lastIndexOf(student0) == -1);
    }

    @Test
    @DisplayName("Contains All test")
    public void containsAllTest() {
        assertTrue(students_List.containsAll(listOfStudents()));
    }

    @Test
    @DisplayName("Contains All when not all present")
    public void containsNotAllTest() {
        assertFalse(students_List.containsAll(Arrays.asList(student1, student2, student0)));
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
        students_List.add(new Student("Karl", LocalDate.parse("2020-01-05"), "Motivated"));
        students_List.add(new Student("Colombo", LocalDate.parse("1940-01-05"), "Investigator"));

        assertTrue(students_List.removeAll(listOfStudents()));
    }

    @Test
    @DisplayName("Test retaining the objects")
    public void testRetainingTheObjects() {
        students_List.add(new Student("Karl", LocalDate.parse("2020-01-05"), "Motivated"));
        students_List.add(new Student("Colombo", LocalDate.parse("1940-01-05"), "Investigator"));
        List<Student> sublistOfStudents = students_List.subList(2, 5);

        assertTrue(students_List.retainAll(sublistOfStudents));
        assertEquals(sublistOfStudents.get(0), students_List.get(0));
        assertEquals(sublistOfStudents.get(1), students_List.get(1));
        assertEquals(sublistOfStudents.get(2), students_List.get(2));
    }

    @Test
    @DisplayName("Check hasNext() of listIterator() when collection is not empty")
    public void checkHasNextListIterator() {
        assertTrue(students_List.listIterator().hasNext());
        assertTrue(students_List.size() != 0);
    }

    @Test
    @DisplayName("Check hasNext() of listIterator() when collection is empty")
    public void checkHasNextListIteratorWhenEmptyCollection() {
        assertFalse(emptyList.listIterator().hasNext());
        assertFalse(emptyList.size() != 0);
    }

    @Test
    @DisplayName("Check throwing NoSuchElementException() of next() when cursor at the end of collection")
    public void checkExceptionOfNext() {
        ListIterator iterator = students_List.listIterator(students_List.size() - 1);
        assertThrows(NoSuchElementException.class, () -> iterator.next());
    }

    @Test
    @DisplayName("Check next() of listIterator() when collection is not empty")
    public void checkNextOfListIterator() {
        assertNotEquals(students_List.listIterator().next(), null);
    }

    @Test
    @DisplayName("Check next() of listIterator() when collection is empty")
    public void checkNextOfListIteratorWhenEmptyCollection() {
        assertEquals(emptyList.listIterator().next(), null);
    }

    @Test
    @DisplayName("Check previous() of listIterator when collection is not empty")
    public void checkPreviousOfListIteratorWhenCollectionIsNotEmpty() {
        ListIterator iterator = students_List.listIterator(2);
        assertTrue(iterator.hasPrevious());
        assertNotEquals(iterator.previous(), null);
        assertNotEquals(iterator.previous(), null);
        assertFalse(iterator.hasPrevious());
    }

    @Test
    @DisplayName("Test throwing IndexOutOfBoundsException when no cursors index is wrong for previous()")
    public void testThrowingIndexOutOfBoundsExceptionWhenNoCursorsIndexIsWrongForPrevious() {
        ListIterator iterator = students_List.listIterator(0);
        assertThrows(NoSuchElementException.class, () -> iterator.previous());
    }

    @Test
    @DisplayName("Check the nextIndex() when it exist")
    public void checkTheNextIndexWhenItExist() {
        ListIterator iterator = students_List.listIterator(0);
        assertTrue(iterator.nextIndex() == 1);
    }

    @Test
    @DisplayName("Check the previousIndex() when it exist")
    public void checkThePreviousIndexWhenItExist() {
        ListIterator iterator = students_List.listIterator(1);
        assertTrue(iterator.previousIndex() == 0);
    }

    @Test
    @DisplayName("Check removing by iterator")
    public void checkRemovingByIterator() {
        emptyList.add(student0);
        ListIterator iterator = emptyList.listIterator();
        iterator.remove();

        assertTrue(emptyList.size() == 0);
    }

    @Test
    @DisplayName("Check adding by iterator")
    public void checkAddingByIterator() {
        ListIterator iterator = emptyList.listIterator();
        iterator.add(student0);

        assertTrue(emptyList.size() == 1 && emptyList.contains(student0));
    }

    @Test
    @DisplayName("Check setting by iterator")
    public void checkSettingByIterator() {
        ListIterator iterator = students_List.listIterator();
        iterator.set(student0);

        assertTrue(students_List.contains(student0));
    }

    @Test
    @DisplayName("Check throwing ConcurrentModificationException by iterator while removing the last element")
    public void checkThrowingConcurrentModificationExceptionWhileRemovingTheLastElement() {
        ListIterator iterator = emptyList.listIterator();

        assertThrows(ConcurrentModificationException.class, () -> iterator.remove());
    }

    @Test
    @DisplayName("Check iterator of iterator()")
    public void checkIteratorOfIterator() {
        Iterator it = students_List.iterator();
        assertTrue(it.hasNext());
        assertFalse(it.next().equals(null));
    }

    @Test
    @DisplayName("Test forEachRemaining() method of iterator")
    public void testForEachRemainingMethodOfIterator() {
        Iterator it = students_List.iterator();
        int sizeBeforeAction = students_List.size();
        it.forEachRemaining(n -> {
            it.remove();
        });

        assertFalse(sizeBeforeAction == students_List.size());
    }

    @Test
    @DisplayName("Check throwing ConcurrentModificationException by iterator while removing")
    public void checkRemovingConcurrently() {
        ListIterator iterator = students_List.listIterator();
        createThreads(400, students_List, "add");
        createThreads(200, students_List, "remove");
        createThreads(400, students_List, "add to index");
        createThreads(100, students_List, "remove by index");

        assertThrows(ConcurrentModificationException.class, () -> iterator.remove());
    }

    @Test
    @DisplayName("Check throwing ConcurrentModificationException by iterator while adding")
    public void checkAddingConcurrently() {
        ListIterator iterator = students_List.listIterator();
        createThreads(800, students_List, "add");
        createThreads(800, students_List, "add to index");
        createThreads(200, students_List, "remove");
        createThreads(300, students_List, "set to index");

        assertThrows(ConcurrentModificationException.class, () -> iterator.add(student0));
    }

    @Test
    @DisplayName("Check throwing ConcurrentModificationException by iterator while set")
    public void checkSetConcurrently() {
        ListIterator iterator = students_List.listIterator();
        createThreads(800, students_List, "add");
        createThreads(800, students_List, "add to index");
        createThreads(200, students_List, "remove");
        createThreads(400, students_List, "set to index");

        assertThrows(ConcurrentModificationException.class, () -> iterator.set(student0));
    }
}