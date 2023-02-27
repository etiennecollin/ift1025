package com.etiennecollin.tp1;

public abstract class Hero {

    // attributes
    private String heroName;
    private int category;
    private double costInCash;
    private int costInArmor;
    private double health;


    // methods
    public abstract void buyArmor(int buyAmount, int armorCost);


}
