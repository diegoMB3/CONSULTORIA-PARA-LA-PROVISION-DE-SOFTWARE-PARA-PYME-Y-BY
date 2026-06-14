package bo.gob.bdp.sam.core.domain.aggregate;

import bo.gob.bdp.sam.core.application.command.RegistrarEvaluacionAgricolaCommand;
import bo.gob.bdp.sam.core.domain.event.EvaluacionAgricolaRegistradaEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateCreationPolicy;
import org.axonframework.modelling.command.CreationPolicy;
import org.axonframework.spring.stereotype.Aggregate;

import java.time.Instant;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class EvaluacionAgricolaAggregate {

    @AggregateIdentifier
    private String clienteId;
    private String tipoCultivo;
    private double superficieHectareas;
    private double rentabilidad;

    protected EvaluacionAgricolaAggregate() {}

    @CommandHandler
    @CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING)
    public void handle(RegistrarEvaluacionAgricolaCommand command) {
        if (command.getClienteId() == null || command.getClienteId().isEmpty()) {
            throw new IllegalArgumentException("Cliente no encontrado para evaluación agrícola");
        }
        if (command.getTipoCultivo() == null || command.getTipoCultivo().isEmpty()) {
            throw new IllegalArgumentException("Tipo de cultivo es requerido");
        }
        if (command.getSuperficieHectareas() <= 0) {
            throw new IllegalArgumentException("La superficie en hectáreas debe ser mayor que 0");
        }
        if (command.getCostosInsumos() < 0 || command.getCostosManoObra() < 0 || command.getIngresosVenta() < 0) {
            throw new IllegalArgumentException("Los costos e ingresos deben ser valores no negativos");
        }
        if (command.getCiclosProductivos() < 1 || command.getCiclosProductivos() > 24) {
            throw new IllegalArgumentException("El número de ciclos productivos debe estar entre 1 y 24");
        }

        double costoTotal = (command.getCostosInsumos() + command.getCostosManoObra()) * command.getCiclosProductivos();
        double ingresoTotal = command.getIngresosVenta() * command.getCiclosProductivos();
        double rentabilidad = ingresoTotal - costoTotal;

        apply(new EvaluacionAgricolaRegistradaEvent(
                command.getClienteId(),
                command.getTipoCultivo(),
                command.getSuperficieHectareas(),
                command.getCostosInsumos(),
                command.getCostosManoObra(),
                command.getIngresosVenta(),
                command.getCiclosProductivos(),
                costoTotal,
                ingresoTotal,
                rentabilidad,
                Instant.now().toString()
        ));
    }

    @EventSourcingHandler
    public void on(EvaluacionAgricolaRegistradaEvent event) {
        this.clienteId = event.getClienteId();
        this.tipoCultivo = event.getTipoCultivo();
        this.superficieHectareas = event.getSuperficieHectareas();
        this.rentabilidad = event.getRentabilidad();
    }
}
