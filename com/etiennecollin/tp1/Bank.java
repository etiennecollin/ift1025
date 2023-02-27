package com.etiennecollin.tp1;
import com.etiennecollin.tp1.hero.*;

public class Bank {

    // attributes
    private double accountBalance; // gold left in bank account

    public void buyHero(String heroName, int category, double costInCash, int costInArmor, int health) {
        accountBalance = 2;
    }
    public void buyArmor(int numOfArmors, int armorUnitCost) {
        accountBalance = accountBalance - Math.ceil(java.lang.Math.log(Hero.getCategory()));
    }

}
