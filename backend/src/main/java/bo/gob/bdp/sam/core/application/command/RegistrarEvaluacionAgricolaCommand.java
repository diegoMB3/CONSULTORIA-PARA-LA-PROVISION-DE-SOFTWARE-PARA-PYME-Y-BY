package bo.gob.bdp.sam.core.application.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@AllArgsConstructor
public class RegistrarEvaluacionAgricolaCommand {
    @TargetAggregateIdentifier
    private final String clienteId;
    private final String tipoCultivo;
    private final double superficieHectareas;
    private final double costosInsumos;
    private final double costosManoObra;
    private final double ingresosVenta;
    private final int ciclosProductivos;
}
