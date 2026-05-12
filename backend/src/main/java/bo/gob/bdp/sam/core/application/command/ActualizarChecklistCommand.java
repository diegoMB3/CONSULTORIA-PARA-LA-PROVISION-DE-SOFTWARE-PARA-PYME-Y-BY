package bo.gob.bdp.sam.core.application.command;

import lombok.Data;
import lombok.AllArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import java.util.Map;

@Data
@AllArgsConstructor
public class ActualizarChecklistCommand {
    @TargetAggregateIdentifier
    private final String clienteId;
    private final Map<String, Boolean> documentosRecibidos;
}