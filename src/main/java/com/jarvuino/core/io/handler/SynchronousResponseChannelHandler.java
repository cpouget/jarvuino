package com.jarvuino.core.io.handler;

import com.jarvuino.core.io.ResponseFuture;
import com.jarvuino.core.io.msg.JarvuinoSynchronousMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;

import static com.google.common.collect.Queues.newArrayBlockingQueue;

public class SynchronousResponseChannelHandler extends SimpleChannelInboundHandler<JarvuinoSynchronousMessage> {

    private static final Logger LOG = LoggerFactory.getLogger(SynchronousResponseChannelHandler.class);

    private BlockingQueue<ResponseFuture> responseFutures;

    public SynchronousResponseChannelHandler() {
        this.responseFutures = newArrayBlockingQueue(1);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JarvuinoSynchronousMessage synchronousMessage) throws Exception {
        LOG.debug("read from channel {}", synchronousMessage);

        if (Objects.equals("ack", synchronousMessage.msg))
            return;

        ResponseFuture responseFuture = responseFutures.take();
        responseFuture.onMessage(synchronousMessage.msg);
    }

    public void addListener(ResponseFuture responseFuture) {
        responseFutures.add(responseFuture);
    }
}
