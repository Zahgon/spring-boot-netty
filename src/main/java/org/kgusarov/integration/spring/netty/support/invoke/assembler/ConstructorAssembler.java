package org.kgusarov.integration.spring.netty.support.invoke.assembler;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import static org.kgusarov.integration.spring.netty.support.invoke.assembler.LabelAssembler.createLabel;
import static org.objectweb.asm.Opcodes.*;

/**
 * Internal API: code generation support - constructor
 */
public final class ConstructorAssembler {

    private ConstructorAssembler() {
    }

    public static void assembleConstructor(final Type invokerType, final String parentName, final ClassWriter cw) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }
}
