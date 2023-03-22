/*
 * Copyright (c) 2023. Etienne Collin #20237904 & Justin Villeneuve #20132792
 */

package com.etiennecollin.tp2.server;

import com.etiennecollin.tp2.server.models.Course;
import com.etiennecollin.tp2.server.models.RegistrationForm;
import javafx.util.Pair;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * The ClientHandler class implements the Runnable interface and handles incoming client connections to the server.
 * <p>
 * It delegates the execution of received commands to appropriate event handlers. The class contains methods for
 * registering a client to a course, filtering courses by semester and disconnecting the client.
 */
public class ClientHandler implements Runnable {
    private final Socket client;
    private final ObjectInputStream objectInputStream;
    private final ObjectOutputStream objectOutputStream;
    private final ArrayList<EventHandler> handlers;

    public ClientHandler(Socket client) throws IOException {
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
    public void addEventHandler(EventHandler handler) {
        this.handlers.add(handler);
    }

    /**
     * Delegates the execution of a command to the appropriate event handler.
     *
     * @param cmd The command to execute.
     * @param arg The argument to pass to the event handler.
     */
    public void handleEvents(String cmd, String arg) {
        System.out.println("[Server] Received command " + cmd + " from client: " + client);
        try {
            if (cmd.equalsIgnoreCase(Server.REGISTER_COMMAND)) {
                handleRegistration();
            } else if (cmd.equalsIgnoreCase(Server.LOAD_COMMAND)) {
                handleLoadCourses(arg);
            } else if (cmd.equalsIgnoreCase(Server.DISCONNECT_COMMAND)) {
                disconnect();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method handles the registration process of a client to a course. It reads the RegistrationForm object from
     * the objectInputStream, writes the registration information to a text file, and sends a confirmation message to
     * the client.
     *
     * @throws IOException            If an I/O error occurs while writing to the text file containing the courses or
     *                                when writing to the ObjectOutputStream.
     * @throws ClassNotFoundException If the content of the ObjectInputStream is not a RegistrationForm object.
     */
    public void handleRegistration() throws IOException, ClassNotFoundException {
        // Read the RegistrationForm object from the object input stream
        RegistrationForm form = (RegistrationForm) objectInputStream.readObject();

        // Write the registration information to a text file
        // TODO Fix path for JAR
        String filePath = "server/src/main/java/com/etiennecollin/tp2/server/data/inscription.txt";

        // Create a PrintWriter object that writes to a file
        // Use a FileWriter object to append to the file if it already exists
        PrintWriter writer = new PrintWriter(new FileWriter(filePath, true));
        writer.println(form.getCourse().getSemester() + "\t" + form.getCourse().getCode() + "\t" + form.getStudentID() + "\t" + form.getFirstName() + "\t" + form.getLastName() + "\t" + form.getEmail());
        writer.close();

        // Send a confirmation message to the client
        String message = "[Server] Registration successful, " + form.getFirstName() + " " + form.getLastName() + ". Thank you for registering to the " + form.getCourse().getSemester() + " " + form.getCourse().getCode() + " course!";
        objectOutputStream.writeObject(message);
    }

    /**
     * Reads a file containing course information, filters the courses by semester,
     * and writes the filtered courses to an object output stream.
     *
     * @param semester A string representing the semester to filter the courses by.
     *
     * @throws IOException If an I/O error occurs while reading from the text file containing the courses or
     *                     when writing to the ObjectOutputStream.
     */
    public void handleLoadCourses(String semester) throws IOException {
        // TODO Fix path for JAR
        String filePath = "server/src/main/java/com/etiennecollin/tp2/server/data/cours.txt";

        List<Course> courses = new ArrayList<>();
        Scanner scanner = new Scanner(new File(filePath));

        // Read all the lines in the file
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            // Extract arguments from line. The format is `code \t name \t semester`
            String[] tokens = line.split("\t");

            // Make sure three arguments form the course in the file
            if (tokens.length != 3) {
                throw new InvalidObjectException("[Server] The courses in " + filePath + " are not properly formatted. The format is `code\tname\tsemester`");
            }

            // Filter for the right semester
            if (tokens[2].equalsIgnoreCase(semester)) {
                courses.add(new Course(tokens[1], tokens[0], tokens[2]));
            }
        }

        scanner.close();
        // Write the list of courses to the object output stream
        objectOutputStream.writeObject("[Server] " + courses);
    }

    /**
     * Closes the input and output streams and the client socket.
     *
     * @throws IOException If an I/O error occurs while closing the object input/output streams or the socket.
     */
    public void disconnect() throws IOException {
        objectOutputStream.writeObject("[Server] Confirming disconnection...");
        objectOutputStream.flush();
        objectOutputStream.close();
        objectInputStream.close();
        client.close();
    }

    @Override
    public void run() {
        // Process the client's command and disconnect
        try {
            listen();
            // disconnect();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            System.out.println("[Server] Client disconnected: " + client);
        }
    }

    /**
     * Reads a command from the client through the object input stream and alerts the registered event handlers
     * to handle the received command.
     *
     * @throws IOException            If an I/O error occurs while reading from the input stream.
     * @throws ClassNotFoundException If the class of the serialized object in the input stream cannot be found.
     */
    public void listen() throws IOException, ClassNotFoundException {
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
    public Pair<String, String> processCommandLine(String line) {
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
