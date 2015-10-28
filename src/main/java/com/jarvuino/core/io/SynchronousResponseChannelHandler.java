package com.jarvuino.core.io;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;

import static com.google.common.collect.Queues.newArrayBlockingQueue;

public class SynchronousResponseChannelHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(SynchronousResponseChannelHandler.class);

    private BlockingQueue<ResponseFuture> responseFutures;

    public SynchronousResponseChannelHandler() {
        this.responseFutures = newArrayBlockingQueue(1);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LOG.debug("read from channel");

        ResponseFuture responseFuture = responseFutures.take();

        ByteBuf buff = (ByteBuf) msg;
        byte[] bytes = new byte[buff.readableBytes()];
        buff.readBytes(bytes);

        responseFuture.onMessage(new String(bytes));
    }

    public <T> void addListener(ResponseFuture responseFuture) {
        responseFutures.add(responseFuture);
    }
}
