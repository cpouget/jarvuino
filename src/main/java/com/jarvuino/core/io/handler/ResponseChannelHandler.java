package com.jarvuino.core.io.handler;

import com.jarvuino.arduino.ArduinoIOException;
import com.jarvuino.core.io.ResponseFuture;
import com.jarvuino.core.io.msg.ArduinoResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Random;

import static com.google.common.collect.Maps.newHashMap;
import static com.jarvuino.core.io.msg.Status.ACK;
import static com.jarvuino.core.io.msg.Status.NACK;

public class ResponseChannelHandler extends SimpleChannelInboundHandler<ArduinoResponseMessage> {

    private static final Logger LOG = LoggerFactory.getLogger(ResponseChannelHandler.class);

    private Map<Integer, ResponseFuture> responseFutures;
    private Random random = new Random();

    public ResponseChannelHandler() {
        this.responseFutures = newHashMap();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ArduinoResponseMessage message) throws Exception {
        LOG.debug("read from channel {}", message);

        if (message.status == ACK)
            return;

        if (message.status == NACK)
            throw new ArduinoException(message.msg);

        if (message.id.isPresent() && responseFutures.containsKey(message.id.get())) {
            responseFutures.get(message.id.get()).onMessage(message.msg);
            return;
        }

        throw new ArduinoIOException("unexpected message");
    }

    public Integer addListener(ResponseFuture responseFuture) {
        Integer id = Math.abs(random.nextInt());

        while (responseFutures.containsKey(id))
            id = Math.abs(random.nextInt());

        responseFutures.put(id, responseFuture);

        return id;
    }
}
