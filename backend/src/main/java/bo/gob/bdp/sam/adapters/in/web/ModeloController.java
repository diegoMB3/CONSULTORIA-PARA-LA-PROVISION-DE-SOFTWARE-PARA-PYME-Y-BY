package bo.gob.bdp.sam.adapters.in.web;

import bo.gob.bdp.sam.core.application.command.CargarModelosCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/modelos")
@CrossOrigin(origins = "*") // Permite que el Web Dashboard y la App se conecten
public class ModeloController {

    private final CommandGateway commandGateway;

    public ModeloController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping("/cargar")
    public CompletableFuture<ResponseEntity<String>> cargarModelos(@RequestParam String usuarioId) {
        // Envía el Comando al Bus (CQRS)
        return commandGateway.send(new CargarModelosCommand(usuarioId, "WEB"))
                .thenApply(result -> ResponseEntity.ok("Evento de Modelos Cargados Disparado. ID: " + result))
                .exceptionally(ex -> ResponseEntity.internalServerError().body("Error de Seguridad: " + ex.getMessage()));
    }
    
    // Endpoint simple para el menú Web (Simulado por ahora)
    @GetMapping("/menu")
    public ResponseEntity<List<String>> obtenerMenu() {
        // En una implementación real, esto iría al Query Side (Lectura)
        List<String> menu = List.of(
            "🌾 Evaluación Agrícola (RF-04.1)",
            "🐄 Evaluación Pecuaria (RF-05.1)",
            "🏭 Evaluación Producción (RF-06.1)"
        );
        return ResponseEntity.ok(menu);
    }
}
