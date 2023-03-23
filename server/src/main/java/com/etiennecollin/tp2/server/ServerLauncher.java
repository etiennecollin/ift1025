package com.etiennecollin.tp2.server;

import java.io.IOException;

/**
 * The ServerLauncher class launches a server on a specified port and starts its execution.
 * <p>
 * The class contains a constant field for the port number and a main method that creates a server instance and starts its execution.
 */
public class ServerLauncher {
    /**
     * The port on which the server is launched.
     */
    private final static int PORT = 1337;

    /**
     * The main method of the ServerLauncher class launches a server on a specified port and starts its execution.
     *
     * @param args An array of command-line arguments passed to the program.
     */
    public static void main(String[] args) {
        try {
            System.out.println("[Server] Running on port " + PORT + ".");
            Server.run(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}