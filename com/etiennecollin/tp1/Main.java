package com.etiennecollin.tp1;

import com.etiennecollin.tp1.guildcommands.GuildCommand;
import com.etiennecollin.tp1.guildcommands.GuildCommandSystem;
import com.etiennecollin.tp1.hero.Hero;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Main class for the TP1 project and entry point to the program.
 */
public class Main {
    private static final ArrayList<String> exceptions = new ArrayList<>();

    /**
     * The entry point of the application. Initializes the `GuildCommandSystem` with the provided
     * arguments and executes the commands in a loop until there are no more commands to process.
     *
     * @param args Array of arguments passed to the program > guild:<initialMoneyAmount>,<initialArmorAmount>
     */
    public static void main(String[] args) {
        // Store the arguments to be processed
        GuildCommandSystem guildCommandSystem = new GuildCommandSystem(args);
        // Create guild with first command
        Guild guild = makeGuild(guildCommandSystem.currentCommand());

        while (guildCommandSystem.hasNextCommand()) {
            try {
                // Get next command
                GuildCommand command = guildCommandSystem.nextCommand();
                switch (command.getName()) {
                    case "buy-hero" -> {
                        String name = command.nextArgString();
                        int category = command.nextArgInt();
                        double costInCash = command.nextArgDouble();
                        int costInArmor = command.nextArgInt();
                        double health = command.nextArgDouble();
                        guild.buyHero(name, category, costInCash, costInArmor, health);
                    }
                    case "buy-armor" -> {
                        int numOfArmors = command.nextArgInt();
                        int costPerArmor = command.nextArgInt();
                        guild.buyArmor(numOfArmors, costPerArmor);
                    }
                    case "do-quest" -> {
                        int questCategory = command.nextArgInt();
                        double healthCost = command.nextArgDouble();
                        int cashReward = command.nextArgInt();
                        int armorReward = command.nextArgInt();
                        guild.doQuest(questCategory, healthCost, cashReward, armorReward);
                    }
                    case "train-hero" -> {
                        String name = command.nextArgString();
                        guild.trainHero(name);
                    }
                }
            } catch (Exception e) {
                exceptions.add(e.getMessage());
            }
        }
        displayOutput(guild);
    }

    /**
     * Helper method to create a new instance of the `Guild` class with the initial amount and
     * the initial number of armors.
     *
     * @param command `GuildCommand` object that holds the information about the creation of a new guild.
     *
     * @return A new instance of the `Guild` class.
     */
    private static Guild makeGuild(GuildCommand command) {
        double initialCashBalance = command.nextArgDouble();
        int initialArmorBalance = command.nextArgInt();
        return new Guild(initialCashBalance, initialArmorBalance);
    }

    /**
     * Displays information about the guild and its heroes.
     *
     * @param guild The guild of which we want the information.
     */
    private static void displayOutput(Guild guild) {
        Bank bank = guild.getBank();
        LinkedList[] categories = guild.getHeroCategories();

        // Display Guild info
        System.out.println();
        System.out.println("Guild bank: " + bank.getCashBalance() + " gold & " + bank.getArmorBalance() + " armors");

        // Generate string with list of heroes
        String heroList = "";
        for (LinkedList<Hero> category : categories) {
            for (Hero hero : category) {
                heroList = heroList.concat(("    " + hero.getName() + ": category=" + hero.getCategory() + ", HP=" + hero.getHealth() + "\n"));
            }
        }

        // Display heroes in guild if there are any
        if (!heroList.equals("")) {
            System.out.println("Heroes:");
            System.out.println(heroList);
        }

        // Displays exceptions if any happened
        if (!exceptions.isEmpty()) {
            System.out.println("Exceptions:");
            for (String exception : exceptions) {
                System.out.println("    " + exception);
            }
        }
    }
}