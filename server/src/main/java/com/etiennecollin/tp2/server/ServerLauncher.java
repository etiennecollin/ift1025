/*
 * Copyright (c) 2023. Etienne Collin #20237904
 */

package com.etiennecollin.tp2.server;

import java.io.IOException;

/**
 * The ServerLauncher class launches a server on a specified port and starts its execution.
 */
public class ServerLauncher {
    protected static final String SERVER = "[Server] ";
    protected static final String ANSI_RESET = "\u001B[0m";
    protected static final String ANSI_BLUE = "\u001B[34m";
    /**
     * Prefix for server standard status messages.
     */
    /**
     * Color code to reset text color to default.
     */
    /**
     * Color code to set text color to blue.
     */
    private static final int PORT = 1337;
    /**
     * Color code to set text color to red.
     */
    private static final String ANSI_RED = "\u001B[31m";
    protected static final String SERVER_ERROR = ANSI_RED + SERVER + ANSI_RESET;
    private static final String ANSI_GREEN = "\u001B[32m";
    protected static final String SERVER_VALID = ANSI_GREEN + SERVER + ANSI_RESET;
    /**
     * Prefix for server error status messages.
     */

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