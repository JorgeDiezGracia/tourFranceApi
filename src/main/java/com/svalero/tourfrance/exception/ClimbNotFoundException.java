package com.svalero.tourfrance.exception;

public class ClimbNotFoundException extends Exception {
    public ClimbNotFoundException() {
        super("The climb does not exist");
    }

    public ClimbNotFoundException (String message) {
        super(message);
    }
}
