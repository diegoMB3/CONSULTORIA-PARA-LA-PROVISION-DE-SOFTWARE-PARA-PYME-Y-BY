package bo.gob.bdp.sam.core.domain.aggregate;

import bo.gob.bdp.sam.core.application.command.RegistrarEvaluacionPecuariaCommand;
import bo.gob.bdp.sam.core.domain.event.EvaluacionPecuariaRegistradaEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateCreationPolicy;
import org.axonframework.modelling.command.CreationPolicy;
import org.axonframework.spring.stereotype.Aggregate;

import java.time.Instant;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class EvaluacionPecuariaAggregate {

    @AggregateIdentifier
    private String clienteId;
    private String tipoGanado;
    private int cantidadInicial;
    private int animalesProyectados;
    private double rentabilidadProyectada;

    protected EvaluacionPecuariaAggregate() {}

    @CommandHandler
    @CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING)
    public void handle(RegistrarEvaluacionPecuariaCommand command) {
        if (command.getClienteId() == null || command.getClienteId().isEmpty()) {
            throw new IllegalArgumentException("Cliente no encontrado para evaluación pecuaria");
        }
        if (command.getTipoGanado() == null || command.getTipoGanado().isEmpty()) {
            throw new IllegalArgumentException("Tipo de ganado es requerido");
        }
        if (command.getCantidadInicial() < 1 || command.getCantidadInicial() > 100000) {
            throw new IllegalArgumentException("La cantidad inicial debe estar entre 1 y 100000 animales");
        }
        if (command.getTasaNatalidad() < 0 || command.getTasaNatalidad() > 100) {
            throw new IllegalArgumentException("La tasa de natalidad debe estar entre 0% y 100%");
        }
        if (command.getTasaMortalidad() < 0 || command.getTasaMortalidad() > 100) {
            throw new IllegalArgumentException("La tasa de mortalidad debe estar entre 0% y 100%");
        }
        if (command.getPrecioVentaUnitario() < 0 || command.getCostoMantenimientoUnitario() < 0) {
            throw new IllegalArgumentException("Los precios e ingresos deben ser valores no negativos");
        }
        if (command.getEscenario() == null || command.getEscenario().isEmpty()) {
            throw new IllegalArgumentException("El escenario es requerido");
        }

        double tasaNeta = (command.getTasaNatalidad() - command.getTasaMortalidad()) / 100.0;
        int animalesProyectados = (int) Math.round(command.getCantidadInicial() * (1 + tasaNeta));
        if (animalesProyectados < 0) {
            animalesProyectados = 0;
        }

        double ingresoProyectado = animalesProyectados * command.getPrecioVentaUnitario();
        double costoProyectado = animalesProyectados * command.getCostoMantenimientoUnitario();
        double rentabilidadProyectada = ingresoProyectado - costoProyectado;

        apply(new EvaluacionPecuariaRegistradaEvent(
                command.getClienteId(),
                command.getTipoGanado(),
                command.getCantidadInicial(),
                command.getTasaNatalidad(),
                command.getTasaMortalidad(),
                command.getPrecioVentaUnitario(),
                command.getCostoMantenimientoUnitario(),
                command.getEscenario(),
                animalesProyectados,
                ingresoProyectado,
                costoProyectado,
                rentabilidadProyectada,
                Instant.now().toString()
        ));
    }

    @EventSourcingHandler
    public void on(EvaluacionPecuariaRegistradaEvent event) {
        this.clienteId = event.getClienteId();
        this.tipoGanado = event.getTipoGanado();
        this.cantidadInicial = event.getCantidadInicial();
        this.animalesProyectados = event.getAnimalesProyectados();
        this.rentabilidadProyectada = event.getRentabilidadProyectada();
    }
}
