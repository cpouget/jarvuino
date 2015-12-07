package com.jarvuino.core.io;

import com.jarvuino.arduino.ArduinoIOException;
import com.jarvuino.core.io.msg.ArduinoResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static com.jarvuino.core.io.msg.Status.*;

public class MessageDecoder extends MessageToMessageDecoder<String> {

    private static final Logger LOG = LoggerFactory.getLogger(MessageDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
        if (msg.contains("ack")) {
            out.add(new ArduinoResponseMessage(ACK, Optional.empty(), ""));

            return;
        }

        String[] tokens = msg.split(":");

        if (tokens.length == 2) {
            if (tokens[0].startsWith("err"))
                out.add(new ArduinoResponseMessage(NACK, Optional.<Integer>empty(), tokens[1]));
            else
                out.add(new ArduinoResponseMessage(OK, Optional.of(Integer.parseInt(tokens[0])), tokens[1]));

            return;
        }

        throw new ArduinoIOException("unexpected response");
    }
}
