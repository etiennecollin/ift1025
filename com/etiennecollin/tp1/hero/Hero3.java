package com.etiennecollin.tp1.hero;

/**
 * A subclass of the Hero class that represents a hero of category 3.
 */
public class Hero3 extends Hero {
    /**
     * Creates a new instance of Hero3 with the given parameters.
     *
     * @param name        The name of the hero, will be converted to lowercase
     * @param category    The category of the hero
     * @param costInCash  The cost of the hero in cash
     * @param costInArmor The cost of the hero in armor
     * @param health      The initial health of the hero
     */
    public Hero3(String name, int category, double costInCash, int costInArmor, double health) {
        this.name = name.toLowerCase();
        this.category = category;
        this.costInCash = costInCash;
        this.costInArmor = costInArmor;
        this.health = health;
    }
}
