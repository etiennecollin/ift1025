/*
 * Copyright (c) 2023. Etienne Collin #20237904
 */

package com.etiennecollin.tp2.clientSimple;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;

import static com.etiennecollin.tp2.server.Server.*;

/**
 * The ClientLauncher class launches a client to bind to a specific server port and starts its execution.
 * <p>
 * The class contains a main method that creates a client instance and starts its execution.
 */
public class ClientLauncher {
    /**
     * Prefix for client standard status messages.
     */
    static final String CLIENT = "[Client] ";
    /**
     * Color code to reset text color to default.
     */
    private static final String ANSI_RESET = "\u001B[0m";
    private static final int PORT = 1337;
    /**
     * Color code to set text color to red.
     */
    private static final String ANSI_RED = "\u001B[31m";
    /**
     * Prefix for server error status messages.
     */
    static final String CLIENT_ERROR = ANSI_RED + CLIENT + ANSI_RESET;
    /**
     * Color code to set text color to green.
     */
    private static final String ANSI_GREEN = "\u001B[32m";
    /**
     * Prefix for server success status messages.
     */
    static final String CLIENT_SUCCESS = ANSI_GREEN + CLIENT + ANSI_RESET;
    /**
     * Color code to set text color to blue.
     */
    private static final String ANSI_BLUE = "\u001B[34m";

    /**
     * The main method of the ClientLauncher class launches a client to bind to a
     * specific server port and starts its execution.
     *
     * @param args An array of command-line arguments passed to the program.
     */
    public static void main(String[] args) {
        try {
            System.out.println(CLIENT + "Running...");
            System.out.println(CLIENT + "Available commands are: " + ANSI_BLUE + LOAD_COMMAND + ANSI_RESET + ", " + ANSI_BLUE + REGISTER_COMMAND + ANSI_RESET + " and " + ANSI_BLUE + DISCONNECT_COMMAND + ANSI_RESET + ".");

            Client.run(PORT);
        } catch (ConnectException e) {
            // Handle the case where no server is found
            System.out.println(CLIENT_ERROR + e.getMessage() + ", no server available on port " + PORT + ".");
        } catch (SocketException e) {
            // Handle the case where the server crashes without disconnecting
            System.out.println(CLIENT_ERROR + e.getMessage() + ", the connection to the server was lost.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}