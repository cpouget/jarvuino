package com.jarvuino.core.io.handler;

import com.jarvuino.core.io.ResponseListenerTask;
import com.jarvuino.core.io.msg.DigitalListenerMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

public class ListenerResponseChannelHandler extends SimpleChannelInboundHandler<DigitalListenerMessage> {

    private static final Logger LOG = LoggerFactory.getLogger(ListenerResponseChannelHandler.class);

    private Map<Integer, ResponseListenerTask> responses;

    public ListenerResponseChannelHandler() {
        this.responses = newHashMap();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DigitalListenerMessage listenerMessage) throws Exception {
        LOG.debug("read from channel");

        responses.get(listenerMessage.pin).onMessage(listenerMessage.msg);
    }

    public void addListener(Integer digitalPin, ResponseListenerTask responseListenerTask) {
        responses.put(digitalPin, responseListenerTask);
    }
}
