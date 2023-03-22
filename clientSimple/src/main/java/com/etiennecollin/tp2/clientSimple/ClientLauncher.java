/*
 * Copyright (c) 2023. Etienne Collin #20237904 & Justin Villeneuve #20132792
 */

package com.etiennecollin.tp2.clientSimple;

import com.etiennecollin.tp2.server.ServerLauncher;

/**
 * Main entry point to the program.
 */
public class ClientLauncher {
    /**
     * @param args The arguments to pass to the main function.
     */
    public static void main(String[] args) {
        try {
            System.out.println("[Client] Running...");
            Client.run(ServerLauncher.PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}