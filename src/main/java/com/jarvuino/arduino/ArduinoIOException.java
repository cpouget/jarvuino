package com.jarvuino.arduino;

public class ArduinoIOException extends Exception {

    public ArduinoIOException(String message) {
        super(message);
    }

    public ArduinoIOException(Throwable cause) {
        super(cause);
    }
}

