package org.kgusarov.integration.spring.netty.support.invoke;

import java.lang.invoke.MethodHandle;

/**
 * Internal API: fast invocation support
 */
final class GeneratedClassLoader {

    private static final MethodHandle DEFINE_CLASS_HANDLE;

    static {
        try {
            DEFINE_CLASS_HANDLE = MethodHandleCreator.create(ClassLoader.class, "defineClass", String.class, byte[].class, int.class, int.class);
        } catch (final IllegalAccessException | NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }

    @SuppressWarnings("unchecked")
    <T> Class<T> load(final byte[] bytecode, final String fqcn) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }
}
