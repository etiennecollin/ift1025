package com.etiennecollin.tp1;
import com.etiennecollin.tp1.hero.*;

public class Bank {

    // attributes
    private double cashBalance; // cash left in bank account
    private int armorBalance; // armor left in bank account

    // constructor


    // getters and setters
    public double getCashBalance() {
        return cashBalance;
    }

    public void setCashBalance(double cashBalance) {
        this.cashBalance = cashBalance;
    }

    public int getArmorBalance() {
        return armorBalance;
    }

    public void setArmorBalance(int armorBalance) {
        this.armorBalance = armorBalance;
    }
}
