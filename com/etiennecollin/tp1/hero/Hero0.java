package com.etiennecollin.tp1.hero;

public class Hero0 extends Hero {
    public Hero0(String heroName, int category, double costInCash, int costInArmor, double health) {
        this.heroName = heroName;
        this.category = category;
        this.costInCash = costInCash;
        this.costInArmor = costInArmor;
        this.health = health;
    }

    @Override
    public void doQuest(int category, int healthCost, int cashReward, int armorReward) {

    }

    @Override
    public void trainHero(String heroName) {

    }

    @Override
    public void buyArmor(int buyAmount, int armorCost) {
    }

}
