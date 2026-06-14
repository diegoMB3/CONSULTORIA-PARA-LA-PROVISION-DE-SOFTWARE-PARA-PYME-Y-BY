package bo.gob.bdp.sam.core.domain.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluacionProduccionRegistradaEvent {
    
    private String clienteId;
    private double ingresosVentas;
    private double costosFijos;
    private double costosVariables;
    private double utilidadNeta;
    private double margenUtilidad;
}