package com.etiennecollin.tp1;

import com.etiennecollin.tp1.hero.Hero;

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Challenge {
    private final boolean wasSuccessful;

    /**
     * Creates a challenge for a hero.
     *
     * @param hero The hero completing the challenge.
     */
    public Challenge(Hero hero) {
        // Generate a random integer and add 1 to upper boundary to make it inclusive
        int integer1 = ThreadLocalRandom.current().nextInt(0, 1000 / hero.getCategory() + 1);
        int integer2 = ThreadLocalRandom.current().nextInt(0, 1000 / hero.getCategory() + 1);
        int sum = integer1 + integer2;

        // Get hero's answer to challenge
        int userAnswer = getUserAnswerForPrompt("Solve " + integer1 + " + " + integer2 + " = ");

        // Determine success of challenge
        this.wasSuccessful = userAnswer == sum;
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
     * Gets success state of the challenge.
     *
     * @return The success state of the challenge.
     */
    public boolean wasSuccessful() {
        return wasSuccessful;
    }
}
