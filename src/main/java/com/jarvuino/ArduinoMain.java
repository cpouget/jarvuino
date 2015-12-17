package com.jarvuino;

import com.google.common.base.Throwables;
import com.jarvuino.arduino.AnalogIO;
import com.jarvuino.arduino.ArduinoIOException;
import com.jarvuino.arduino.DigitalIO;
import com.jarvuino.core.ArduinoChannelWrapper;
import com.jarvuino.core.io.handler.ResponseChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.rxtx.RxtxDeviceAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;

import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static com.jarvuino.arduino.constants.PinMode.OUTPUT;
import static com.jarvuino.arduino.constants.PinPower.HIGH;
import static java.util.concurrent.TimeUnit.MILLISECONDS;


public class ArduinoMain {
    static final String PORT = System.getProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyS80");

    private static final Logger LOG = LoggerFactory.getLogger(ArduinoMain.class);

    public static void main(String[] args) throws Exception {
        EventLoopGroup group = new OioEventLoopGroup();

        ResponseChannelHandler synchronousHandler = new ResponseChannelHandler();

        ArduinoChannelWrapper arduinoChannelWrapper = new ArduinoChannelWrapper(new RxtxDeviceAddress(PORT), group, synchronousHandler);

        try (ArduinoChannelWrapper channel = arduinoChannelWrapper.connect()) {

            DigitalIO digitalIO = new DigitalIO(channel);
            AnalogIO analogIO = new AnalogIO(channel);

            int blue = 3;
            int green = 9;
            int red = 10;

            digitalIO.pinMode(blue, OUTPUT);
            digitalIO.pinMode(green, OUTPUT);
            digitalIO.pinMode(red, OUTPUT);

            digitalIO.digitalWrite(blue, HIGH);
            digitalIO.digitalWrite(green, HIGH);
            digitalIO.digitalWrite(red, HIGH);

            IntStream.range(0, 256).forEach(value -> {
                try {
                    analogIO.analogWrite(blue, 255);
                    analogIO.analogWrite(green, 255 - value);
                    analogIO.analogWrite(red, value);
                    sleepUninterruptibly(50, MILLISECONDS);
                } catch (ArduinoIOException e) {
                    throw Throwables.propagate(e);
                }
            });

            IntStream.range(0, 256).forEach(value -> {
                try {
                    analogIO.analogWrite(green, 255);
                    analogIO.analogWrite(red, 255 - value);
                    analogIO.analogWrite(blue, value);
                    sleepUninterruptibly(50, MILLISECONDS);
                } catch (ArduinoIOException e) {
                    throw Throwables.propagate(e);
                }
            });

            IntStream.range(0, 256).forEach(value -> {
                try {
                    analogIO.analogWrite(red, 255);
                    analogIO.analogWrite(blue, 255 - value);
                    analogIO.analogWrite(green, value);
                    sleepUninterruptibly(50, MILLISECONDS);
                } catch (ArduinoIOException e) {
                    throw Throwables.propagate(e);
                }
            });
        }
    }
}