/*
 * Copyright (c) 2023. Etienne Collin #20237904 & Justin Villeneuve #20132792
 */

package com.etiennecollin.tp2.clientSimple;

import com.etiennecollin.tp2.server.Server;
import com.etiennecollin.tp2.server.models.Course;
import com.etiennecollin.tp2.server.models.RegistrationForm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

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
    public static void run(int port) throws IOException {
        boolean doExit = false;
        // Scan user input
        Scanner scanner = new Scanner(System.in);

        while (!doExit) {
            // Connect to the server
            client = new Socket("localhost", port);

            // Create streams to write/read to/from the server
            objectOutputStream = new ObjectOutputStream(client.getOutputStream());
            objectInputStream = new ObjectInputStream(client.getInputStream());

            // Prepare to read user input
            String line = null;

            try {
                // Read user input
                line = scanner.nextLine();
                // Parse command
                String[] command = line.split(" ");

                // Check if command is valid
                if (command[0].equalsIgnoreCase(Server.REGISTER_COMMAND)) {
                    register(command);
                } else if (command[0].equalsIgnoreCase(Server.LOAD_COMMAND)) {
                    getCourses(command);
                } else if (command[0].equalsIgnoreCase(Server.DISCONNECT_COMMAND)) {
                    doExit = true;
                    disconnect();
                } else {
                    // TODO check if this is a vulnerability
                    objectOutputStream.writeObject(command[0]);
                    objectOutputStream.flush();
                    throw new IllegalArgumentException("[Client] Input command is invalid.");
                }

                // Print server reply
                System.out.println(objectInputStream.readObject());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } finally {
                // Closing the scanner object
                cleanup();
            }
        }
    }

    /**
     * Sends a registration form to the server.
     *
     * @param command The registration command and its arguments.
     *
     * @throws IOException If an I/O error occurs when dealing with the client input/output streams.
     */
    public static void register(String[] command) throws IOException {
        if (command.length != 8) {
            throw new IllegalArgumentException("INSCRIRE requires 8 arguments specifying student and course info.");
        }
        RegistrationForm form = new RegistrationForm(command[1], command[2], command[3], command[4], new Course(command[5], command[6], command[7]));
        objectOutputStream.writeObject(command[0]);
        objectOutputStream.flush();
        objectOutputStream.writeObject(form);
        objectOutputStream.flush();
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
    public static void cleanup() throws IOException {
        objectOutputStream.close();
        objectInputStream.close();
        client.close();
    }
}

