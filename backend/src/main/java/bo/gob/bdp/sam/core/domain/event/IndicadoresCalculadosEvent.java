package bo.gob.bdp.sam.core.domain.event;

import lombok.Value;

@Value
public class IndicadoresCalculadosEvent {
    private final String clienteId;
    private final double liquidezCorriente;
    private final double endeudamiento;
    private final double solvencia;
    private final double roa;
    private final double roe;
    private final String timestamp;
}
