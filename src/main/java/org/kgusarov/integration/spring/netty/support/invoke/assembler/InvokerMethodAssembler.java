package org.kgusarov.integration.spring.netty.support.invoke.assembler;

import io.netty.channel.Channel;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.stream.IntStream;
import static org.kgusarov.integration.spring.netty.support.invoke.assembler.Descriptors.*;
import static org.kgusarov.integration.spring.netty.support.invoke.assembler.LabelAssembler.createLabel;
import static org.kgusarov.integration.spring.netty.support.invoke.assembler.LocalVariableAssembler.*;
import static org.objectweb.asm.Opcodes.*;

/**
 * Internal API: code generation support - invoker method
 */
public final class InvokerMethodAssembler {

    private InvokerMethodAssembler() {
    }

    public static void assembleInvokerMethod(final ClassWriter cw, final String invokerDescriptor, final String invokerInternalName, final Method invokerMethod, final Method targetMethod, final boolean sendResult, final MethodPrefixAssembler prefixAssembler) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }
}
