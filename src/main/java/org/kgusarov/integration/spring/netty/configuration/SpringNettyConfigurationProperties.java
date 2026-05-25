package org.kgusarov.integration.spring.netty.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.List;

@ConfigurationProperties(prefix = "netty")
public class SpringNettyConfigurationProperties {

    private List<TcpServerProperties> servers;

    public List<TcpServerProperties> getServers() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public void setServers(final List<TcpServerProperties> servers) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }
}
