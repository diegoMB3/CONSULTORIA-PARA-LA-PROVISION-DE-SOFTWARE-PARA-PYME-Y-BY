package bo.gob.bdp.sam.core.application.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrarEvaluacionProduccionCommand {

    @TargetAggregateIdentifier
    private String clienteId;

    private String evaluacionId;
    private double ingresos;
    private double costosVariables;
    private double costosFijos;
}
