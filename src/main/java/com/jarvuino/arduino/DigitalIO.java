package com.jarvuino.arduino;

import com.jarvuino.arduino.constants.PinMode;
import com.jarvuino.arduino.constants.PinPower;
import com.jarvuino.core.ArduinoChannelWrapper;
import com.jarvuino.core.io.ResponseFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.String.format;

public class DigitalIO {

    private static final Logger LOG = LoggerFactory.getLogger(DigitalIO.class);

    private ArduinoChannelWrapper channel;

    public DigitalIO(ArduinoChannelWrapper channel) {
        this.channel = channel;
    }

    public void pinMode(int pin, PinMode mode) throws ArduinoIOException {
        LOG.debug("send command: pin-mode/{}/{}", pin, mode.ordinal());

        try {
            channel.get().writeAndFlush(format(":pin-mode/%d/%d\n", pin, mode.ordinal())).sync();
        } catch (Exception e) {
            throw new ArduinoIOException(e);
        }
    }

    public void digitalWrite(int pin, PinPower value) throws ArduinoIOException {
        LOG.debug("send command: d-write/{}/{} [{}] ({} bytes)", pin, value.ordinal(), value.name(), format("d-write/%d/%d\n", pin, value.ordinal()).getBytes().length);

        try {
            channel.get().writeAndFlush(format(":d-write/%d/%d\n", pin, value.ordinal())).sync();
        } catch (Exception e) {
            throw new ArduinoIOException(e);
        }
    }

    public String digitalRead(int pin) throws ArduinoIOException {
        LOG.debug("send command: d-read/{}", pin);

        try {
            ResponseFuture responseFuture = new ResponseFuture(channel.synchronousHandler);

            channel.get().writeAndFlush(format(":d-read/%d\n", pin)).sync();
            String msg = responseFuture.get();

            LOG.debug("read value: {}", msg);

            return msg;
        } catch (Exception e) {
            throw new ArduinoIOException(e);
        }
    }

}
