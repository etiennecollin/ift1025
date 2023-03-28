/*
 * Copyright (c) 2023. Etienne Collin #20237904
 */

package com.etiennecollin.tp2.clientFx;

import java.io.EOFException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;

/**
 * The ClientLauncher class launches a client to bind to a specific server port and starts its execution.
 * <p>
 * The class contains a main method that creates a client instance and starts its execution.
 */
public class ClientLauncher {
    private final static int PORT = 1337;

    /**
     * The main method of the ClientLauncher class launches a client to bind to a
     * specific server port and starts its execution.
     *
     * @param args An array of command-line arguments passed to the program.
     */
    public static void main(String[] args) {
        try {
            Client.run(PORT);
        } catch (ConnectException e) {
            // Handle the case where no server is found
            System.err.println("[Client] " + e.getMessage() + ", no server available on port " + PORT + ".");
        } catch (SocketException | EOFException e) {
            // Handle the case where the server crashes without disconnecting
            System.err.println("[Client] " + e.getMessage() + ", the connection to the server was lost.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}