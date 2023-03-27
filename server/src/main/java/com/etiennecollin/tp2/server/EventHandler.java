/*
 * Copyright (c) 2023. Etienne Collin #20237904
 */

package com.etiennecollin.tp2.server;

/**
 * A functional interface for handling events received by the server.
 * <p>
 * Implementations of this interface must provide a method to handle events, taking in a command and an argument as strings.
 */
@FunctionalInterface
public interface EventHandler {
    /**
     * Handles an event with the given command and argument.
     *
     * @param cmd The command associated with the event.
     * @param arg The argument associated with the event.
     */
    void handle(String cmd, String arg);
}
