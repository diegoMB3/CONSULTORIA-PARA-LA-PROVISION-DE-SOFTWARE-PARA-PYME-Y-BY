package bo.gob.bdp.sam.core.application.command;

import lombok.Data;
import lombok.AllArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@AllArgsConstructor
public class RegistrarClienteCommand {
    @TargetAggregateIdentifier
    private final String numeroDocumento; // Usamos el CI/NIT como ID de agregado
    private final String nombreCompleto;
    private final String email;
    private final String telefono;
}
