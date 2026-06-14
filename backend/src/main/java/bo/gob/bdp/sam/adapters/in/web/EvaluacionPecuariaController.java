package bo.gob.bdp.sam.adapters.in.web;

import bo.gob.bdp.sam.core.application.command.RegistrarEvaluacionPecuariaCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/evaluacion-pecuaria")
@CrossOrigin(origins = "*")
public class EvaluacionPecuariaController {

    private final CommandGateway commandGateway;

    public EvaluacionPecuariaController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping("/registrar")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> registrarEvaluacion(@RequestBody Map<String, Object> request) {
        String clienteId = (String) request.getOrDefault("clienteId", "SIN_CLIENTE");
        String tipoGanado = (String) request.getOrDefault("tipoGanado", "");
        int cantidadInicial = Integer.parseInt(request.getOrDefault("cantidadInicial", "0").toString());
        double tasaNatalidad = Double.parseDouble(request.getOrDefault("tasaNatalidad", "0").toString());
        double tasaMortalidad = Double.parseDouble(request.getOrDefault("tasaMortalidad", "0").toString());
        double precioVenta = Double.parseDouble(request.getOrDefault("precioVentaUnitario", "0").toString());
        double costoMantenimiento = Double.parseDouble(request.getOrDefault("costoMantenimientoUnitario", "0").toString());
        String escenario = (String) request.getOrDefault("escenario", "CRIA");

        RegistrarEvaluacionPecuariaCommand cmd = new RegistrarEvaluacionPecuariaCommand(
                clienteId, tipoGanado, cantidadInicial, tasaNatalidad, tasaMortalidad,
                precioVenta, costoMantenimiento, escenario
        );

        return commandGateway.send(cmd)
                .thenApply(result -> {
                    double tasaNeta = (tasaNatalidad - tasaMortalidad) / 100.0;
                    int animalesProy = (int) Math.round(cantidadInicial * (1 + tasaNeta));
                    if (animalesProy < 0) {
                        animalesProy = 0;
                    }
                    double ingreso = animalesProy * precioVenta;
                    double costo = animalesProy * costoMantenimiento;
                    double rentabilidad = ingreso - costo;

                    Map<String, Object> response = new LinkedHashMap<>();
                    response.put("clienteId", clienteId);
                    response.put("tipoGanado", tipoGanado);
                    response.put("cantidadInicial", cantidadInicial);
                    response.put("tasaNatalidad", tasaNatalidad);
                    response.put("tasaMortalidad", tasaMortalidad);
                    response.put("tasaNeta", Math.round(tasaNeta * 10000.0) / 100.0);
                    response.put("animalesProyectados", animalesProy);
                    response.put("precioVentaUnitario", precioVenta);
                    response.put("costoMantenimientoUnitario", costoMantenimiento);
                    response.put("ingresoProyectado", ingreso);
                    response.put("costoProyectado", costo);
                    response.put("rentabilidadProyectada", rentabilidad);
                    response.put("escenario", escenario);
                    response.put("mensaje", rentabilidad >= 0 ? "✅ Proyección rentable" : "❌ Proyección no rentable");

                    return ResponseEntity.ok(response);
                })
                .exceptionally(ex -> ResponseEntity.badRequest().body(Map.of(
                        "error", "Error al registrar evaluación pecuaria",
                        "detalle", ex.getMessage()
                )));
    }
}
