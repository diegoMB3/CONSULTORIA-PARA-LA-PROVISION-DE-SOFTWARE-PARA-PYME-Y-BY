package bo.gob.bdp.sam.adapters.in.web;

import bo.gob.bdp.sam.core.application.command.RegistrarClienteCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    private final CommandGateway commandGateway;
    
    // Simulación de Base de Datos de Lectura (Para validar duplicados rápidamente)
    // En producción esto iría en una tabla PostgreSQL separada (Query Side)
    private final Map<String, String> clientesExistentes = new HashMap<>();

    public ClienteController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping("/registrar")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> registrarCliente(@RequestBody RegistrarClienteRequest request) {
        // VALIDACIÓN DE DUPLICADOS (RF-02.1: Evitar registros duplicados)
        if (clientesExistentes.containsKey(request.getNumeroDocumento())) {
            throw new IllegalArgumentException("ERROR: El cliente con CI/NIT " + request.getNumeroDocumento() + " ya existe en el sistema.");
        }
        
        // Marcar como existente (Simulación)
        clientesExistentes.put(request.getNumeroDocumento(), request.getNombreCompleto());

        // Enviar Comando CQRS
        RegistrarClienteCommand cmd = new RegistrarClienteCommand(
            request.getNumeroDocumento(),
            request.getNombreCompleto(),
            request.getEmail(),
            request.getTelefono()
        );

        return commandGateway.send(cmd)
                .thenApply(result -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("mensaje", "Cliente registrado exitosamente");
                    response.put("expedienteId", result.toString());
                    response.put("clienteId", result.toString());
                    response.put("timestamp", java.time.Instant.now().toString());
                    return ResponseEntity.ok(response);
                })
                .exceptionally(ex -> {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("error", "Error de validación");
                    errorResponse.put("detalle", ex.getMessage());
                    return ResponseEntity.badRequest().body(errorResponse);
                });
    }
}

// Clase interna para el Request Body
class RegistrarClienteRequest {
    private String nombreCompleto;
    private String numeroDocumento;
    private String email;
    private String telefono;
    // Getters y Setters (Lombok no está en este snippet, los ponemos manual)
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public String getNumeroDocumento() { return numeroDocumento; }
    public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}
