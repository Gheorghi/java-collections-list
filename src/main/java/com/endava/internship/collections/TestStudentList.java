package com.endava.internship.collections;

import com.endava.internship.collections.Student;
import com.endava.internship.collections.StudentList;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

class TestStudentList {
    StudentList students_List;
    Student student1 = new Student("Bob1", LocalDate.parse("2020-01-01"), "Good student");
    Student student2 = new Student("Bob2", LocalDate.parse("2020-01-02"), "Good student");
    Student student3 = new Student("Bob3", LocalDate.parse("2020-01-03"), "Good student");
    Student student4 = new Student("Bob4", LocalDate.parse("2020-01-04"), "Good student");
    Student student5 = new Student("Bob5", LocalDate.parse("2020-01-05"), "Good student");
    Student student6 = new Student("Bob6", LocalDate.parse("2020-01-06"), "Good student");
    Student student7 = new Student("Bob7", LocalDate.parse("2020-01-07"), "Good student");
    Student student8 = new Student("Bob8", LocalDate.parse("2020-01-08"), "Good student");
    Student student9 = new Student("Bob9", LocalDate.parse("2020-01-09"), "Good student");
    Student student10 = new Student("Bob10", LocalDate.parse("2020-01-10"), "Good student");
    Student student11 = new Student("Bob11", LocalDate.parse("2020-01-11"), "Good student");
    Student student12 = new Student("Bob12", LocalDate.parse("2020-01-12"), "Good student");
    Student student0 = new Student("Jery", LocalDate.parse("2020-01-19"), "Best Student"); // Note this objects will not be added in setUp()

    @Nested
    @DisplayName("When list of students is not empty")
    class WhenListOfStudentsIsNotEmpty {
        @BeforeEach
        void setUp() {
            students_List = new StudentList(Arrays.asList(student1, student2, student3, student4, student5, student6, student7, student8, student9, student10, student11, student12));
        }

        @Test
        @DisplayName("Should return the actual size of list")
        void shouldReturnTheActualSizeOfList() {
            assertEquals(12, students_List.size());
        }

        @Test
        @DisplayName("Check the list if it is empty after removing the content")
        void checkTheListIfItIsEmptyAfterTheRemovingTheContent() {
            students_List.clear();
            assertTrue(students_List.isEmpty());
        }

        @DisplayName("Check if list contains objects")
        @Test
//        @MethodSource("existingStudents") //TODO investigate this way
        void checkIfListContainsObjects() {
            assertAll("Contains",
                    () -> assertTrue(students_List.contains(student1)),
                    () -> assertTrue(students_List.contains(student2)),
                    () -> assertTrue(students_List.contains(student3)),
                    () -> assertTrue(students_List.contains(student4)),
                    () -> assertTrue(students_List.contains(student5)),
                    () -> assertTrue(students_List.contains(student6)),
                    () -> assertTrue(students_List.contains(student7)),
                    () -> assertTrue(students_List.contains(student8)),
                    () -> assertTrue(students_List.contains(student9)),
                    () -> assertTrue(students_List.contains(student10)),
                    () -> assertTrue(students_List.contains(student11)));
        }

        //        List<Student> existingStudents() { //TODO investigate this way
//            return Arrays.asList(student1, student2, student3, student4, student5, student6, student7, student8, student9, student10, student11);
//        }

        @Test
        @DisplayName("Verify getting the Objects from the beginning, middle and last indexes")
        void verifyGettingTheObjectByIndex() {
            assertAll("Getting",
                    () -> assertEquals(students_List.get(0), student1),
                    () -> assertEquals(students_List.get(5), student6),
                    () -> assertEquals(students_List.get(11), student12));
        }

        @Test
        @DisplayName("Check removing the Object from the beginning, middle and last indexes")
        void checkRemovingTheObjectFromTheList() {
            assertAll("Removing",
                    () -> assertTrue(students_List.remove(student1)),
                    () -> assertFalse(students_List.contains(student1)),
                    () -> assertTrue(students_List.remove(student6)),
                    () -> assertFalse(students_List.contains(student6)),
                    () -> assertTrue(students_List.remove(student12)),
                    () -> assertFalse(students_List.contains(student12)));
        }

        @Test
        @DisplayName("Check clearing the list by removing one by one")
        void checkClearingTheList() {
            assertAll("Clearing",
                    () -> assertTrue(students_List.remove(student1)),
                    () -> assertFalse(students_List.contains(student1)),
                    () -> assertTrue(students_List.remove(student2)),
                    () -> assertFalse(students_List.contains(student2)),
                    () -> assertTrue(students_List.remove(student3)),
                    () -> assertFalse(students_List.contains(student3)),
                    () -> assertTrue(students_List.remove(student4)),
                    () -> assertFalse(students_List.contains(student4)),
                    () -> assertTrue(students_List.remove(student5)),
                    () -> assertFalse(students_List.contains(student5)),
                    () -> assertTrue(students_List.remove(student6)),
                    () -> assertFalse(students_List.contains(student6)),
                    () -> assertTrue(students_List.remove(student7)),
                    () -> assertFalse(students_List.contains(student7)),
                    () -> assertTrue(students_List.remove(student8)),
                    () -> assertFalse(students_List.contains(student8)),
                    () -> assertTrue(students_List.remove(student9)),
                    () -> assertFalse(students_List.contains(student9)),
                    () -> assertTrue(students_List.remove(student10)),
                    () -> assertFalse(students_List.contains(student10)),
                    () -> assertTrue(students_List.remove(student11)),
                    () -> assertFalse(students_List.contains(student11)),
                    () -> assertTrue(students_List.remove(student12)),
                    () -> assertFalse(students_List.contains(student12)));
        }

        @Test
        @DisplayName("Check throwing an IndexOutOfBoundsException while deleting from unfilled Index")
        void checkThrowingAn_IndexOutOfBoundsException_WhileDeletingFromUnfilledIndex() {
            assertThrows(IndexOutOfBoundsException.class, () -> students_List.remove(students_List.size() + 1));
        }

        @Test
        @DisplayName("Check throwing IndexOutOfBoundsException while adding to unallocated Index ")
        void checkThrowingIndexOutOfBoundsExceptionWhileAddingToUnallocatedIndex() {
            assertThrows(IndexOutOfBoundsException.class, () -> students_List.add(students_List.size() + 1, student0));
        }

        @Test
        @DisplayName("Check the returning of sub list elements")
        void checkTheReturningOfSubListElements() {
            List<Student> studentsExternal = Arrays.asList(student4, student5, student6);
            List<Student> studentsActual = students_List.subList(3, 6);
            assertEquals(studentsActual.size(), studentsExternal.size());
            assertEquals(studentsActual, studentsExternal);
        }

        @Test
        @DisplayName("Check returning of IndexOutOfBoundsException if 'to > arraySize' in subListRangeCheck()")
        void checkReturningOf_IndexOutOfBoundsException_IfToArraySizeInSubListRangeCheck() {
            assertThrows(IndexOutOfBoundsException.class, () -> students_List.subList(students_List.size() - 1, students_List.size() + 1));
        }

        @Test
        @DisplayName("Check returning of IllegalArgumentException if 'from > to' in subListRangeCheck()")
        void checkReturningOf_IllegalArgumentException_IfFromToInSubListRangeCheck() {
            assertThrows(IllegalArgumentException.class, () -> students_List.subList(students_List.size() + 1, students_List.size()));
        }

        @Test
        @DisplayName("Check returning of IndexOutOfBoundsException if 'from < 0' in subListRangeCheck()")
        void checkReturningOf_IndexOutOfBoundsException_IfFromToInSubListRangeCheck() {
            assertThrows(IllegalArgumentException.class, () -> students_List.subList(0, -1));
        }

        @Test
        @DisplayName("Verify T[] toArray(final T[] input)")
        void verifyGenericToArray() {
            Student[] students = new Student[students_List.size()];
            students = students_List.toArray(students);

            assertEquals(students.length, students_List.size());
            assertEquals(students[0], students_List.get(0)); // Check first objects
            assertEquals(students[students.length - 1], students_List.get(students_List.size() - 1)); // Check last objects
        }

        @Test
        @DisplayName("Verify T[] toArray(final T[] input) when input.length < size()")
        void verifyTToArrayFinalTInputWhenInputLengthSize() {
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
        void addObjectToTheIndex() {
            int index = students_List.size() - 3;
            students_List.add(index, student0);

            assertEquals(student0, students_List.get(index));
        }

//        @DisplayName("Check if list contains objects")
//        @ParameterizedTest
//        @MethodSource("existingStudents")
//            //TODO investigate this way
//        void checkIfList_Contains_Objects(Student students) {
////            for (Student student : students) {
//                assertTrue(students_List.contains(students));
////            }
////            assertAll("Contains",
////                    () -> assertTrue(students_List.contains(student1)),
////                    () -> assertTrue(students_List.contains(student2)),
////                    () -> assertTrue(students_List.contains(student3)),
////                    () -> assertTrue(students_List.contains(student4)),
////                    () -> assertTrue(students_List.contains(student5)),
////                    () -> assertTrue(students_List.contains(student6)),
////                    () -> assertTrue(students_List.contains(student7)),
////                    () -> assertTrue(students_List.contains(student8)),
////                    () -> assertTrue(students_List.contains(student9)),
////                    () -> assertTrue(students_List.contains(student10)),
////                    () -> assertTrue(students_List.contains(student11)));
//        }
//
//        Student[] existingStudents() { //TODO investigate this way
//            Student[] students = new Student[11];
//            students[0]= student1;
//            students[1]= student1;
//            students[2]= student1;
//            students[3]= student1;
//            students[4]= student1;
//            students[5]= student1;
//            students[6]= student1;
//            students[7]= student1;
//            students[8]= student1;
//            students[9]= student1;
//            students[10]= student1;
//            students[11]= student1;
//
//            return students;
//        }

    }
}