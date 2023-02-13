package com.etiennecollin.tp1;

import com.etiennecollin.tp1.guildcommands.GuildCommand;
import com.etiennecollin.tp1.guildcommands.GuildCommandSystem;

public class Main {
    /**
     * Args: array with
     * guild:<montant initial>,<armures initiales>
     *
     * @param args
     */
    public static void main(String[] args) {
        GuildCommandSystem guildCommandSystem = new GuildCommandSystem(args);

        Guilde maGuilde = makeGuilde(guildCommandSystem.nextCommand());

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


    public static Guilde makeGuilde(GuildCommand command) {
        int montantInitial = command.nextInt();
        int nbArmures = command.nextInt();
        return new Guilde(montantInitial, nbArmures);
    }
}