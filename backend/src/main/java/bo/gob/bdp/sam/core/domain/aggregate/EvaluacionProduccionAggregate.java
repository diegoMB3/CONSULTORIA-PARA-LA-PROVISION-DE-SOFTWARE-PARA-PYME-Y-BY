package bo.gob.bdp.sam.core.domain.aggregate;

import bo.gob.bdp.sam.core.application.command.RegistrarEvaluacionProduccionCommand;
import bo.gob.bdp.sam.core.domain.event.EvaluacionProduccionRegistradaEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import lombok.NoArgsConstructor;

@Aggregate
@NoArgsConstructor
public class EvaluacionProduccionAggregate {

    @AggregateIdentifier
    private String clienteId;
    private double ingresosVentas;
    private double costosFijos;
    private double costosVariables;
    private double utilidadNeta;
    private double margenUtilidad;

    @CommandHandler
    public EvaluacionProduccionAggregate(RegistrarEvaluacionProduccionCommand cmd) {
        if (cmd.getClienteId() == null || cmd.getClienteId().trim().isEmpty()) {
            throw new IllegalArgumentException("❌ Error: El código de cliente es obligatorio.");
        }
        if (cmd.getIngresosVentas() <= 0) {
            throw new IllegalArgumentException("❌ Error Industrial: Los ingresos de ventas deben ser mayores a cero.");
        }
        if (cmd.getCostosFijos() < 0 || cmd.getCostosVariables() < 0) {
            throw new IllegalArgumentException("❌ Error Financiero: Los costos no pueden ser valores negativos.");
        }

        double costosTotales = cmd.getCostosFijos() + cmd.getCostosVariables();
        double calculoUtilidadNeta = cmd.getIngresosVentas() - costosTotales;
        
        if (calculoUtilidadNeta <= 0) {
            throw new IllegalArgumentException("❌ Evaluación Denegada: La utilidad neta calculada es negativa o cero.");
        }

        double calculoMargen = (calculoUtilidadNeta / cmd.getIngresosVentas()) * 100;
        
        if (calculoMargen < 10.0) {
            throw new IllegalArgumentException("❌ Riesgo de Operación: El margen de eficiencia industrial es inferior al 10% estipulado.");
        }

        apply(new EvaluacionProduccionRegistradaEvent(
                cmd.getClienteId(),
                cmd.getIngresosVentas(),
                cmd.getCostosFijos(),
                cmd.getCostosVariables(),
                calculoUtilidadNeta,
                calculoMargen
        ));
    }

    @EventSourcingHandler
    public void on(EvaluacionProduccionRegistradaEvent event) {
        this.clienteId = event.getClienteId();
        this.ingresosVentas = event.getIngresosVentas();
        this.costosFijos = event.getCostosFijos();
        this.costosVariables = event.getCostosVariables();
        this.utilidadNeta = event.getUtilidadNeta();
        this.margenUtilidad = event.getMargenUtilidad();
    }
}