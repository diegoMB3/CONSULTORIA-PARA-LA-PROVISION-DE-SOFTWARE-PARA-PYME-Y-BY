package bo.gob.bdp.sam.core.application.event;

import lombok.Value;

@Value
public class EvaluacionProduccionRegistradaEvent {
    String evaluacionId;
    String clienteId;
    double ingresos;
    double costosVariables;
    double costosFijos;
    double utilidadNeta;
}