/*
 * Copyright (c) 2023. Etienne Collin #20237904 & Justin Villeneuve #20132792
 */

package com.etiennecollin.tp2.server.models;

import java.io.Serializable;

/**
 * A serializable class representing a registration form for a course.
 * <p>
 * The registration form contains information about the student registering for a course, including their first name,
 * last name, email, and student ID. It also includes the course the student is registering for.
 */
public class RegistrationForm implements Serializable {
    private String firstName;
    private String lastName;
    private String email;
    private String studentID;
    private Course course;

    /**
     * Constructs a new RegistrationForm for a course with the given student information.
     *
     * @param firstName The first name of the student.
     * @param lastName  The last name of the student.
     * @param email     The email address of the student.
     * @param studentID The student ID of the student.
     * @param course    The course the student wants to register for.
     */
    public RegistrationForm(String firstName, String lastName, String email, String studentID, Course course) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.studentID = studentID;
        this.course = course;
    }

    /**
     * Returns the first name of the student.
     *
     * @return the first name of the student.
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
     * @param lastName the new last name of the student.
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
     * Returns the course the student wants to register for.
     *
     * @return The course the student wants to register for.
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Sets the course the student wants to register for.
     *
     * @param course The new course the student wants to register for.
     */
    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     * Returns a string representation of the RegistrationForm object. The string representation includes the
     * first name, last name, email, student ID, and course information of the registration form.
     *
     * @return A string representation of the RegistrationForm object.
     */
    @Override
    public String toString() {
        return "InscriptionForm{" + "prenom='" + firstName + '\'' + ", nom='" + lastName + '\'' + ", email='" + email + '\'' + ", matricule='" + studentID + '\'' + ", course='" + course + '\'' + '}';
    }
}
