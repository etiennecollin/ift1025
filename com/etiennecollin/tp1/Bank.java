package com.etiennecollin.tp1;

/**
 * This class represents a bank object that manages a cash and armor balance.
 */
public class Bank {
    // The current cash balance in the bank account
    private double cashBalance;
    // The current armor balance in the bank account
    private int armorBalance;

    /**
     * Returns whether the current cash balance in the bank account is greater than or equal to the given cost.
     *
     * @param cost The cost to be checked against the cash balance.
     *
     * @return True if the cash balance is greater than or equal to the cost, false otherwise.
     */
    public boolean isCashCostValid(double cost) {
        return this.getCashBalance() >= cost;
    }

    /**
     * Returns the current cash balance in the bank account.
     *
     * @return The current cash balance in the bank account.
     */
    public double getCashBalance() {
        return cashBalance;
    }

    /**
     * Sets the current cash balance in the bank account to the given value.
     *
     * @param cashBalance The new cash balance to be set.
     */
    public void setCashBalance(double cashBalance) {
        this.cashBalance = Double.parseDouble(String.format("%.2f", cashBalance));
    }

    /**
     * Returns whether the current armor balance in the bank account is greater than or equal to the given cost.
     *
     * @param cost The cost to be checked against the armor balance.
     *
     * @return True if the armor balance is greater than or equal to the cost, false otherwise.
     */
    public boolean isArmorCostValid(int cost) {
        return this.getArmorBalance() >= cost;
    }

    /**
     * Returns the current armor balance in the bank account.
     *
     * @return The current armor balance in the bank account.
     */
    public int getArmorBalance() {
        return armorBalance;
    }

    /**
     * Sets the current armor balance in the bank account to the given value.
     *
     * @param armorBalance The new armor balance to be set.
     */
    public void setArmorBalance(int armorBalance) {
        this.armorBalance = armorBalance;
    }

    /**
     * Returns a string representation of the bank object.
     *
     * @return A string representation of the bank object.
     */
    @Override
    public String toString() {
        return "Bank{" + "cashBalance=" + cashBalance + ", armorBalance=" + armorBalance + '}';
    }
}
