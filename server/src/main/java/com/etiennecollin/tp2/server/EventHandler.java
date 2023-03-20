package com.etiennecollin.tp2.server;

@FunctionalInterface
public interface EventHandler {
    void handle(String cmd, String arg);
}
