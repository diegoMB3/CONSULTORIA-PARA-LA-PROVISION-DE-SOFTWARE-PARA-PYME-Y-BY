package bo.gob.bdp.sam.bdp_credit_backend.config;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Proxy;
import java.util.concurrent.CompletableFuture;

@Configuration
public class LocalAxonConfig {

    @Bean
    public CommandGateway commandGateway() {
        return (CommandGateway) Proxy.newProxyInstance(
                CommandGateway.class.getClassLoader(),
                new Class[]{CommandGateway.class},
                (proxy, method, args) -> {
                    Class<?> rt = method.getReturnType();
                    if (CompletableFuture.class.isAssignableFrom(rt)) {
                        return CompletableFuture.completedFuture(null);
                    }
                    if (rt.equals(void.class)) {
                        return null;
                    }
                    if (rt.isPrimitive()) {
                        if (rt.equals(boolean.class)) return false;
                        if (rt.equals(byte.class)) return (byte) 0;
                        if (rt.equals(short.class)) return (short) 0;
                        if (rt.equals(int.class)) return 0;
                        if (rt.equals(long.class)) return 0L;
                        if (rt.equals(float.class)) return 0f;
                        if (rt.equals(double.class)) return 0d;
                        if (rt.equals(char.class)) return '\u0000';
                    }
                    return null;
                }
        );
    }

    @Bean
    public EventStorageEngine eventStorageEngine() {
        return new InMemoryEventStorageEngine();
    }

    @Bean
    public EmbeddedEventStore eventStore(EventStorageEngine storageEngine) {
        return EmbeddedEventStore.builder()
                .storageEngine(storageEngine)
                .build();
    }
}