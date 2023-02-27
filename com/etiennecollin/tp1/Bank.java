package com.etiennecollin.tp1;
import com.etiennecollin.tp1.hero.*;

public class Bank {

    private double cashBalance; // cash left in bank account
    private int armorBalance; // armor left in bank account


    public double getCashBalance() {
        return cashBalance;
    }

    public void setCashBalance(double cashBalance) {
        this.cashBalance = cashBalance;
    }

    public double getArmorBalance() {
        return armorBalance;
    }

    public void setArmorBalance(int armorBalance) {
        this.armorBalance = armorBalance;
    }
}
