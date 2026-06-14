package bo.gob.bdp.sam.core.application.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrarEvaluacionProduccionCommand {

    @TargetAggregateIdentifier
    private String evaluacionId;

    private String clienteId;
    private double ingresos;
    private double costosVariables;
    private double costosFijos;
    private double utilidadNeta;

    public double getIngresosVentas() {
        return ingresos;
    }

    public void setIngresosVentas(double ingresosVentas) {
        this.ingresos = ingresosVentas;
    }
}