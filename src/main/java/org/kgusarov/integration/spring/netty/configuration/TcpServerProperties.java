package org.kgusarov.integration.spring.netty.configuration;

import org.kgusarov.integration.spring.netty.ChannelOptions;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Configuration properties for a single TCP server instance
 */
public class TcpServerProperties {

    @NotBlank
    private String name;

    @NotBlank
    private String host;

    @NotNull
    private Integer port;

    private Integer bossThreads;

    private Integer workerThreads;

    @NestedConfigurationProperty
    private ChannelOptions options;

    @NestedConfigurationProperty
    private ChannelOptions childOptions;

    public String getName() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public void setName(final String name) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public Integer getBossThreads() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public void setBossThreads(final Integer bossThreads) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public Integer getWorkerThreads() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public void setWorkerThreads(final Integer workerThreads) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public String getHost() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public void setHost(final String host) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public Integer getPort() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public void setPort(final Integer port) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public ChannelOptions getOptions() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public void setOptions(final ChannelOptions options) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public ChannelOptions getChildOptions() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public void setChildOptions(final ChannelOptions childOptions) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }
}
