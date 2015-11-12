package com.jarvuino.core.io;

import com.jarvuino.core.io.handler.ListenerResponseChannelHandler;

public abstract class ResponseListenerTask implements Runnable, ResponseListener<String> {

    public final Integer pin;
    private boolean running;

    public ResponseListenerTask(Integer pin, ListenerResponseChannelHandler handler) {
        handler.addListener(pin, this);
        this.running = true;
        this.pin = pin;
    }

    @Override
    public void run() {
        while (running) {

        }
    }

    public void stop() {
        running = false;
    }
}
