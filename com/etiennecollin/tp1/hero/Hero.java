package com.etiennecollin.tp1.hero;

public abstract class Hero {

    // attributes
    protected String heroName;
    protected int heroCategory;
    protected double costInCash;
    protected int costInArmor;
    protected double heroHealth;

    // getters and setters
    public String getHeroName() {
        return this.heroName;
    }

    public void setHeroName(String heroName) {
        this.heroName = heroName;
    }

    public int getHeroCategory() {
        return this.heroCategory;
    }

    public void setHeroCategory(int heroCategory) {
        this.heroCategory = heroCategory;
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

    public double getHeroHealth() {
        return this.heroHealth;
    }

    public void setHeroHealth(double health) {
        this.heroHealth = health;
    }

}
