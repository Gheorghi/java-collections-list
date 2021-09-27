package com.endava.internship.collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class TestStudent {
    static StudentList students_List;

    @BeforeEach
    void setUp() {
        students_List = new StudentList();
        students_List.add(new Student("Colombo", LocalDate.parse("1920-01-01"), "Good student"));
    }

    @Test
    @DisplayName("Test for getting an Username")
    public void testForGettingAnUserName() {
        assert students_List.get(0).getName().equals("Colombo");
    }

    @Test
    @DisplayName("Test for getting an User's birthdate")
    public void testForGettingAnUserBirthdate() {
        assert students_List.get(0).getDateOfBirth().equals(LocalDate.parse("1920-01-01"));
    }

    @Test
    @DisplayName("Test getting an User's details")
    public void testGettingAnUserSDetails() {
        assert students_List.get(0).getDetails().equals("Good student");
    }


}
