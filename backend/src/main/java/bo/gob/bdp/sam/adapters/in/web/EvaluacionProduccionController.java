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

    @PostMapping(value = {"", "/{clienteId}"})
    public CompletableFuture<String> registrarEvaluacion(
            @PathVariable(required = false) String clienteId,
            @RequestBody EvaluacionProdRequest request) {

        String resolvedClienteId = (clienteId != null && !clienteId.isBlank())
                ? clienteId
                : (request.getClienteId() != null && !request.getClienteId().isBlank())
                ? request.getClienteId()
                : "CLIENTE-ANONIMO";

        String evaluacionId = UUID.randomUUID().toString();
        RegistrarEvaluacionProduccionCommand command = new RegistrarEvaluacionProduccionCommand(
                resolvedClienteId,
                evaluacionId,
                request.getIngresos(),
                request.getCostosVariables(),
                request.getCostosFijos()
        );

        return commandGateway.send(command).thenApply(result -> evaluacionId);
    }
}

@Data
class EvaluacionProdRequest {
    private String clienteId;
    private double ingresos;
    private double costosVariables;
    private double costosFijos;
}
