package com.jarvuino.arduino;

import com.google.common.base.Throwables;
import com.jarvuino.arduino.constants.PinMode;
import com.jarvuino.arduino.constants.PinPower;
import com.jarvuino.core.ArduinoChannelWrapper;
import com.jarvuino.core.io.ResponseFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.lang.String.format;

public class DigitalIO {

    private static final Logger LOG = LoggerFactory.getLogger(DigitalIO.class);

    private ArduinoChannelWrapper channel;

    public DigitalIO(ArduinoChannelWrapper channel) {
        this.channel = channel;
    }

    public void pinMode(int pin, PinMode mode) {
        LOG.debug("send command: pin-mode/{}/{}", pin, mode.ordinal());

        try {
            channel.get().writeAndFlush(format(":pin-mode/%d/%d\n", pin, mode.ordinal()));
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    public void digitalWrite(int pin, PinPower value) throws ExecutionException, InterruptedException {
        LOG.debug("send command: d-write/{}/{} [{}] ({} bytes)", pin, value.ordinal(), value.name(), format("d-write/%d/%d\n", pin, value.ordinal()).getBytes().length);

        try {
            channel.get().writeAndFlush(format(":d-write/%d/%d\n", pin, value.ordinal())).get();
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    public String digitalRead(int pin) throws InterruptedException, ExecutionException, TimeoutException {
        LOG.debug("send command: d-read/{}", pin);

        ResponseFuture responseFuture = new ResponseFuture(channel.synchronousHandler);

        try {
            channel.get().writeAndFlush(format(":d-read/%d\n", pin)).get();
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }

        String msg = responseFuture.get();

        LOG.debug("read value: {}", msg);

        return msg;
    }


}
