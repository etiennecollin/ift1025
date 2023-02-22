package com.etiennecollin.tp1.guildcommands;

/**
 * GuildCommand is a class that takes a single command as input, and provides methods
 * to retrieve and parse the command arguments.
 */
public class GuildCommand {

    /**
     * The list of arguments for the command.
     */
    private final String[] commandArgs;

    /**
     * The name of the command.
     */
    private final String name; // Initially wasn't final

    /**
     * The index of the current argument being processed.
     */
    private int index = 0;

    /**
     * Constructs a new instance of GuildCommand with the given command.
     *
     * @param command The command.
     */
    public GuildCommand(String command) {
        this.name = command.split(":")[0];
        this.commandArgs = (command.split(":")[1]).split(",");
    }

    /**
     * Returns the name of the command.
     *
     * @return The name of the command.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the next argument as an integer.
     *
     * @return The next argument as an integer.
     */
    public int nextArgInt() {
        return Integer.parseInt(commandArgs[index++]);
    }

    /**
     * Returns the next argument as a double.
     *
     * @return The next argument as a double.
     */
    public double nextArgDouble() {
        return Double.parseDouble(commandArgs[index++]);
    }

    /**
     * Returns the next argument as a string.
     *
     * @return The next argument as a string.
     */
    public String nextArgString() {

        return commandArgs[index++];
    }
}
