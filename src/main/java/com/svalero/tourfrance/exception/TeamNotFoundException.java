package com.svalero.tourfrance.exception;

public class TeamNotFoundException extends Exception {

    public TeamNotFoundException() {
        super("The team does not exist");
    }

    public TeamNotFoundException(String message) {
        super(message);
    }
}
