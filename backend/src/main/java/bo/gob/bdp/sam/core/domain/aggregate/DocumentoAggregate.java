package bo.gob.bdp.sam.core.domain.aggregate;

import bo.gob.bdp.sam.core.application.command.ActualizarChecklistCommand;
import bo.gob.bdp.sam.core.domain.event.ChecklistActualizadoEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import java.util.HashMap;
import java.util.Map;
import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class DocumentoAggregate {

    @AggregateIdentifier
    private String clienteId;
    private Map<String, Boolean> documentosEstado;
    private boolean checklistCompleto;

    protected DocumentoAggregate() {}

    // ==========================================
    // LADO ESCRITURA: Actualizar Checklist
    // ==========================================
    @CommandHandler
    public DocumentoAggregate(ActualizarChecklistCommand command) {
        // 1. Validar que el cliente existe (simulado)
        if (command.getClienteId() == null || command.getClienteId().isEmpty()) {
            throw new IllegalArgumentException("Cliente no encontrado - No se puede crear checklist");
        }

        // 2. Verificar documentos obligatorios (RF-02.2)
        Map<String, Boolean> docsRecibidos = command.getDocumentosRecibidos();
        
        // Lista de documentos OBLIGATORIOS según normativa BDP
        String[] obligatorios = {"CI_FRENTE", "CI_REVERSO", "FACTURA_AGUA", "FACTURA_LUZ", "ESTADO_CUENTA"};
        boolean completo = true;
        
        for (String doc : obligatorios) {
            if (!docsRecibidos.getOrDefault(doc, false)) {
                completo = false;
                break;
            }
        }

        // 3. DISPARAR EVENTO (Inmutable)
        apply(new ChecklistActualizadoEvent(
            command.getClienteId(),
            docsRecibidos,
            completo,
            java.time.Instant.now().toString()
        ));
    }

    @EventSourcingHandler
    public void on(ChecklistActualizadoEvent event) {
        this.clienteId = event.getClienteId();
        this.documentosEstado = event.getDocumentosEstado();
        this.checklistCompleto = event.isChecklistCompleto();
    }
}