package bo.gob.bdp.sam.core.domain.event;

import lombok.Value;

@Value
public class EvaluacionAgricolaRegistradaEvent {
    private final String clienteId;
    private final String tipoCultivo;
    private final double superficieHectareas;
    private final double costosInsumos;
    private final double costosManoObra;
    private final double ingresosVenta;
    private final int ciclosProductivos;
    private final double costoTotal;
    private final double ingresoTotal;
    private final double rentabilidad;
    private final String timestamp;
}
