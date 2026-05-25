package org.kgusarov.integration.spring.netty;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.kgusarov.integration.spring.netty.configuration.NettyServers;
import org.springframework.beans.factory.BeanInitializationException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Component that performs initialization of all the netty servers
 */
public class TcpServerLifeCycle {

    private final NettyServers nettyServers;

    public TcpServerLifeCycle(final NettyServers nettyServers) {
        this.nettyServers = nettyServers;
    }

    /**
     * Start all servers
     */
    @PostConstruct
    public void start() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Stop all servers
     */
    @PreDestroy
    public void stop() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }
}
