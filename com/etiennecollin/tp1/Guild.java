/*
 * Copyright (c) 2023. Etienne Collin #20237904 & Justin Villeneuve #20132792
 */

package com.etiennecollin.tp1;

import com.etiennecollin.tp1.hero.*;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * The Guild class represents a guild of heroes.
 * The guild contains a list of heroes for each of the 5 hero categories (0 to 4).
 * The guild also has a bank containing cash and armor resources.
 */
public class Guild {
    private final Bank bank = new Bank();
    // We implemented a main heroCategories array which contains LinkedLists (used as queues) for heroes of each category.
    // As the index of each queue in the main array match the category of hero they contain, finding heroes with the
    // right category is extremely efficient. Indeed, we only need to check if the queue is empty or not. The time
    // complexity is O(1) for the hero-category-related operations as the lookup in the main array is O(1) and the add(),
    // getFirst() and removeFirst() operations in the linked lists are also O(1).
    //
    // If we had implemented a single main list, we would have had to do multiple passes to find an hero of a certain
    // level. In the worst case, we would have had to check all the elements of the array list 5 times (because there
    // are 5 hero levels). It would have also been possible to sort the elements of the arrayList before searching for
    // an element, but this would have been inefficient because sorting complexity is O(nlog(n)) (in the case of
    // quicksort). We would then also have to search for the hero once the sorting is done, which would be O(log(n))
    // using binary search. Overall, this would be terribly inefficient.
    private final LinkedList<Hero> heroesCategory1 = new LinkedList<>();
    private final LinkedList<Hero> heroesCategory2 = new LinkedList<>();
    private final LinkedList<Hero> heroesCategory3 = new LinkedList<>();
    private final LinkedList<Hero> heroesCategory4 = new LinkedList<>();
    private final LinkedList<Hero> heroesCategory0 = new LinkedList<>();
    private final LinkedList[] heroCategories = {heroesCategory0, heroesCategory1, heroesCategory2, heroesCategory3, heroesCategory4};

    /**
     * The Guild constructor creates a new guild and initializes it with a bank.
     *
     * @param initialCashBalance  A double representing the initial cash balance of the guild's bank.
     * @param initialArmorBalance An integer representing the initial armor balance of the guild's bank.
     */
    public Guild(double initialCashBalance, int initialArmorBalance) {
        bank.setCashBalance(initialCashBalance);
        bank.setArmorBalance(initialArmorBalance);
    }

    /**
     * Returns the hero categories of the current guild.
     *
     * @return The hero categories of the current guild.
     */
    public LinkedList[] getHeroCategories() {
        return heroCategories;
    }

    /**
     * Returns the bank object of the current guild.
     *
     * @return The bank object of the current guild.
     */
    public Bank getBank() {
        return bank;
    }

    /**
     * The buyHero method adds a new hero to the guild if it doesn't already exist in the guild and if the guild can
     * afford it.
     *
     * @param name        A String representing the name of the new hero.
     * @param category    An integer representing the category of the new hero.
     * @param costInCash  A double representing the cost in cash of the new hero.
     * @param costInArmor An integer representing the cost in armor of the new hero.
     * @param maxHealth   A double representing the maximum health of the new hero.
     *
     * @throws Exception If the hero already exists in the guild, if the hero category is invalid, or if the guild
     *                   can't afford the hero.
     */
    public void buyHero(String name, int category, double costInCash, int costInArmor, double maxHealth) throws Exception {
        // Check if hero already exists
        Hero hero = findHeroWithName(name);
        if (hero != null) {
            throw new Exception("The hero named " + hero.getName() + " is already part of this guild");
        }
        // Declare hero with proper category
        switch (category) {
            case 0 -> hero = new Hero0(name, category, costInCash, costInArmor, maxHealth);
            case 1 -> hero = new Hero1(name, category, costInCash, costInArmor, maxHealth);
            case 2 -> hero = new Hero2(name, category, costInCash, costInArmor, maxHealth);
            case 3 -> hero = new Hero3(name, category, costInCash, costInArmor, maxHealth);
            case 4 -> hero = new Hero4(name, category, costInCash, costInArmor, maxHealth);
            default -> {
                throw new Exception("Invalid hero category for 'buy-hero'. Category is an integer in range [0, 4]");
            }
        }
        // Check if guild has enough resources to afford hero
        if (!bank.isCashCostValid(costInCash) || !bank.isArmorCostValid(costInArmor)) {
            throw new Exception("Not enough money or armor to buy hero " + hero.getName());
        }
        // All checks passed, add hero and deduce its price from bank
        heroCategories[category].add(hero);
        bank.setCashBalance(bank.getCashBalance() - costInCash);
        bank.setArmorBalance(bank.getArmorBalance() - costInArmor);
    }

    /**
     * Private helper method that searches for a hero with the specified name in all categories of the guild.
     *
     * @param name The name of the hero to search for.
     *
     * @return The hero object with the specified name, or null if the hero was not found.
     */
    private Hero findHeroWithName(String name) {
        for (LinkedList<Hero> category : heroCategories) {
            for (Hero hero : category) {
                if (name.equals(hero.getName())) {
                    // Hero was found
                    return hero;
                }
            }
        }
        // Hero was not found
        return null;
    }

    /**
     * Allows the guild to purchase armor with its cash balance.
     *
     * @param numOfArmors  The number of armors to purchase.
     * @param costPerArmor The cost per armor in cash.
     *
     * @throws Exception If the guild does not have enough cash to afford the armor.
     */
    public void buyArmor(int numOfArmors, int costPerArmor) throws Exception {
        int totalCost = numOfArmors * costPerArmor;
        // Check if guild has enough resources to afford armor
        if (!bank.isCashCostValid(totalCost)) {
            throw new Exception("Not enough cash to buy " + numOfArmors + " armor");
        }
        // All checks passed, buy armor
        bank.setArmorBalance(bank.getArmorBalance() + numOfArmors);
        bank.setCashBalance(bank.getCashBalance() - totalCost);
    }

    /**
     * Allows a hero from the guild to complete a quest with the specified category, health cost, and rewards.
     *
     * @param questCategory The category of hero required to complete the quest.
     * @param healthCost    The amount of health the hero must spend to complete the quest.
     * @param cashReward    The amount of cash reward for completing the quest.
     * @param armorReward   The amount of armor reward for completing the quest.
     *
     * @throws Exception If no hero is available to complete the quest or if the hero dies during the quest.
     */
    public void doQuest(int questCategory, double healthCost, int cashReward, int armorReward) throws Exception {
        Hero hero = findHeroForQuest(questCategory);
        Quest quest = new Quest(questCategory, healthCost, cashReward, armorReward);
        quest.complete(hero, this);
    }

    /**
     * Tries to find a hero from the guild to complete a quest with the specified category.
     *
     * @param questCategory The category of hero required to complete the quest.
     *
     * @throws Exception If no hero is available to complete the quest.
     */
    private Hero findHeroForQuest(int questCategory) throws Exception {
        // Check if guild has a hero with the proper category
        Hero hero = findHeroWithCategory(questCategory);
        // If a hero with the optimal level was not found, try to find another one
        if (hero == null) {
            // Go through the 4 possible remaining categories
            for (int i = 0; i < heroCategories.length; i++) {
                // Try to find a hero with a higher level
                if (questCategory + i >= 0 && questCategory + i < heroCategories.length) {
                    hero = findHeroWithCategory(questCategory + i);
                    // Check if a hero was found
                    if (hero != null) {
                        return hero;
                    }
                }
                // Try to find a hero with a lower level
                if (questCategory - i >= 0 && questCategory - i < heroCategories.length) {
                    hero = findHeroWithCategory(questCategory - i);
                    // Check if a hero was found
                    if (hero != null) {
                        return hero;
                    }
                }
            }
            // No hero was found
            throw new Exception("No heroes are available for the quest");
        }
        // If a hero with the optimal level was found, return it
        return hero;
    }

    /**
     * Finds a hero in the guild that belongs to the specified category.
     *
     * @param category The category of the hero to find.
     *
     * @return The hero belonging to the specified category, or null if no hero was found.
     */
    private Hero findHeroWithCategory(int category) {
        if (!heroCategories[category].isEmpty()) {
            // Hero was found
            return (Hero) heroCategories[category].getFirst();
        }
        // Hero was not found
        return null;
    }

    /**
     * Trains the hero with the specified name, increasing their category and health, and decreasing the guild's cash
     * and armor balances.
     *
     * @param name The name of the hero to train.
     *
     * @throws Exception If the hero is not part of the guild, or if the hero is already at the maximum category level, or if the guild does not have enough resources to afford the training.
     */
    public void trainHero(String name) throws Exception {
        // Find hero in guild
        Hero hero = findHeroWithName(name);
        if (hero == null) {
            throw new Exception("The hero named " + name + " is not part of the guild");
        }

        // Check if hero is already max category
        int heroCategory = hero.getCategory();
        if (heroCategory >= heroCategories.length - 1) {
            throw new Exception("The hero named " + name + " cannot be trained as it is already category " + heroCategory);
        }

        // Set training parameters
        double heroUpgradedMaxHealth = hero.getMaxHealth() * 1.5;
        double upgradeCostInCash = 20 * Math.log(heroCategory + 10);
        int upgradeCostInArmor = (int) Math.ceil(Math.log(heroCategory + 10));

        // Check if guild has enough resources to afford training
        if (!bank.isCashCostValid(upgradeCostInCash) || !bank.isArmorCostValid(upgradeCostInArmor)) {
            throw new Exception("Not enough money or armor to train hero " + hero.getName());
        }

        // All checks passed, change hero category
        hero.setCategory(heroCategory + 1);
        heroCategories[heroCategory].remove(hero);
        heroCategories[heroCategory + 1].add(hero);

        // Upgrade hero maximum health and restore health
        hero.setMaxHealth(heroUpgradedMaxHealth);
        hero.setHealth(heroUpgradedMaxHealth);

        // Make guild pay training costs
        bank.setCashBalance(bank.getCashBalance() - upgradeCostInCash);
        bank.setArmorBalance(bank.getArmorBalance() - upgradeCostInArmor);
    }

    /**
     * Returns a string representation of the guild, including its categories of heroes, and its bank.
     *
     * @return A string representation of the guild.
     */
    @Override
    public String toString() {
        return "Guild{" + "\n bank=" + bank + ",\n heroesCategory0=" + heroesCategory0 + ",\n heroesCategory1=" + heroesCategory1 + ",\n heroesCategory2=" + heroesCategory2 + ",\n heroesCategory3=" + heroesCategory3 + ",\n heroesCategory4=" + heroesCategory4 + ",\n heroCategories=" + Arrays.toString(heroCategories) + '}';
    }
}