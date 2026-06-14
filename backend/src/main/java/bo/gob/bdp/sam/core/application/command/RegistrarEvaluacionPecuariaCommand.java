package bo.gob.bdp.sam.core.application.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@AllArgsConstructor
public class RegistrarEvaluacionPecuariaCommand {
    @TargetAggregateIdentifier
    private final String clienteId;
    private final String tipoGanado;
    private final int cantidadInicial;
    private final double tasaNatalidad;
    private final double tasaMortalidad;
    private final double precioVentaUnitario;
    private final double costoMantenimientoUnitario;
    private final String escenario;
}
