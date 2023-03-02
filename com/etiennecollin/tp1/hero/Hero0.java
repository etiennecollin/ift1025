package com.etiennecollin.tp1.hero;

/**
 * A subclass of the Hero class that represents a hero of category 0.
 */
public class Hero0 extends Hero {
    /**
     * Creates a new instance of Hero0 with the given parameters.
     *
     * @param name        The name of the hero.
     * @param category    The category of the hero.
     * @param costInCash  The cost of the hero in cash.
     * @param costInArmor The cost of the hero in armor.
     * @param maxHealth   The maximum health of the hero.
     */
    public Hero0(String name, int category, double costInCash, int costInArmor, double maxHealth) {
        this.setName(name);
        this.setCategory(category);
        this.setCostInCash(costInCash);
        this.setCostInArmor(costInArmor);
        this.setMaxHealth(maxHealth);
        this.setHealth(maxHealth);
    }
}
