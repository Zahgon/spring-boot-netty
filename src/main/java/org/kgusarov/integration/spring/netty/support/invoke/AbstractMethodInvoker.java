package org.kgusarov.integration.spring.netty.support.invoke;

import org.kgusarov.integration.spring.netty.support.invoke.assembler.MethodPrefixAssembler;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLong;
import static org.kgusarov.integration.spring.netty.support.invoke.assembler.ConstructorAssembler.assembleConstructor;
import static org.kgusarov.integration.spring.netty.support.invoke.assembler.InvokerMethodAssembler.assembleInvokerMethod;
import static org.kgusarov.integration.spring.netty.support.invoke.assembler.MethodHandleFieldAssembler.assembleMethodHandleField;
import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;
import static org.objectweb.asm.Opcodes.*;

/**
 * Internal API: invocation support for {@link org.kgusarov.integration.spring.netty.annotations.NettyController}
 * methods
 */
abstract class AbstractMethodInvoker {

    @SuppressWarnings("AbstractClassNeverImplemented")
    abstract static class InvokerBase {

        Object bean;
    }

    private static final String[] EMPTY_INTERFACES = new String[0];

    private static final GeneratedClassLoader CLASS_LOADER = new GeneratedClassLoader();

    private static final AtomicLong COUNTER = new AtomicLong(1);

    final <T extends InvokerBase> T buildInvoker(final Class<?> parent, final Method targetMethod, final Method invokerMethod, final boolean sendResult, final MethodPrefixAssembler prefixAssembler) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }
}
