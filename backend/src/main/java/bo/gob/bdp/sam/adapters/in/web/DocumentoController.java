package bo.gob.bdp.sam.adapters.in.web;

import bo.gob.bdp.sam.core.application.command.ActualizarChecklistCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/documentos")
@CrossOrigin(origins = "*")
public class DocumentoController {

    private final CommandGateway commandGateway;
    
    // Simulación de Base de Datos de Lectura (Query Side)
    private final Map<String, Map<String, Boolean>> checklistPorCliente = new HashMap<>();

    public DocumentoController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @GetMapping("/checklist/{clienteId}")
    public ResponseEntity<Map<String, Object>> obtenerChecklist(@PathVariable String clienteId) {
        Map<String, Boolean> estado = checklistPorCliente.getOrDefault(clienteId, getDocumentosDefault());
        
        // Verificar si está completo (RF-02.2)
        boolean completo = estado.values().stream().allMatch(Boolean::booleanValue);
        
        Map<String, Object> response = new HashMap<>();
        response.put("clienteId", clienteId);
        response.put("documentos", estado);
        response.put("completo", completo);
        response.put("puedeAvanzar", completo);
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/checklist/{clienteId}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> actualizarChecklist(
            @PathVariable String clienteId,
            @RequestBody Map<String, Boolean> documentos) {
        
        // Guardar en Query Side simulado
        checklistPorCliente.put(clienteId, documentos);
        
        // Enviar Comando CQRS
        ActualizarChecklistCommand cmd = new ActualizarChecklistCommand(clienteId, documentos);
        
        return commandGateway.send(cmd)
                .thenApply(result -> {
                    // Verificar si está completo después de actualizar
                    boolean completo = documentos.values().stream().allMatch(Boolean::booleanValue);
                    Map<String, Object> response = new HashMap<>();
                    response.put("mensaje", "Checklist actualizado exitosamente");
                    response.put("checklistCompleto", completo);
                    response.put("puedeAvanzar", completo ? "HABILITADO" : "BLOQUEADO - Faltan documentos obligatorios");
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
    
    private Map<String, Boolean> getDocumentosDefault() {
        Map<String, Boolean> docs = new LinkedHashMap<>();
        docs.put("CI_FRENTE", false);
        docs.put("CI_REVERSO", false);
        docs.put("FACTURA_AGUA", false);
        docs.put("FACTURA_LUZ", false);
        docs.put("ESTADO_CUENTA", false);
        docs.put("AVAL_BANCARIO", false);
        docs.put("DECLARACION_JURADA", false);
        return docs;
    }
}