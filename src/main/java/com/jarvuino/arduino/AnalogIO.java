package com.jarvuino.arduino;

import com.jarvuino.arduino.constants.ReferenceVoltage;
import com.jarvuino.core.ArduinoChannelWrapper;
import com.jarvuino.core.io.ResponseFuture;
import com.jarvuino.core.io.handler.SynchronousResponseChannelHandler;
import io.netty.channel.Channel;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;

public class AnalogIO {

    private static final Logger LOG = LoggerFactory.getLogger(AnalogIO.class);

    private ArduinoChannelWrapper channel;

    public AnalogIO(ArduinoChannelWrapper channel) {
        this.channel = channel;
    }

    public void analogReference(ReferenceVoltage refVolt) throws ArduinoIOException {
        LOG.debug("send command: ref-volt/{}", refVolt.ordinal());

        try {
            channel.get().writeAndFlush(format(":ref-volt/%d\n", refVolt.ordinal()));
        } catch (Exception e) {
            throw new ArduinoIOException(e);
        }
    }

    public void analogWrite(int pin, int value) throws ArduinoIOException {
        checkArgument(value >= 0 && value <= 255, "duty cycle must be between 0 (always off) and 255 (always on)");

        LOG.debug("send command: a-write/{}/{}", pin, value);

        try {
            channel.get().writeAndFlush(format(":a-write/%d/%d\n", pin, value)).get();
        } catch (Exception e) {
            throw new ArduinoIOException(e);
        }
    }

    public String analogRead(int pin) throws ArduinoIOException {

        LOG.debug("send command: a-read/{}", pin);

        ResponseFuture responseFuture = new ResponseFuture(channel.synchronousHandler);

        String msg = null;
        try {
            channel.get().writeAndFlush(format(":a-read/%d\n", pin)).get();
            msg = responseFuture.get();
        } catch (Exception e) {
            throw new ArduinoIOException(e);
        }

        LOG.debug("read value: {}", msg);

        return msg;

    }
}
