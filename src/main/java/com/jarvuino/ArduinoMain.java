package com.jarvuino;

import com.jarvuino.arduino.DigitalIO;
import com.jarvuino.core.ArduinoChannelWrapper;
import com.jarvuino.core.io.ResponseListenerTask;
import com.jarvuino.core.io.handler.ListenerResponseChannelHandler;
import com.jarvuino.core.io.handler.SynchronousResponseChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.rxtx.RxtxDeviceAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.jarvuino.arduino.constants.PinPower.HIGH;
import static com.jarvuino.arduino.constants.PinPower.LOW;
import static java.util.concurrent.TimeUnit.SECONDS;


public class ArduinoMain {
    static final String PORT = System.getProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyS80");

    private static final Logger LOG = LoggerFactory.getLogger(ArduinoMain.class);

    public static void main(String[] args) throws Exception {
        EventLoopGroup group = new OioEventLoopGroup();

        SynchronousResponseChannelHandler synchronousHandler = new SynchronousResponseChannelHandler();
        ListenerResponseChannelHandler handler = new ListenerResponseChannelHandler();

        ExecutorService executorService = Executors.newFixedThreadPool(1);

        ResponseListenerTask responseListenerTask = new ResponseListenerTask(13, handler) {
            @Override
            public void onMessage(String msg) {
                LOG.info("message from pin #{}: {}", pin, msg);
            }
        };

        executorService.submit(responseListenerTask);

        ArduinoChannelWrapper arduinoChannelWrapper = new ArduinoChannelWrapper(new RxtxDeviceAddress(PORT), group, synchronousHandler, handler);

        try (ArduinoChannelWrapper channel = arduinoChannelWrapper.connect()) {

            DigitalIO digitalIO = new DigitalIO(channel);

            digitalIO.digitalWrite(13, HIGH);
            digitalIO.digitalWrite(13, LOW);
            digitalIO.digitalWrite(13, HIGH);
            digitalIO.digitalWrite(13, LOW);

            digitalIO.digitalWrite(13, HIGH);
            digitalIO.digitalRead(13);
            digitalIO.digitalWrite(13, LOW);
            digitalIO.digitalRead(13);
        }

        responseListenerTask.stop();

        executorService.shutdown();
        executorService.awaitTermination(10, SECONDS);
    }
}