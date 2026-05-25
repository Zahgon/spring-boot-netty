package org.kgusarov.integration.spring.netty.support.invoke;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Internal API: fast invocation support
 */
public final class MethodHandleCreator {

    private MethodHandleCreator() {
    }

    static MethodHandle createUniversal(final String className, final String methodName, final Class<?>... params) throws IllegalAccessException, NoSuchMethodException, ClassNotFoundException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    static MethodHandle create(final String className, final String methodName, final Class<?>... params) throws IllegalAccessException, NoSuchMethodException, ClassNotFoundException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    static MethodHandle create(final Class<?> clazz, final String methodName, final Class<?>... params) throws IllegalAccessException, NoSuchMethodException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }
}
