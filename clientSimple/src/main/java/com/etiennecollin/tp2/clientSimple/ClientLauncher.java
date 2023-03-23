/*
 * Copyright (c) 2023. Etienne Collin #20237904 & Justin Villeneuve #20132792
 */

package com.etiennecollin.tp2.clientSimple;

import java.io.IOException;

/**
 * The ClientLauncher class launches a client to bind to a specific server port and starts its execution.
 * <p>
 * The class contains a main method that creates a client instance and starts its execution.
 */
public class ClientLauncher {
    /**
     * The port on which the server is launched.
     */
    private final static int PORT = 1337;

    /**
     * The main method of the ClientLauncher class launches a client to bind to a
     * specific server port and starts its execution.
     *
     * @param args An array of command-line arguments passed to the program.
     */
    public static void main(String[] args) {
        try {
            System.out.println("[Client] Running...");
        } catch (IOException | ClassNotFoundException e) {
            Client.run(PORT);
            e.printStackTrace();
        }
    }
}