package bo.gob.bdp.sam.adapters.in.web;

import bo.gob.bdp.sam.core.application.command.RegistrarEvaluacionProduccionCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.Data;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/evaluacion-produccion")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class EvaluacionProduccionController {

    private final CommandGateway commandGateway;

    @PostMapping
    public CompletableFuture<String> registrarEvaluacion(@RequestBody EvaluacionProdRequest request) {
        String uuid = UUID.randomUUID().toString();
        
        RegistrarEvaluacionProduccionCommand command = new RegistrarEvaluacionProduccionCommand(
                uuid,
                request.getClienteId() != null ? request.getClienteId() : "CLIENTE-ANONIMO",
                request.getIngresos(),
                request.getCostosVariables(),
                request.getCostosFijos(),
                request.getUtilidadNeta()
        );

        return commandGateway.send(command);
    }
}

@Data
class EvaluacionProdRequest {
    private String clienteId;
    private double ingresos;
    private double costosVariables;
    private double costosFijos;
    private double utilidadNeta;
}