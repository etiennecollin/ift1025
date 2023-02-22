package com.etiennecollin.tp1;

import com.etiennecollin.tp1.guildcommands.GuildCommand;
import com.etiennecollin.tp1.guildcommands.GuildCommandSystem;

/**
 * Main class for the TP1 project and entry point to the program.
 */
public class Main {
    /**
     * The entry point of the application. Initializes the `GuildCommandSystem` with the provided
     * arguments and executes the commands in a loop until there are no more commands to process.
     *
     * @param args array of arguments passed to the program > guild:<initialMoneyAmount>,<initialArmorAmount>
     */
    public static void main(String[] args) {
        // Store the arguments to be processed
        GuildCommandSystem guildCommandSystem = new GuildCommandSystem(args);
        // Create guild with first command
        Guild myGuild = makeGuild(guildCommandSystem.currentCommand());

        while (guildCommandSystem.hasNextCommand()) {
            GuildCommand command = guildCommandSystem.nextCommand();
            switch (command.getName()) {
                case "buy-hero" -> {
                    // TODO1
                }
                case "buy-armor" -> {
                    // TODO2
                }
                case "do-quest" -> {
                    // TODO3
                }
                case "train-hero" -> {
                    // TODO4
                }
            }
        }
    }

    /**
     * Helper method to create a new instance of the `Guild` class with the initial amount and
     * the initial number of armors.
     *
     * @param command `GuildCommand` object that holds the information about the creation of a new guild.
     * @return a new instance of the `Guild` class
     */
    public static Guild makeGuild(GuildCommand command) {
        double initialMoneyAmount = command.nextArgDouble();
        int initialArmorAmount = command.nextArgInt();
        return new Guild(initialMoneyAmount, initialArmorAmount);
    }
}