package com.jarvuino.arduino;

import com.jarvuino.arduino.constants.PinPower;
import io.netty.channel.Channel;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteOrder;

public class AdvancedIO {

    private static final Logger LOG = LoggerFactory.getLogger(AdvancedIO.class);

    private Channel channel;

    public AdvancedIO(Channel channel) {
        this.channel = channel;
    }

    public void noTone(int pin) {
        throw new NotImplementedException("AdvancedIO.noTone is not implemented");
    }

    public void tone(int pin, int frequency) {
        throw new NotImplementedException("AdvancedIO.tone is not implemented");
    }

    public void tone(int pin, int frequency, long duration) {
        throw new NotImplementedException("AdvancedIO.tone is not implemented");
    }

    public void shiftOut(int dataPin, int clockPin, ByteOrder bitOrder, byte value) {
        throw new NotImplementedException("AdvancedIO.shiftOut is not implemented");
    }

    public byte shiftIn(int dataPin, int clockPin, ByteOrder bitOrder) {
        throw new NotImplementedException("AdvancedIO.shiftIn is not implemented");
    }

    public long pulseIn(int pin, PinPower value) {
        throw new NotImplementedException("AdvancedIO.pulseIn is not implemented");
    }

    public long pulseIn(int pin, PinPower value, long timeout) {
        throw new NotImplementedException("AdvancedIO.pulseIn is not implemented");
    }

}
