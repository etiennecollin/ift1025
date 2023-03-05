/*
 * Copyright (c) 2023. Etienne Collin #20237904 & Justin Villeneuve #20132792
 */

package com.etiennecollin.tp1;

import com.etiennecollin.tp1.hero.Hero;

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Creates a challenge for a hero. This is part of the "wow-factor".
 * The higher the hero category, the easier the challenge will be.
 * To disable challenges, set the variable enabled to false
 */
public class Challenge {
    /**
     * Enables or disables challenges in quests by bypassing them or not.
     */
    private static boolean isBypassed = false;
    /**
     * The success state of the current challenge.
     */
    private final boolean wasSuccessful;

    /**
     * Creates a challenge for a hero.
     *
     * @param hero The hero completing the challenge.
     */
    public Challenge(Hero hero) {
        if (!isBypassed) {
            // Generate a random integer and add 1 to upper boundary to make it inclusive
            // The higher the hero category, the easier the challenge will be (the numbers will be smaller)
            int integer1 = ThreadLocalRandom.current().nextInt(0, 1000 / hero.getCategory() + 1);
            int integer2 = ThreadLocalRandom.current().nextInt(0, 1000 / hero.getCategory() + 1);
            int sum = integer1 + integer2;

            // Get hero's answer to challenge
            int userAnswer = getUserAnswerForPrompt("Solve " + integer1 + " + " + integer2 + " = ");

            // Determine success of challenge
            this.wasSuccessful = userAnswer == sum;
        } else {
            this.wasSuccessful = true;
        }
    }

    /**
     * Gets the answer to a question from the hero.
     *
     * @param question The question to be answered.
     *
     * @return The answer to the question given by the hero.
     */
    private int getUserAnswerForPrompt(String question) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(question);
        return scanner.nextInt();
    }

    /**
     * Enables or disables challenges in quests by bypassing them or not.
     *
     * @param state True to bypass challenges and false to allow them.
     */
    public static void setIsBypassed(boolean state) {
        isBypassed = state;
    }

    /**
     * Gets success state of the challenge.
     *
     * @return The success state of the challenge.
     */
    public boolean wasSuccessful() {
        return wasSuccessful;
    }
}
