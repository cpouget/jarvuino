package com.jarvuino.arduino;

public class ArduinoIOException extends Exception {

    public ArduinoIOException() {
    }

    public ArduinoIOException(String message) {
        super(message);
    }

    public ArduinoIOException(Throwable cause) {
        super(cause);
    }

    public ArduinoIOException(String message, Throwable cause) {
        super(message, cause);
    }
}

