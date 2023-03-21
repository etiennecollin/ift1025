package com.etiennecollin.tp2.server;

import com.etiennecollin.tp2.server.models.Course;
import com.etiennecollin.tp2.server.models.RegistrationForm;
import javafx.util.Pair;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * This class represents a server that listens on a specified port for incoming client requests.
 * <p>
 * It can handle two types of commands, "INSCRIRE" and "CHARGER", and delegates their execution to registered event handlers.
 */
public class Server {
    /**
     * The command used to register a new student.
     */
    public final static String REGISTER_COMMAND = "INSCRIRE";
    /**
     * The command used to load course information for a given semester.
     */
    public final static String LOAD_COMMAND = "CHARGER";
    private final ServerSocket server;
    private final ArrayList<EventHandler> handlers;
    private Socket client;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    /**
     * Constructs a new Server object and binds it to the specified port.
     *
     * @param port The port on which to bind the server socket.
     *
     * @throws IOException If an I/O error occurs while creating the server socket.
     */
    public Server(int port) throws IOException {
        this.server = new ServerSocket(port, 1);
        this.handlers = new ArrayList<EventHandler>();
        this.addEventHandler(this::handleEvents);
    }

    /**
     * Adds a new event handler to the server's list of handlers.
     *
     * @param h The event handler to add.
     */
    public void addEventHandler(EventHandler h) {
        this.handlers.add(h);
    }

    /**
     * Delegates the execution of a command to the appropriate event handler.
     *
     * @param cmd The command to execute.
     * @param arg The argument to pass to the event handler.
     */
    public void handleEvents(String cmd, String arg) {
        if (cmd.equals(REGISTER_COMMAND)) {
            handleRegistration();
        } else if (cmd.equals(LOAD_COMMAND)) {
            handleLoadCourses(arg);
        }
    }

    /**
     * Récupérer l'objet 'RegistrationForm' envoyé par le client en utilisant 'objectInputStream', l'enregistrer
     * dans un fichier texte et renvoyer un message de confirmation au client.
     * <p>
     * throws Exception si une erreur se produit lors de la lecture de l'objet, l'écriture dans un fichier ou dans le flux de sortie.
     */
    public void handleRegistration() {
        // Read the RegistrationForm object from the object input stream
        RegistrationForm form = null;
        try {
            form = (RegistrationForm) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        // Write the registration information to a text file
        String filePath = "data/inscription.txt";
        // Create a PrintWriter object that writes to a file
        // Use a FileWriter object to append to the file if it already exists
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
            writer.println(form);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Send a confirmation message to the client
        String message = "Inscription réussie, " + form.getFirstName() + form.getLastName() + ". Merci de vous être inscrit au cours " + form.getCourse() + "!";
        try (PrintWriter writer = new PrintWriter(client.getOutputStream(), true)) {
            writer.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads a file containing course information, filters the courses by semester,
     * and writes the filtered courses to an object output stream.
     *
     * @param semester A string representing the semester to filter the courses by.
     */
    public void handleLoadCourses(String semester) {
        List<Course> courses = new ArrayList<>();
        String filePath = "data/cours.txt";
        try (Scanner scanner = new Scanner(new File(filePath))) {
            // Read all the lines in the file
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // Extract arguments from line. The format is `code \t name \t semester`
                String[] tokens = line.split("\t");

                // Make sure three arguments form the course
                if (tokens.length != 3) {
                    throw new InvalidObjectException("The courses in " + filePath + " are not properly formatted. The format is `code\tname\tsemester`");
                }

                // Filter for the right semester
                if (tokens[2].equals(semester)) {
                    courses.add(new Course(tokens[1], tokens[0], tokens[2]));
                }
            }
        } catch (FileNotFoundException e) {
            // If the file is not found, print the stack trace and return.
            e.printStackTrace();
            return;
        } catch (InvalidObjectException e) {
            e.printStackTrace();
            return;
        }

        try {
            // Write the list of courses to the object output stream
            objectOutputStream.writeObject(courses);
        } catch (IOException e) {
            // If an error occurs while writing to the object output stream, print the stack trace
            e.printStackTrace();
        }
    }

    /**
     * Starts the server and listens for incoming client requests.
     * <p>
     * When a request is received, it creates an ObjectInputStream and ObjectOutputStream,
     * processes the command received from the client using the listen method,
     * and then disconnects from the client.
     */
    public void run() {
        // Continuously listen for incoming client requests
        while (true) {
            try {
                // Accept the request
                client = server.accept();
                System.out.println("Connecté au client: " + client);

                // Create streams to read/write from/to the client
                objectInputStream = new ObjectInputStream(client.getInputStream());
                objectOutputStream = new ObjectOutputStream(client.getOutputStream());

                // Process the client's command and disconnect
                listen();
                disconnect();
                System.out.println("Client déconnecté!");
            } catch (Exception e) {
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
     * Closes the input and output streams and the client socket.
     *
     * @throws IOException If an I/O error occurs while closing the streams or the socket.
     */
    public void disconnect() throws IOException {
        objectOutputStream.close();
        objectInputStream.close();
        client.close();
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
     * @param arg The argument to pass to the handlers.
     */
    private void alertHandlers(String cmd, String arg) {
        for (EventHandler h : this.handlers) {
            h.handle(cmd, arg);
        }
    }
}

