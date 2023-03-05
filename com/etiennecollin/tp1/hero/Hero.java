/*
 * Copyright (c) 2023. Etienne Collin #20237904 & Justin Villeneuve #20132792
 */

package com.etiennecollin.tp1.hero;

/**
 * Hero is an abstract class representing a character in a game.
 */
public abstract class Hero {
    private String name;
    private int category;
    private double costInCash;
    private int costInArmor;
    private double maxHealth;
    private double health;

    /**
     * Returns the name of the hero.
     *
     * @return The name of the hero.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of the hero.
     *
     * @param name The name to set for the hero.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the category of the hero.
     *
     * @return The category of the hero.
     */
    public int getCategory() {
        return this.category;
    }

    /**
     * Sets the category of the hero.
     *
     * @param category The category to set for the hero.
     */
    public void setCategory(int category) {
        this.category = category;
    }

    /**
     * Returns the cost of the hero in cash.
     *
     * @return The cost of the hero in cash.
     */
    public double getCostInCash() {
        return this.costInCash;
    }

    /**
     * Sets the cost of the hero in cash.
     *
     * @param costInCash The cost in cash to set for the hero.
     */
    public void setCostInCash(double costInCash) {
        this.costInCash = costInCash;
    }

    /**
     * Returns the cost of the hero in armor.
     *
     * @return The cost of the hero in armor.
     */
    public int getCostInArmor() {
        return this.costInArmor;
    }

    /**
     * Sets the cost of the hero in armor.
     *
     * @param costInArmor The cost in armor to set for the hero.
     */
    public void setCostInArmor(int costInArmor) {
        this.costInArmor = costInArmor;
    }

    /**
     * Returns the maximum health of the hero.
     *
     * @return The maximum health of the hero.
     */
    public double getMaxHealth() {
        return this.maxHealth;
    }

    /**
     * Sets the maximum health of the hero.
     *
     * @param maxHealth The maximum health to set for the hero.
     */
    public void setMaxHealth(double maxHealth) {
        this.maxHealth = Double.parseDouble(String.format("%.2f", maxHealth));
    }

    /**
     * Returns the health of the hero.
     *
     * @return The health of the hero.
     */
    public double getHealth() {
        return this.health;
    }

    /**
     * Sets the health of the hero.
     *
     * @param health The health to set for the hero.
     */
    public void setHealth(double health) {
        this.health = Double.parseDouble(String.format("%.2f", health));
    }

    @Override
    public String toString() {
        return "Hero{" + "name='" + name + '\'' + ", category=" + category + ", costInCash=" + costInCash + ", costInArmor=" + costInArmor + ", maxHealth=" + maxHealth + ", health=" + health + '}';
    }
}
