package com.etiennecollin.tp1;

import com.etiennecollin.tp1.hero.*;
import java.util.ArrayList;
import java.lang.Math;

public class Guild {

    // attributes
    ArrayList<Hero> heroes = new ArrayList<>();
    Bank bank = new Bank();

    // constructor
    public Guild(double initialCashBalance, int initialArmorBalance) {
        bank.setCashBalance(initialCashBalance);
        bank.setArmorBalance(initialArmorBalance);
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

        if (bank.getCashBalance() >= hero.getCostInCash() && bank.getArmorBalance() >= hero.getCostInArmor()) {
            heroes.add(hero);
            bank.setCashBalance(bank.getCashBalance() - hero.getCostInCash());
        } else {
            System.out.println("Error: not enough money or armor to buy hero");
        }
    }

    public void buyArmor(int numOfArmors, int costPerArmor) {
        int totalCost = numOfArmors * costPerArmor;
        if (bank.getCashBalance() >= totalCost) {
            bank.setArmorBalance(bank.getArmorBalance() + numOfArmors);
            bank.setCashBalance(bank.getCashBalance() - totalCost);
        } else {
            System.out.println("Error: not enough money to buy armor");
        }
    }

    public void doQuest(int questCategory, double healthCost, int cashReward, int armorReward) {
        // TODO
    }

    public void trainHero(String heroName) {
        for (int i = 0; i < heroes.size(); i++) {

            if (heroName == heroes.get(i).getHeroName()) {
                int heroCategory = heroes.get(i).getHeroCategory();
                double costInCash = 20 * Math.log(heroCategory + 10);
                int upgradeCostInArmor = (int) Math.ceil(Math.log(heroCategory + 10));

                if (bank.getCashBalance() >= costInCash) {
                    heroes.get(i).setHeroCategory(heroCategory + 1);
                    bank.setCashBalance(bank.getCashBalance() - costInCash);
                } else {
                    System.out.println("Error: not enough money to upgrade " + heroName);
                }
                break;

            } else {
                System.out.println("The hero named " + heroName + " is not in the list of heroes");
            }

        }
    }


}
