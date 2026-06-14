package bo.gob.bdp.sam.adapters.out.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "evaluacion_produccion")
@Data
public class EvaluacionProduccionEntity {
    @Id
    private String evaluacionId;
    private String clienteId;
    private double ingresos;
    private double costosVariables;
    private double costosFijos;
    private double utilidadNeta;
}