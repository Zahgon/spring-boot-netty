package org.kgusarov.integration.spring.netty.support.invoke.assembler;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import java.lang.reflect.Parameter;
import static org.kgusarov.integration.spring.netty.support.invoke.assembler.Descriptors.OBJ_DESCRIPTOR;

/**
 * Internal API: code generation support - local variables
 */
final class LocalVariableAssembler {

    private LocalVariableAssembler() {
    }

    static int getResultIdx(final Parameter[] invokeParameters) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    static int getFirstVarIdx(final Parameter[] invokeParameters, final boolean sendResult) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    static void assembleLocalVariables(final MethodVisitor m, final Label ms, final Label me, final String invokerDescriptor, final Parameter[] invokeParameters, final Parameter[] targetMethodParameters, final boolean sendResult) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }
}
