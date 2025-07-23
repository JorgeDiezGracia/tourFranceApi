package com.svalero.tourfrance.exception;

public class StageNotFoundException extends Exception{
    public StageNotFoundException() {
        super("The stage does not exist");
    }

    public StageNotFoundException(String message) {
        super(message);
    }

}
