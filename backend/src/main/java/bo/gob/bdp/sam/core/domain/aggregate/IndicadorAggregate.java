package bo.gob.bdp.sam.core.domain.aggregate;

import bo.gob.bdp.sam.core.application.command.CalcularIndicadoresCommand;
import bo.gob.bdp.sam.core.domain.event.IndicadoresCalculadosEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import java.time.Instant;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class IndicadorAggregate {

    @AggregateIdentifier
    private String clienteId;
    private double liquidezCorriente;
    private double endeudamiento;
    private double solvencia;
    private double roa;
    private double roe;

    protected IndicadorAggregate() {}

    @CommandHandler
    public IndicadorAggregate(CalcularIndicadoresCommand command) {
        if (command.getClienteId() == null || command.getClienteId().isEmpty()) {
            throw new IllegalArgumentException("Cliente no encontrado");
        }

        apply(new IndicadoresCalculadosEvent(
            command.getClienteId(),
            0.0, 0.0, 0.0, 0.0, 0.0,
            Instant.now().toString()
        ));
    }

    @EventSourcingHandler
    public void on(IndicadoresCalculadosEvent event) {
        this.clienteId = event.getClienteId();
        this.liquidezCorriente = event.getLiquidezCorriente();
        this.endeudamiento = event.getEndeudamiento();
        this.solvencia = event.getSolvencia();
        this.roa = event.getRoa();
        this.roe = event.getRoe();
    }
}
