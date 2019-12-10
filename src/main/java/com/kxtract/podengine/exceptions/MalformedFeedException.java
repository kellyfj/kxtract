package com.kxtract.podengine.exceptions;

public class MalformedFeedException extends Exception {

    public MalformedFeedException(String message) {
        super(message);
    }

    public MalformedFeedException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
