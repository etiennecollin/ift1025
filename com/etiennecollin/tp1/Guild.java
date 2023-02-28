package com.etiennecollin.tp1;

import com.etiennecollin.tp1.hero.*;

import java.util.LinkedList;

public class Guild {

    // attributes
    LinkedList<Hero> heroesCategory0 = new LinkedList<>();
    LinkedList<Hero> heroesCategory1 = new LinkedList<>();
    LinkedList<Hero> heroesCategory2 = new LinkedList<>();
    LinkedList<Hero> heroesCategory3 = new LinkedList<>();
    LinkedList<Hero> heroesCategory4 = new LinkedList<>();
    LinkedList[] categories = {heroesCategory0, heroesCategory1, heroesCategory2, heroesCategory3, heroesCategory4};
    Bank bank = new Bank();

    // constructor
    public Guild(double initialCashBalance, int initialArmorBalance) {
        bank.setCashBalance(initialCashBalance);
        bank.setArmorBalance(initialArmorBalance);
    }

    public void buyHero(String heroName, int heroCategory, double costInCash, int costInArmor, double heroHealth) {
        heroName = heroName.toLowerCase();

        for (LinkedList category : categories) {
            for (Object hero : category) {
                if (heroName.equals(((Hero) hero).getHeroName())) {
                    System.out.println("Error: this hero already exists");
                }
            }
        }


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
            categories[heroCategory].add(hero);
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
        int heroCategory = questCategory;

        // First, tries to find a hero of the right level. If there is no hero of the right level, tries to find hero of higher level.
        // Second, tries to find a hero of lower level

        while (heroCategory >= 0 && heroCategory <= 4) {
            if (!categories[heroCategory].isEmpty()) {
                Hero hero = (Hero) categories[heroCategory].getFirst();
                if (healthCost > hero.getHeroHealth()) {
                    double healthLost = healthCost - (questCategory - hero.getHeroCategory()) * 1.5;
                    hero.setHeroHealth(hero.getHeroHealth() - healthLost);
                    bank.setCashBalance(bank.getCashBalance() + cashReward);
                    bank.setArmorBalance(bank.getArmorBalance() + armorReward);
                } else {
                    categories[heroCategory].removeFirst();
                    System.out.println("The hero died during his quest");
                }
                return;
            } else if (heroCategory >= questCategory) heroCategory++;

            if (heroCategory > 4 || heroCategory < questCategory) heroCategory = questCategory--;
        }

        System.out.println("No heroes are available for the quest");
    }

    public void trainHero(String heroName) {
        heroName = heroName.toLowerCase();

        for (LinkedList category : categories) {
            for (Object hero : category) {
                Hero theHero = (Hero) hero;
                if (heroName.equals(theHero.getHeroName())) {
                    int heroCategory = theHero.getHeroCategory();
                    double heroUpgradedHealth = theHero.getHeroHealth() * 1.5;
                    double upgradeCostInCash = 20 * Math.log(heroCategory + 10);
                    int upgradeCostInArmor = (int) Math.ceil(Math.log(heroCategory + 10));

                    if (bank.getCashBalance() >= upgradeCostInCash && bank.getArmorBalance() >= upgradeCostInArmor) {
                        theHero.setHeroCategory(heroCategory + 1);
                        theHero.setHeroHealth(heroUpgradedHealth);
                        bank.setCashBalance(bank.getCashBalance() - upgradeCostInCash);
                        bank.setArmorBalance(bank.getArmorBalance() - upgradeCostInArmor);
                    } else {
                        System.out.println("Error: not enough money or armor to upgrade hero " + heroName);
                    }
                    return;
                }
            }
        }
        System.out.println("The hero named " + heroName + " is not in the list of heroes");
    }
}