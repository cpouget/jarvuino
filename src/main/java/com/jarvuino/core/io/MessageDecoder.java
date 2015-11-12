package com.jarvuino.core.io;

import com.jarvuino.core.io.msg.DigitalListenerMessage;
import com.jarvuino.core.io.msg.JarvuinoSynchronousMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MessageDecoder extends MessageToMessageDecoder<String> {

    private static final Logger LOG = LoggerFactory.getLogger(MessageDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
        LOG.info("msg : {}", msg);

        if (msg.startsWith("d-list")) {
            String[] tokens = msg.split("/");

            out.add(new DigitalListenerMessage(Integer.parseInt(tokens[1]), tokens[2]));
        }

        out.add(new JarvuinoSynchronousMessage(msg));
    }
}
