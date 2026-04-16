package bo.gob.bdp.sam.core.domain.event;

import lombok.Value;

import java.util.Map;

@Value
public class BalanceActualizadoEvent {
    private final String clienteId;
    private final Map<String, Double> cuentas;
    private final double activoCorriente;
    private final double activoNoCorriente;
    private final double pasivoCorriente;
    private final double pasivoNoCorriente;
    private final double patrimonio;
    private final boolean balanceCuadrado;
    private final double diferencia;
    private final String timestamp;
}
