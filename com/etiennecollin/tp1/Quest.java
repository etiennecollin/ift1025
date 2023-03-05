/*
 * Copyright (c) 2023. Etienne Collin #20237904 & Justin Villeneuve #20132792
 */

package com.etiennecollin.tp1;

import com.etiennecollin.tp1.hero.Hero;

import java.util.LinkedList;

public class Quest {
    private int questCategory;
    private double healthCost;
    private int cashReward;
    private int armorReward;

    /**
     * Creates a new instance of a quest with the given parameters.
     *
     * @param questCategory The category of hero required to complete the quest.
     * @param healthCost    The amount of health the hero must spend to complete the quest.
     * @param cashReward    The amount of cash reward for completing the quest.
     * @param armorReward   The amount of armor reward for completing the quest.
     */
    public Quest(int questCategory, double healthCost, int cashReward, int armorReward) {
        setQuestCategory(questCategory);
        setHealthCost(healthCost);
        setCashReward(cashReward);
        setArmorReward(armorReward);
    }

    /**
     * Returns the quest category.
     *
     * @return The quest category.
     */
    public int getQuestCategory() {
        return questCategory;
    }

    /**
     * Sets the quest category.
     *
     * @param questCategory The quest category.
     */
    public void setQuestCategory(int questCategory) {
        this.questCategory = questCategory;
    }

    /**
     * Returns the health cost of the quest.
     *
     * @return The health cost of the quest.
     */
    public double getHealthCost() {
        return healthCost;
    }

    /**
     * Sets the health cost of the quest.
     *
     * @param healthCost The health cost of the quest.
     */
    public void setHealthCost(double healthCost) {
        this.healthCost = healthCost;
    }

    /**
     * Completes the quest with the specified hero and guild.
     *
     * @param hero  The hero completing the quest.
     * @param guild The guild completing the quest.
     *
     * @throws Exception If the hero dies during the quest.
     */
    public void complete(Hero hero, Guild guild) throws Exception {
        Bank bank = guild.getBank();
        LinkedList[] heroCategories = guild.getHeroCategories();

        // Check if hero has enough health
        if (healthCost < hero.getHealth()) {
            // A challenge was added just for fun and for the "wow-factor"
            // Gives a challenge to the hero to open the chest containing the rewards
            System.out.println(hero.getName() + " approaches the end of the quest, but the chest containing the loot is locked. There seems to be a mathematical equation written on the chest; can you solve it?");
            Challenge challenge = new Challenge(hero);

            if (challenge.wasSuccessful()) {
                System.out.println("Success! The chest opened. It contains " + cashReward + " gold & " + armorReward + " armors.");
                // Update bank with rewards
                bank.setCashBalance(bank.getCashBalance() + cashReward);
                bank.setArmorBalance(bank.getArmorBalance() + armorReward);
            } else {
                System.out.println("Challenge failed! The chest did not open.");
            }

            // Update hero health
            double healthLost = healthCost - (hero.getCategory() - questCategory) * 1.5;
            hero.setHealth(hero.getHealth() - healthLost);
        } else {
            // Remove hero that has died
            String deadHeroName = hero.getName();
            heroCategories[questCategory].removeFirst();
            throw new Exception("The hero " + deadHeroName + " died during their quest");
        }
    }

    /**
     * Returns the cash reward of the quest.
     *
     * @return The cash reward of the quest.
     */
    public int getCashReward() {
        return cashReward;
    }

    /**
     * Sets the cash reward of the quest.
     *
     * @param cashReward The cash reward of the quest.
     */
    public void setCashReward(int cashReward) {
        this.cashReward = cashReward;
    }

    /**
     * Returns the armor reward of the quest.
     *
     * @return The armor reward of the quest.
     */
    public int getArmorReward() {
        return armorReward;
    }

    /**
     * Sets the armor reward of the quest.
     *
     * @param armorReward The armor reward of the quest.
     */
    public void setArmorReward(int armorReward) {
        this.armorReward = armorReward;
    }
}
