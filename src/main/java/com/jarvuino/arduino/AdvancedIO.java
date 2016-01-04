package com.jarvuino.arduino;

import com.jarvuino.arduino.constants.BitOrder;
import com.jarvuino.arduino.constants.PinPower;
import com.jarvuino.core.ArduinoChannelWrapper;
import com.jarvuino.core.io.ResponseFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.String.format;

public class AdvancedIO {

    private static final Logger LOG = LoggerFactory.getLogger(AdvancedIO.class);

    private ArduinoChannelWrapper channel;

    public AdvancedIO(ArduinoChannelWrapper channel) {
        this.channel = channel;
    }

    public void noTone(int pin) throws ArduinoIOException {
        LOG.debug("send command: advio/no-tone/{}", pin);

        try {
            channel.get().writeAndFlush(format(":advio/no-tone/%d\n", pin)).sync();
        } catch (Exception e) {
            throw new ArduinoIOException(e);
        }
    }

    public void tone(int pin, int frequency) throws ArduinoIOException {
        LOG.debug("send command: advio/tone/{}/{}", pin, frequency);

        try {
            channel.get().writeAndFlush(format(":advio/tone/%d/%d\n", pin, frequency)).sync();
        } catch (Exception e) {
            throw new ArduinoIOException(e);
        }
    }

    public void tone(int pin, int frequency, long duration) throws ArduinoIOException {
        LOG.debug("send command: advio/tone/{}/{}/{}", pin, frequency, duration);

        try {
            channel.get().writeAndFlush(format(":advio/tone/%d/%d/%d\n", pin, frequency, duration)).sync();
        } catch (Exception e) {
            throw new ArduinoIOException(e);
        }
    }

    public void shiftOut(int dataPin, int clockPin, BitOrder bitOrder, byte value) throws ArduinoIOException {
        LOG.debug("send command: advio/s-out/{}/{}/{}/{}", dataPin, clockPin, bitOrder, value);

        try {
            channel.get().writeAndFlush(format(":advio/s-out/%d/%d/%d/%d\n", dataPin, clockPin, bitOrder.ordinal(), value)).sync();
        } catch (Exception e) {
            throw new ArduinoIOException(e);
        }
    }

    public String shiftIn(int dataPin, int clockPin, BitOrder bitOrder) throws ArduinoIOException {
        LOG.debug("send command: advio/s-in/{}/{}/{}", dataPin, clockPin, bitOrder);

        ResponseFuture responseFuture = new ResponseFuture(channel.handler);

        try {
            channel.get().writeAndFlush(format(":advio/s-in/%d/%d/%d\n", dataPin, clockPin, bitOrder.ordinal())).sync();

            return responseFuture.get();

        } catch (Exception e) {
            throw new ArduinoIOException(e);
        }
    }

    public String pulseIn(int pin, PinPower value) throws ArduinoIOException {
        LOG.debug("send command: advio/p-in/{}/{}", pin, value);

        ResponseFuture responseFuture = new ResponseFuture(channel.handler);

        try {
            channel.get().writeAndFlush(format(":advio/p-in/%d/%d", pin, value.ordinal())).sync();


            return responseFuture.get();

        } catch (Exception e) {
            throw new ArduinoIOException(e);
        }
    }

    public String pulseIn(int pin, PinPower value, long timeout) throws ArduinoIOException {
        LOG.debug("send command: advio/p-in/{}/{}/{}", pin, value, timeout);

        ResponseFuture responseFuture = new ResponseFuture(channel.handler);

        try {
            channel.get().writeAndFlush(format(":advio/p-in/%d/%d/%d", pin, value.ordinal(), timeout)).sync();

            return responseFuture.get();

        } catch (Exception e) {
            throw new ArduinoIOException(e);
        }
    }

}
