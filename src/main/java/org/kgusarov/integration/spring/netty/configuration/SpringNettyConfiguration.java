package org.kgusarov.integration.spring.netty.configuration;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.netty.channel.ChannelHandler;
import org.kgusarov.integration.spring.netty.ChannelOptions;
import org.kgusarov.integration.spring.netty.TcpServer;
import org.kgusarov.integration.spring.netty.TcpServerLifeCycle;
import org.kgusarov.integration.spring.netty.annotations.*;
import org.kgusarov.integration.spring.netty.support.SpringChannelFutureListener;
import org.kgusarov.integration.spring.netty.support.SpringChannelHandler;
import org.kgusarov.integration.spring.netty.support.invoke.OnConnectMethodInvoker;
import org.kgusarov.integration.spring.netty.support.invoke.OnDisconnectMethodInvoker;
import org.kgusarov.integration.spring.netty.support.invoke.OnMessageMethodInvoker;
import org.kgusarov.integration.spring.netty.support.resolvers.NettyCallbackParameterResolver;
import org.kgusarov.integration.spring.netty.support.resolvers.NettyOnConnectParameterResolver;
import org.kgusarov.integration.spring.netty.support.resolvers.NettyOnDisconnectParameterResolver;
import org.kgusarov.integration.spring.netty.support.resolvers.NettyOnMessageParameterResolver;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.MethodParameter;
import org.springframework.util.ReflectionUtils;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import static com.google.common.collect.Sets.symmetricDifference;
import static com.google.common.collect.Sets.union;
import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;

@Configuration
@ComponentScan(basePackageClasses = SpringChannelHandler.class)
@EnableConfigurationProperties(SpringNettyConfigurationProperties.class)
public class SpringNettyConfiguration {

    @VisibleForTesting
    static final Comparator<Method> ON_CONNECT_METHOD_COMPARATOR = Comparator.comparingInt(m -> {
        final NettyOnConnect ann = findAnnotation(m, NettyOnConnect.class);
        if (ann == null) {
            throw new IllegalStateException();
        }
        return ann.priority();
    });

    @VisibleForTesting
    static final Comparator<Method> ON_DISCONNECT_METHOD_COMPARATOR = Comparator.comparingInt(m -> {
        final NettyOnDisconnect ann = findAnnotation(m, NettyOnDisconnect.class);
        if (ann == null) {
            throw new IllegalStateException();
        }
        return ann.priority();
    });

    @VisibleForTesting
    static final Comparator<Method> ON_MESSAGE_METHOD_COMPARATOR = Comparator.comparingInt(m -> {
        final NettyOnMessage ann = findAnnotation(m, NettyOnMessage.class);
        if (ann == null) {
            throw new IllegalStateException();
        }
        return ann.priority();
    });

    @VisibleForTesting
    static final Comparator<Object> FILTER_BEAN_COMPARATOR = Comparator.comparingInt(o -> {
        final Class<?> clazz = AopProxyUtils.ultimateTargetClass(o);
        final NettyFilter ann = findAnnotation(clazz, NettyFilter.class);
        if (ann == null) {
            throw new IllegalStateException();
        }
        return ann.priority();
    });

    private final SpringNettyConfigurationProperties configurationProperties;

    private final ConfigurableListableBeanFactory beanFactory;

    @Autowired
    public SpringNettyConfiguration(final SpringNettyConfigurationProperties configurationProperties, final ConfigurableListableBeanFactory beanFactory) {
        this.configurationProperties = configurationProperties;
        this.beanFactory = beanFactory;
    }

    @Bean
    public TcpServerLifeCycle tcpServerLifeCycle() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Bean
    public NettyServers tcpServers() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    private Map<String, List<ChannelHandler>> buildFilterHandlers(final Map<String, Object> filters) {
        final Map<String, List<ChannelHandler>> filterHandlers = new HashMap<>();
        filters.forEach((ignored, bean) -> {
            final Class<?> beanClass = AopProxyUtils.ultimateTargetClass(bean);
            if (!ChannelHandler.class.isAssignableFrom(beanClass)) {
                throw new IllegalStateException("Bean annotated with @NettyFilter doesn't implement ChannelHandler: " + bean);
            }
            final NettyFilter annotation = findAnnotation(beanClass, NettyFilter.class);
            if (annotation != null) {
                final String serverName = annotation.serverName();
                filterHandlers.computeIfAbsent(serverName, k -> new ArrayList<>()).add((ChannelHandler) bean);
            }
        });
        //noinspection NestedMethodCall
        filterHandlers.values().forEach(l -> l.sort(FILTER_BEAN_COMPARATOR));
        return filterHandlers;
    }

    private List<Supplier<ChannelHandler>> buildFilterSuppliers(final BiMap<String, Object> filterBeans, final Map<String, List<ChannelHandler>> filterHandlers, final String name) {
        return filterHandlers.getOrDefault(name, new ArrayList<>()).stream().map(o -> {
            final Class<?> beanClass = AopProxyUtils.ultimateTargetClass(o);
            final ChannelHandler.Sharable sharable = findAnnotation(beanClass, ChannelHandler.Sharable.class);
            if (sharable == null) {
                final Scope scope = findAnnotation(beanClass, Scope.class);
                //noinspection NestedMethodCall
                if ((scope == null) || !ConfigurableBeanFactory.SCOPE_PROTOTYPE.equals(scope.value())) {
                    throw new IllegalStateException("Non-sharable handler should be presented by a prototype bean");
                }
            }
            final String beanName = filterBeans.inverse().get(o);
            @SuppressWarnings("UnnecessaryLocalVariable")
            final Supplier<ChannelHandler> beanSupplier = (sharable == null) ? () -> (ChannelHandler) beanFactory.getBean(beanName, beanClass) : () -> o;
            return beanSupplier;
        }).collect(Collectors.toList());
    }

    private TcpServer buildTcpServer(final TcpServerProperties serverProperties, final String name) {
        final Integer bossThreads = serverProperties.getBossThreads();
        final Integer workerThreads = serverProperties.getWorkerThreads();
        final ChannelOptions childOptions = serverProperties.getChildOptions();
        final ChannelOptions options = serverProperties.getOptions();
        final String host = serverProperties.getHost();
        final Integer port = serverProperties.getPort();
        final TcpServer server = new TcpServer(name);
        server.setHost(host);
        server.setPort(port);
        if (bossThreads != null) {
            server.setBossThreads(bossThreads);
        }
        if (workerThreads != null) {
            server.setWorkerThreads(workerThreads);
        }
        if (options != null) {
            server.setOptions(options);
        }
        if (childOptions != null) {
            server.setChildOptions(childOptions);
        }
        return server;
    }

    private void fillControllerHandlers(final Map<String, List<Method>> connectHandlers, final Map<String, List<Method>> disconnectHandlers, final Map<String, List<Method>> messageHandlers) {
        final Map<String, Object> controllers = beanFactory.getBeansWithAnnotation(NettyController.class);
        controllers.forEach((ignored, bean) -> {
            final Class<?> beanClass = AopProxyUtils.ultimateTargetClass(bean);
            final Method[] methods = ReflectionUtils.getAllDeclaredMethods(beanClass);
            for (final Method method : methods) {
                final boolean isConnectHandler = checkForConnectHandler(connectHandlers, method);
                final boolean isDisconnectHandler = checkForDisconnectHandler(disconnectHandlers, method);
                final boolean isMessageHandler = checkForMessageHandler(messageHandlers, method);
                final boolean isHandler = isConnectHandler || isDisconnectHandler || isMessageHandler;
                if (isHandler) {
                    final long c = Stream.of(isConnectHandler, isDisconnectHandler, isMessageHandler).filter(b -> b).count();
                    if (c != 1) {
                        throw new IllegalStateException("Method " + method + " is handler of various events. Currently this is not allowed!");
                    }
                }
            }
        });
        //noinspection NestedMethodCall
        connectHandlers.values().forEach(l -> l.sort(ON_CONNECT_METHOD_COMPARATOR));
        //noinspection NestedMethodCall
        disconnectHandlers.values().forEach(l -> l.sort(ON_DISCONNECT_METHOD_COMPARATOR));
        //noinspection NestedMethodCall
        messageHandlers.values().forEach(l -> l.sort(ON_MESSAGE_METHOD_COMPARATOR));
    }

    private List<OnDisconnectMethodInvoker> buildDisconnectMethodInvokers(final String serverName, final Map<String, List<Method>> disconnectHandlers, final Collection<NettyOnDisconnectParameterResolver> disconnectParameterResolvers) {
        final List<Method> onDisconnect = disconnectHandlers.getOrDefault(serverName, new ArrayList<>());
        final List<OnDisconnectMethodInvoker> result = new ArrayList<>();
        for (final Method method : onDisconnect) {
            final List<NettyOnDisconnectParameterResolver> resolvers = buildMethodParameterResolvers(method, disconnectParameterResolvers);
            ReflectionUtils.makeAccessible(method);
            final Class<?> declaringClass = method.getDeclaringClass();
            final Object bean = beanFactory.getBean(declaringClass);
            final OnDisconnectMethodInvoker invoker = new OnDisconnectMethodInvoker(bean, method, resolvers);
            result.add(invoker);
        }
        return result;
    }

    private List<OnMessageMethodInvoker> buildMessageMethodInvokers(final String serverName, final Map<String, List<Method>> messageHandlers, final Collection<NettyOnMessageParameterResolver> messageParameterResolvers) {
        final List<Method> onMessage = messageHandlers.getOrDefault(serverName, new ArrayList<>());
        final List<OnMessageMethodInvoker> result = new ArrayList<>();
        for (final Method method : onMessage) {
            final List<NettyOnMessageParameterResolver> resolvers = buildMethodParameterResolvers(method, messageParameterResolvers);
            final Class<?> returnType = method.getReturnType();
            final boolean sendInvocationResultBack = !void.class.equals(returnType);
            ReflectionUtils.makeAccessible(method);
            final Class<?> declaringClass = method.getDeclaringClass();
            final Object bean = beanFactory.getBean(declaringClass);
            final OnMessageMethodInvoker invoker = new OnMessageMethodInvoker(bean, method, resolvers, sendInvocationResultBack);
            result.add(invoker);
        }
        return result;
    }

    private List<OnConnectMethodInvoker> buildConnectMethodInvokers(final String serverName, final Map<String, List<Method>> connectHandlers, final Collection<NettyOnConnectParameterResolver> connectParameterResolvers) {
        final List<Method> onConnect = connectHandlers.getOrDefault(serverName, new ArrayList<>());
        final List<OnConnectMethodInvoker> result = new ArrayList<>();
        for (final Method method : onConnect) {
            final List<NettyOnConnectParameterResolver> resolvers = buildMethodParameterResolvers(method, connectParameterResolvers);
            final Class<?> returnType = method.getReturnType();
            final boolean sendInvocationResultBack = !void.class.equals(returnType);
            final Class<?> declaringClass = method.getDeclaringClass();
            final Object bean = beanFactory.getBean(declaringClass);
            final OnConnectMethodInvoker invoker = new OnConnectMethodInvoker(bean, method, resolvers, sendInvocationResultBack);
            result.add(invoker);
        }
        return result;
    }

    private static <T extends NettyCallbackParameterResolver> List<T> buildMethodParameterResolvers(final Method method, final Collection<T> candidates) {
        final int parameterCount = method.getParameterCount();
        return IntStream.range(0, parameterCount).mapToObj(i -> new MethodParameter(method, i)).map(methodParameter -> findMethodParameterResolver(candidates, methodParameter)).collect(Collectors.toList());
    }

    private static <T extends NettyCallbackParameterResolver> T findMethodParameterResolver(final Collection<T> candidates, final MethodParameter methodParameter) {
        //noinspection NestedMethodCall
        return candidates.stream().filter(r -> r.canResolve(methodParameter)).findFirst().orElseThrow(() -> new IllegalStateException("Unable to find resolver for " + methodParameter));
    }

    private static boolean checkForConnectHandler(final Map<String, List<Method>> connectHandlers, final Method method) {
        final NettyOnConnect annotation = findAnnotation(method, NettyOnConnect.class);
        if (annotation != null) {
            final String serverName = annotation.serverName();
            connectHandlers.computeIfAbsent(serverName, k -> new ArrayList<>()).add(method);
            return true;
        }
        return false;
    }

    private static boolean checkForDisconnectHandler(final Map<String, List<Method>> disconnectHandlers, final Method method) {
        final NettyOnDisconnect annotation = findAnnotation(method, NettyOnDisconnect.class);
        if (annotation != null) {
            final String serverName = annotation.serverName();
            disconnectHandlers.computeIfAbsent(serverName, k -> new ArrayList<>()).add(method);
            return true;
        }
        return false;
    }

    private static boolean checkForMessageHandler(final Map<String, List<Method>> messageHandlers, final Method method) {
        final NettyOnMessage annotation = findAnnotation(method, NettyOnMessage.class);
        if (annotation != null) {
            final String serverName = annotation.serverName();
            messageHandlers.computeIfAbsent(serverName, k -> new ArrayList<>()).add(method);
            return true;
        }
        return false;
    }

    private static void validateConfiguration(final List<TcpServerProperties> servers, final Map<String, List<Method>> connectHandlers, final Map<String, List<Method>> disconnectHandlers, final Map<String, List<Method>> messageHandlers, final Map<String, List<ChannelHandler>> filterHandlers) {
        final Set<String> serversInConfig = servers.stream().map(TcpServerProperties::getName).distinct().collect(Collectors.toSet());
        if (serversInConfig.size() != servers.size()) {
            throw new IllegalStateException("Configuration has duplicate server definitions");
        }
        final Set<String> connectServerNames = connectHandlers.keySet();
        final Set<String> disconnectServerNames = disconnectHandlers.keySet();
        final Set<String> messageServerNames = messageHandlers.keySet();
        final Set<String> filterServerNames = filterHandlers.keySet();
        final Set<String> serversInHandlers = union(union(connectServerNames, disconnectServerNames), union(messageServerNames, filterServerNames));
        final Set<String> diff = symmetricDifference(serversInConfig, serversInHandlers);
        if (!diff.isEmpty()) {
            throw new IllegalStateException("Handlers are not present both in config and handler beans: " + diff);
        }
    }
}
