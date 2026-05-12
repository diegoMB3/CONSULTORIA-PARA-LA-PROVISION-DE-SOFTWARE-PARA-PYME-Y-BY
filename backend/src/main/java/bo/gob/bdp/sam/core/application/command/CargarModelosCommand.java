package bo.gob.bdp.sam.core.application.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import lombok.Data;
import lombok.AllArgsConstructor;

// Este comando representa la INTENCIÓN de cargar los modelos
@Data
@AllArgsConstructor
public class CargarModelosCommand {
    @TargetAggregateIdentifier
    private final String usuarioId; // Identificador del analista (RF-AUTH-01: Según rol)
    private final String tipoDispositivo; // "MOBILE" o "WEB"
}
