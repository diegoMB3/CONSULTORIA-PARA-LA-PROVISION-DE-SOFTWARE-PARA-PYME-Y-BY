package bo.gob.bdp.sam.core.domain.aggregate;

import bo.gob.bdp.sam.core.application.command.GenerarPlanPagoCommand;
import bo.gob.bdp.sam.core.domain.event.PlanPagoGeneradoEvent;
import bo.gob.bdp.sam.core.domain.event.PlanPagoGeneradoEvent.CuotaDetalle;
import bo.gob.bdp.sam.core.domain.util.AmortizacionUtil;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateCreationPolicy;
import org.axonframework.modelling.command.CreationPolicy;
import org.axonframework.spring.stereotype.Aggregate;

import java.time.Instant;
import java.util.List;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class SimuladorPagoAggregate {

    @AggregateIdentifier
    private String clienteId;
    private double monto;
    private double tasaAnual;
    private int plazoMeses;
    private double cuotaMensual;
    private List<CuotaDetalle> cronograma;

    protected SimuladorPagoAggregate() {}

    @CommandHandler
    @CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING)
    public void handle(GenerarPlanPagoCommand command) {
        if (command.getClienteId() == null || command.getClienteId().isEmpty()) {
            throw new IllegalArgumentException("Cliente no encontrado para simular pago");
        }
        if (command.getMonto() <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que 0");
        }
        if (command.getTasaAnual() < 0 || command.getTasaAnual() > 100) {
            throw new IllegalArgumentException("La tasa anual debe estar entre 0% y 100%");
        }
        if (command.getPlazoMeses() < 1 || command.getPlazoMeses() > 360) {
            throw new IllegalArgumentException("El plazo debe estar entre 1 y 360 meses");
        }

        List<CuotaDetalle> plan = AmortizacionUtil.generarCronogramaFrances(
                command.getMonto(), command.getTasaAnual(), command.getPlazoMeses());

        double cuotaMensualCalculada = plan.isEmpty() ? 0 : plan.get(0).getTotal();

        apply(new PlanPagoGeneradoEvent(
                command.getClienteId(),
                command.getMonto(),
                command.getTasaAnual(),
                command.getPlazoMeses(),
                cuotaMensualCalculada,
                plan,
                Instant.now().toString()
        ));
    }

    @EventSourcingHandler
    public void on(PlanPagoGeneradoEvent event) {
        this.clienteId = event.getClienteId();
        this.monto = event.getMonto();
        this.tasaAnual = event.getTasaAnual();
        this.plazoMeses = event.getPlazoMeses();
        this.cuotaMensual = event.getCuotaMensual();
        this.cronograma = event.getCronograma();
    }
}
