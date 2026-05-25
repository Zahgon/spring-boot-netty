package org.kgusarov.integration.spring.netty;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

/**
 * Instance of the named TCP server
 */
public final class TcpServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(TcpServer.class);

    private final AtomicBoolean initialized = new AtomicBoolean(false);

    private final Map<String, Supplier<ChannelHandler>> handlers = Maps.newLinkedHashMap();

    private final List<Supplier<ChannelFutureListener>> closeFutureListeners = Lists.newArrayList();

    private final List<Supplier<ChannelHandler>> channelActiveHandlers = Lists.newArrayList();

    private final String name;

    private int bossThreads = Runtime.getRuntime().availableProcessors();

    private int workerThreads = Runtime.getRuntime().availableProcessors();

    private String host;

    private int port;

    private ChannelOptions options = new ChannelOptions();

    private ChannelOptions childOptions = new ChannelOptions();

    private EventLoopGroup bossThreadGroup;

    private EventLoopGroup workerThreadGroup;

    private int boundToPort = -1;

    /**
     * Create TCP server instance of the given name
     *
     * @param name Name of the TCP server
     */
    public TcpServer(final String name) {
        this.name = name;
    }

    /**
     * Get the port this server is bound to
     *
     * @return Port server is currently bound to, {@code -1} if not bound
     */
    public int getBoundToPort() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Set the number of the Netty boss treads
     *
     * @param bossThreads Boss thread count
     */
    public void setBossThreads(final int bossThreads) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Set the number of the Netty worker treads
     *
     * @param workerThreads Worker thread count
     */
    public void setWorkerThreads(final int workerThreads) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Set the host the server will listen on
     *
     * @param host Server host
     */
    public void setHost(final String host) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Set the port the server will listen on
     *
     * @param port Server port
     */
    public void setPort(final int port) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Set the options for the acceptor channel
     *
     * @param options Channel options
     */
    public void setOptions(final ChannelOptions options) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Set the options for the newly created channels
     *
     * @param childOptions Channel options
     */
    public void setChildOptions(final ChannelOptions childOptions) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Stops the current server
     */
    void stop() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Add new disconnection listener
     *
     * @param listener Listener to be added
     */
    public void onDisconnect(final Supplier<ChannelFutureListener> listener) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Add new connect listener
     *
     * @param handler Listener to be added
     */
    public void onConnect(final Supplier<ChannelHandler> handler) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Add new named channel handler
     *
     * @param name           Handler name
     * @param channelHandler Handler to be added
     */
    public void addHandler(final String name, final Supplier<ChannelHandler> channelHandler) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Start the current server
     *
     * @return              Empty future that will resolve when server will actually start
     */
    ListenableFuture<Void> start() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    private ServerBootstrap createServerBootstrap() {
        bossThreadGroup = new NioEventLoopGroup(bossThreads);
        workerThreadGroup = new NioEventLoopGroup(workerThreads);
        final ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossThreadGroup, workerThreadGroup);
        setOptions(bootstrap);
        initialized.set(true);
        return initServerBoostrap(bootstrap);
    }

    private ServerBootstrap initServerBoostrap(final ServerBootstrap bootstrap) {
        return bootstrap.channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(final SocketChannel ch) {
                throw new UnsupportedOperationException("STUB: not implemented");
            }
        });
    }

    private void initChildChannel(final SocketChannel ch) {
        final ChannelPipeline pipeline = ch.pipeline();
        for (final Map.Entry<String, Supplier<ChannelHandler>> entry : handlers.entrySet()) {
            final ChannelHandler handler = entry.getValue().get();
            final String key = entry.getKey();
            pipeline.addLast(key, handler);
        }
        if (!closeFutureListeners.isEmpty()) {
            for (final Supplier<ChannelFutureListener> listener : closeFutureListeners) {
                final ChannelFutureListener cfl = listener.get();
                ch.closeFuture().addListener(cfl);
            }
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void setOptions(final ServerBootstrap bootstrap) {
        final Map<ChannelOption, Object> channelOptions = options.get();
        final Map<ChannelOption, Object> childChannelOptions = childOptions.get();
        for (final Map.Entry<ChannelOption, Object> entry : channelOptions.entrySet()) {
            final ChannelOption key = entry.getKey();
            final Object value = entry.getValue();
            bootstrap.option(key, value);
        }
        for (final Map.Entry<ChannelOption, Object> entry : childChannelOptions.entrySet()) {
            final ChannelOption key = entry.getKey();
            final Object value = entry.getValue();
            bootstrap.childOption(key, value);
        }
    }

    private TcpServer checkState() {
        if (initialized.get()) {
            throw new IllegalStateException("Server already initialized");
        }
        return this;
    }
}
