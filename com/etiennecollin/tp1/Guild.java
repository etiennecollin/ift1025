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
    protected LinkedList<Hero> heroesCategory0 = new LinkedList<>();
    protected LinkedList<Hero> heroesCategory1 = new LinkedList<>();
    protected LinkedList<Hero> heroesCategory2 = new LinkedList<>();
    protected LinkedList<Hero> heroesCategory3 = new LinkedList<>();
    protected LinkedList<Hero> heroesCategory4 = new LinkedList<>();
    protected LinkedList[] categories = {heroesCategory0, heroesCategory1, heroesCategory2, heroesCategory3, heroesCategory4};
    protected Bank bank = new Bank();

    /**
     * The Guild constructor creates a new guild and initializes it with a bank.
     *
     * @param initialCashBalance  A double representing the initial cash balance of the guild's bank.
     * @param initialArmorBalance An integer representing the initial armor balance of the guild's bank.
     */
    public Guild(double initialCashBalance, int initialArmorBalance) {
        this.bank.setCashBalance(initialCashBalance);
        this.bank.setArmorBalance(initialArmorBalance);
    }

    /**
     * The buyHero method adds a new hero to the guild if it doesn't already exist in the guild and if the guild can afford it.
     *
     * @param name        A String representing the name of the new hero.
     * @param category    An integer representing the category of the new hero.
     * @param costInCash  A double representing the cost in cash of the new hero.
     * @param costInArmor An integer representing the cost in armor of the new hero.
     * @param health      A double representing the initial health of the new hero.
     *
     * @throws Exception if the hero already exists in the guild, if the hero category is invalid, or if the guild can't afford the hero.
     */
    public void buyHero(String name, int category, double costInCash, int costInArmor, double health) throws Exception {
        // Check if hero already exists
        Hero hero = findHeroWithName(name);
        if (hero != null) {
            throw new Exception("The hero named " + hero.getDisplayName() + " is already part of this guild");
        }
        // Declare hero with proper category
        switch (category) {
            case 0 -> hero = new Hero0(name, category, costInCash, costInArmor, health);
            case 1 -> hero = new Hero1(name, category, costInCash, costInArmor, health);
            case 2 -> hero = new Hero2(name, category, costInCash, costInArmor, health);
            case 3 -> hero = new Hero3(name, category, costInCash, costInArmor, health);
            case 4 -> hero = new Hero4(name, category, costInCash, costInArmor, health);
            default -> {
                throw new Exception("Invalid hero category for 'buy-hero'. Category is an integer in range [0, 4]");
            }
        }
        // Check if guild has enough resources to afford hero
        if (!this.bank.isCashBalanceValid(costInCash) || !this.bank.isArmorBalanceValid(costInArmor)) {
            throw new Exception("Not enough money or armor to buy hero " + hero.getDisplayName());
        }
        // All checks passed, add hero and deduce its price from this.bank
        this.categories[category].add(hero);
        this.bank.setCashBalance(this.bank.getCashBalance() - costInCash);
        this.bank.setArmorBalance(this.bank.getArmorBalance() - costInArmor);
    }

    /**
     * Private helper method that searches for a hero with the specified name in all categories of the guild.
     *
     * @param name The name of the hero to search for.
     *
     * @return the hero object with the specified name, or null if the hero was not found.
     */
    private Hero findHeroWithName(String name) {
        for (LinkedList<Hero> category : this.categories) {
            for (Hero hero : category) {
                if (name.toLowerCase().equals(hero.getName())) {
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
     * @throws Exception if the guild does not have enough cash to afford the armor.
     */
    public void buyArmor(int numOfArmors, int costPerArmor) throws Exception {
        int totalCost = numOfArmors * costPerArmor;
        // Check if guild has enough resources to afford armor
        if (!this.bank.isCashBalanceValid(totalCost)) {
            throw new Exception("Not enough cash to buy " + numOfArmors + " armor");
        }
        // All checks passed, buy armor
        this.bank.setArmorBalance(this.bank.getArmorBalance() + numOfArmors);
        this.bank.setCashBalance(this.bank.getCashBalance() - totalCost);
    }

    /**
     * Allows a hero from the guild to complete a quest with the specified category, health cost, and rewards.
     *
     * @param questCategory The category of hero required to complete the quest.
     * @param healthCost    The amount of health the hero must spend to complete the quest.
     * @param cashReward    The amount of cash reward for completing the quest.
     * @param armorReward   The amount of armor reward for completing the quest.
     *
     * @throws Exception if no hero is available to complete the quest or if the hero dies during the quest.
     */
    public void doQuest(int questCategory, double healthCost, int cashReward, int armorReward) throws Exception {
        // Check if guild has a hero with the proper category
        Hero hero = findHeroWithCategory(questCategory);
        // A hero with the optimal level was not found, try to find another one
        if (hero == null) {
            // Go through the 4 possible remaining categories
            for (int i = 0; i < 4; i++) {
                // Try to find a hero with a higher level
                if (questCategory + i >= 0 && questCategory + i < categories.length) {
                    hero = findHeroWithCategory(questCategory + i);
                    // Check if a hero was found
                    if (hero != null) {
                        break;
                    }
                }
                // Try to find a hero with a lower level
                if (questCategory - i >= 0 && questCategory - i < categories.length) {
                    hero = findHeroWithCategory(questCategory - i);
                    // Check if a hero was found
                    if (hero != null) {
                        break;
                    }
                }
            }
            // Check if no hero was found
            if (hero == null) {
                throw new Exception("No heroes are available for the quest");
            }
        }
        // A hero was found, complete the quest
        if (healthCost < hero.getHealth()) {
            // Update hero health
            double healthLost = healthCost - (questCategory - hero.getCategory()) * 1.5;
            hero.setHealth(hero.getHealth() - healthLost);
            // Update bank with rewards
            this.bank.setCashBalance(this.bank.getCashBalance() + cashReward);
            this.bank.setArmorBalance(this.bank.getArmorBalance() + armorReward);
        } else {
            // Remove hero that has died
            String deadHeroName = hero.getDisplayName();
            this.categories[questCategory].removeFirst();
            throw new Exception("The hero " + deadHeroName + " died during their quest");
        }
    }

    /**
     * Finds a hero in the guild that belongs to the specified category.
     *
     * @param category The category of the hero to find.
     *
     * @return The hero belonging to the specified category, or null if no hero was found.
     */
    private Hero findHeroWithCategory(int category) {
        if (!this.categories[category].isEmpty()) {
            // Hero was found
            return (Hero) this.categories[category].getFirst();
        }
        // Hero was not found
        return null;
    }

    /**
     * Trains the hero with the specified name, increasing their category and health, and decreasing the guild's cash and armor balances.
     *
     * @param heroName The name of the hero to train.
     *
     * @throws Exception if the hero is not part of the guild, or if the guild does not have enough resources to afford the training.
     */
    public void trainHero(String heroName) throws Exception {
        // Find hero in guild
        Hero hero = findHeroWithName(heroName);
        if (hero == null) {
            throw new Exception("The hero named " + heroName + " is not part of this guild");
        }
        // Set training parameters
        int heroCategory = hero.getCategory();
        double heroUpgradedHealth = hero.getHealth() * 1.5;
        // Upgrade costs
        double upgradeCostInCash = 20 * Math.log(heroCategory + 10);
        int upgradeCostInArmor = (int) Math.ceil(Math.log(heroCategory + 10));
        // Check if guild has enough resources to afford training
        if (!this.bank.isCashBalanceValid(upgradeCostInCash) || !this.bank.isArmorBalanceValid(upgradeCostInArmor)) {
            throw new Exception("Not enough money or armor to upgrade hero " + hero.getDisplayName());
        }
        // All checks passed, train hero
        hero.setCategory(heroCategory + 1);
        hero.setHealth(heroUpgradedHealth);
        this.bank.setCashBalance(this.bank.getCashBalance() - upgradeCostInCash);
        this.bank.setArmorBalance(this.bank.getArmorBalance() - upgradeCostInArmor);
    }

    /**
     * Returns a string representation of the guild, including its categories of heroes, and its bank.
     *
     * @return a string representation of the guild.
     */
    @Override
    public String toString() {
        return "Guild{" + "heroesCategory0=" + heroesCategory0 + ", heroesCategory1=" + heroesCategory1 + ", " + "heroesCategory2=" + heroesCategory2 + ", heroesCategory3=" + heroesCategory3 + ", heroesCategory4=" + heroesCategory4 + ", categories=" + Arrays.toString(categories) + ", bank=" + bank + '}';
    }
}