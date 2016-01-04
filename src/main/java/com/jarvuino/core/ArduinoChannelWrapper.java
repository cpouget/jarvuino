package com.jarvuino.core;

import com.google.common.base.Throwables;
import com.jarvuino.core.io.MessageDecoder;
import com.jarvuino.core.io.handler.ResponseChannelHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.rxtx.RxtxChannel;
import io.netty.channel.rxtx.RxtxChannelOption;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.io.Closeable;
import java.io.IOException;
import java.net.SocketAddress;

import static java.util.concurrent.TimeUnit.MINUTES;

public class ArduinoChannelWrapper implements Closeable {

    private SocketAddress socketAddress;
    private EventLoopGroup group;
    public final ResponseChannelHandler handler;
    private Channel channel;

    public ArduinoChannelWrapper(SocketAddress socketAddress, EventLoopGroup group, ResponseChannelHandler handler) {
        this.socketAddress = socketAddress;
        this.group = group;
        this.handler = handler;
    }

    public ArduinoChannelWrapper connect() {

        Bootstrap b = new Bootstrap()
                .group(group)
                .channel(RxtxChannel.class)
                .handler(new ChannelInitializer<RxtxChannel>() {
                    @Override
                    public void initChannel(RxtxChannel ch) throws Exception {
                        ch.config().setOption(RxtxChannelOption.WAIT_TIME, 2000);
                        ch.config().setOption(RxtxChannelOption.READ_TIMEOUT, 200);

                        ch.pipeline().addLast("encoder", new StringEncoder());
                        ch.pipeline().addLast("line decoder", new LineBasedFrameDecoder(256));
                        ch.pipeline().addLast("string decoder", new StringDecoder());
                        ch.pipeline().addLast("jarvuino decoder", new MessageDecoder());

                        ch.pipeline().addLast("synchronous handler", handler);
                    }
                });

        this.channel = b.connect(socketAddress)
                .awaitUninterruptibly()
                .channel();

        return this;
    }

    public Channel get() {
        return channel;
    }

    @Override
    public void close() throws IOException {
        try {
            this.channel.close().sync();

            this.group.shutdownGracefully();
            this.group.awaitTermination(1, MINUTES);
        } catch (InterruptedException e) {
            throw Throwables.propagate(e);
        }
    }
}
