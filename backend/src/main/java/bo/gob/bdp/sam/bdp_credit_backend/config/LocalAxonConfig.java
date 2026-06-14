package bo.gob.bdp.sam.bdp_credit_backend.config;

import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LocalAxonConfig {

    // ELIMINADO: El Bean de CommandGateway mockeado.
    // Axon creará el CommandGateway real automáticamente para rutear a los Aggregates.

    @Bean
    public EventStorageEngine eventStorageEngine() {
        // Almacena los eventos en memoria mientras desarrollamos sin Axon Server
        return new InMemoryEventStorageEngine();
    }

    @Bean
    public EmbeddedEventStore eventStore(EventStorageEngine storageEngine) {
        return EmbeddedEventStore.builder()
                .storageEngine(storageEngine)
                .build();
    }
}
