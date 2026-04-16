package bo.gob.bdp.sam.core.domain.aggregate;

import bo.gob.bdp.sam.core.application.command.ActualizarBalanceCommand;
import bo.gob.bdp.sam.core.domain.event.BalanceActualizadoEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import java.time.Instant;
import java.util.Map;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

/**
 * Agregado que gestiona el Volteo de Balances.
 * Incluye validación estricta de descuadre ±10 Bs (RNF-02).
 */
@Aggregate
public class BalanceAggregate {

    @AggregateIdentifier
    private String clienteId;
    private Map<String, Double> cuentas;
    private double activoCorriente;
    private double activoNoCorriente;
    private double pasivoCorriente;
    private double pasivoNoCorriente;
    private double patrimonio;
    private boolean balanceCuadrado;
    private double diferencia;
    private String ultimaActualizacion;

    protected BalanceAggregate() {}

    /**
     * Command Handler: Actualiza el balance completo o una celda específica.
     */
    @CommandHandler
    public BalanceAggregate(ActualizarBalanceCommand command) {
        if (command.getClienteId() == null || command.getClienteId().isEmpty()) {
            throw new IllegalArgumentException("Cliente no encontrado - No se puede procesar balance");
        }

        Map<String, Double> cuentasRecibidas = command.getCuentas();

        double actCte = calcularSubtotalActivoCorriente(cuentasRecibidas);
        double actNoCte = calcularSubtotalActivoNoCorriente(cuentasRecibidas);
        double pasCte = calcularSubtotalPasivoCorriente(cuentasRecibidas);
        double pasNoCte = calcularSubtotalPasivoNoCorriente(cuentasRecibidas);
        double pat = calcularSubtotalPatrimonio(cuentasRecibidas);

        double activoTotal = actCte + actNoCte;
        double pasivoTotal = pasCte + pasNoCte;

        double diferenciaContable = Math.abs(activoTotal - (pasivoTotal + pat));
        boolean cuadrado = diferenciaContable <= 10.0;

        if (!cuadrado && command.isValidacionFinal()) {
            throw new IllegalArgumentException(
                    String.format("❌ BALANCE DESCUIDADO. Diferencia: %.2f Bs (Máximo permitido: ±10 Bs). " +
                                  "Activo: %.2f | Pasivo+Patrimonio: %.2f",
                                  diferenciaContable, activoTotal, pasivoTotal + pat)
            );
        }

        apply(new BalanceActualizadoEvent(
                command.getClienteId(),
                cuentasRecibidas,
                actCte, actNoCte, pasCte, pasNoCte, pat,
                cuadrado,
                diferenciaContable,
                Instant.now().toString()
        ));
    }

    @EventSourcingHandler
    public void on(BalanceActualizadoEvent event) {
        this.clienteId = event.getClienteId();
        this.cuentas = event.getCuentas();
        this.activoCorriente = event.getActivoCorriente();
        this.activoNoCorriente = event.getActivoNoCorriente();
        this.pasivoCorriente = event.getPasivoCorriente();
        this.pasivoNoCorriente = event.getPasivoNoCorriente();
        this.patrimonio = event.getPatrimonio();
        this.balanceCuadrado = event.isBalanceCuadrado();
        this.diferencia = event.getDiferencia();
        this.ultimaActualizacion = event.getTimestamp();
    }

    private double calcularSubtotalActivoCorriente(Map<String, Double> cuentas) {
        return cuentas.getOrDefault("CAJA_BANCOS", 0.0) +
               cuentas.getOrDefault("CUENTAS_COBRAR", 0.0) +
               cuentas.getOrDefault("INVENTARIOS", 0.0);
    }

    private double calcularSubtotalActivoNoCorriente(Map<String, Double> cuentas) {
        return cuentas.getOrDefault("TERRENOS", 0.0) +
               cuentas.getOrDefault("EDIFICIOS", 0.0) +
               cuentas.getOrDefault("MAQUINARIA", 0.0) +
               cuentas.getOrDefault("VEHICULOS", 0.0);
    }

    private double calcularSubtotalPasivoCorriente(Map<String, Double> cuentas) {
        return cuentas.getOrDefault("PROVEEDORES", 0.0) +
               cuentas.getOrDefault("IMPUESTOS_POR_PAGAR", 0.0) +
               cuentas.getOrDefault("SUELDOS_POR_PAGAR", 0.0);
    }

    private double calcularSubtotalPasivoNoCorriente(Map<String, Double> cuentas) {
        return cuentas.getOrDefault("PRESTAMOS_BANCARIOS_LP", 0.0);
    }

    private double calcularSubtotalPatrimonio(Map<String, Double> cuentas) {
        return cuentas.getOrDefault("CAPITAL_SOCIAL", 0.0) +
               cuentas.getOrDefault("RESERVAS", 0.0) +
               cuentas.getOrDefault("RESULTADOS_ACUMULADOS", 0.0) +
               cuentas.getOrDefault("RESULTADO_EJERCICIO", 0.0);
    }
}
