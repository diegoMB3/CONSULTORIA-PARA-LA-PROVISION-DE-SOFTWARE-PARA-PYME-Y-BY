package bo.gob.bdp.sam.core.application.command;

import lombok.Data;
import lombok.AllArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@AllArgsConstructor
public class CalcularIndicadoresCommand {
    @TargetAggregateIdentifier
    private final String clienteId;
}
