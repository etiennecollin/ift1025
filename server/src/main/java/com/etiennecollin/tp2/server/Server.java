package com.etiennecollin.tp2.server;

import javafx.util.Pair;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

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
        try {
            // TODO: implémenter cette méthode
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Lire un fichier texte contenant des informations sur les cours et les transofmer en liste d'objets 'Course'.
     * La méthode filtre les cours par la session spécifiée en argument.
     * Ensuite, elle renvoie la liste des cours pour une session au client en utilisant l'objet 'objectOutputStream'.
     * <p>
     * throws Exception si une erreur se produit lors de la lecture du fichier ou de l'écriture de l'objet dans le flux
     *
     * @param arg The session for which to retrieve the list of courses.
     */
    public void handleLoadCourses(String arg) {
        try {
            // TODO: implémenter cette méthode
        } catch (Exception e) {
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

