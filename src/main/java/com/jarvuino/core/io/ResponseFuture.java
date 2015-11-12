package com.jarvuino.core.io;

import com.google.common.util.concurrent.AbstractFuture;
import com.jarvuino.core.io.handler.SynchronousResponseChannelHandler;

public class ResponseFuture extends AbstractFuture<String> implements ResponseListener<String> {

    public ResponseFuture(SynchronousResponseChannelHandler handler) {
        handler.addListener(this);
    }

    @Override
    public void onMessage(String msg) {
        this.set(msg);
    }
}
