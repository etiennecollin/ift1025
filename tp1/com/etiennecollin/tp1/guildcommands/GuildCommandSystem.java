/*
 * Copyright (c) 2023. Etienne Collin #20237904 & Justin Villeneuve #20132792
 */

package com.etiennecollin.tp1.guildcommands;

/**
 * GuildCommandSystem is a class that takes a list of commands as input, and provides methods
 * to retrieve and parse the commands one by one.
 */
public class GuildCommandSystem {
    // The list of commands passed as an input
    final String[] command;
    // The index of the current command being processed.
    int index = 0;

    /**
     * Constructs a new instance of GuildCommandSystem with the given list of commands.
     *
     * @param args The list of commands.
     */
    public GuildCommandSystem(String[] args) {
        command = args;
    }

    /**
     * Returns true if there are more commands to be processed, and false otherwise.
     *
     * @return True if there are more commands to be processed, and false otherwise.
     */
    public boolean hasNextCommand() {
        return index < (command.length - 1);
    }

    /**
     * Returns the current command in the list as a GuildCommand instance.
     *
     * @return The current command in the list as a GuildCommand instance.
     */
    public GuildCommand currentCommand() {
        return new GuildCommand(command[index]);
    }

    /**
     * Returns the next command in the list as a GuildCommand instance.
     *
     * @return The next command in the list as a GuildCommand instance.
     */
    public GuildCommand nextCommand() {
        return new GuildCommand(command[++index]);
    }
}
