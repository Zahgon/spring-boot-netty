package org.kgusarov.integration.spring.netty;

import com.google.common.collect.Maps;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.WriteBufferWaterMark;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Map;
import java.util.function.Supplier;

/**
 * A typesafe configuration for {@code io.netty.channel.ChannelOption} options
 */
@SuppressWarnings({ "rawtypes", "SameParameterValue", "WeakerAccess" })
public final class ChannelOptions implements Supplier<Map<ChannelOption, Object>> {

    private final Map<ChannelOption, Object> options = Maps.newHashMap();

    public void setAllocator(final ByteBufAllocator allocator) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public void setRecvBufAllocator(final RecvByteBufAllocator allocator) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public void setConnectTimeout(final int milliseconds) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public void setWriteSpinCount(final int count) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public void setWriteBufferWaterMark(final WriteBufferWaterMark mark) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public void setAllowHalfClosure(final boolean allow) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public void setAutoRead(final boolean autoRead) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public void setSoBroadcast(final boolean broadcast) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public void setSoKeepAlive(final boolean keepAlive) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public void setSoSndBuf(final int buf) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public void setSoRcvBuf(final int buf) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public void setSoReuseAddr(final boolean reuseAddr) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public void setSoLinger(final int linger) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public void setSoBacklog(final int backlog) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public void setSoTimeout(final int timeout) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public void setIpTos(final int tos) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public void setIpMulticastAddr(final InetAddress addr) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public void setIpMulticastIf(final NetworkInterface iface) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public void setIpMulticastTtl(final int ttl) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public void setIpMulticastLoopDisabled(final boolean loopDisabled) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public void setTcpNodelay(final boolean noDelay) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public Map<ChannelOption, Object> get() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }
}
