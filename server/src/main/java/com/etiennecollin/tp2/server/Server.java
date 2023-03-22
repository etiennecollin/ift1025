package com.etiennecollin.tp2.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
    /**
     * The command used to disconnect from a client.
     */
    public final static String DISCONNECT_COMMAND = "DISCONNECT";

    /**
     * Starts the server and listens for incoming client requests.
     * <p>
     * When a request is received, it creates a new ClientHandler which will process
     * the client interactions with the server in a separate thread.
     */
    public static void run(int port) throws IOException {
        ServerSocket server = new ServerSocket(port, 1);
        // Continuously listen for incoming client requests
        while (true) {
            try {
                // Wait for connection request, then accept it
                System.out.println("[Server] Waiting for a client...");
                Socket client = server.accept();
                System.out.println("[Server] Connected to client: " + client);

                // Create a new thread to listen to the client
                ClientHandler clientHandler = new ClientHandler(client);
                new Thread(clientHandler).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

