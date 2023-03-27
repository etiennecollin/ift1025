/*
 * Copyright (c) 2023. Etienne Collin #20237904
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
    private Course course;
    private Student student;

    /**
     * Constructs a new RegistrationForm for a course for the given student.
     *
     * @param student The student to be registered.
     * @param course  The course the student wants to register for.
     */
    public RegistrationForm(Student student, Course course) {
        this.student = student;
        this.course = course;
    }

    /**
     * Returns the student to be registered.
     *
     * @return The student to be registered.
     */
    public Student getStudent() {
        return student;
    }

    /**
     * Sets the student that is to be registered.
     *
     * @param student The student that is to be registered.
     */
    public void setStudent(Student student) {
        this.student = student;
    }

    /**
     * Returns the first name of the student.
     *
     * @return The first name of the student.
     */
    public String getFirstName() {
        return student.getFirstName();
    }

    /**
     * Returns the last name of the student.
     *
     * @return The last name of the student.
     */
    public String getLastName() {
        return student.getLastName();
    }

    /**
     * Returns the email address of the student.
     *
     * @return The email address of the student.
     */
    public String getEmail() {
        return student.getEmail();
    }

    /**
     * Returns the student ID of the student.
     *
     * @return The student ID of the student.
     */
    public String getStudentID() {
        return student.getStudentID();
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
        // return "RegistrationForm{" + "firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", email='" + email + '\'' + ", studentID='" + studentID + '\'' + ", course='" + course + '\'' + '}';
        return "RegistrationForm{" + "student='" + student + "'" + ", course='" + course + "'" + "}";
    }
}
