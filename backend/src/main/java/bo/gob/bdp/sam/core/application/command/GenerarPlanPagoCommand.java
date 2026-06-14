package bo.gob.bdp.sam.core.application.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@AllArgsConstructor
public class GenerarPlanPagoCommand {
    @TargetAggregateIdentifier
    private final String clienteId;
    private final double monto;
    private final double tasaAnual;
    private final int plazoMeses;
}
