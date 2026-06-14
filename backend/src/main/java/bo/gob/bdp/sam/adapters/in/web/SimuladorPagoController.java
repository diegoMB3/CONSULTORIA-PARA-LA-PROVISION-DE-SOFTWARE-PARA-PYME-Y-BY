package bo.gob.bdp.sam.adapters.in.web;

import bo.gob.bdp.sam.core.application.command.GenerarPlanPagoCommand;
import bo.gob.bdp.sam.core.domain.event.PlanPagoGeneradoEvent.CuotaDetalle;
import bo.gob.bdp.sam.core.domain.util.AmortizacionUtil;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/simulador-pagos")
@CrossOrigin(origins = "*")
public class SimuladorPagoController {

    private final CommandGateway commandGateway;

    public SimuladorPagoController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping("/generar")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> generarPlan(@RequestBody Map<String, Object> request) {
        String clienteId = (String) request.getOrDefault("clienteId", "SIN_CLIENTE");
        double monto = Double.parseDouble(request.getOrDefault("monto", "0").toString());
        double tasaAnual = Double.parseDouble(request.getOrDefault("tasaAnual", "0").toString());
        int plazoMeses = Integer.parseInt(request.getOrDefault("plazoMeses", "1").toString());

        GenerarPlanPagoCommand cmd = new GenerarPlanPagoCommand(clienteId, monto, tasaAnual, plazoMeses);

        return commandGateway.send(cmd)
                .thenApply(result -> {
                    List<CuotaDetalle> cronograma = AmortizacionUtil.generarCronogramaFrances(monto, tasaAnual, plazoMeses);
                    double cuota = cronograma.isEmpty() ? 0 : cronograma.get(0).getTotal();

                    Map<String, Object> response = new LinkedHashMap<>();
                    response.put("clienteId", clienteId);
                    response.put("monto", monto);
                    response.put("tasaAnual", tasaAnual);
                    response.put("plazoMeses", plazoMeses);
                    response.put("cuotaMensual", cuota);
                    response.put("cronograma", cronograma);
                    response.put("timestamp", java.time.Instant.now().toString());

                    return ResponseEntity.ok(response);
                })
                .exceptionally(ex -> ResponseEntity.badRequest().body(Map.of(
                        "error", "Error al generar plan de pagos",
                        "detalle", ex.getMessage()
                )));
    }
}
