/*
 * Copyright (c) 2023. Etienne Collin #20237904
 */

package com.etiennecollin.tp2.server;

import java.io.IOException;

/**
 * The ServerLauncher class launches a server on a specified port and starts its execution.
 */
class ServerLauncher {
    /**
     * Prefix for server standard status messages.
     */
    static final String SERVER = "[Server] ";
    /**
     * Color code to reset text color to default.
     */
    static final String ANSI_RESET = "\u001B[0m";
    /**
     * Color code to set text color to blue.
     */
    static final String ANSI_BLUE = "\u001B[34m";
    private static final int PORT = 1337;
    /**
     * Color code to set text color to red.
     */
    private static final String ANSI_RED = "\u001B[31m";
    /**
     * Prefix for server error status messages.
     */
    static final String SERVER_ERROR = ANSI_RED + SERVER + ANSI_RESET;

    /**
     * The main method of the ServerLauncher class launches a server on a specified port and starts its execution.
     *
     * @param args An array of command-line arguments passed to the program.
     */
    public static void main(String[] args) {
        try {
            Server.run(PORT);
            System.out.println(SERVER + "Running on port " + PORT + ".");
        } catch (IOException e) {
            System.out.println(SERVER_ERROR + e.getMessage());
        }
    }
}