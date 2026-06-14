package bo.gob.bdp.sam.core.domain.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluacionProduccionRegistradaEvent {
    private String evaluacionId;
    private String clienteId;
    private double ingresos;
    private double costosVariables;
    private double costosFijos;
    private double utilidadNeta;
    private double margenUtilidad;
}