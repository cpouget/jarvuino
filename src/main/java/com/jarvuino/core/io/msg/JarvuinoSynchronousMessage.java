package com.jarvuino.core.io.msg;

public class JarvuinoSynchronousMessage {

    public final String msg;

    public JarvuinoSynchronousMessage(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "JarvuinoSynchronousMessage{" +
                "msg='" + msg + '\'' +
                '}';
    }
}
