package org.kgusarov.integration.spring.netty.handlers;

import com.google.common.annotations.VisibleForTesting;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;
import static io.netty.buffer.Unpooled.copiedBuffer;

/**
 * Simple handler implementation for sending cross-domain connection policy
 * (Adobe Flash format) to the clients. This handler always permits connection
 * from any host to any port as well as disconnects the client after the policy
 * has been sent.
 */
@ChannelHandler.Sharable
public final class FlashPolicyHandler extends ChannelInboundHandlerAdapter {

    @VisibleForTesting
    static final String POLICY_FILE_REQUEST = "<policy-file-request/>";

    @VisibleForTesting
    static final String POLICY_FILE_RESPONSE = "<?xml version=\"1.0\"?>" + "<!DOCTYPE cross-domain-policy SYSTEM \"/xml/dtds/cross-domain-policy.dtd\">" + "<cross-domain-policy> " + "   <site-control permitted-cross-domain-policies=\"master-only\"/>" + "   <allow-access-from domain=\"*\" to-ports=\"*\" />" + "</cross-domain-policy>";

    private final ByteBuf requestBuffer = copiedBuffer(POLICY_FILE_REQUEST, CharsetUtil.UTF_8);

    private final ByteBuf responseBuffer = copiedBuffer(POLICY_FILE_RESPONSE, CharsetUtil.UTF_8);

    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        throw new UnsupportedOperationException("STUB: not implemented");
    }
}
