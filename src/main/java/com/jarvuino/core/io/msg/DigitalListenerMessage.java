package com.jarvuino.core.io.msg;

public class DigitalListenerMessage {

    public final Integer pin;
    public final String msg;

    public DigitalListenerMessage(Integer pin, String msg) {
        this.pin = pin;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "DigitalListenerMessage{" +
                "pin=" + pin +
                ", msg='" + msg + '\'' +
                '}';
    }
}
