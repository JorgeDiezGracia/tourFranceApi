package com.svalero.tourfrance.exception;

public class CyclistNotFoundException extends Exception{
    public CyclistNotFoundException() {
        super("The cyclist does not exist");
    }

    public CyclistNotFoundException(String message) {
        super(message);
    }
}
