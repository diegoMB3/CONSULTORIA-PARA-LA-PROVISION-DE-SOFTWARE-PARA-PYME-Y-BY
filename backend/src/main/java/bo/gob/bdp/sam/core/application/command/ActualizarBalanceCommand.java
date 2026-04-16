package bo.gob.bdp.sam.core.application.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.Map;

@Data
@AllArgsConstructor
public class ActualizarBalanceCommand {
    @TargetAggregateIdentifier
    private final String clienteId;
    private final Map<String, Double> cuentas;
    private final boolean validacionFinal;  // true = bloquear si descuadrado, false = solo calcular
}
