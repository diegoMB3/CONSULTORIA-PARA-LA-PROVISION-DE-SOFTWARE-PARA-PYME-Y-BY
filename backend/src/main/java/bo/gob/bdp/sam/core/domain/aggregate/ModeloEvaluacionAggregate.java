package bo.gob.bdp.sam.core.domain.aggregate;

import bo.gob.bdp.sam.core.application.command.CargarModelosCommand;
import bo.gob.bdp.sam.core.domain.event.ModelosCargadosEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class ModeloEvaluacionAggregate {

    @AggregateIdentifier
    private String usuarioId;
    private List<String> modelosPermitidos;

    // Constructor obligatorio para Axon
    protected ModeloEvaluacionAggregate() {}

    // ==========================================
    // LADO ESCRITURA: Recibe el Comando
    // ==========================================
    @CommandHandler
    public ModeloEvaluacionAggregate(CargarModelosCommand command) {
        // 1. Lógica de Negocio Hexagonal (Validación de permisos)
        List<String> modelosBase = Arrays.asList(
            "EVALUACIÓN AGRÍCOLA (RF-04.1)",
            "EVALUACIÓN PECUARIA (RF-05.1)",
            "EVALUACIÓN PRODUCCIÓN (RF-06.1)"
        );
        
        // 2. Verificar si el usuario tiene restricciones (Simulado)
        if (command.getUsuarioId() == null || command.getUsuarioId().isEmpty()) {
            throw new IllegalArgumentException("Usuario no autenticado - Violación RNF-01");
        }

        // 3. Disparar el Evento (ESCRITURA INMUTABLE)
        apply(new ModelosCargadosEvent(
            command.getUsuarioId(),
            modelosBase,
            Instant.now().toString()
        ));
    }

    // ==========================================
    // MANEJO DEL EVENTO: Actualiza el Estado
    // ==========================================
    @EventSourcingHandler
    public void on(ModelosCargadosEvent event) {
        this.usuarioId = event.getUsuarioId();
        this.modelosPermitidos = event.getNombresModelos();
    }
}
