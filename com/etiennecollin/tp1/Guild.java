package com.etiennecollin.tp1;

import com.etiennecollin.tp1.hero.*;

import java.util.ArrayList;

public class Guild extends Bank {

    // attributes
    ArrayList<Hero> heroes = new ArrayList<>();

    public Guild(double initialCashBalance, int initialArmorBalance) {
        setCashBalance(initialCashBalance);
        setArmorBalance(initialArmorBalance);
    }

    public void buyHero(String heroName, int heroCategory, double costInCash, int costInArmor, double heroHealth) {
        Hero hero;
        switch (heroCategory) {
            case 0 -> hero = new Hero0(heroName, heroCategory, costInCash, costInArmor, heroHealth);
            case 1 -> hero = new Hero1(heroName, heroCategory, costInCash, costInArmor, heroHealth);
            case 2 -> hero = new Hero2(heroName, heroCategory, costInCash, costInArmor, heroHealth);
            case 3 -> hero = new Hero3(heroName, heroCategory, costInCash, costInArmor, heroHealth);
            case 4 -> hero = new Hero4(heroName, heroCategory, costInCash, costInArmor, heroHealth);
            default -> {
                System.out.println("Error: Invalid second argument for 'buy-hero' --> hero category");
                hero = null;
            }
        }
        heroes.add(hero);
    }

    public void buyArmor() {
        // TODO
    }

    public void doQuest() {
        // TODO
    }

    public void trainHero() {
        // TODO
    }


}
