package bo.gob.bdp.sam.core.domain.aggregate;

import bo.gob.bdp.sam.core.application.command.RegistrarEvaluacionProduccionCommand;
import bo.gob.bdp.sam.core.domain.event.EvaluacionProduccionRegistradaEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateCreationPolicy;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.CreationPolicy;
import org.axonframework.spring.stereotype.Aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class EvaluacionProduccionAggregate {

    @AggregateIdentifier
    private String clienteId;
    private String evaluacionId;
    private double ingresos;
    private double costosVariables;
    private double costosFijos;
    private double utilidadNeta;
    private double margenUtilidad;

    protected EvaluacionProduccionAggregate() {
        // Required by Axon for event sourcing reconstruction.
    }

    @CommandHandler
    @CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING)
    public void handle(RegistrarEvaluacionProduccionCommand cmd) {
        if (cmd.getClienteId() == null || cmd.getClienteId().trim().isEmpty()) {
            throw new IllegalArgumentException("El cliente es obligatorio para registrar la evaluación de producción.");
        }
        if (cmd.getIngresos() <= 0) {
            throw new IllegalArgumentException("Los ingresos deben ser mayores que cero.");
        }
        if (cmd.getCostosFijos() < 0 || cmd.getCostosVariables() < 0) {
            throw new IllegalArgumentException("Los costos no pueden ser negativos.");
        }

        double calculoUtilidadNeta = cmd.getIngresos() - (cmd.getCostosFijos() + cmd.getCostosVariables());
        if (calculoUtilidadNeta <= 0) {
            throw new IllegalArgumentException("La utilidad neta calculada no puede ser negativa o cero.");
        }

        double calculoMargen = (calculoUtilidadNeta / cmd.getIngresos()) * 100;
        if (calculoMargen < 10.0) {
            throw new IllegalArgumentException("El margen de utilidad debe ser igual o mayor al 10%.");
        }

        String eventoId = cmd.getEvaluacionId();
        if (eventoId == null || eventoId.isBlank()) {
            eventoId = java.util.UUID.randomUUID().toString();
        }

        apply(new EvaluacionProduccionRegistradaEvent(
                eventoId,
                cmd.getClienteId(),
                cmd.getIngresos(),
                cmd.getCostosVariables(),
                cmd.getCostosFijos(),
                calculoUtilidadNeta,
                calculoMargen
        ));
    }

    @EventSourcingHandler
    public void on(EvaluacionProduccionRegistradaEvent event) {
        this.clienteId = event.getClienteId();
        this.evaluacionId = event.getEvaluacionId();
        this.ingresos = event.getIngresos();
        this.costosVariables = event.getCostosVariables();
        this.costosFijos = event.getCostosFijos();
        this.utilidadNeta = event.getUtilidadNeta();
        this.margenUtilidad = event.getMargenUtilidad();
    }
}
