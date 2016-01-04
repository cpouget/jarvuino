package com.jarvuino.arduino.lcd;

import com.jarvuino.arduino.ArduinoIOException;
import com.jarvuino.core.ArduinoChannelWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.String.format;

public class Lcd {

    private static final Logger LOG = LoggerFactory.getLogger(Lcd.class);

    private ArduinoChannelWrapper channel;

    public Lcd(ArduinoChannelWrapper channel) {
        this.channel = channel;
    }

    public void create(int lcdId, int rs, int enable, int d4, int d5, int d6, int d7) throws ArduinoIOException {

        LOG.debug("send command: lcd/{}/create/{}/{}/{}/{}/{}/{}", lcdId, rs, enable, d4, d5, d6, d7);

        try {
            channel.get().writeAndFlush(format(":lcd/%d/create/%d/%d/%d/%d/%d/%d\n", lcdId, rs, enable, d4, d5, d6, d7)).sync();
        } catch (Exception e) {
            throw new ArduinoIOException(e);
        }
    }

    public void begin(int lcdId, int numCols, int numLines) throws ArduinoIOException {

        LOG.debug("send command: lcd/{}/begin/{}/{}", lcdId, numCols, numLines);

        try {
            channel.get().writeAndFlush(format(":lcd/%d/begin/%d/%d\n", lcdId, numCols, numLines)).sync();
        } catch (Exception e) {
            throw new ArduinoIOException(e);
        }
    }

    public void clear(int lcdId) throws ArduinoIOException {

        LOG.debug("send command: lcd/{}/clear", lcdId);

        try {
            channel.get().writeAndFlush(format(":lcd/%d/clear\n", lcdId)).sync();
        } catch (Exception e) {
            throw new ArduinoIOException(e);
        }
    }

    public void home(int lcdId) throws ArduinoIOException {

        LOG.debug("send command: lcd/{}/home", lcdId);

        try {
            channel.get().writeAndFlush(format(":lcd/%d/home\n", lcdId)).sync();
        } catch (Exception e) {
            throw new ArduinoIOException(e);
        }
    }

    public void print(int lcdId, String message) throws ArduinoIOException {

        LOG.debug("send command: lcd/{}/print/{}", lcdId, message);

        try {
            channel.get().writeAndFlush(format(":lcd/%d/print/%s\n", lcdId, message)).sync();
        } catch (Exception e) {
            throw new ArduinoIOException(e);
        }
    }
}
