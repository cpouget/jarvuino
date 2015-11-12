package com.jarvuino.core;

import com.google.common.base.Throwables;
import com.jarvuino.core.io.MessageDecoder;
import com.jarvuino.core.io.handler.ListenerResponseChannelHandler;
import com.jarvuino.core.io.handler.SynchronousResponseChannelHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.rxtx.RxtxChannel;
import io.netty.channel.rxtx.RxtxChannelOption;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.io.Closeable;
import java.io.IOException;
import java.net.SocketAddress;

import static java.util.concurrent.TimeUnit.MINUTES;

public class ArduinoChannelWrapper implements Closeable {

    private SocketAddress socketAddress;
    private EventLoopGroup group;
    public final ListenerResponseChannelHandler listenerResponseChannelHandler;
    public final SynchronousResponseChannelHandler synchronousHandler;
    private Channel channel;

    public ArduinoChannelWrapper(SocketAddress socketAddress, EventLoopGroup group, SynchronousResponseChannelHandler synchronousHandler, ListenerResponseChannelHandler listenerResponseChannelHandler) {
        this.socketAddress = socketAddress;
        this.group = group;
        this.synchronousHandler = synchronousHandler;
        this.listenerResponseChannelHandler = listenerResponseChannelHandler;
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
                        ch.pipeline().addLast("string decoder", new StringDecoder());
                        ch.pipeline().addLast("jarvuino decoder", new MessageDecoder());

                        ch.pipeline().addLast("synchronous handler", synchronousHandler);
                        ch.pipeline().addLast("digital listener handler", listenerResponseChannelHandler);
                    }
                });

        this.channel = b.connect(socketAddress)
                .awaitUninterruptibly()
                .channel();

        return this;
    }

    public void register() {
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
