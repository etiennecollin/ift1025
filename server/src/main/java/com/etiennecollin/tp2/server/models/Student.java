/*
 * Copyright (c) 2023. Etienne Collin #20237904
 */

package com.etiennecollin.tp2.server.models;

import java.io.Serializable;

/**
 * The Student class represents a student in the academic system.
 * <p>
 * It contains the first name, last name, email and student ID of the student.
 */
public class Student implements Serializable {
    /**
     * The first name of the student.
     */
    private String firstName;
    /**
     * The last name of the student.
     */
    private String lastName;
    /**
     * The email address of the student.
     */
    private String email;
    /**
     * The student ID of the student.
     */
    private String studentID;

    /**
     * Creates a new Student object with the specified first name, last name, email and student ID.
     *
     * @param firstName The first name of the student.
     * @param lastName  The last name of the student.
     * @param email     The email address of the student.
     * @param studentID The student ID of the student.
     */
    public Student(String firstName, String lastName, String email, String studentID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.studentID = studentID;
    }

    /**
     * Returns the first name of the student.
     *
     * @return The first name of the student.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the student.
     *
     * @param firstName The new first name of the student.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name of the student.
     *
     * @return The last name of the student.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the student.
     *
     * @param lastName The new last name of the student.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the email address of the student.
     *
     * @return The email address of the student.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the student.
     *
     * @param email The new email address of the student.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the student ID of the student.
     *
     * @return The student ID of the student.
     */
    public String getStudentID() {
        return studentID;
    }

    /**
     * Sets the student ID of the student.
     *
     * @param studentID The new student ID of the student.
     */
    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    /**
     * Returns a string representation of the Student object. The string representation includes the first name, last name, email, and student ID of the student.
     *
     * @return A string representation of the Student object.
     */
    @Override
    public String toString() {
        return "Student{" + "firstName='" + firstName + "'" + ", lastName='" + lastName + "'" + ", email='" + email + "'" + ", studentID='" + studentID + "'" + "}";
    }
}
