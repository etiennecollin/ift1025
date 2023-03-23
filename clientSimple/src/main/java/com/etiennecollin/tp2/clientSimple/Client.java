/*
 * Copyright (c) 2023. Etienne Collin #20237904 & Justin Villeneuve #20132792
 */

package com.etiennecollin.tp2.clientSimple;

import com.etiennecollin.tp2.server.Server;
import com.etiennecollin.tp2.server.models.Course;
import com.etiennecollin.tp2.server.models.RegistrationForm;
import com.etiennecollin.tp2.server.models.Student;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Client {
    private static Socket client;
    private static ObjectInputStream objectInputStream;
    private static ObjectOutputStream objectOutputStream;

    /**
     * Runs the client, allowing the user to interact with the server.
     *
     * @param port The port number of the server to connect to.
     *
     * @throws IOException If an I/O error occurs when scanning user input or when getting the client input/output streams.
     */
    public static void run(int port) throws IOException, ClassNotFoundException {
        // Connect to the server
        client = new Socket("localhost", port);

        // Create streams to write/read to/from the server
        objectOutputStream = new ObjectOutputStream(client.getOutputStream());
        objectInputStream = new ObjectInputStream(client.getInputStream());

        // Continuously get user to input commands
        while (!done) {
            getUserInput();
        }

        // Close the streams and client
        cleanup();
    }
    public static void getUserInput() throws IOException, ClassNotFoundException {
        // Read and parse user input
        Scanner scanner = new Scanner(System.in);

        try {
            // Tell the user the client is waiting for input via this symbol
            System.out.print("> ");
            String[] command = scanner.nextLine().split(" ");

            // Check if command is valid
            if (command[0].equalsIgnoreCase(Server.REGISTER_COMMAND)) {
                String serverAnswer = register(command, scanner);
                // Print server answer
                System.out.println("[Client] " + serverAnswer);
            } else if (command[0].equalsIgnoreCase(Server.LOAD_COMMAND)) {
                Object object = getCourses(command);
                // Print available courses or a message
                System.out.println("[Client] " + object);
            } else if (command[0].equalsIgnoreCase(Server.DISCONNECT_COMMAND)) {
                done = true;
                scanner.close();
                disconnect();
            } else {
                throw new IllegalArgumentException("[Client] Input command is invalid.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            // Tell the server that the command was invalid as it expects a command
            objectOutputStream.writeObject("INVALID");
            objectOutputStream.flush();
        }
    }

    /**
     * Closes the objectOutputStream, objectInputStream, and client.
     *
     * @throws IOException If an I/O error occurs when closing the streams or the client socket.
     */
    public static void cleanup() throws IOException {
        objectOutputStream.close();
        objectInputStream.close();
        client.close();
    }

    /**
     * Sends a registration form to the server.
     *
     * @param command The registration command and its arguments.
     *
     * @throws IOException If an I/O error occurs when dealing with the client input/output streams.
     */
    public static String register(String[] command, Scanner scanner) throws IOException, ClassNotFoundException {
        // Initialize objects
        RegistrationForm form;
        Student student;
        Course course;

        if (command.length == 8) {
            student = new Student(command[1], command[2], command[3], command[4]);
            course = new Course(command[5], command[6], command[7]);
        } else if (command.length == 1) {
            student = createStudent(scanner);
            course = createCourse(scanner);
        } else {
            throw new IllegalArgumentException("[Client] " + Server.REGISTER_COMMAND + " requires either 0 or 7 arguments specifying the required form information.");
        }

        // Create form
        form = new RegistrationForm(student, course);

        // Send command to the server
        objectOutputStream.writeObject(Server.REGISTER_COMMAND);
        objectOutputStream.flush();

        // Send form to the server
        objectOutputStream.writeObject(form);
        objectOutputStream.flush();

        // Get the answer from the server
        return (String) objectInputStream.readObject();
    }

    /**
     * Sends a command to load courses for a specific semester to the server.
     *
     * @param command The load command and its argument.
     *
     * @throws IOException              If an I/O error occurs when dealing with the client input/output streams.
     * @throws IllegalArgumentException If the command has an incorrect number of arguments.
     */
    public static void getCourses(String[] command) throws IOException, IllegalArgumentException {
        if (command.length != 2) {
            throw new IllegalArgumentException("CHARGER requires an argument specifying the semester to filter.");
        }
        objectOutputStream.writeObject(Server.LOAD_COMMAND + " " + command[1]);
        objectOutputStream.flush();
    }

    /**
     * Disconnects the client from the server by sending a DISCONNECT_COMMAND to the server through the objectOutputStream,
     * and then closes the objectOutputStream, objectInputStream, and client. Throws an IOException if there is an error
     * during the disconnection process.
     *
     * @throws IOException If an I/O error occurs when writing to the objectOutputStream.
     */
    public static void disconnect() throws IOException {
        objectOutputStream.writeObject(Server.DISCONNECT_COMMAND);
        objectOutputStream.flush();
        System.out.println("[Client] Disconnecting from server...");
    }

    /**
     * Closes the objectOutputStream, objectInputStream, and client. Throws an IOException if there is an error
     * during the cleanup process.
     *
     * @throws IOException If an I/O error occurs when closing the streams or the client socket.
     */
    public static Student createStudent(Scanner scanner) {
        // Get first name
        System.out.print("[Client] Input first name: ");
        String firstName = scanner.nextLine();

        // Get last name
        System.out.print("[Client] Input last name: ");
        String lastName = scanner.nextLine();

        // Get valid email
        String email;
        do {
            System.out.print("[Client] Input email: ");
            email = scanner.nextLine();
        } while (!isEmailValid(email));

        // Get student ID
        String studentID;
        do {
            System.out.print("[Client] Input student ID: ");
            studentID = scanner.nextLine();
        } while (!isStudentIDValid(studentID));

        // Return student information
        return new Student(firstName, lastName, email, studentID);
    }

    /**
     * Creates and validates a course object with user input.
     *
     * @param scanner The scanner used to get user input.
     *
     * @return A valid Course object.
     *
     * @throws IOException            If the method {@link #isCourseValid(Course) isCourseValid()} throws the exception.
     * @throws ClassNotFoundException If the method {@link #isCourseValid(Course) isCourseValid()} throws the exception.
     */
    public static Course createCourse(Scanner scanner) throws IOException, ClassNotFoundException {
        Course course;
        do {
            // Get course name
            System.out.print("[Client] Input course name: ");
            String name = scanner.nextLine();

            // Get course code
            System.out.print("[Client] Input course code: ");
            String code = scanner.nextLine();

            // Get course semester
            System.out.print("[Client] Input course semester: ");
            String semester = scanner.nextLine();

            course = new Course(name, code, semester);
        } while (!isCourseValid(course));

        return course;
    }

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
        boolean isValid = pattern.matcher(email).matches();

        // Print message in case of invalid email
        if (!isValid) {
            System.out.println("[Client] The email is invalid.");
        }

        return isValid;
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
        boolean isValid = pattern.matcher(studentID).matches();

        // Print message in case of invalid student ID
        if (!isValid) {
            System.out.println("[Client] The student ID is invalid.");
        }

        return isValid;
    }

    /**
     * Validates that a course is available to take.
     *
     * @param targetCourse The course that is searched in the list of available courses.
     *
     * @return Whether the course is available or not.
     *
     * @throws IOException            If the method {@link #getCourses(String[]) getCourses()} throws the exception.
     * @throws ClassNotFoundException If the method {@link #getCourses(String[]) getCourses()} throws the exception.
     */
    public static boolean isCourseValid(Course targetCourse) throws IOException, ClassNotFoundException {
        Object object = getCourses(new String[]{Server.LOAD_COMMAND, targetCourse.getSemester()});

        // Check that an ArrayList was returned by getCourses()
        if (object instanceof ArrayList) {
            ArrayList<Course> courses = (ArrayList<Course>) object;
            // Check if the course is available
            for (Course course : courses) {
                if (course.equals(targetCourse)) {
                    return true;
                }
            }
        }

        System.out.println("[Client] This course is unavailable.");
        return false;
    }
}

