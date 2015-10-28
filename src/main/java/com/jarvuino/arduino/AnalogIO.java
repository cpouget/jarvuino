package com.jarvuino.arduino;

import com.jarvuino.arduino.constants.ReferenceVoltage;
import io.netty.channel.Channel;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkArgument;

public class AnalogIO {

    private static final Logger LOG = LoggerFactory.getLogger(AnalogIO.class);

    private Channel channel;

    public AnalogIO(Channel channel) {
        this.channel = channel;
    }

    public void analogReference(ReferenceVoltage referenceVoltage) {
        throw new NotImplementedException("AnalogIO.analogReference is not implemented");
    }

    public void analogWrite(int pin, int value) {
        checkArgument(value >= 0 && value <= 255, "duty cycle must be between 0 (always off) and 255 (always on)");

        throw new NotImplementedException("AnalogIO.analogWrite is not implemented");
    }

    public int analogRead(int pin) {
        throw new NotImplementedException("AnalogIO.analogRead is not implemented");
    }
}
