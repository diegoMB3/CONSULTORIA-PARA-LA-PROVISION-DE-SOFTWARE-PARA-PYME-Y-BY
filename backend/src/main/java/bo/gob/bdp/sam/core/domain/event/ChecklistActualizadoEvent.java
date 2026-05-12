package bo.gob.bdp.sam.core.domain.event;

import lombok.Value;
import java.util.Map;

@Value
public class ChecklistActualizadoEvent {
    private final String clienteId;
    private final Map<String, Boolean> documentosEstado;
    private final boolean checklistCompleto;
    private final String timestamp;
}