package com.etiennecollin.tp1;

public class Hero1 extends Heros {
    public Hero1(String heroName, int category, double costInCash, int costInArmor, double health) {
        this.heroName = heroName;
        this.category = category;
        this.costInCash = costInCash;
        this.costInArmor = costInArmor;
        this.health = health;
public class Hero1 extends Hero {
    @Override
    public void buyArmor(int buyAmount, int armorCost) {

    }
}
