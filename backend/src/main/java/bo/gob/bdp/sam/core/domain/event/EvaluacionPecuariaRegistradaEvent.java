package bo.gob.bdp.sam.core.domain.event;

import lombok.Value;

@Value
public class EvaluacionPecuariaRegistradaEvent {
    private final String clienteId;
    private final String tipoGanado;
    private final int cantidadInicial;
    private final double tasaNatalidad;
    private final double tasaMortalidad;
    private final double precioVentaUnitario;
    private final double costoMantenimientoUnitario;
    private final String escenario;
    private final int animalesProyectados;
    private final double ingresoProyectado;
    private final double costoProyectado;
    private final double rentabilidadProyectada;
    private final String timestamp;
}
