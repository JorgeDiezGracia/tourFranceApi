package com.svalero.tourfrance.exception;

public class SponsorNotFoundException extends Exception {
    public SponsorNotFoundException() {
        super("The sponsor does not exist");
    }

    public SponsorNotFoundException(String message) {
        super(message);
    }
}
