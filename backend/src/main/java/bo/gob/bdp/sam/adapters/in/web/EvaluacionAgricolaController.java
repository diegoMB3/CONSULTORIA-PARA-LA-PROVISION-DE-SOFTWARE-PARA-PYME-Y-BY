package bo.gob.bdp.sam.adapters.in.web;

import bo.gob.bdp.sam.core.application.command.RegistrarEvaluacionAgricolaCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/evaluacion-agricola")
@CrossOrigin(origins = "*")
public class EvaluacionAgricolaController {

    private final CommandGateway commandGateway;

    public EvaluacionAgricolaController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping("/registrar")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> registrarEvaluacion(@RequestBody Map<String, Object> request) {
        String clienteId = (String) request.getOrDefault("clienteId", "SIN_CLIENTE");
        String tipoCultivo = (String) request.getOrDefault("tipoCultivo", "");
        double superficie = Double.parseDouble(request.getOrDefault("superficieHectareas", "0").toString());
        double costosInsumos = Double.parseDouble(request.getOrDefault("costosInsumos", "0").toString());
        double costosManoObra = Double.parseDouble(request.getOrDefault("costosManoObra", "0").toString());
        double ingresosVenta = Double.parseDouble(request.getOrDefault("ingresosVenta", "0").toString());
        int ciclos = Integer.parseInt(request.getOrDefault("ciclosProductivos", "1").toString());

        RegistrarEvaluacionAgricolaCommand cmd = new RegistrarEvaluacionAgricolaCommand(
                clienteId, tipoCultivo, superficie, costosInsumos, costosManoObra, ingresosVenta, ciclos
        );

        return commandGateway.send(cmd)
                .thenApply(result -> {
                    double costoTotal = (costosInsumos + costosManoObra) * ciclos;
                    double ingresoTotal = ingresosVenta * ciclos;
                    double rentabilidad = ingresoTotal - costoTotal;

                    Map<String, Object> response = new LinkedHashMap<>();
                    response.put("clienteId", clienteId);
                    response.put("tipoCultivo", tipoCultivo);
                    response.put("superficieHectareas", superficie);
                    response.put("costosInsumos", costosInsumos);
                    response.put("costosManoObra", costosManoObra);
                    response.put("ingresosVenta", ingresosVenta);
                    response.put("ciclosProductivos", ciclos);
                    response.put("costoTotal", costoTotal);
                    response.put("ingresoTotal", ingresoTotal);
                    response.put("rentabilidad", rentabilidad);
                    response.put("mensaje", rentabilidad >= 0 ? "✅ Cultivo rentable" : "❌ Cultivo no rentable");

                    return ResponseEntity.ok(response);
                })
                .exceptionally(ex -> ResponseEntity.badRequest().body(Map.of(
                        "error", "Error al registrar evaluación agrícola",
                        "detalle", ex.getMessage()
                )));
    }
}
