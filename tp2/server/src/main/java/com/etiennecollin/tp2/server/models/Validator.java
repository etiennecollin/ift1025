/*
 * Copyright (c) 2023. Etienne Collin #20237904
 */

package com.etiennecollin.tp2.server.models;

import java.util.regex.Pattern;

/**
 * The Validator class implements methods that validate information.
 */
public class Validator {
    /**
     * Validates an email.
     * <p>
     * Source of pattern: <a href="https://stackoverflow.com/a/8204716">StackOverflow</a>
     *
     * @param email The email to validate.
     *
     * @return Whether the email is valid or not.
     */
    public static boolean isEmailValid(String email) {
        // Generate email regex pattern
        Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        return pattern.matcher(email).matches();
    }

    /**
     * Validates a student ID.
     *
     * @param studentID The student ID to validate.
     *
     * @return Whether the student ID is valid or not.
     */
    public static boolean isStudentIDValid(String studentID) {
        // Generate email regex pattern
        Pattern pattern = Pattern.compile("^\\d{8}$");
        return pattern.matcher(studentID).matches();
    }
}
