/*
 * Copyright (c) 2023. Etienne Collin #20237904
 */

package com.etiennecollin.tp2.server;

import com.etiennecollin.tp2.server.models.Course;
import com.etiennecollin.tp2.server.models.RegistrationForm;
import javafx.util.Pair;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static com.etiennecollin.tp2.server.ServerLauncher.*;

/**
 * The ClientHandler class implements the Runnable interface and handles incoming client connections to the server.
 * <p>
 * It delegates the execution of received commands to appropriate event handlers. The class contains methods for
 * registering a client to a course, sending available courses to the client, filtering courses by semester and
 * disconnecting from the client.
 */
class ClientHandler implements Runnable {
    /**
     * Represents the client socket.
     */
    private final Socket client;
    /**
     * Represents the input stream of the client socket.
     */
    private final ObjectInputStream objectInputStream;
    /**
     * Represents the output stream of the client socket.
     */
    private final ObjectOutputStream objectOutputStream;
    /**
     * Represents the list of event handlers for this ClientHandler.
     */
    private final ArrayList<EventHandler> handlers;
    /**
     * Used to tell the handler whether it can stop listening to the client or not.
     */
    private boolean isClientDisconnecting = false;

    /**
     * Creates a new client handler for a specific client.
     *
     * @param client The client that is to be handled.
     *
     * @throws IOException If an I/O error occurs when getting the client streams.
     */
    ClientHandler(Socket client) throws IOException {
        // Store the client and create streams to read/write from/to the client
        this.client = client;
        this.objectInputStream = new ObjectInputStream(client.getInputStream());
        this.objectOutputStream = new ObjectOutputStream(client.getOutputStream());

        // Create handlers
        this.handlers = new ArrayList<>();
        addEventHandler(this::handleEvents);
    }

    /**
     * Adds a new event handler to the server's list of handlers.
     *
     * @param handler The event handler to add.
     */
    private void addEventHandler(EventHandler handler) {
        this.handlers.add(handler);
    }

    /**
     * Delegates the execution of a command to the appropriate event handler.
     *
     * @param cmd The command to execute.
     * @param arg The argument to pass to the event handler.
     */
    private void handleEvents(String cmd, String arg) {
        System.out.println(SERVER + "Received command " + ANSI_BLUE + cmd + ANSI_RESET + " from client: " + ANSI_BLUE + client + ANSI_RESET);
        try {
            if (cmd.equalsIgnoreCase(Server.REGISTER_COMMAND)) {
                handleRegistration();
            } else if (cmd.equalsIgnoreCase(Server.LOAD_COMMAND)) {
                handleLoadCourses(arg);
            } else if (cmd.equalsIgnoreCase(Server.DISCONNECT_COMMAND)) {
                disconnect();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(SERVER_ERROR + e.getMessage());
            e.getStackTrace();
        }
    }

    /**
     * This method handles the registration process of a client to a course.
     * <p>
     * It reads the RegistrationForm object from the objectInputStream, writes the registration information to a text
     * file, and sends a confirmation message to the client.
     *
     * @throws IOException            If an I/O error occurs while writing to the text file containing the courses or
     *                                when dealing with the input/output streams.
     * @throws ClassNotFoundException If the content of the ObjectInputStream is not a RegistrationForm object.
     */
    private void handleRegistration() throws IOException, ClassNotFoundException {
        // Read the RegistrationForm object from the object input stream
        RegistrationForm form = (RegistrationForm) objectInputStream.readObject();

        // Get the file
        String fileName = "inscription.txt";
        String file = System.getProperty("user.dir") + "/data/" + fileName;

        // Create a PrintWriter object that writes to a file
        // Use a FileWriter object to append to the file if it already exists
        PrintWriter writer = new PrintWriter(new FileWriter(file, true));
        writer.println(form.getCourse().getSemester() + "\t" + form.getCourse().getCode() + "\t" + form.getStudentID() + "\t" + form.getFirstName() + "\t" + form.getLastName() + "\t" + form.getEmail());
        writer.close();

        // Send a confirmation message to the client
        String message = "Registration successful, " + form.getFirstName() + " " + form.getLastName() + ". Thank you for registering to the " + form.getCourse().getSemester() + " " + form.getCourse().getCode() + " course!";
        objectOutputStream.writeObject(message);
    }

    /**
     * Reads a file containing course information, filters the courses by semester if needed,
     * and writes the filtered courses to an object output stream.
     *
     * @param semester A string representing the semester to filter the courses by.
     *
     * @throws IOException If an I/O error occurs while reading from the text file containing the courses or
     *                     when writing to the output stream.
     */
    private void handleLoadCourses(String semester) throws IOException {
        ArrayList<Course> courses = new ArrayList<>();

        // Read the file
        String fileName = "cours.txt";
        File file = new File(System.getProperty("user.dir") + "/data/" + fileName);
        Scanner scanner = new Scanner(file);

        // Read all the lines in the file
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            // Extract arguments from line. The format is `code \t name \t semester`
            String[] tokens = line.split("\t");

            // Make sure three arguments form the course in the file
            if (tokens.length != 3) {
                System.out.println(SERVER_ERROR + "The courses in " + fileName + " are not properly formatted. The format is `code\tname\tsemester`");
                break;
            }

            if (semester.equals("")) {
                // If no semester is provided, load all the courses
                courses.add(new Course(tokens[1], tokens[0], tokens[2]));
            } else if (tokens[2].equalsIgnoreCase(semester)) {
                // Filter for the right semester
                courses.add(new Course(tokens[1], tokens[0], tokens[2]));
            }
        }
        scanner.close();

        // Write the list of courses to the object output stream
        objectOutputStream.writeObject(courses);
        objectOutputStream.flush();
    }

    /**
     * Sends a disconnection confirmation to the client, then closes the input/output streams and the client socket.
     *
     * @throws IOException If an I/O error occurs while closing the object input/output streams or the socket.
     */
    private void disconnect() throws IOException {
        // Tell the handler it can stop listening to the client
        isClientDisconnecting = true;

        // Send confirmation
        objectOutputStream.writeObject(SERVER + "Confirming disconnection...");
        System.out.println(SERVER + "Client disconnected: " + ANSI_BLUE + client + ANSI_RESET);
        objectOutputStream.flush();

        // Close streams
        objectOutputStream.close();
        objectInputStream.close();

        // Close socket
        client.close();
    }

    /**
     * Runs a continuous loop that listens to the client's command until the client disconnects.
     * <p>
     * It catches any IO exceptions or class not found exceptions that may occur while listening to the client's command
     * and handles the case where the client crashes without disconnecting by properly terminating the connection to the
     * crashed client.
     */
    @Override
    public void run() {
        while (!isClientDisconnecting) {
            try {
                // Listen to the client's command
                listen();
            } catch (EOFException e) {
                System.out.println(SERVER_ERROR + "Client improperly disconnected. Trying to properly close the streams...");
                // Handle the case where the client crashes without disconnecting
                try {
                    // Properly terminate the connection to the crashed client
                    disconnect();
                } catch (IOException ex) {
                    ex.getStackTrace();
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Reads a command from the client through the object input stream and alerts the registered event handlers
     * to handle the received command.
     *
     * @throws IOException            If an I/O error occurs while reading from the input stream.
     * @throws ClassNotFoundException If the class of the serialized object in the input stream cannot be found.
     */
    private void listen() throws IOException, ClassNotFoundException {
        String line;

        if ((line = this.objectInputStream.readObject().toString()) != null) {
            Pair<String, String> parts = processCommandLine(line);
            String cmd = parts.getKey();
            String arg = parts.getValue();
            this.alertHandlers(cmd, arg);
        }
    }

    /**
     * Processes a given command line into a pair of command and argument strings.
     *
     * @param line The command line to process.
     *
     * @return A pair containing the command and its arguments.
     */
    private Pair<String, String> processCommandLine(String line) {
        // Split the command into two parts
        String[] parts = line.split(" ");

        // The first word is considered the command, and the rest is considered the arguments.
        String cmd = parts[0];
        String args = String.join(" ", Arrays.asList(parts).subList(1, parts.length));
        return new Pair<>(cmd, args);
    }

    /**
     * Alerts the registered event handlers to handle a given command and argument.
     *
     * @param cmd The command to handle.
     * @param arg The argument of the command to handle.
     */
    private void alertHandlers(String cmd, String arg) {
        for (EventHandler handler : this.handlers) {
            handler.handle(cmd, arg);
        }
    }
}
