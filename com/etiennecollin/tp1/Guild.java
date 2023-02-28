package com.etiennecollin.tp1;

import com.etiennecollin.tp1.hero.*;
import java.util.ArrayList;
import java.lang.Math;
import java.util.LinkedList;

public class Guild {

    // attributes
    LinkedList<Hero> heroesLVL0 = new LinkedList<>();
    LinkedList<Hero> heroesLVL1 = new LinkedList<>();
    LinkedList<Hero> heroesLVL2 = new LinkedList<>();
    LinkedList<Hero> heroesLVL3 = new LinkedList<>();
    LinkedList<Hero> heroesLVL4 = new LinkedList<>();
    LinkedList[] heroes = {heroesLVL0, heroesLVL1, heroesLVL2, heroesLVL3, heroesLVL4};
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

        if (hero != null && bank.getCashBalance() >= hero.getCostInCash() && bank.getArmorBalance() >= hero.getCostInArmor()) {
            heroes[heroCategory].add(hero);
            bank.setCashBalance(bank.getCashBalance() - hero.getCostInCash());
            bank.setArmorBalance(bank.getArmorBalance() - hero.getCostInArmor()); // TODO: check if this is correct
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
        for (int i = 0; i <= 4; i++){
            for (int j = 0; j < heroes[i].size(); j++) {
                if (heroName == heroes[i].get(j).getHeroName()) {
                    int heroCategory = heroes[i].get(j).getHeroCategory();
                    double upgradeCostInCash = 20 * Math.log(heroCategory + 10);
                    int upgradeCostInArmor = (int) Math.ceil(Math.log(heroCategory + 10));

                    if (bank.getCashBalance() >= upgradeCostInCash) {
                        heroes[i].get(j).setHeroCategory(heroCategory + 1);
                        bank.setCashBalance(bank.getCashBalance() - upgradeCostInCash);
                        bank.setArmorBalance(bank.getArmorBalance() - upgradeCostInArmor); // TODO: check if this is correct
                    } else {
                        System.out.println("Error: not enough money to upgrade " + heroName);
                    }
                    return;

                } else {
                    System.out.println("The hero named " + heroName + " is not in the list of heroes");
                }


            }
        }
    }

}

// shouldn't it be better to use 5 double linked lists (instead of one arrayList), one for each hero level that implements a stack?