package com.jarvuino;

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
import static com.jarvuino.arduino.constants.PinPower.LOW;
import static java.util.concurrent.TimeUnit.SECONDS;


public class ArduinoMain {
    static final String PORT = System.getProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyS80");

    private static final Logger LOG = LoggerFactory.getLogger(ArduinoMain.class);

    public static void main(String[] args) throws Exception {
        EventLoopGroup group = new OioEventLoopGroup();

        ResponseChannelHandler synchronousHandler = new ResponseChannelHandler();

        ArduinoChannelWrapper arduinoChannelWrapper = new ArduinoChannelWrapper(new RxtxDeviceAddress(PORT), group, synchronousHandler);

        try (ArduinoChannelWrapper channel = arduinoChannelWrapper.connect()) {

            DigitalIO digitalIO = new DigitalIO(channel);

            digitalIO.pinMode(13, OUTPUT);

            IntStream.range(0, 3)
                    .forEach(value -> {
                        try {
                            digitalIO.digitalWrite(13, HIGH);
                            sleepUninterruptibly(1, SECONDS);
                            digitalIO.digitalWrite(13, LOW);
                        } catch (ArduinoIOException e) {
                            e.printStackTrace();
                        }
                    });

            digitalIO.digitalWrite(13, LOW);
        }
    }
}