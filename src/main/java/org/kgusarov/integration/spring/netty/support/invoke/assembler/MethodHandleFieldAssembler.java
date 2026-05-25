package org.kgusarov.integration.spring.netty.support.invoke.assembler;

import com.google.common.primitives.Primitives;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import static org.kgusarov.integration.spring.netty.support.invoke.assembler.Descriptors.*;
import static org.kgusarov.integration.spring.netty.support.invoke.assembler.Descriptors.MH_DESCRIPTOR;
import static org.kgusarov.integration.spring.netty.support.invoke.assembler.LabelAssembler.createLabel;
import static org.objectweb.asm.Opcodes.*;

/**
 * Internal API: code generation support - method handle private static final field
 */
public final class MethodHandleFieldAssembler {

    private MethodHandleFieldAssembler() {
    }

    public static void assembleMethodHandleField(final ClassWriter cw, final Method targetMethod, final String invokerName) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }
}
