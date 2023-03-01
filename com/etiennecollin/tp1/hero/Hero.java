package com.etiennecollin.tp1.hero;

/**
 * Hero is an abstract class representing a character in a game.
 */
public abstract class Hero {
    protected String name;
    protected int category;
    protected double costInCash;
    protected int costInArmor;
    protected double health;

    /**
     * Returns the display name of the hero, which is the name with the first letter capitalized.
     *
     * @return the display name of the hero.
     */
    public String getDisplayName() {
        return this.name.substring(0, 1).toUpperCase() + this.name.substring(1);
    }

    /**
     * Returns the name of the hero.
     *
     * @return the name of the hero.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of the hero. The name will be converted to lowercase.
     *
     * @param name The name to set for the hero.
     */
    public void setName(String name) {
        this.name = name.toLowerCase();
    }

    /**
     * Returns the category of the hero.
     *
     * @return the category of the hero.
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
     * @return the cost of the hero in cash.
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
     * @return the cost of the hero in armor.
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
     * Returns the health of the hero.
     *
     * @return the health of the hero.
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
        this.health = health;
    }
}
