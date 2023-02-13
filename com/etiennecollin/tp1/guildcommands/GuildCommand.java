package com.etiennecollin.tp1.guildcommands;

public class GuildCommand {

    private final String[] nextValues;
    private String name;
    private int index = 0;

    public GuildCommand(String command) {
        name = command.split(":")[0];
        nextValues = (command.split(":")[1]).split(",");
    }

    public int nextInt() {
        return Integer.parseInt(nextValues[index++]);
    }

    public String nextString() {
        return nextValues[index++];
    }

    public String getName() {
        return name;
    }

    public double nextDouble() {
        return Double.parseDouble(nextValues[index++]);
    }
}
