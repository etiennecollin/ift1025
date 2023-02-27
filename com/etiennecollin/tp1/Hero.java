package com.etiennecollin.tp1;

public abstract class Hero {

    // attributes
    protected String heroName;
    protected int category;
    protected double costInCash;
    protected int costInArmor;
    protected double health;

    // getters and setters
    public String getHeroName() {
        return this.heroName;
    }

    public void setHeroName(String heroName) {
        this.heroName = heroName;
    }

    public int getCategory() {
        return this.category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public double getCostInCash() {
        return this.costInCash;
    }

    public void setCostInCash(double costInCash) {
        this.costInCash = costInCash;
    }

    public int getCostInArmor() {
        return this.costInArmor;
    }

    public void setCostInArmor(int costInArmor) {
        this.costInArmor = costInArmor;
    }

    public double getHealth() {
        return this.health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    // methods
    public abstract void doQuest(int category, int healthCost, int cashReward, int armorReward);

    public abstract void trainHero(String heroName);

    public abstract void buyArmor(int buyAmount, int armorCost);

}
