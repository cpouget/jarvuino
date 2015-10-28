package com.jarvuino;

import com.jarvuino.arduino.DigitalIO;
import com.jarvuino.core.ArduinoChannelWrapper;
import com.jarvuino.core.io.SynchronousResponseChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.rxtx.RxtxDeviceAddress;

import static com.jarvuino.arduino.constants.PinPower.HIGH;
import static com.jarvuino.arduino.constants.PinPower.LOW;


public class ArduinoMain {
    static final String PORT = System.getProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyS80");

    public static void main(String[] args) throws Exception {
        EventLoopGroup group = new OioEventLoopGroup();

        SynchronousResponseChannelHandler handler = new SynchronousResponseChannelHandler();

        try (ArduinoChannelWrapper channel = new ArduinoChannelWrapper(new RxtxDeviceAddress(PORT), group, handler).connect()) {

            DigitalIO digitalIO = new DigitalIO(channel);

            //FIXME I don't know why but if I didn't make a first fake write, first read is erroneous
            digitalIO.digitalWrite(13, HIGH);

            digitalIO.digitalWrite(13, HIGH);
            digitalIO.digitalRead(13);
            digitalIO.digitalWrite(13, LOW);
            digitalIO.digitalRead(13);
        }
    }
}