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

/**
 * The Client class represents a client that can interact with a server through a socket connection.
 * <p>
 * The client can send registration forms, load courses for a specific semester, and disconnect from the server.
 * The client is run using the static method run(int port), which connects the client to the server and then allows
 * the user to input commands that are sent to the server. The client uses object input/output streams to communicate
 * with the server.
 */
public class Client {
    private static final String[] semesters = new String[]{"Automne", "Hiver", "Ete"};
    private static Socket client;
    private static ObjectInputStream objectInputStream;
    private static ObjectOutputStream objectOutputStream;
    private static boolean done = false;

    /**
     * Runs the client, allowing the user to interact with the server.
     *
     * @param port The port number of the server to connect to.
     *
     * @throws IOException            If the methods {@link #getUserInput() getUserInput()} or
     *                                {@link #cleanup() cleanup()} throw the exception or when dealing with the client
     *                                input/output streams.
     * @throws ClassNotFoundException If the method {@link #getUserInput() getUserInput()} throws the exception.
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

    /**
     * Gets the user input command and processes, it if it is valid, before passing it to the server.
     *
     * @throws IOException            If the methods {@link #register(String[], Scanner) register()},
     *                                {@link #getCourses(String[]) getCourses()} or {@link #disconnect() disconnect()}
     *                                throw the exception, or if an I/O error occurs when dealing
     *                                with the client input/output streams.
     * @throws ClassNotFoundException If the methods {@link #register(String[], Scanner) register()} or
     *                                {@link #getCourses(String[]) getCourses()} throw the exception.
     */
    public static void getUserInput() throws IOException, ClassNotFoundException {
        // Read and parse user input
        Scanner scanner = new Scanner(System.in);

        try {
            // Tell the user the client is waiting for input via this symbol
            System.out.print("> ");
            String[] command = scanner.nextLine().split(" ");

            // Check if command is valid
            if (command.length == 0) {
                throw new IllegalArgumentException("[Client] Input command is invalid.");
            } else if (command[0].equalsIgnoreCase(Server.REGISTER_COMMAND)) {
                String serverAnswer = register(command, scanner);
                // Print server answer
                System.out.println("\n[Client] " + serverAnswer);
            } else if (command[0].equalsIgnoreCase(Server.LOAD_COMMAND)) {
                Object object = getCourses(command);
                // Print available courses or a message
                System.out.println("\n[Client] " + object);
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
     * @param scanner The scanner which will read the user input.
     *
     * @return The answer from the server.
     *
     * @throws IOException            If the method {@link #courseSelectionMenu(Scanner) courseSelectionMenu()} throws the exception or if an I/O error occurs when dealing with the client input/output streams.
     * @throws ClassNotFoundException If the method {@link #courseSelectionMenu(Scanner) courseSelectionMenu()} throws the exception or if the returned String by the server is invalid.
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
            System.out.println("\n[Client] Welcome to the course registration portal of the UdeM.");
            course = courseSelectionMenu(scanner);
            student = createStudent(scanner);
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
     * Sends a command to the server to load available courses with the option to filter for a specific semester.
     *
     * @param command The load command and its optional argument.
     *
     * @throws IOException              If an I/O error occurs when dealing with the client input/output streams.
     * @throws IllegalArgumentException If the command has an incorrect number of arguments.
     * @throws ClassNotFoundException   If the returned Course object by the server is invalid.
     */
    public static Object getCourses(String[] command) throws IOException, IllegalArgumentException, ClassNotFoundException {
        // Send command to server
        if (command.length == 1) {
            objectOutputStream.writeObject(Server.LOAD_COMMAND);
        } else if (command.length == 2) {
            objectOutputStream.writeObject(Server.LOAD_COMMAND + " " + command[1]);
        } else {
            throw new IllegalArgumentException("[Client] " + Server.LOAD_COMMAND + " requires a maximum of 1 argument specifying the semester to filter.");
        }
        objectOutputStream.flush();

        // Get server reply
        ArrayList<Course> courses = (ArrayList<Course>) objectInputStream.readObject();

        // Check if there are available courses or not
        if (courses.isEmpty()) {
            // Check if a semester was provided
            if (command.length == 1) {
                return "No courses are available.";
            } else {
                return "No courses are available during the " + command[1] + " semester.";
            }
        }

        return courses;
    }

    /**
     * Disconnects the client from the server by sending a DISCONNECT_COMMAND to the server through the objectOutputStream.
     *
     * @throws IOException If an I/O error occurs when writing to the objectOutputStream.
     */
    public static void disconnect() throws IOException {
        objectOutputStream.writeObject(Server.DISCONNECT_COMMAND);
        objectOutputStream.flush();
        System.out.println("[Client] Disconnecting from server...");
    }

    /**
     * Makes the user select an available course.
     *
     * @param scanner The scanner used to get user input.
     *
     * @return A valid Course object.
     *
     * @throws IOException            If the method {@link #getCourses(String[]) getCourses()} throws the exception.
     * @throws ClassNotFoundException If the method {@link #getCourses(String[]) getCourses()} throws the exception.
     */
    public static Course courseSelectionMenu(Scanner scanner) throws IOException, ClassNotFoundException {
        // Toggle used throughout method to stop loops
        boolean isValid;

        while (true) {
            String semester = null;

            // Reset isValid and get semester
            isValid = false;
            while (!isValid) {
                // List available semesters
                System.out.println("[Client] Please select the semester for which you would like to consult available courses:");
                for (int i = 0; i < semesters.length; i++) {
                    System.out.println(i + ". " + semesters[i]);
                }

                // Let user select semester
                System.out.print("> ");
                try {
                    int choice = Integer.parseInt(scanner.nextLine());
                    semester = semesters[choice];
                    isValid = true;
                } catch (Exception e) {
                    System.out.println("[Client] Invalid input.");
                }
            }

            // Get available courses
            Object object = getCourses(new String[]{Server.LOAD_COMMAND, semester});

            // Check if courses were found
            if (!(object instanceof ArrayList)) {
                System.out.println("[Client] " + object);
                continue;
            }

            // List available courses
            ArrayList<Course> courses = (ArrayList<Course>) object;
            Course course = null;

            // Reset isValid and get course
            isValid = false;
            while (!isValid) {
                // List available courses
                System.out.println("[Client] Choose one ot the available courses:");
                for (int i = 0; i < courses.size(); i++) {
                    System.out.println(i + ". " + courses.get(i).getCode() + "\t" + courses.get(i).getName());
                }

                // Let user select course
                System.out.print("> ");
                try {
                    int choice = Integer.parseInt(scanner.nextLine());
                    course = courses.get(choice);
                    isValid = true;
                } catch (Exception e) {
                    System.out.println("[Client] Invalid input.");
                }
            }

            // Return selected course
            return course;
        }
    }

    /**
     * Creates an array of strings containing the information about a student.
     *
     * @param scanner The scanner which will read the user input.
     *
     * @return An array of strings containing the information about a student.
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
}

