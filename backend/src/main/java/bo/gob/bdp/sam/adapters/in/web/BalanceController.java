package bo.gob.bdp.sam.adapters.in.web;

import bo.gob.bdp.sam.core.application.command.ActualizarBalanceCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/balances")
@CrossOrigin(origins = "*")
public class BalanceController {

    private final CommandGateway commandGateway;
    private final Map<String, Map<String, Double>> balancesPorCliente = new HashMap<>();

    public BalanceController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<Map<String, Object>> obtenerBalance(@PathVariable String clienteId) {
        Map<String, Double> cuentas = balancesPorCliente.getOrDefault(clienteId, getBalanceInicial());

        double actCte = cuentas.getOrDefault("CAJA_BANCOS", 0.0) +
                        cuentas.getOrDefault("CUENTAS_COBRAR", 0.0) +
                        cuentas.getOrDefault("INVENTARIOS", 0.0);
        double actNoCte = cuentas.getOrDefault("TERRENOS", 0.0) +
                          cuentas.getOrDefault("EDIFICIOS", 0.0) +
                          cuentas.getOrDefault("MAQUINARIA", 0.0) +
                          cuentas.getOrDefault("VEHICULOS", 0.0);
        double pasCte = cuentas.getOrDefault("PROVEEDORES", 0.0) +
                        cuentas.getOrDefault("IMPUESTOS_POR_PAGAR", 0.0) +
                        cuentas.getOrDefault("SUELDOS_POR_PAGAR", 0.0);
        double pasNoCte = cuentas.getOrDefault("PRESTAMOS_BANCARIOS_LP", 0.0);
        double pat = cuentas.getOrDefault("CAPITAL_SOCIAL", 0.0) +
                     cuentas.getOrDefault("RESERVAS", 0.0) +
                     cuentas.getOrDefault("RESULTADOS_ACUMULADOS", 0.0) +
                     cuentas.getOrDefault("RESULTADO_EJERCICIO", 0.0);

        double activoTotal = actCte + actNoCte;
        double pasivoTotal = pasCte + pasNoCte;
        double diferencia = Math.abs(activoTotal - (pasivoTotal + pat));
        boolean cuadrado = diferencia <= 10.0;

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("clienteId", clienteId);
        response.put("cuentas", cuentas);
        response.put("activoCorriente", actCte);
        response.put("activoNoCorriente", actNoCte);
        response.put("activoTotal", activoTotal);
        response.put("pasivoCorriente", pasCte);
        response.put("pasivoNoCorriente", pasNoCte);
        response.put("pasivoTotal", pasivoTotal);
        response.put("patrimonio", pat);
        response.put("pasivoPatrimonioTotal", pasivoTotal + pat);
        response.put("diferencia", diferencia);
        response.put("balanceCuadrado", cuadrado);
        response.put("mensaje", cuadrado ? "✅ Balance Cuadrado" : String.format("❌ Descuadre: %.2f Bs (Máximo ±10 Bs)", diferencia));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{clienteId}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> actualizarBalance(
            @PathVariable String clienteId,
            @RequestBody Map<String, Double> cuentas) {

        balancesPorCliente.put(clienteId, cuentas);
        ActualizarBalanceCommand cmd = new ActualizarBalanceCommand(clienteId, cuentas, false);

        return commandGateway.send(cmd)
                .thenApply(result -> {
                    double actCte = cuentas.getOrDefault("CAJA_BANCOS", 0.0) +
                                    cuentas.getOrDefault("CUENTAS_COBRAR", 0.0) +
                                    cuentas.getOrDefault("INVENTARIOS", 0.0);
                    double actNoCte = cuentas.getOrDefault("TERRENOS", 0.0) +
                                      cuentas.getOrDefault("EDIFICIOS", 0.0) +
                                      cuentas.getOrDefault("MAQUINARIA", 0.0) +
                                      cuentas.getOrDefault("VEHICULOS", 0.0);
                    double pasCte = cuentas.getOrDefault("PROVEEDORES", 0.0) +
                                    cuentas.getOrDefault("IMPUESTOS_POR_PAGAR", 0.0) +
                                    cuentas.getOrDefault("SUELDOS_POR_PAGAR", 0.0);
                    double pasNoCte = cuentas.getOrDefault("PRESTAMOS_BANCARIOS_LP", 0.0);
                    double pat = cuentas.getOrDefault("CAPITAL_SOCIAL", 0.0) +
                                 cuentas.getOrDefault("RESERVAS", 0.0) +
                                 cuentas.getOrDefault("RESULTADOS_ACUMULADOS", 0.0) +
                                 cuentas.getOrDefault("RESULTADO_EJERCICIO", 0.0);

                    double activoTotal = actCte + actNoCte;
                    double pasivoPatTotal = pasCte + pasNoCte + pat;
                    double diferencia = Math.abs(activoTotal - pasivoPatTotal);
                    boolean cuadrado = diferencia <= 10.0;

                    Map<String, Object> response = new LinkedHashMap<>();
                    response.put("mensaje", "Balance actualizado");
                    response.put("activoTotal", activoTotal);
                    response.put("pasivoPatrimonioTotal", pasivoPatTotal);
                    response.put("diferencia", diferencia);
                    response.put("balanceCuadrado", cuadrado);
                    response.put("timestamp", java.time.Instant.now().toString());
                    return ResponseEntity.ok(response);
                })
                .exceptionally(ex -> ResponseEntity.badRequest().body(Map.of(
                        "error", "Error en balance",
                        "detalle", ex.getMessage()
                )));
    }

    @PostMapping("/{clienteId}/validar")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> validarBalanceFinal(
            @PathVariable String clienteId,
            @RequestBody Map<String, Double> cuentas) {

        ActualizarBalanceCommand cmd = new ActualizarBalanceCommand(clienteId, cuentas, true);

        return commandGateway.send(cmd)
                .thenApply(result -> {
                    Map<String, Object> response = new LinkedHashMap<>();
                    response.put("mensaje", "✅ Balance validado correctamente. Puede continuar a Indicadores Financieros (RF-03.2)");
                    response.put("validacion", "APROBADA");
                    return ResponseEntity.ok(response);
                })
                .exceptionally(ex -> {
                    Map<String, Object> errorResponse = new LinkedHashMap<>();
                    errorResponse.put("error", "❌ BALANCE RECHAZADO");
                    errorResponse.put("detalle", ex.getMessage());
                    errorResponse.put("validacion", "RECHAZADA");
                    return ResponseEntity.badRequest().body(errorResponse);
                });
    }

    private Map<String, Double> getBalanceInicial() {
        Map<String, Double> inicial = new LinkedHashMap<>();
        inicial.put("CAJA_BANCOS", 0.0);
        inicial.put("CUENTAS_COBRAR", 0.0);
        inicial.put("INVENTARIOS", 0.0);
        inicial.put("TERRENOS", 0.0);
        inicial.put("EDIFICIOS", 0.0);
        inicial.put("MAQUINARIA", 0.0);
        inicial.put("VEHICULOS", 0.0);
        inicial.put("PROVEEDORES", 0.0);
        inicial.put("IMPUESTOS_POR_PAGAR", 0.0);
        inicial.put("SUELDOS_POR_PAGAR", 0.0);
        inicial.put("PRESTAMOS_BANCARIOS_LP", 0.0);
        inicial.put("CAPITAL_SOCIAL", 0.0);
        inicial.put("RESERVAS", 0.0);
        inicial.put("RESULTADOS_ACUMULADOS", 0.0);
        inicial.put("RESULTADO_EJERCICIO", 0.0);
        return inicial;
    }
}
