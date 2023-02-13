package com.etiennecollin.tp1.guildcommands;

public class GuildCommandSystem {

    String[] command;
    int index = 0;

    public GuildCommandSystem(String[] args) {
        command = args;
    }

    public boolean hasNextCommand() {
        return index < command.length;
    }

    public GuildCommand nextCommand() {
        index++;
        return new GuildCommand(command[index--]);
    }
}
