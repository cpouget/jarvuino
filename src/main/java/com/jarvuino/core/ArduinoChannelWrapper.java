package com.jarvuino.core;

import com.google.common.base.Throwables;
import com.jarvuino.core.io.SynchronousResponseChannelHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.rxtx.RxtxChannel;
import io.netty.channel.rxtx.RxtxChannelOption;
import io.netty.handler.codec.string.StringEncoder;

import java.io.Closeable;
import java.io.IOException;
import java.net.SocketAddress;

import static java.util.concurrent.TimeUnit.MINUTES;

public class ArduinoChannelWrapper implements Closeable {

    private SocketAddress socketAddress;
    private EventLoopGroup group;
    public final SynchronousResponseChannelHandler synchronousResponseChannelHandler;
    private Channel channel;

    public ArduinoChannelWrapper(SocketAddress socketAddress, EventLoopGroup group, SynchronousResponseChannelHandler synchronousResponseChannelHandler) {
        this.socketAddress = socketAddress;
        this.group = group;
        this.synchronousResponseChannelHandler = synchronousResponseChannelHandler;
    }

    public ArduinoChannelWrapper connect() {

        Bootstrap b = new Bootstrap()
                .group(group)
                .channel(RxtxChannel.class)
                .handler(new ChannelInitializer<RxtxChannel>() {
                    @Override
                    public void initChannel(RxtxChannel ch) throws Exception {
                        ch.config().setOption(RxtxChannelOption.WAIT_TIME, 2000);

                        ch.pipeline().addLast("encoder", new StringEncoder());

                        ch.pipeline().addLast("arduino channel handler", synchronousResponseChannelHandler);
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
