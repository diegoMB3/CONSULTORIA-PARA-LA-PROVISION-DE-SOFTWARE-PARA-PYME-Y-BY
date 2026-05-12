package bo.gob.bdp.sam.core.domain.aggregate;

import bo.gob.bdp.sam.core.application.command.RegistrarClienteCommand;
import bo.gob.bdp.sam.core.domain.event.ClienteRegistradoEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import java.util.UUID;
import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class ClienteAggregate {

    @AggregateIdentifier
    private String clienteId;
    private String nombreCompleto;
    private String numeroDocumento;
    private String email;
    private String telefono;

    protected ClienteAggregate() {}

    // ==========================================
    // LADO ESCRITURA: Registro del Cliente
    // ==========================================
    @CommandHandler
    public ClienteAggregate(RegistrarClienteCommand command) {
        // 1. VALIDACIONES DE NEGOCIO (RNF-02: Integridad de Datos)
        if (command.getNumeroDocumento() == null || command.getNumeroDocumento().length() < 7) {
            throw new IllegalArgumentException("Número de Documento inválido - No cumple normativa ASFI");
        }
        if (command.getNombreCompleto() == null || command.getNombreCompleto().isEmpty()) {
            throw new IllegalArgumentException("Nombre Completo es obligatorio (RF-02.1)");
        }

        // 2. Generar ID Único (Expediente Digital Único - RF-02.1)
        String nuevoId = command.getNumeroDocumento() + "-" + UUID.randomUUID().toString().substring(0, 8);
        
        // 3. DISPARAR EVENTO (Inmutable)
        apply(new ClienteRegistradoEvent(
            nuevoId,
            command.getNombreCompleto(),
            command.getNumeroDocumento(),
            command.getEmail(),
            command.getTelefono()
        ));
    }

    @EventSourcingHandler
    public void on(ClienteRegistradoEvent event) {
        this.clienteId = event.getClienteId();
        this.nombreCompleto = event.getNombreCompleto();
        this.numeroDocumento = event.getNumeroDocumento();
        this.email = event.getEmail();
        this.telefono = event.getTelefono();
    }
}
